package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.VimeoVideoModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class SearchVimeoVideoVolleyTask implements TTResponseListener<VimeoVideoModel> {

    private SearchVimeoVideoListener listener;
    private Activity activity;

    public SearchVimeoVideoVolleyTask(Activity activity, SearchVimeoVideoListener searchVideoListener) {
        this.activity = activity;
        this.listener = searchVideoListener;
    }

    public void execute(String token, String query) {
        try {
            String url = Config.getSearchVimeoVideosUrl(token, query);
            TTGsonRequest<VimeoVideoModel> nNacresGsonRequest
                    = new TTGsonRequest<VimeoVideoModel>(activity, url, null, this, VimeoVideoModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }
    }

    @Override
    public void onResponse(TTRequest<VimeoVideoModel> request, VimeoVideoModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onSuccessVimeoSearch(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<VimeoVideoModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onErrorVimeoSearch(error);
        }
    }

    public interface SearchVimeoVideoListener {
        public void onSuccessVimeoSearch(VimeoVideoModel response);
        public void onErrorVimeoSearch(VolleyError error);
    }
}
