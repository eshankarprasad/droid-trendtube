package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shankar on 10/12/15.
 */
public class IndVMOModel extends VMOItem {

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
        return "IndVMOModel{" +
                "videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
