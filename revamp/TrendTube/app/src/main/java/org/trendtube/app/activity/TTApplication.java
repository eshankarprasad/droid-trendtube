package org.trendtube.app.activity;

import android.app.Application;

import org.trendtube.app.R;
import org.trendtube.app.model.BasicItem;

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
    public static int fragmentIndex;
    public static String query;

    @Override
    public void onCreate() {
        super.onCreate();
        categories = new ArrayList<>();
        regions = new ArrayList<>();
        categotyId = "0";
        regionId = "IN";
        navIndex = 0;
        fragmentIndex = 0;
        query = "";

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("myfonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
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
