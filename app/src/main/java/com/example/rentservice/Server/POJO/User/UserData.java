package com.example.rentservice.Server.POJO.User;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("token")
    String token;
    @SerializedName("user")
    User user;

    public UserData(String token, String user_name, String email, String first_name, String last_name, String phone, String role, String description){
        this.token = token;
        this.user = new User(user_name, email, first_name, last_name, phone, role, description);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
