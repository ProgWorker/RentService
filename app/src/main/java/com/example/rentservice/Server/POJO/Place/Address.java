package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("country")
    String country;
    @SerializedName("region")
    String region;
    @SerializedName("city")
    String city;
    @SerializedName("street")
    String street;
    @SerializedName("home")
    String home;

    public Address(String country, String region, String city, String street, String home) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.home = home;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}
