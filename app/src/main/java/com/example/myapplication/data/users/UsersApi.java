package com.example.myapplication.data.users;

import com.example.myapplication.data.RetrofitService;
import com.example.myapplication.data.barcode.Barcode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersApi {
    @GET("addBarcode")
    Call<Void> addBarcode(@Query("userId") Long userId, @Query("barcode") Long barcode,
                          @Query("rating") Float rating, @Query("description") String description);

    @GET("findBarcode")
    Call<Barcode> findBarcode(@Query("barcode") Long barcode);

    @GET("addUser")
    Call<Void> addUser(@Query("userId") Long userId);

    @GET("addPoints")
    Call<Void> addPoints(@Query("userId") Long userId, @Query("points") int points);

    @GET("getCategory")
    Call<Void> getCategory(@Query("category") String category);

    @GET("user")
    Call<List<User>> getUsers();

    static UsersApi getService() {
        Retrofit retrofit = RetrofitService.getInstance();
        return retrofit.create(UsersApi.class);
    }
}