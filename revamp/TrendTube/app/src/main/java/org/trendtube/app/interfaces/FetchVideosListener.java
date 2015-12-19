package org.trendtube.app.interfaces;

import com.android.volley.VolleyError;

import org.trendtube.app.model.YouTubeVideoModel;

/**
 * Created by shankar on 9/12/15.
 */
public interface FetchVideosListener {
    public void onVideoFetched(YouTubeVideoModel response);
    public void onVideoFetchedError(VolleyError error);
}
