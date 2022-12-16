package com.example.rentservice.Server.Endpoints;

import com.example.rentservice.Server.POJO.Place.Cats;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Post;
import com.example.rentservice.Server.POJO.User.UserAuth;
import com.example.rentservice.Server.POJO.User.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("/posts/{id}")
    public Call<Post> getPostWithID(@Path("id") int id);

    @POST("/api/apilogin")
    public Call<UserData> authorize(@Body UserAuth auth_data);

    @GET("/api/apigetplaces")
    public Call<PBase> getPlaces();

    @GET("/api/apigetcategories")
    public Call<Cats> getCategories();
}