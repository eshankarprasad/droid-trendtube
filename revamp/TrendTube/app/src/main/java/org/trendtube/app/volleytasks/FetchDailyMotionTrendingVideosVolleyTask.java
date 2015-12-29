package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.DMModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchDailyMotionTrendingVideosVolleyTask implements TTResponseListener<DMModel> {

    private FetchDailyMotionTrendingVideoListener listener;
    private Activity activity;

    public FetchDailyMotionTrendingVideosVolleyTask(Activity activity, FetchDailyMotionTrendingVideoListener fetchDailyMotionTrendingVideoListener) {
        this.activity = activity;
        this.listener = fetchDailyMotionTrendingVideoListener;
    }

    public void execute(String token) {
        try {
            String url = Config.getDMMostPopularVideosUrl(token);
            TTGsonRequest<DMModel> nNacresGsonRequest = new TTGsonRequest<DMModel>(activity, url, null, this, DMModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<DMModel> request, DMModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onFetchedDailyMotionTrendingVideos(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<DMModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onFetchedErrorDailyMotionTrendingVideos(error);
        }
    }

    public interface FetchDailyMotionTrendingVideoListener {
        public void onFetchedDailyMotionTrendingVideos(DMModel response);
        public void onFetchedErrorDailyMotionTrendingVideos(VolleyError error);
    }
}
