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
    @SerializedName("p_name")
    String p_name = "";
    @SerializedName("room_id")
    int room_id;
    @SerializedName("id")
    int id;



    public Order(String status, int user, int persons, String date_from, String date_to) {
        this.status = status;
        this.user = user;
        this.persons = persons;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public Order(String date_from, String date_to, int persons, int room_id){
        this.room_id = room_id;
        this.persons = persons;
        this.date_from = date_from;
        this.date_to = date_to;
    }

    public Order(){
        this.status = "niodfg";
        this.user = 1;
        this.persons = 2;
        this.date_from = "2022-12-18T12:00:00";
        this.date_to = "2022-12-20T15:00:00";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
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
