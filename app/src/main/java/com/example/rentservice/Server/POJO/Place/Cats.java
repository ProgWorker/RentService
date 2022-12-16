package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cats {
    @SerializedName("categories")
    List<Cat> cats;

    public Cats(List<Cat> cats) {
        this.cats = cats;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }
}
