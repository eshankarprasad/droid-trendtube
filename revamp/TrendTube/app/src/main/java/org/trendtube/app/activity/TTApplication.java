package org.trendtube.app.activity;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import org.trendtube.app.R;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.utils.AnalyticsTrackers;
import org.trendtube.app.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by shankar on 11/12/15.
 */
public class TTApplication extends Application {
    public static List<BasicItem> categories;
    public static List<BasicItem> regions;
    public static String regionId;
    public static String categotyId;

    public static String topViewedDateFilter = "TODAY";

    public static int navIndex;
    public static int currentTabIndex;
    public static int tabIndex;
    public static String query;

    private static TTApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        categories = new ArrayList<>();
        regions = new ArrayList<>();
        categotyId = "0";
        regionId = "IN";
        navIndex = 0;
        tabIndex = 0;
        currentTabIndex = 0;
        query = "";

        mInstance = this;

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("myfonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    public static synchronized TTApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();

        MyLog.e("Tracker: Send " + screenName);
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                            .setDescription(
                                    new StandardExceptionParser(this, null)
                                            .getDescription(Thread.currentThread().getName(), e))
                            .setFatal(false)
                            .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    @Override
    public void onTerminate() {
        categories.clear();
        categories = null;
        regions.clear();
        regions = null;
        query = null;
        super.onTerminate();
    }
}
