package org.trendtube.app.interfaces;

import com.android.volley.VolleyError;

import org.trendtube.app.model.VideoModel;

/**
 * Created by shankar on 9/12/15.
 */
public interface FetchVideosListener {
    public void onVideoFetched(VideoModel response);
    public void onVideoFetchedError(VolleyError error);
}
