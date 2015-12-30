package org.trendtube.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.adapter.BasicRecyclerAdapter;
import org.trendtube.app.adapter.TopPagerAdapter;
import org.trendtube.app.adapter.SearchPagerAdapter;
import org.trendtube.app.adapter.TrendingPagerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.BasicItemSelectedListener;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.CategoryModel;
import org.trendtube.app.model.RegionModel;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchCategoriesVolleyTask;
import org.trendtube.app.volleytasks.FetchRegionVolleyTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TTActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BasicItemSelectedListener,
        FetchRegionVolleyTask.FetchRegionListener, FetchCategoriesVolleyTask.FetchCategoriesListener {

    private static final String TAG = TTActivity.class.getSimpleName();
    private static final String BUNDLE_SELECTED_CATEGORY = "bundle_category";
    private static final String BUNDLE_SELECTED_REGION = "bundle_region";
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TTProgressDialog ttProgressDialog;
    private BasicItem selectedCategory, selectedRegion;
    private TextView txtRegion;
    //private FloatingActionButton fabCategory;
    private TrendingPagerAdapter trendingPagerAdapter;
    private TopPagerAdapter topPagerAdapter;
    private SearchPagerAdapter searchPagerAdapter;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, TTActivity.class);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedRegion = (BasicItem) savedInstanceState.getSerializable(BUNDLE_SELECTED_REGION);
        selectedCategory = (BasicItem) savedInstanceState.getSerializable(BUNDLE_SELECTED_CATEGORY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_SELECTED_REGION, selectedRegion);
        outState.putSerializable(BUNDLE_SELECTED_CATEGORY, selectedCategory);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if ("".equals(Utils.getStringPreference(this, Constants.KEY_CATEGORY_ID))
                && "".equals(Utils.getStringPreference(this, Constants.KEY_CATEGORY_NAME))) {
            selectedCategory = new BasicItem("0", "ALL");
        } else {
            selectedCategory = new BasicItem(Utils.getStringPreference(this, Constants.KEY_CATEGORY_ID),
                    Utils.getStringPreference(this, Constants.KEY_CATEGORY_NAME));
        }

        if ("".equals(Utils.getStringPreference(this, Constants.KEY_REGION_ID))
                && "".equals(Utils.getStringPreference(this, Constants.KEY_REGION_NAME))) {
            selectedRegion = new BasicItem("IN", "INDIA");
        } else {
            selectedRegion = new BasicItem(Utils.getStringPreference(this, Constants.KEY_REGION_ID),
                    Utils.getStringPreference(this, Constants.KEY_REGION_NAME));
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(TTApplication.navIndex);
        setTitle(R.string.nav_item_trending_videos);

        trendingPagerAdapter = new TrendingPagerAdapter(getResources().getStringArray(R.array.tab_items_normal), getSupportFragmentManager());
        topPagerAdapter = new TopPagerAdapter(getResources().getStringArray(R.array.tab_items_normal), getSupportFragmentManager());
        searchPagerAdapter = new SearchPagerAdapter(getResources().getStringArray(R.array.tab_items_search),getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(trendingPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        View headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        txtRegion = (TextView) headerView.findViewById(R.id.btn_region);
        txtRegion.setText(selectedRegion.getName());
        txtRegion.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trend_tube, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                showSearchScreen();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchScreen() {
        MyLog.e("TTApplication.currentTabIndex: " + TTApplication.currentTabIndex);

        // Saving current tab index
        TTApplication.tabIndex = TTApplication.currentTabIndex;

        Intent intent = SearchActivity.newIntent(this);
        startActivityForResult(intent, Constants.REQUEST_SEARCH);
        Utils.animateActivity(this, "zero");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        Menu filterMenu = mNavigationView.getMenu();
        switch (menuItem.getItemId()) {
            case R.id.nav_sub_menu_like:
                mDrawerLayout.closeDrawers();
                like();
                break;
            case R.id.nav_item_trending_videos:
                filterMenu.setGroupVisible(R.id.menu_group_filter, false);
                setupPager(0, Constants.DATE_FILTER_TODAY, View.VISIBLE, 0);
                setTitle(menuItem.getTitle());
                mNavigationView.getMenu().getItem(1).setEnabled(true);
                break;
            case R.id.nav_item_top_viewed_videsos:
                filterMenu.setGroupVisible(R.id.menu_group_filter, true);
                setupPager(1, Constants.DATE_FILTER_TODAY, View.VISIBLE ,500);
                menuItem.setEnabled(false);
                setTitle(menuItem.getTitle());
                mNavigationView.getMenu().getItem(3).setChecked(true);
                break;
            default:
                setupPager(1, Constants.filter.get(menuItem.getItemId()), View.VISIBLE ,0);
        }
        return true;
    }

    private void setupPager(int navIndex, String dateFilter, int regionVisibility, int drawerClosingDuration) {

        TTApplication.navIndex = navIndex;
        TTApplication.query = "";
        TTApplication.topViewedDateFilter = dateFilter;

        //Replacing Vimeo tab index to youtube
        TTApplication.tabIndex = TTApplication.currentTabIndex == 2 ? 0 : TTApplication.currentTabIndex;

        mViewPager.setAdapter(navIndex == 0 ? trendingPagerAdapter : topPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(TTApplication.tabIndex);
        txtRegion.setVisibility(regionVisibility);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                }
            }
        }, drawerClosingDuration);
    }

    private void like() {
        Toast.makeText(this, "Facebook Like", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_region:
                regionButtonClicked();
                break;
        }
    }

    private void regionButtonClicked() {

        if (TTApplication.currentTabIndex != 0) {
            mDrawerLayout.closeDrawers();
            Snackbar.make(findViewById(R.id.coordinator), "Region is not available for DailyMotion videos", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TTApplication.regions != null && TTApplication.regions.size() > 0) {
            showRegionListDialog();
        } else {
            showProgressDialog();
            FetchRegionVolleyTask task = new FetchRegionVolleyTask(this, this);
            task.execute();
        }
    }

    private void showProgressDialog() {
        if (ttProgressDialog == null) {
            ttProgressDialog = new TTProgressDialog(this);
        }

        if (!isFinishing() && !ttProgressDialog.isShowing()) {
            ttProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {

        if (ttProgressDialog != null && ttProgressDialog.isShowing()) {
            ttProgressDialog.dismiss();
            ttProgressDialog = null;
        }
    }

    private void showRegionListDialog() {
        showListViewDialog(this, getString(R.string.title_regions),
                TTApplication.regions, selectedRegion,
                Constants.SpinnerType.REGION.getSpinnerType());
    }

    final public void showListViewDialog(final BasicItemSelectedListener listener, String title, List<BasicItem> items, BasicItem selectedItem, final int type) {

        final Dialog dialog = Utils.getBasicDialog(this, title);
        BasicRecyclerAdapter basicAdapter = new BasicRecyclerAdapter(this, dialog, items, selectedItem, this, type);
        RecyclerView listView = (RecyclerView) dialog.findViewById(R.id.dropDownListInfoType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(listView.getContext());
        listView.setLayoutManager(linearLayoutManager);
        listView.setAdapter(basicAdapter);
        if (!this.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }


    @Override
    public void onFetchedRegion(RegionModel response) {
        MyLog.e(response.toString());
        dismissProgressDialog();
        saveRegion(response);
        showRegionListDialog();
    }

    private void saveRegion(RegionModel regionModel) {
        Set<String> ids = regionModel.getRegionMap().keySet();
        List<BasicItem> regions = new ArrayList<>();
        for (String id : ids) {
            BasicItem item = new BasicItem(id, regionModel.getRegionMap().get(id).getName());
            regions.add(item);
        }
        TTApplication.regions = regions;
    }

    @Override
    public void onErrorFetchedRegion(VolleyError error) {
        dismissProgressDialog();
        Utils.handleError(this, error);
        MyLog.e("onErrorFetchedRegion");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SEARCH && resultCode == RESULT_OK) {
            String query = data.getExtras().getString(Constants.BUNDLE_QUERY);
            MyLog.e("Query Received: " + query);
            TTApplication.query = query;
            mViewPager.setAdapter(null);
            mViewPager.setAdapter(searchPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            setTitle(R.string.hint_search);
            mViewPager.setCurrentItem(TTApplication.tabIndex);
            txtRegion.setVisibility(View.GONE);
            mNavigationView.getMenu().getItem(0).setChecked(false);
            mNavigationView.getMenu().getItem(1).setChecked(false);
        }
    }

    @Override
    public void onListViewItemSelected(BasicItem item, int type) {

        if (type == Constants.SpinnerType.REGION.getSpinnerType()) {
            mDrawerLayout.closeDrawers();
            this.selectedRegion = item;
            TTApplication.regionId = selectedRegion.getId();
            Utils.setPreference(this, Constants.KEY_REGION_ID, selectedRegion.getId());
            Utils.setPreference(this, Constants.KEY_REGION_NAME, selectedRegion.getName());
            if (TTApplication.navIndex == 0) {
                mViewPager.setAdapter(trendingPagerAdapter);
            } else {
                mViewPager.setAdapter(topPagerAdapter);
            }
            txtRegion.setText(selectedRegion.getName());
        } else if (type == Constants.SpinnerType.CATEGORY.getSpinnerType()) {
            mDrawerLayout.closeDrawers();
            this.selectedCategory = item;
            TTApplication.categotyId = selectedCategory.getId();
            Utils.setPreference(this, Constants.KEY_CATEGORY_ID, selectedCategory.getId());
            Utils.setPreference(this, Constants.KEY_CATEGORY_NAME, selectedCategory.getName());
            if (TTApplication.navIndex == 0) {
                mViewPager.setAdapter(trendingPagerAdapter);
            } else {
                mViewPager.setAdapter(topPagerAdapter);
            }
        }
    }

    public void categoryButtonClicked() {

        MyLog.e("Fragment Index: " + TTApplication.currentTabIndex);

        if (TTApplication.categories != null && TTApplication.categories.size() > 0) {
            showCategoryListDialog();
        } else {
            showProgressDialog();
            FetchCategoriesVolleyTask task = new FetchCategoriesVolleyTask(this, this);
            task.execute();
        }
    }

    private void showCategoryListDialog() {
        showListViewDialog(this, getString(R.string.title_categories),
                TTApplication.categories, selectedCategory,
                Constants.SpinnerType.CATEGORY.getSpinnerType());
    }

    @Override
    public void onCategoriesFetched(CategoryModel response) {
        dismissProgressDialog();
        MyLog.e(response.toString());
        saveCategory(response);
        showCategoryListDialog();
    }

    @Override
    public void onCategoriesFetchedError(VolleyError error) {
        dismissProgressDialog();
        Utils.handleError(this, error);
        error.printStackTrace();
    }

    private void saveCategory(CategoryModel categoryMap) {

        Set<String> ids = categoryMap.getCategoryMap().keySet();
        List<BasicItem> categories = new ArrayList<>();
        for (String id : ids) {
            BasicItem item = new BasicItem(id, categoryMap.getCategoryMap().get(id).getName());
            categories.add(item);
        }
        TTApplication.categories = categories;
    }

    /*private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constants.INTENT_FILTER_CONNECTIVITY_CHANGE)
                    || intent.getAction().equals(Constants.INTENT_FILTER_WI_FI_STATE_CHANGE)) {
                if (checkInternet(context)) {
                    Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Network unavailable Do operations", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private boolean checkInternet(Context context) {
            ServiceManager serviceManager = new ServiceManager(context);
            if (serviceManager.isNetworkAvailable()) {
                return true;
            } else {
                return false;
            }
        }
    };*/

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
