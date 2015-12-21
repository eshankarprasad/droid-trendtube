package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.constants.Config;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.YouTubeVideoModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchYouTubeVideosVolleyTask implements TTResponseListener<YouTubeVideoModel> {

    private FetchVideosListener listener;
    private Activity activity;

    public FetchYouTubeVideosVolleyTask(Activity activity, FetchVideosListener fetchVideosListener) {
        this.activity = activity;
        this.listener = fetchVideosListener;
    }

    public void execute(String nextPageToken) {
        try {
            String url = null;
            if (TTApplication.navIndex == 0) {
                url = Config.getYouTubeMostPopularVideosUrl(nextPageToken);
            } else {
                url = Config.getYouTubeMostViewedVideosUrl(nextPageToken);
            }
            MyLog.e(url);
            TTGsonRequest<YouTubeVideoModel> nNacresGsonRequest = new TTGsonRequest<YouTubeVideoModel>(activity, url, null, this, YouTubeVideoModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, true);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<YouTubeVideoModel> request, YouTubeVideoModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onVideoFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<YouTubeVideoModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onVideoFetchedError(error);
        }
    }
}
