package org.trendtube.app.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.trendtube.app.R;
import org.trendtube.app.adapter.BasicListViewAdapter;
import org.trendtube.app.adapter.TrendTubePagerAdapter;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.interfaces.BasicItemSelectedListener;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.CategoryModel;
import org.trendtube.app.ui.TTProgressDialog;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volleytasks.FetchCategoriesVolleyTask;

import java.util.List;

public class TrendTubeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        FetchCategoriesVolleyTask.FetchCategoriesListener,BasicItemSelectedListener {

    private static final String TAG = TrendTubeActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private TTProgressDialog ttProgressDialog;
    private BasicItem selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_tube);

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_category);
        fab.setOnClickListener(this);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.coordinator), "Select CategoryModel", Snackbar.LENGTH_LONG).setAction("All", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TrendTubeActivity.this, "CategoryModel will be shown", Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
        });*/

        TrendTubePagerAdapter adapter = new TrendTubePagerAdapter(TrendTubeActivity.this, getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.selector_tab_trending_videos);
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_tab_top_videos);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                navigationView.getMenu().getItem(tab.getPosition()).setChecked(true);
                mViewPager.setCurrentItem(tab.getPosition(), true);
                switch (tab.getPosition()) {
                    case 0:
                        setTitle(getString(R.string.nav_item_trending_videos));
                        break;
                    case 1:
                        setTitle(getString(R.string.nav_item_top_viewed_videos));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setTitle(getString(R.string.nav_item_trending_videos));
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trend_tube, menu);
        return true;
    }*/

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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();

        switch (menuItem.getItemId()) {
            case R.id.nav_item_trending_videos:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_item_top_viewed_videsos:
                mViewPager.setCurrentItem(1);
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
        }
    }

    private void categoryButtonClicked() {

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
        TTApplication.categories = response.getCategories();
        showCategoryListDialog();
    }

    private void showCategoryListDialog() {
        showListViewDialog(this, getString(R.string.title_categories),
                TTApplication.categories, selectedCategory,
                Constants.SpinnerType.CATEGORY.getSpinnerType());
    }

    @Override
    public void onCategoriesFetchedError(VolleyError error) {
        dismissProgressDialog();
        Utils.handleError(this, error);
        error.printStackTrace();
    }

    final public void showListViewDialog(final BasicItemSelectedListener listener, String title, List<BasicItem> items, BasicItem selectedItem, final int type) {

        final Dialog dialog = Utils.getBasicDialog(this, title);
        final BasicListViewAdapter basicAdapter = new BasicListViewAdapter(this, items, selectedItem);

        ListView listView = (ListView) dialog.findViewById(R.id.dropDownListInfoType);
        listView.setAdapter(basicAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BasicItem selectedItem = basicAdapter.getItem(position);
                dialog.dismiss();
                listener.onListViewItemSelected(selectedItem, type);
            }
        });

        if (!this.isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }

    }

    @Override
    public void onListViewItemSelected(BasicItem item, int type) {
        if (type == Constants.SpinnerType.CATEGORY.getSpinnerType()) {
            this.selectedCategory = item;
        }
    }
}
