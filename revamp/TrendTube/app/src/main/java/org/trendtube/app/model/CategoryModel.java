package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shankar on 11/12/15.
 */
public class CategoryModel extends ResponseMetadata {

    @SerializedName("categories")
    private List<BasicItem> categories;

    public List<BasicItem> getCategories() {
        return categories;
    }

    public void setCategories(List<BasicItem> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categories=" + categories +
                '}';
    }
}
