package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by shankar on 11/12/15.
 */
public class CategoryModel extends ResponseMetadata {

    @SerializedName("category_map")
    private Map<String, CategoryItem> categoryMap;

    public Map<String, CategoryItem> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, CategoryItem> categoryMap) {
        this.categoryMap = categoryMap;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryMap=" + categoryMap +
                '}';
    }
}
