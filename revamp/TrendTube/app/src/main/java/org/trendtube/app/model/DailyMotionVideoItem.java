package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankarprasad on 20-12-2015.
 */
public class DailyMotionVideoItem implements Serializable {

    @SerializedName("thumbnail_large_url")
    private String thumbnailLargeUrl;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("channel")
    private String channel;

    @SerializedName("description")
    private String description;

    @SerializedName("views_total")
    private int viewsTotal;

    @SerializedName("published")
    private boolean published;

    @SerializedName("duration")
    private long duration;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("created_time")
    private long createdTime;

    @SerializedName("channel.id")
    private String channelId;

    public String getThumbnailLargeUrl() {
        return thumbnailLargeUrl;
    }

    public void setThumbnailLargeUrl(String thumbnailLargeUrl) {
        this.thumbnailLargeUrl = thumbnailLargeUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViewsTotal() {
        return viewsTotal;
    }

    public void setViewsTotal(int viewsTotal) {
        this.viewsTotal = viewsTotal;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "DailyMotionVideoItem{" +
                "thumbnailLargeUrl='" + thumbnailLargeUrl + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", channel='" + channel + '\'' +
                ", description='" + description + '\'' +
                ", viewsTotal=" + viewsTotal +
                ", published=" + published +
                ", duration=" + duration +
                ", tags=" + tags +
                ", createdTime=" + createdTime +
                ", channelId='" + channelId + '\'' +
                '}';
    }
}
