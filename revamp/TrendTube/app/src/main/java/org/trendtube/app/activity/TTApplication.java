package org.trendtube.app.activity;

import android.app.Application;

import org.trendtube.app.model.BasicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shankar on 11/12/15.
 */
public class TTApplication extends Application {
    public static List<BasicItem> categories;
    public static List<BasicItem> regions;
    public static String regionId;
    public static String categotyId;
    public static int navIndex;
    public static int fragmentIndex;

    @Override
    public void onCreate() {
        super.onCreate();
        categories = new ArrayList<>();
        regions = new ArrayList<>();
        categotyId = "0";
        regionId = "IN";
        navIndex = 0;
        fragmentIndex = 0;
    }

    @Override
    public void onTerminate() {
        categories.clear();
        categories = null;
        regions.clear();
        regions = null;
        super.onTerminate();
    }
}
