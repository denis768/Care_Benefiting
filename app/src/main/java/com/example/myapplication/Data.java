package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Data {
    static TreeMap<Long, String> products = new TreeMap<>();
    static Retrofit retrofit;

    public Data() {
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.14:8080/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static void getProduct(long barcode, Context context) {
        sendBarcodeService sendService = retrofit.create(sendBarcodeService.class);
        Call<Product> call = sendService.getProduct(barcode);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        Product product = response.body();
                        Data.products.put(product.getBarcode(), String.valueOf(product.getRating()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addProduct(long barcode, float rating, Context context) {
        sendBarcodeService sendService = retrofit.create(sendBarcodeService.class);
        Call<Boolean> req = sendService.send(barcode, String.valueOf(rating));
        req.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                try {
                    Toast.makeText(context, "" + response.body(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
