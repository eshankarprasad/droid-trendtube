package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shankar on 10/12/15.
 */
public class YTModel extends ResponseMetadata {

    @SerializedName("id")
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", prevPageToken='" + prevPageToken + '\'' +
                ", pageInfo=" + pageInfo +
                ", YTItems=" + YTItems +
                '}';
    }

    public static class PageInfo implements Serializable {

        @SerializedName("totalResults")
        private int totalResults;

        @SerializedName("resultsPerPage")
        private int resultsPerPage;

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        public int getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }

        @Override
        public String toString() {
            return "PageInfo{" +
                    "totalResults=" + totalResults +
                    ", resultsPerPage=" + resultsPerPage +
                    '}';
        }
    }

}
