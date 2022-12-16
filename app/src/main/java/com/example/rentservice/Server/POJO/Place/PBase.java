package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PBase {
    @SerializedName("places")
    public List<Place> places;

    public PBase(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
