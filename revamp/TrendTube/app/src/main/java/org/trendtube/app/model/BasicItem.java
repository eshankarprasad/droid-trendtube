package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by shankar on 11/12/15.
 */
public class BasicItem implements Serializable {

    @SerializedName("youtube_cat_id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BasicItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
