package org.trendtube.app.activity;

import android.app.Application;

import org.trendtube.app.model.BasicItem;
import org.trendtube.app.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shankar on 11/12/15.
 */
public class TTApplication extends Application {
    public static List<BasicItem> categories;
    public static String selectedCategory;

    @Override
    public void onCreate() {
        super.onCreate();
        categories = new ArrayList<>();
    }
}
