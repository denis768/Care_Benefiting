package com.example.myapplication.data;

import static com.example.myapplication.feature.ui.EntryVK.userId;

import androidx.annotation.NonNull;

import com.example.myapplication.data.barcode.Barcode;
import com.example.myapplication.data.users.User;
import com.example.myapplication.data.users.UsersApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Data {
    private static Long barcode;
    private static Float rating;
    private static String description;
    private static Barcode foundBarcode;
    private static Object foundProduct;

    public static void findProduct(Long barcode, Callback<Barcode> callback) {
        UsersApi usersApi = UsersApi.getService();
        Call<Barcode> call = usersApi.findBarcode(barcode);
        call.enqueue(callback);
    }

    public static void saveProduct() {
        Call<Void> call = UsersApi.getService().addBarcode(userId, barcode, rating, description);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("Data inserted successfully");
                } else {
                    System.out.println("Failed to insert data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void getUsers() {
        UsersApi.getService().getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    // Handle the response
                } else {
                    System.out.println("Failed to retrieve users");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

//    public static void uploadImage(String category, String subcategory, Bitmap bitmap) {
//        DatabaseReference productRef = database.getReference("applicationx5/products/");
//        DatabaseReference categoryRef = database.getReference("applicationx5/products/categories/" + category + "/subcategories/" + subcategory + "/products");
//        String path = "products/" + barcode + ".jpeg";
//        StorageReference imageRef = storageRef.child(path);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imageRef.putBytes(data);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
//                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String imageUrl = uri.toString();
//                        Map<String, Object> update = new HashMap<>();
//                        update.put("image_url", imageUrl);
//
//                        productRef.child(String.valueOf(barcode)).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(context, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(context, "Failed to save image URL", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        categoryRef.child(String.valueOf(barcode)).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(context, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
//                                Data.addUser(2);
//                                Data.addPoints(2, "За добавление изображения");
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }