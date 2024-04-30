package org.cuatrovientos.blabla4v.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RouteResponse {
    @SerializedName("features")
    private List<Feature> features;

    // Getter and setter
    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}

