package org.trendtube.app.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by shankar on 11/12/15.
 */
public class RegionModel extends ResponseMetadata {

    @SerializedName("region_map")
    private Map<String, RegionItem> regionMap;

    public Map<String, RegionItem> getRegionMap() {
        return regionMap;
    }

    public void setRegionMap(Map<String, RegionItem> regionMap) {
        this.regionMap = regionMap;
    }

    @Override
    public String toString() {
        return "RegionModel{" +
                "regionMap=" + regionMap +
                '}';
    }
}
