package org.trendtube.app.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.adapter.BasicRecyclerAdapter;
import org.trendtube.app.adapter.TrendTubePagerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.fragment.YouTubeVideosFragment;
import org.trendtube.app.interfaces.BasicItemSelectedListener;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.CategoryModel;
import org.trendtube.app.model.RegionModel;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.ServiceManager;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchCategoriesVolleyTask;
import org.trendtube.app.volleytasks.FetchRegionVolleyTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrendTubeSearchResultActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        FetchCategoriesVolleyTask.FetchCategoriesListener, BasicItemSelectedListener,
        FetchRegionVolleyTask.FetchRegionListener {

    private static final String TAG = TrendTubeSearchResultActivity.class.getSimpleName();
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {

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
    };
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TTProgressDialog ttProgressDialog;
    private BasicItem selectedCategory, selectedRegion;
    private TextView txtRegion;
    private TrendTubePagerAdapter pagerAdapter;
    private List<String> testList;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;
    private Filter mSearchFilter;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, TrendTubeSearchResultActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_tube);

        testList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            testList.add("abcdefg" + i);
        }

        /*ListView listView = (ListView) findViewById(R.id.listview_search_filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                testList);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        mSearchFilter = adapter.getFilter();*/


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_category);
        fab.setOnClickListener(this);

        pagerAdapter = new TrendTubePagerAdapter(TrendTubeSearchResultActivity.this, getResources().getStringArray(R.array.tab_items_search), getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        View headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        txtRegion = (TextView) headerView.findViewById(R.id.btn_region);
        txtRegion.setText(selectedRegion.getName());
        txtRegion.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trend_tube, menu);

        /*MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
                Toast.makeText(getApplicationContext(), "Search Text: "+ text, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                //Toast.makeText(getApplicationContext(), "sssssss "+text, Toast.LENGTH_LONG).show();
                *//*if (TextUtils.isEmpty(text)) {
                    mSearchFilter.filter(null);
                } else {
                    mSearchFilter.filter(text);
                }*//*
                return true;
            }
        });*/


        //mSearchView.setOnQueryTextListener(this);
        //searchView.setQueryHint(getString(R.string.action_search));
        /*SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setTextSize(14);*/
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
        MyLog.e("TTApplication.fragmentIndex: " + TTApplication.fragmentIndex);
        Intent intent = SearchActivity.newIntent(this);
        //intent.putExtra(Constants.BUNDLE_FRAGMENT_INDEX, TTApplication.fragmentIndex);
        startActivityForResult(intent, Constants.REQUEST_SEARCH);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_item_trending_videos:
                TTApplication.navIndex = 0;
                TTApplication.query = "";
                finish();
                startActivity(TrendTubeActivity.newIntent(this));
                break;
            case R.id.nav_item_top_viewed_videsos:
                TTApplication.navIndex = 1;
                TTApplication.query = "";
                finish();
                startActivity(TrendTubeActivity.newIntent(this));
                break;
            case R.id.nav_sub_menu_like:
                like();
                break;
        }
        return true;
    }

    private void like() {
        Toast.makeText(this, "Facebook Like", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_category:
                categoryButtonClicked();
                break;

            case R.id.btn_region:
                regionButtonClicked();
                break;
        }
    }

    private void regionButtonClicked() {

        MyLog.e(TAG, "Fragment Index: " + TTApplication.fragmentIndex);
        if (TTApplication.fragmentIndex != 0) {
            mDrawerLayout.closeDrawers();
            Utils.showSnacK(findViewById(R.id.coordinator), "Region is not available for DailyMotion videos", "Ok", Snackbar.LENGTH_LONG, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing
                }
            });
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

    private void categoryButtonClicked() {

        MyLog.e(TAG, "Fragment Index: " + TTApplication.fragmentIndex);
        if (TTApplication.fragmentIndex != 0) {

            Utils.showSnacK(findViewById(R.id.coordinator), "Category is not available for DailyMotion videos", "Ok", Snackbar.LENGTH_LONG, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do nothing
                }
            });
            return;
        }

        if (TTApplication.categories != null && TTApplication.categories.size() > 0) {
            showCategoryListDialog();
        } else {
            showProgressDialog();
            FetchCategoriesVolleyTask task = new FetchCategoriesVolleyTask(this, this);
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

    @Override
    public void onCategoriesFetched(CategoryModel response) {
        dismissProgressDialog();
        MyLog.e(response.toString());
        saveCategory(response);
        showCategoryListDialog();
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

    private void showCategoryListDialog() {
        showListViewDialog(this, getString(R.string.title_categories),
                TTApplication.categories, selectedCategory,
                Constants.SpinnerType.CATEGORY.getSpinnerType());
    }

    private void showRegionListDialog() {
        showListViewDialog(this, getString(R.string.title_regions),
                TTApplication.regions, selectedRegion,
                Constants.SpinnerType.REGION.getSpinnerType());
    }

    @Override
    public void onCategoriesFetchedError(VolleyError error) {
        dismissProgressDialog();
        Utils.handleError(this, error);
        error.printStackTrace();
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
    public void onListViewItemSelected(BasicItem item, int type) {

        if (type == Constants.SpinnerType.CATEGORY.getSpinnerType()) {
            this.selectedCategory = item;
            TTApplication.categotyId = selectedCategory.getId();
            Utils.setPreference(this, Constants.KEY_CATEGORY_ID, selectedCategory.getId());
            Utils.setPreference(this, Constants.KEY_CATEGORY_NAME, selectedCategory.getName());
            onFilterVideos();
        } else if (type == Constants.SpinnerType.REGION.getSpinnerType()) {
            this.selectedRegion = item;
            TTApplication.regionId = selectedRegion.getId();
            Utils.setPreference(this, Constants.KEY_REGION_ID, selectedRegion.getId());
            Utils.setPreference(this, Constants.KEY_REGION_NAME, selectedRegion.getName());
            txtRegion.setText(selectedRegion.getName());
            onFilterVideos();
        }
        mDrawerLayout.closeDrawers();
    }

    private void onFilterVideos() {
        ((YouTubeVideosFragment) pagerAdapter.getCurrentFragment(0)).refreshVideos();
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
        }
    }
}
