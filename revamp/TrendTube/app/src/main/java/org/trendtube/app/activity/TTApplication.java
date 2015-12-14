package org.trendtube.app.activity;

import android.app.Application;

import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.CategoryItem;
import org.trendtube.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shankar on 11/12/15.
 */
public class TTApplication extends Application {
    public static List<BasicItem> categories;
    public static List<BasicItem> regions;
    public static String region;
    public static String categoty;

    @Override
    public void onCreate() {
        super.onCreate();
        categories = new ArrayList<>();
        regions = new ArrayList<>();
        categoty = "0";
        region = "IN";
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
