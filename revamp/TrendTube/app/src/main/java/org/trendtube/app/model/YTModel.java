package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 10/12/15.
 */
public class YTModel extends ResponseMetadata {

    @SerializedName("kind")
    private String kind;

    @SerializedName("etag")
    private String etag;

    @SerializedName("nextPageToken")
    private String nextPageToken;

    @SerializedName("prevPageToken")
    private String prevPageToken;

    @SerializedName("pageInfo")
    private PageInfo pageInfo;

    @SerializedName("items")
    private List<YTItem> YTItems;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<YTItem> getYTItems() {
        return YTItems;
    }

    public void setYTItems(List<YTItem> YTItems) {
        this.YTItems = YTItems;
    }

    @Override
    public String toString() {
        return "YTModel{" +
                ", kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", prevPageToken='" + prevPageToken + '\'' +
                ", pageInfo=" + pageInfo +
                ", YTItems=" + YTItems +
                '}';
    }
}
