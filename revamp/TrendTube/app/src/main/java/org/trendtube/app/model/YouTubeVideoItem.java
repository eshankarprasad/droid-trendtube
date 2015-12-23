package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 10/12/15.
 */
public class YouTubeVideoItem extends ResponseMetadata {

    @SerializedName("kind")
    private String kind;

    @SerializedName("etag")
    private String etag;

    @SerializedName("id")
    private String id;

    @SerializedName("snippet")
    private Snippet snippet;

    @SerializedName("contentDetails")
    private ContentDetails contentDetails;

    @SerializedName("statistics")
    private Statistics statistics;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "YouTubeVideoItem{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", snippet=" + snippet +
                ", contentDetails=" + contentDetails +
                ", statistics=" + statistics +
                '}';
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public static class ContentDetails implements Serializable {

        @SerializedName("duration")
        private String duration;

        @SerializedName("dimension")
        private String dimension;

        @SerializedName("definition")
        private String definition;

        @SerializedName("caption")
        private String caption;

        @SerializedName("licensedContent")
        private boolean licensedContent;

        @SerializedName("regionRestriction")
        private RegionRestriction regionRestriction;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDimension() {
            return dimension;
        }

        public void setDimension(String dimension) {
            this.dimension = dimension;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public boolean isLicensedContent() {
            return licensedContent;
        }

        public void setLicensedContent(boolean licensedContent) {
            this.licensedContent = licensedContent;
        }

        public RegionRestriction getRegionRestriction() {
            return regionRestriction;
        }

        public void setRegionRestriction(RegionRestriction regionRestriction) {
            this.regionRestriction = regionRestriction;
        }

        @Override
        public String toString() {
            return "ContentDetails{" +
                    "duration='" + duration + '\'' +
                    ", dimension='" + dimension + '\'' +
                    ", definition='" + definition + '\'' +
                    ", caption='" + caption + '\'' +
                    ", licensedContent=" + licensedContent +
                    ", regionRestriction='" + regionRestriction + '\'' +
                    '}';
        }
    }

    public static class RegionRestriction implements Serializable {

        @SerializedName("blocked")
        private List<String> blockedRegions;

        public List<String> getBlockedRegions() {
            return blockedRegions;
        }

        public void setBlockedRegions(List<String> blockedRegions) {
            this.blockedRegions = blockedRegions;
        }

        @Override
        public String toString() {
            return "RegionRestriction{" +
                    "blockedRegions=" + blockedRegions +
                    '}';
        }
    }

    public static class Statistics implements Serializable {
        @SerializedName("viewCount")
        private String viewCount;

        @SerializedName("likeCount")
        private String likeCount;

        @SerializedName("dislikeCount")
        private String dislikeCount;

        @SerializedName("favoriteCount")
        private String favoriteCount;

        @SerializedName("commentCount")
        private String commentCount;

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public String getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(String likeCount) {
            this.likeCount = likeCount;
        }

        public String getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(String dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public String getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(String favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        @Override
        public String toString() {
            return "Statistics{" +
                    "viewCount='" + viewCount + '\'' +
                    ", likeCount='" + likeCount + '\'' +
                    ", dislikeCount='" + dislikeCount + '\'' +
                    ", favoriteCount='" + favoriteCount + '\'' +
                    ", commentCount='" + commentCount + '\'' +
                    '}';
        }
    }

}
