package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 10/12/15.
 */
public class IndYTItem extends YTItem {

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
        return "IndYTItem{" +
                "videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
