package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.activity.TTApplication;
import org.trendtube.app.constants.Config;
import org.trendtube.app.model.DailyMotionVideoModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchDailyMotionVideosVolleyTask implements TTResponseListener<DailyMotionVideoModel> {

    private FetchDailyMotionVideoListener listener;
    private Activity activity;

    public interface FetchDailyMotionVideoListener {
        public void onFetchedDailyMotionVideos(DailyMotionVideoModel response);
        public void onFetchedErrorDailyMotionVideos(VolleyError error);
    }

    public FetchDailyMotionVideosVolleyTask(Activity activity, FetchDailyMotionVideoListener fetchDailyMotionVideoListener) {
        this.activity = activity;
        this.listener = fetchDailyMotionVideoListener;
    }

    public void execute(String token) {
        try {
            String url = null;
            if (TTApplication.navIndex == 0) {
                url = Config.getDMMostPopularVideosUrl(token);
            } else {
                url = Config.getDMMostViewedVideosUrl(token);
            }
            TTGsonRequest<DailyMotionVideoModel> nNacresGsonRequest = new TTGsonRequest<DailyMotionVideoModel>(activity, url, null, this, DailyMotionVideoModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, true);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<DailyMotionVideoModel> request, DailyMotionVideoModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onFetchedDailyMotionVideos(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<DailyMotionVideoModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onFetchedErrorDailyMotionVideos(error);
        }
    }
}
