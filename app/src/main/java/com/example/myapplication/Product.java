package com.example.myapplication;

public class Product {
    private String barcode;
    private String rating;

    public Product() {}

    public Product(String barcode, String rating) {
        this.barcode = barcode;
        this.rating = rating;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {this.barcode = barcode;}

    public String getRating() {return rating;}

    public void setRating(String rating) {this.rating = rating;}
}