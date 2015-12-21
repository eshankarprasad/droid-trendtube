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
import org.trendtube.app.adapter.TrendTubeSearchPagerAdapter;
import org.trendtube.app.adapter.TrendingVideosPagerAdapter;
import org.trendtube.app.constants.Constants;
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
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private TrendTubeSearchPagerAdapter pagerAdapter;
    private TabLayout mTabLayout;
    private NavigationView mNavigationView;

    public static Intent newIntent(Activity activity) {
        return new Intent(activity, TrendTubeSearchResultActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_tube_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.getMenu().getItem(0).setChecked(false);
        mNavigationView.getMenu().getItem(1).setChecked(false);

        pagerAdapter = new TrendTubeSearchPagerAdapter(getResources().getStringArray(R.array.tab_items_search), getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        View headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        headerView.findViewById(R.id.btn_region).setVisibility(View.GONE);
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
        startActivityForResult(intent, Constants.REQUEST_SEARCH);
        Utils.animateActivity(this, "zero");
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
                Utils.animateActivity(this, "zero");
                break;
            case R.id.nav_item_top_viewed_videsos:
                TTApplication.navIndex = 1;
                TTApplication.query = "";
                finish();
                startActivity(TrendTubeActivity.newIntent(this));
                Utils.animateActivity(this, "zero");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SEARCH && resultCode == RESULT_OK) {
            String query = data.getExtras().getString(Constants.BUNDLE_QUERY);
            MyLog.e("Query Received: " + query);
            if (query.equalsIgnoreCase(TTApplication.query)) {
                TTApplication.query = query;
                mViewPager.setAdapter(pagerAdapter);
            }
        }
    }
}
