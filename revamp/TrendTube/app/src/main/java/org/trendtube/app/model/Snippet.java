package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 10/12/15.
 */
public class Snippet implements Serializable {

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("channelId")
    private String channelId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnails")
    private Thumbnails thumbnails;

    @SerializedName("channelTitle")
    private String channelTitle;

    @SerializedName("tags")
    private List<String> tags;

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("liveBroadcastContent")
    private String liveBroadcastContent;

    @SerializedName("localized")
    private Localized localized;

    @SerializedName("defaultAudioLanguage")
    private String defaultAudioLanguage;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public void setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
    }

    public Localized getLocalized() {
        return localized;
    }

    public void setLocalized(Localized localized) {
        this.localized = localized;
    }

    public String getDefaultAudioLanguage() {
        return defaultAudioLanguage;
    }

    public void setDefaultAudioLanguage(String defaultAudioLanguage) {
        this.defaultAudioLanguage = defaultAudioLanguage;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "publishedAt='" + publishedAt + '\'' +
                ", channelId='" + channelId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnails=" + thumbnails +
                ", channelTitle='" + channelTitle + '\'' +
                ", tags=" + tags +
                ", categoryId='" + categoryId + '\'' +
                ", liveBroadcastContent='" + liveBroadcastContent + '\'' +
                ", localized=" + localized +
                ", defaultAudioLanguage='" + defaultAudioLanguage + '\'' +
                '}';
    }

    public static class Thumbnails implements Serializable {

        @SerializedName("default")
        private ImageAttribute defaultImage;

        @SerializedName("medium")
        private ImageAttribute mediumImage;

        @SerializedName("high")
        private ImageAttribute highImage;

        @SerializedName("standard")
        private ImageAttribute standardImage;

        @SerializedName("maxres")
        private ImageAttribute maxResImage;

        public ImageAttribute getDefaultImage() {
            return defaultImage;
        }

        public void setDefaultImage(ImageAttribute defaultImage) {
            this.defaultImage = defaultImage;
        }

        public ImageAttribute getMediumImage() {
            return mediumImage;
        }

        public void setMediumImage(ImageAttribute mediumImage) {
            this.mediumImage = mediumImage;
        }

        public ImageAttribute getHighImage() {
            return highImage;
        }

        public void setHighImage(ImageAttribute highImage) {
            this.highImage = highImage;
        }

        public ImageAttribute getStandardImage() {
            return standardImage;
        }

        public void setStandardImage(ImageAttribute standardImage) {
            this.standardImage = standardImage;
        }

        public ImageAttribute getMaxResImage() {
            return maxResImage;
        }

        public void setMaxResImage(ImageAttribute maxResImage) {
            this.maxResImage = maxResImage;
        }

        @Override
        public String toString() {
            return "Thumbnails{" +
                    "defaultImage=" + defaultImage +
                    ", mediumImage=" + mediumImage +
                    ", highImage=" + highImage +
                    ", standardImage=" + standardImage +
                    ", maxResImage=" + maxResImage +
                    '}';
        }
    }

    public static class Localized implements Serializable {

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Localized{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class ImageAttribute implements Serializable {

        @SerializedName("url")
        private String url;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "Thumbnails{" +
                    "url='" + url + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }
}