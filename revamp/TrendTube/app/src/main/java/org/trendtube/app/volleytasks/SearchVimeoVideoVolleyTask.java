package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.VMOModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class SearchVimeoVideoVolleyTask implements TTResponseListener<VMOModel> {

    private SearchVimeoVideoListener listener;
    private Activity activity;

    public SearchVimeoVideoVolleyTask(Activity activity, SearchVimeoVideoListener searchVideoListener) {
        this.activity = activity;
        this.listener = searchVideoListener;
    }

    public void execute(String token, String query) {
        try {
            String url = Config.getSearchVimeoVideosUrl(token, query);
            TTGsonRequest<VMOModel> nNacresGsonRequest
                    = new TTGsonRequest<VMOModel>(activity, url, null, this, VMOModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }
    }

    @Override
    public void onResponse(TTRequest<VMOModel> request, VMOModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onSuccessVimeoSearch(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<VMOModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onErrorVimeoSearch(error);
        }
    }

    public interface SearchVimeoVideoListener {
        public void onSuccessVimeoSearch(VMOModel response);
        public void onErrorVimeoSearch(VolleyError error);
    }
}
