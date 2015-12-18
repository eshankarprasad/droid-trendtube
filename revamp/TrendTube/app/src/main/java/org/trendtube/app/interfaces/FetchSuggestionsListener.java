package org.trendtube.app.interfaces;

import com.android.volley.VolleyError;

import org.trendtube.app.model.SuggestionModel;

public interface FetchSuggestionsListener {

    public void onFetchedSuggestor(SuggestionModel result);

    public void onErrorFetchedSuggestor(VolleyError error);

}
