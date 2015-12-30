package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.IndDMModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchIndDMVolleyTask implements TTResponseListener<IndDMModel> {

    private FetchIndVideosListener listener;
    private Activity activity;

    public FetchIndDMVolleyTask(Activity activity, FetchIndVideosListener fetchIndVideosListener) {
        this.activity = activity;
        this.listener = fetchIndVideosListener;
    }

    public void execute(String videoId) {
        try {
            String url = Config.getIndDMUrl(videoId);
            MyLog.e(url);
            TTGsonRequest<IndDMModel> nNacresGsonRequest = new TTGsonRequest<IndDMModel>(activity, url, null, this, IndDMModel.class);
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
    public void onResponse(TTRequest<IndDMModel> request, IndDMModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onIndVideoFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<IndDMModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onIndVideoFetchedError(error);
        }
    }

    public interface FetchIndVideosListener {
        public void onIndVideoFetched(IndDMModel response);

        public void onIndVideoFetchedError(VolleyError error);
    }
}
