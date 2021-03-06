package org.trendtube.app.model;

import java.io.Serializable;

/**
 * Created by ankitgarg on 24/2/14.
 */
public class BasicItem implements Serializable {

  private String id;
  private String name;

    public BasicItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "BasicItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
