package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shankar on 10/12/15.
 */
public class IndDMModel extends DMItem {

    @SerializedName("videoUrl")
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "IndDMModel{" +
                "videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
