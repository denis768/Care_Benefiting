package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
    private static final String TAG = "Data";
    static Long userId = EntryVK.getUserId();
    static String description="Test";
    static long timestamp = System.currentTimeMillis();
    private static long barcode;
    private static Context context;
    private Product foundProduct;

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference productRef = database.getReference("applicationx5/products"+userId);
    private static final DatabaseReference userRef = database.getReference("applicationx5/users");
    private static final DatabaseReference pointsRef = database.getReference("applicationx5/points_history");

    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private final Float rating;

    public Data(long barcode, Float rating, Context context) {
        this.barcode = barcode;
        this.rating = rating;
        this.context = context;
    }

    public Product findProduct() {
        Query productQuery = productRef.orderByChild("barcode").equalTo(barcode);
        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        String productString = product.getBarcode();
                        Log.d(TAG, "onDataChange: Found product: " + product.getBarcode());
                        foundProduct = product;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        });
        return foundProduct;
    }


    void saveProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("barcode", barcode);
        product.put("rating", rating);

        productRef.child(String.valueOf(barcode)).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Product saved successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to save product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void uploadImage(Bitmap bitmap) {
        String path = "products/" + barcode + ".jpeg";
        StorageReference imageRef = storageRef.child(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        Map<String, Object> update = new HashMap<>();
                        update.put("image_url", imageUrl);

                        productRef.child(String.valueOf(barcode)).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
                                Data.addUser(5);
                                Data.addPoints(5);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to save image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Failed", e);
            }
        });
    }
    public static void addUser(int numPoints) {
        DatabaseReference userRef = database.getReference("applicationx5/users").child(userId.toString());

        userRef.child("points").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(numPoints);
                } else {
                    mutableData.setValue(currentValue + numPoints);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    Log.e(TAG, "Failed to add points to user with ID: " + userId, databaseError.toException());
                } else {
                    Log.d(TAG, "Added " + numPoints + " points to user with ID: " + userId);
                }
            }
        });

        userRef.child("userId").setValue(userId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Added user with ID: " + userId);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to add user with ID: " + userId, e);
            }
        });
    }
    public static void addPoints(int numPoints) {
        DatabaseReference userPointsRef = database.getReference("applicationx5/points_history/" + userId);
        String pointId = userPointsRef.push().getKey();

        Map<String, Object> point = new HashMap<>();
        point.put("id", pointId);
        point.put("user_id", userId);
        point.put("date", ServerValue.TIMESTAMP);
        point.put("points", numPoints);
        point.put("description", description);

        DatabaseReference pointsRef = userPointsRef.child(pointId).child("points");
        pointsRef.setValue(ServerValue.increment(numPoints));

        userPointsRef.child(pointId).setValue(point);
    }

    public void getPointsHistory(String userId, long startDate, long endDate) {
        DatabaseReference pointsRef = FirebaseDatabase.getInstance().getReference("points_history/" + userId);
        Query query = pointsRef.orderByChild("date").startAt(startDate).endAt(endDate);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Point> points = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Point point = snapshot.getValue(Point.class);
                    points.add(point);
                }
                // Обработка полученных данных
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPoints:onCancelled", databaseError.toException());
            }
        });
    }
}