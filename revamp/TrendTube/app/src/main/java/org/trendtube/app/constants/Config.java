package org.trendtube.app.constants;

import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;

/**
 * Created by shankar on 9/12/15.
 */
public class Config extends ServerConfig {

    public static final String TAG = "Config";

    public static String getYouTubeMostPopularVideosUrl(String token) {

        //MyLog.e(TAG, "Region Id: " + TTApplication.regionId);
        //MyLog.e(TAG, "Category Id: " + TTApplication.categotyId);
        if ("".equals(token)) {
            return REST_HOST_YOUTUBE + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&part=snippet,statistics,contentDetails&regionCode=" + TTApplication.regionId + "&catId=" + TTApplication.categotyId +"&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST_YOUTUBE + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&part=snippet,statistics,contentDetails&regionCode=" + TTApplication.regionId + "&catId=" + TTApplication.categotyId + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getYouTubeMostViewedVideosUrl(String token) {

        //MyLog.e(TAG, "Region Id: " + TTApplication.regionId);
        //MyLog.e(TAG, "Category Id: " + TTApplication.categotyId);
        if ("".equals(token)) {
            return REST_HOST_YOUTUBE + "/getMostViewedVideosBySearchAPI?server_key=" + SERVER_KEY + "&part=snippet&regionCode=" + TTApplication.regionId + "&catId=" + TTApplication.categotyId + "&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST_YOUTUBE + "/getMostViewedVideosBySearchAPI?server_key=" + SERVER_KEY + "&part=snippet&regionCode=" + TTApplication.regionId + "&catId=" + TTApplication.categotyId + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getDMMostPopularVideosUrl(String token) {

        if ("".equals(token)) {
            return REST_HOST_DAILYMOTION + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST_DAILYMOTION + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getDMMostViewedVideosUrl(String token) {

        if ("".equals(token)) {
            return REST_HOST_DAILYMOTION + "/getMostViewedVideos?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST_DAILYMOTION + "/getMostViewedVideos?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getRegionUrl() {
        return REST_HOST_YOUTUBE + "/getYouTubeRegionsFromDB?server_key=" +SERVER_KEY;
    }

    public static String getCategoriesUrl() {
        return REST_HOST_YOUTUBE + "/getYouTubeCategoriesFromDB?server_key=" +SERVER_KEY;
    }

    public static String getSuggestionUrl(CharSequence query) {
        return "http://suggestqueries.google.com/complete/search?client=youtube&ds=yt&hl=en&q=" + Utils.encodeBankSpaces(query.toString());
    }

    public static String getSearchYouTubeVideosUrl(String token, String query) {
        //http://www.trendtube.org/YouTubeApi/searchVideosByQuery?server_key=jhsdasr7645234jfsklj0938209sr923&resultPerPage=1&pageToken=1
        if ("".equals(token)) {
            return REST_HOST_YOUTUBE + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&q=" + Utils.encodeBankSpaces(query);
        } else {
            return REST_HOST_YOUTUBE + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token + "&q=" + Utils.encodeBankSpaces(query);
        }
    }

    public static String getSearchDailyMotionVideosUrl(String token, String query) {

        if ("".equals(token)) {
            return REST_HOST_DAILYMOTION + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&q=" + Utils.encodeBankSpaces(query);
        } else {
            return REST_HOST_DAILYMOTION + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token + "&q=" + Utils.encodeBankSpaces(query);
        }
    }

    public static String getSearchVimeoVideosUrl(String token, String query) {

        if ("".equals(token)) {
            return REST_HOST_VIMEO + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&q=" + Utils.encodeBankSpaces(query);
        } else {
            return REST_HOST_VIMEO + "/searchVideosByQuery?server_key=" + SERVER_KEY + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token + "&q=" + Utils.encodeBankSpaces(query);
        }
    }
}
