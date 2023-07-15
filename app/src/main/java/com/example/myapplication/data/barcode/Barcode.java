package com.example.myapplication.data.barcode;

import java.io.Serializable;

public class Barcode {
    private Long barcode;
    private Float rating;
    private String category;
    private String subcategory;

    public Barcode() {
    }

    public Barcode(Long barcode, Float rating) {
        this.barcode = barcode;
        this.rating = rating;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Serializable getBarcode() {
        return barcode;
//        return rating;
//        return category;
//        return subcategory;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
        this.rating = rating;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}