package org.trendtube.app.constants;

import org.trendtube.app.activity.TTApplication;

/**
 * Created by shankar on 9/12/15.
 */
public class Config extends ServerConfig {

    //http://www.trendtube.org/YouTubeApi/getMostPopularVideos?server_key=asad&part=snippet&regionCode=IN&catId=1&resultPerPage=2
    public static String getPopularVideosUrl(String token) {
        if ("".equals(token)) {
            return REST_HOST + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&part=snippet,statistics,contentDetails&regionCode=" + TTApplication.region + "&catId=" + TTApplication.categoty +"&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST + "/getMostPopularVideos?server_key=" + SERVER_KEY + "&part=snippet,statistics,contentDetails&regionCode=" + TTApplication.region + "&catId=" + TTApplication.categoty + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getTopViewedVideosUrl(String token) {

        if ("".equals(token)) {
            return REST_HOST + "/getMostViewedVideosBySearchAPI?server_key=" + SERVER_KEY + "&part=snippet&regionCode=" + TTApplication.region + "&catId=" + TTApplication.categoty + "&resultPerPage=" + RESULT_PER_PAGE;
        } else {
            return REST_HOST + "/getMostViewedVideosBySearchAPI?server_key=" + SERVER_KEY + "&part=snippet&regionCode=" + TTApplication.region + "&catId=" + TTApplication.categoty + "&resultPerPage=" + RESULT_PER_PAGE + "&pageToken=" + token;
        }
    }

    public static String getRegionUrl() {
        //HOST/YouTubeApi/getYouTubeRegionsFromDB?server_key=jhsdasr7645234jfsklj0938209sr923
        return REST_HOST + "/getYouTubeRegionsFromDB?server_key=" +SERVER_KEY;
    }

    public static String getCategoriesUrl() {
        //http://www.trendtube.org/YouTubeApi/getYouTubeCategoriesFromDB?server_key=jhsdasr7645234jfsklj0938209sr923
        return REST_HOST + "/getYouTubeCategoriesFromDB?server_key=" +SERVER_KEY;
    }
}
