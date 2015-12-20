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
}
