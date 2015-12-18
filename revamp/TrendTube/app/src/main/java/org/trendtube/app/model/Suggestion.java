package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shankar on 18/12/15.
 */
public class Suggestion implements Serializable{
    @SerializedName("suggestion")
    private String suggestion;

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "suggestion='" + suggestion + '\'' +
                '}';
    }
}
