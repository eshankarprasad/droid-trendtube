package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.YTModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchYouTubeTrendingVideosVolleyTask implements TTResponseListener<YTModel> {

    private FetchVideosListener listener;
    private Activity activity;

    public FetchYouTubeTrendingVideosVolleyTask(Activity activity, FetchVideosListener fetchVideosListener) {
        this.activity = activity;
        this.listener = fetchVideosListener;
    }

    public void execute(String nextPageToken) {
        try {
            String url = Config.getYouTubeMostPopularVideosUrl(nextPageToken);
            MyLog.e(url);
            TTGsonRequest<YTModel> nNacresGsonRequest = new TTGsonRequest<YTModel>(activity, url, null, this, YTModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    public void cancelTask() {
        TTVolleyManager.cancelAllByTaskId(activity, this);
    }

    @Override
    public void onResponse(TTRequest<YTModel> request, YTModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onVideoFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<YTModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onVideoFetchedError(error);
        }
    }
}
