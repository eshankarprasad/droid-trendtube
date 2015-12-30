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
public class SearchDMVolleyTask implements TTResponseListener<DMModel> {

    private SearchDailyMotionVideoListener listener;
    private Activity activity;

    public SearchDMVolleyTask(Activity activity, SearchDailyMotionVideoListener searchVideoListener) {
        this.activity = activity;
        this.listener = searchVideoListener;
    }

    public void execute(String token, String query) {
        try {
            String url = Config.getSearchDailyMotionVideosUrl(token, query);
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
                this.listener.onSuccessDailyMotionSearch(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<DMModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onErrorDailyMotionSearch(error);
        }
    }

    public interface SearchDailyMotionVideoListener {
        public void onSuccessDailyMotionSearch(DMModel response);
        public void onErrorDailyMotionSearch(VolleyError error);
    }
}
