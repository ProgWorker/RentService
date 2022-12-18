package com.example.rentservice.Server.Endpoints;

import com.example.rentservice.Server.POJO.Place.CPlace;
import com.example.rentservice.Server.POJO.Place.Cats;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.Post;
import com.example.rentservice.Server.POJO.User.UserAuth;
import com.example.rentservice.Server.POJO.User.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {

    @POST("/api/apilogin")
    public Call<UserData> authorize(@Body UserAuth auth_data);

    @GET("/api/apigetplaces")
    public Call<PBase> getPlaces();

    @GET("/api/apigetcategories")
    public Call<Cats> getCategories();

    @GET("/api/apigetplace")
    public Call<CPlace> getPlace(@Query("place_id")int id);

    @GET("api/apigetuplaces")
    public Call<List<Place>> getUPlaces(@Header("token") String token);

    @GET("api/apigetorders")
    public Call<List<Order>> getOrders(@Header("token") String token);

    @POST("api/apibookorder")
    public Call<String> book(@Body Order order, @Header("token") String token);
}