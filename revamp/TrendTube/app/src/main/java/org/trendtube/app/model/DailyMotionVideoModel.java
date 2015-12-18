package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 16/12/15.
 */
public class DailyMotionVideoModel extends ResponseMetadata {

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("explicit")
    private boolean explicit;

    @SerializedName("total")
    private int total;

    @SerializedName("has_more")
    private boolean hasMore;

    @SerializedName("list")
    private List<Item> list;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DailyMotionVideoModel{" +
                "page=" + page +
                ", limit=" + limit +
                ", explicit=" + explicit +
                ", total=" + total +
                ", hasMore=" + hasMore +
                ", list=" + list +
                '}';
    }

    public static class Item implements Serializable {

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
        private int duration;

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

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
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
            return "Item{" +
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
}
