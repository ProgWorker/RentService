package com.example.rentservice.Server.POJO.Place;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("desc")
    String desc;
    @SerializedName("user")
    int user;

    public Comment(String desc, int user) {
        this.desc = desc;
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
