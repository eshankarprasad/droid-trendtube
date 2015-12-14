package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shankar on 11/12/15.
 */
public class CategoryItem implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("slug")
    private String slug;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryItem(String slug, String name) {
        this.slug = slug;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
