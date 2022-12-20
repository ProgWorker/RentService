package com.example.rentservice.Server.Endpoints;

import com.example.rentservice.Server.POJO.Place.CPlace;
import com.example.rentservice.Server.POJO.Place.Cats;
import com.example.rentservice.Server.POJO.Place.Order;
import com.example.rentservice.Server.POJO.Place.PBase;
import com.example.rentservice.Server.POJO.Place.Place;
import com.example.rentservice.Server.POJO.Place.Room;
import com.example.rentservice.Server.POJO.Post;
import com.example.rentservice.Server.POJO.User.User;
import com.example.rentservice.Server.POJO.User.UserAuth;
import com.example.rentservice.Server.POJO.User.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    @GET("api/params")
    public Call<ArrayList<String>> getParams();

    @GET("api/filter")
    public Call<ArrayList<Place>> filterPlaces(@QueryMap Map<String, String> m, @Query("params") ArrayList<String> values);

    @POST("api/approveorder/{id}")
    Call<String> approveOrder(@Path("id") int id);

    @POST("api/apibookorder")
    public Call<String> book(@Body Order order, @Header("token") String token);

    @POST("api/updateuser")
    Call<String> updateUser(@Body User user, @Header("token") String token);

    @Multipart
    @POST("api/updateavatar")
    public Call<String> updateAvatar(@Header("token") String token, @Part MultipartBody.Part image);

    @POST("api/updateroom")
    Call<String> updateRoom(@Body HashMap<String, Object> data);

    @Multipart
    @POST("api/updateplaceavatar/{place_id}")
    Call<String> updatePlaceAvatar(@Path("place_id") int place_id, @Part MultipartBody.Part image);

    @POST("api/updateplace")
    Call<String> updatePlace(@Body Place place);

    @Multipart
    @POST("api/createplace")
    Call<String> createPlace(@Part("place") Place place, @Part MultipartBody.Part image, @Header("token") String token);

    @POST("api/postcomment")
    Call<String> postComment(@Header("token") String token, @Body HashMap<String, Object> data);

    @POST("api/createuser")
    Call<UserData> createUser(@Body User user);
}