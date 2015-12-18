package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestionModel extends ResponseMetadata {

    @SerializedName("suggestions")
    private List<String> suggestions;

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    @Override
    public String toString() {
        return "SuggestionModel{" +
                "suggestions=" + suggestions +
                '}';
    }
}
