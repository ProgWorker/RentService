package com.example.rentservice.Server.POJO.User;

public class UserAuth {
    public String username;
    public String password;

    public UserAuth(String user, String password){
        this.username = user;
        this.password = password;
    }
}
