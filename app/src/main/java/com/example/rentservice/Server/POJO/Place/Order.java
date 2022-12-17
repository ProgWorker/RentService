package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("status")
    String status;
    @SerializedName("user")
    int user;
    @SerializedName("persons")
    int persons;
    @SerializedName("date_from")
    String date_from;
    @SerializedName("date_to")
    String date_to;

    public Order(String status, int user, int persons, String date_from, String date_to) {
        this.status = status;
        this.user = user;
        this.persons = persons;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }
}
