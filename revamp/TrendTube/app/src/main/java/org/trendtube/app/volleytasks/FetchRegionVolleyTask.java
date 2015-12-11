package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.VideoModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchRegionVolleyTask implements TTResponseListener<VideoModel> {

    private FetchVideosListener listener;
    private Activity activity;

    public FetchRegionVolleyTask(Activity activity, FetchVideosListener fetchVideosListener) {
        this.activity = activity;
        this.listener = fetchVideosListener;
    }

    public void execute(String nextPageToken) {
        try {
            String url = Config.getPopularVideosUrl(nextPageToken);
            TTGsonRequest<VideoModel> nNacresGsonRequest = new TTGsonRequest<VideoModel>(activity, url, null, this, VideoModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, true);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<VideoModel> request, VideoModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onVideoFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<VideoModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onVideoFetchedError(error);
        }
    }
}
