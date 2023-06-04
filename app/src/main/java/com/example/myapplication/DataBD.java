package com.example.myapplication;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataBD {
    private static final String TAG = "DataBD";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("applicationx5");
    private long barcode;

    public DataBD(long barcode) {
        this.barcode = barcode;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String quantity = dataSnapshot.child(String.valueOf(barcode)).getValue(String.class);
                if (quantity != null) {
                    // баркод был найден, получаем соответствующее значение количества
                    Log.d(TAG, "Quantity for " + barcode + " is " + quantity);
                } else {
                    // баркод не был найден, записываем новые значения
                    String newQuantity = "Молоко, упаковка TetraPak, класс экологичности 3";
                    myRef.child(String.valueOf(barcode)).setValue(newQuantity);
                    Log.d(TAG, "New quantity " + newQuantity + " has been added for barcode " + barcode);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}