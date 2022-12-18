package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Room {
    @SerializedName("status")
    String status;
    @SerializedName("info")
    String info;
    @SerializedName("persons")
    int persons;
    @SerializedName("cost")
    float cost;
    @SerializedName("orders")
    List<Order> orderdata;
    @SerializedName("id")
    int id=0;

    public Room(String status, String info, int persons, float cost, List<Order> orderdata) {
        this.status = status;
        this.info = info;
        this.persons = persons;
        this.cost = cost;
        this.orderdata = orderdata;
    }

    public Room(){
        this.status = "asd";
        this.info = "asdfg";
        this.persons = 12;
        this.cost = 122;
        ArrayList<Order> d = new ArrayList<>();
        d.add(new Order());
        d.add(new Order());
        d.add(new Order());
        this.orderdata = d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public List<Order> getOrderdata() {
        return orderdata;
    }

    public void setOrderdata(List<Order> orderdata) {
        this.orderdata = orderdata;
    }
}
