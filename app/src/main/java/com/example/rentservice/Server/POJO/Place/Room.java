package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Room {
    @SerializedName("status")
    String status;
    @SerializedName("info")
    String info;
    @SerializedName("persons")
    String persons;
    @SerializedName("cost")
    String cost;
    @SerializedName("orders")
    List<Order> orderdata;

    public Room(String status, String info, String persons, String cost, List<Order> orderdata) {
        this.status = status;
        this.info = info;
        this.persons = persons;
        this.cost = cost;
        this.orderdata = orderdata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<Order> getOrderdata() {
        return orderdata;
    }

    public void setOrderdata(List<Order> orderdata) {
        this.orderdata = orderdata;
    }
}
