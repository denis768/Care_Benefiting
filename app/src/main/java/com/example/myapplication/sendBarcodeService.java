package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface sendBarcodeService {
    @GET("/send")
    Call<Boolean> send(@Query("barcode") long barcode, @Query("quantity") String quantity);

    @GET("/get")
    Call<Product> getProduct(@Query("barcode") long barcode);
}