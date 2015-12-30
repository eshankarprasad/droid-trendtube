package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.IndDMModel;
import org.trendtube.app.model.IndVMOModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchIndVMOVolleyTask implements TTResponseListener<IndVMOModel> {

    private FetchIndVideosListener listener;
    private Activity activity;

    public FetchIndVMOVolleyTask(Activity activity, FetchIndVideosListener fetchIndVideosListener) {
        this.activity = activity;
        this.listener = fetchIndVideosListener;
    }

    public void execute(String videoId) {
        try {
            String url = Config.getIndVMOUrl(videoId);
            MyLog.e(url);
            TTGsonRequest<IndVMOModel> nNacresGsonRequest = new TTGsonRequest<IndVMOModel>(activity, url, null, this, IndVMOModel.class);
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
    public void onResponse(TTRequest<IndVMOModel> request, IndVMOModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onIndVideoFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<IndVMOModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onIndVideoFetchedError(error);
        }
    }

    public interface FetchIndVideosListener {
        public void onIndVideoFetched(IndVMOModel response);

        public void onIndVideoFetchedError(VolleyError error);
    }
}
