package org.cuatrovientos.blabla4v.models;

import com.google.gson.annotations.SerializedName;

public class Feature {
    @SerializedName("geometry")
    private Geometry geometry;

    // Getter and setter
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
