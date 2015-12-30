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
public class FetchDMTopVolleyTask implements TTResponseListener<DMModel> {

    private FetchDailyMotionTopVideoListener listener;
    private Activity activity;

    public FetchDMTopVolleyTask(Activity activity, FetchDailyMotionTopVideoListener fetchDailyMotionTopVideoListener) {
        this.activity = activity;
        this.listener = fetchDailyMotionTopVideoListener;
    }

    public void execute(String token) {
        try {
            String url = Config.getDMMostViewedVideosUrl(token);
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
                this.listener.onFetchedDailyMotionTopVideos(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<DMModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onFetchedErrorDailyMotionTopVideos(error);
        }
    }

    public interface FetchDailyMotionTopVideoListener {
        public void onFetchedDailyMotionTopVideos(DMModel response);
        public void onFetchedErrorDailyMotionTopVideos(VolleyError error);
    }
}
