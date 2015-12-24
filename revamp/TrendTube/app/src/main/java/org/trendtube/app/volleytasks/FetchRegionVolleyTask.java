package org.trendtube.app.volleytasks;

import android.app.Activity;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import org.trendtube.app.constants.Config;
import org.trendtube.app.model.RegionModel;
import org.trendtube.app.volley.NullResponseError;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

/**
 * Created by shankarprasad on 24-07-2015.
 */
public class FetchRegionVolleyTask implements TTResponseListener<RegionModel> {

    private FetchRegionListener listener;
    private Activity activity;

    public FetchRegionVolleyTask(Activity activity, FetchRegionListener fetchVideosListener) {
        this.activity = activity;
        this.listener = fetchVideosListener;
    }

    public void execute() {
        try {
            String url = Config.getRegionUrl();
            TTGsonRequest<RegionModel> nNacresGsonRequest = new TTGsonRequest<RegionModel>(activity, url, null, this, RegionModel.class);
            nNacresGsonRequest.setTaskId(this);
            TTVolleyManager.addToQueue(nNacresGsonRequest, false);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorResponse(null, new ParseError());
        }

    }

    @Override
    public void onResponse(TTRequest<RegionModel> request, RegionModel response) {
        if (null != response) {
            if (this.listener != null) {
                this.listener.onFetchedRegion(response);
            }
        } else {
            onErrorResponse(request, new NullResponseError());
        }

    }

    @Override
    public void onErrorResponse(TTRequest<RegionModel> request, VolleyError error) {
        if (this.listener != null) {
            this.listener.onErrorFetchedRegion(error);
        }
    }

    public interface FetchRegionListener {
        public void onFetchedRegion(RegionModel response);
        public void onErrorFetchedRegion(VolleyError error);
    }
}
