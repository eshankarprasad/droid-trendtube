package org.trendtube.app.constants;

/**
 * Created by shankar on 9/12/15.
 */
public class Constants {

    public static final boolean DEBUG = true;
    public static final String APP_ID = "org.trendtube.app";
    public static final String BUNDLE_VIDEO_ID = "bundle_video_id";
    public static final String KEY_CATEGORY_ID = "key_category_id";
    public static final String KEY_CATEGORY_NAME = "key_category_name";
    public static final int REQUEST_VIDEO_DETAIL = 3001;

    public static enum SpinnerType {

        CATEGORY(1);
        private int spinnerType;

        private SpinnerType(int sType) {
            spinnerType = sType;
        }
        public int getSpinnerType() {
            return spinnerType;
        }
    }
}
