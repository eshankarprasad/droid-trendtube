package org.trendtube.app.utils;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;

import com.android.volley.VolleyError;

import org.trendtube.app.activity.SearchActivity;
import org.trendtube.app.constants.Config;
import org.trendtube.app.interfaces.FetchSuggestionsListener;
import org.trendtube.app.model.SuggestionModel;
import org.trendtube.app.volley.TTGsonRequest;
import org.trendtube.app.volley.TTRequest;
import org.trendtube.app.volley.TTResponseListener;
import org.trendtube.app.volley.TTVolleyManager;

import java.util.Timer;
import java.util.TimerTask;

public class SuggestorQueryListener implements SearchView.OnQueryTextListener, TTResponseListener<SuggestionModel> {

    private final long DELAY = 300;
    private Activity activity;
    private Timer timer = new Timer();
    private FetchSuggestionsListener listener;
    private int suggestor_text_length_limit = 2;

    public SuggestorQueryListener(Activity activity, FetchSuggestionsListener listener) {

        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public void onResponse(TTRequest<SuggestionModel> request, SuggestionModel response) {
        if (listener != null) {
            listener.onFetchedSuggestor(response);
        }
    }

    @Override
    public void onErrorResponse(TTRequest<SuggestionModel> request, VolleyError error) {
        listener.onErrorFetchedSuggestor(error);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listener.onQueryCompleted(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        final Runnable getSuggestedKeywords = new Runnable() {
            public void run() {

                if (newText.trim().length() >= suggestor_text_length_limit) {
                    String url = Config.getSuggestionUrl(newText);
                    TTGsonRequest<SuggestionModel> nNacresGsonRequest =
                            new TTGsonRequest<SuggestionModel>(activity, url, null,
                                    SuggestorQueryListener.this, SuggestionModel.class);
                    TTVolleyManager.addToQueue(nNacresGsonRequest, false);
                } else {
                    ((SearchActivity) activity).clearAutoSuggestor();
                }
            }
        };

        TimerTask task = new TimerTask() {
            public void run() {
                activity.runOnUiThread(getSuggestedKeywords);
            }
        };

        timer.cancel();
        timer = new Timer();
        timer.schedule(task, DELAY);
        return true;
    }
}

