package org.trendtube.app.constants;

import android.util.SparseArray;

import org.trendtube.app.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shankar on 9/12/15.
 */
public class Constants {

    public static final boolean DEBUG = true;
    public static final String APP_ID = "org.trendtube.app";
    public static final String BUNDLE_VIDEO = "bundle_video_id";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_CATEGORY_NAME = "key_category_name";
    public static final String KEY_REGION_ID = "key_region_id";
    public static final String KEY_REGION_NAME = "key_region_name";

    public static final String INTENT_FILTER_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String INTENT_FILTER_WI_FI_STATE_CHANGE = "android.net.wifi.WIFI_STATE_CHANGED";

    public static final int REQUEST_REFRESH = 1;
    public static final int REQUEST_VIDEO_DETAIL = 3001;
    public static final int REQUEST_SEARCH = 3002;

    public static final String CONNECTION_ERROR = "Please check your internet connection and try again";
    public static final String SERVER_ERROR = "Server Error. Please Try Again after some time";
    public static final String APPLICATION_ERROR = "Applcation Error. Please Try Again";
    public static final String VIDEOS_NOT_FOUND_ERROR = "Videos not found";

    public static final String BUNDLE_QUERY = "bundle_query";
    public static final String BUNDLE_FRAGMENT_INDEX = "bundle_fragment_index";
    public static final String DEVELOPER_KEY = "AIzaSyAJGgPwppILAzpK3RP03k81sZSulBSpY8k";

    public static final String BUNDLE_VIDEO_FLAG = "bundle_video_flag";

    public static final int FLAG_YOUTUBE_SEARCH_VIDEO = 0;
    public static final int FLAG_DAILYMOTION_SEARCH_VIDEO = 1;
    public static final int FLAG_VIMEO_SEARCH_VIDEO = 2;

    public static final String DATE_FILTER_ALLTIME = "ALL-TIME";
    public static final String DATE_FILTER_TODAY = "TODAY";
    public static final String DATE_FILTER_THIS_WEEK = "THIS-WEEK";
    public static final String DATE_FILTER_THIS_MONTH = "THIS-MONTH";
    public static final String DATE_FILTER_LAST_THREE_MONTH = "LAST-THREE-MONTHS";
    public static final String DATE_FILTER_LAST_SIX_MONTH = "LAST-SIX-MONTHS";
    public static final String DATE_FILTER_THIS_YEAR = "THIS-YEAR";

    public static final SparseArray<String> filter = new SparseArray<String>() {
        {
            put(R.id.item_all_time, DATE_FILTER_ALLTIME);
            put(R.id.item_today, DATE_FILTER_TODAY);
            put(R.id.item_this_week, DATE_FILTER_THIS_WEEK);
            put(R.id.item_this_month, DATE_FILTER_THIS_MONTH);
            put(R.id.item_last_three_month, DATE_FILTER_LAST_THREE_MONTH);
            put(R.id.item_lasst_six_month, DATE_FILTER_LAST_SIX_MONTH);
            put(R.id.item_this_year, DATE_FILTER_THIS_YEAR);
        }
    };


    public static final String ERROR_UNSUPPORTED_CHARACTER = "Invalid character";

    public static enum SpinnerType {

        CATEGORY(1), REGION(2);
        private int spinnerType;

        private SpinnerType(int sType) {
            spinnerType = sType;
        }
        public int getSpinnerType() {
            return spinnerType;
        }
    }
}
