package com.example.rentservice.Server.POJO.Place;

import com.example.rentservice.Server.POJO.User.User;
import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("id")
    int id;
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("rating")
    float rating;
    @SerializedName("user")
    String user;
    @SerializedName("category")
    String category;
    @SerializedName("address")
    Address address;


    @SerializedName("pict")
    String pict;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Place(String title, String description, float rating, String user, String category) {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.user = user;
        this.category = category;
    }

    public Place(String title, String description, String user, String category) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.category = category;
    }


    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
