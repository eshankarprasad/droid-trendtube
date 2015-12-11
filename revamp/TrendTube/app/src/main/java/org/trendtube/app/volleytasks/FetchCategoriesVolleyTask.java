package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.interfaces.FetchVideosListener;
import org.trendtube.app.model.CategoryModel;
import org.trendtube.app.model.VideoModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchCategoriesVolleyTask implements TTResponseListener<CategoryModel> {

    private FetchCategoriesListener listener;
    private Activity activity;

    public FetchCategoriesVolleyTask(Activity activity, FetchCategoriesListener fetchCategoriesListener) {
        this.activity = activity;
        this.listener = fetchCategoriesListener;
    }

    public void execute() {
        try {
            String url = Config.getCategoriesUrl();
            TTGsonRequest<CategoryModel> nNacresGsonRequest = new TTGsonRequest<CategoryModel>(activity, url, null, this, CategoryModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, true);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<CategoryModel> request, CategoryModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onCategoriesFetched(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }
    }

    @Override
    public void onErrorResponse(TTRequest<CategoryModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onCategoriesFetchedError(error);
        }
    }

    public interface FetchCategoriesListener {
        public void onCategoriesFetched(CategoryModel response);
        public void onCategoriesFetchedError(VolleyError error);
    }
}
