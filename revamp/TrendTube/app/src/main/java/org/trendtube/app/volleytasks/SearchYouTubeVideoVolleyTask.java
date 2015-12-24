package org.trendtube.app.volleytasks;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.constants.Constants;
import org.trendtube.app.model.YouTubeVideoModel;
import org.trendtube.app.utils.MyLog;
import org.trendtube.app.utils.Utils;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

import java.io.UnsupportedEncodingException;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class SearchYouTubeVideoVolleyTask implements TTResponseListener<YouTubeVideoModel> {

    private SearchYouTubeVideoListener listener;
    private Activity activity;

    public SearchYouTubeVideoVolleyTask(Activity activity, SearchYouTubeVideoListener searchYouTubeVideoListener) {
        this.activity = activity;
        this.listener = searchYouTubeVideoListener;
    }

    public void execute(String token, String query) {
        try {
            String url = Config.getSearchYouTubeVideosUrl(token, query);
            MyLog.e("Generated Url: " + url);
            TTGsonRequest<YouTubeVideoModel> nNacresGsonRequest = new TTGsonRequest<YouTubeVideoModel>(activity, url, null, this, YouTubeVideoModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Utils.showErrorToast(activity, Constants.ERROR_UNSUPPORTED_CHARACTER, Toast.LENGTH_SHORT);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }
    }

    @Override
    public void onResponse(TTRequest<YouTubeVideoModel> request, YouTubeVideoModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onSuccessYouTubeSearch(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }
    }

    @Override
    public void onErrorResponse(TTRequest<YouTubeVideoModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onErrorYouTubeSearch(error);
        }
    }

    public interface SearchYouTubeVideoListener {
        public void onSuccessYouTubeSearch(YouTubeVideoModel response);
        public void onErrorYouTubeSearch(VolleyError error);
    }
}
