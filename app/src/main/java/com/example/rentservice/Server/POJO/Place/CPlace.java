package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CPlace {
    @SerializedName("place")
    Place place;
    @SerializedName("roomsdata")
    List<Room> roomsdata;
    @SerializedName("comments")
    List<Comment> comments;

    public CPlace(Place place, List<Room> roomsdata, List<Comment> comments) {
        this.place = place;
        this.roomsdata = roomsdata;
        this.comments = comments;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Room> getRoomsdata() {
        return roomsdata;
    }

    public void setRoomsdata(List<Room> roomsdata) {
        this.roomsdata = roomsdata;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
