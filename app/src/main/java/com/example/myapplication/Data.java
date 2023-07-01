package com.example.myapplication;

import static com.example.myapplication.EntryVK.userId;

import android.content.Context;
import android.content.SharedPreferences;
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
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String TAG = "Data";
    private static final String PREF_NAME = "myprefs";
    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static long barcode;
    private static Context context;
    private final Float rating;
    private String description;
    private Product foundProduct;

    public Data(long barcode, float rating, Context context) {
        Data.context = context;
        Data.barcode = barcode;
        this.rating = rating;
        this.description = description;
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong("vk_id", userId);
    }

    public static void CreateDB() {
        DatabaseReference productRef = database.getReference("applicationx5/products/");
        DatabaseReference categoriesRef = productRef.child("categories");

        categoriesRef.child("Молочные продукты").setValue(true);
        categoriesRef.child("Мясо").setValue(true);
        categoriesRef.child("Птица").setValue(true);
        categoriesRef.child("Яйцо").setValue(true);
        categoriesRef.child("Мясные продукты").setValue(true);
        categoriesRef.child("Рыба").setValue(true);
        categoriesRef.child("Майонез и соусы").setValue(true);
        categoriesRef.child("Бакалея").setValue(true);
        categoriesRef.child("Кофе и чай").setValue(true);
        categoriesRef.child("Кондитерские изделия").setValue(true);
        categoriesRef.child("Фрукты, овощи").setValue(true);
        categoriesRef.child("Хлеб").setValue(true);
        categoriesRef.child("Замороженные продукты").setValue(true);
        categoriesRef.child("Консервы").setValue(true);
        categoriesRef.child("Кулинария").setValue(true);
        categoriesRef.child("Напитки").setValue(true);

        DatabaseReference milkSubcategoriesRef = categoriesRef.child("Молочные продукты").child("subcategories");
        milkSubcategoriesRef.child("Молоко").setValue(true);
        milkSubcategoriesRef.child("Кисломолочные продукты").setValue(true);
        milkSubcategoriesRef.child("Сыр").setValue(true);
        milkSubcategoriesRef.child("Сыр плавленый").setValue(true);
        milkSubcategoriesRef.child("Масло, маргарин, спред").setValue(true);
        milkSubcategoriesRef.child("Творог").setValue(true);
        milkSubcategoriesRef.child("Сметана").setValue(true);
        milkSubcategoriesRef.child("Сливки").setValue(true);
        milkSubcategoriesRef.child("Йогурт").setValue(true);
        milkSubcategoriesRef.child("Сырок").setValue(true);
        milkSubcategoriesRef.child("Творожок").setValue(true);
        milkSubcategoriesRef.child("Десерт, пудинг").setValue(true);
        milkSubcategoriesRef.child("Сыр творожный").setValue(true);
        milkSubcategoriesRef.child("Молоко сгущенное").setValue(true);
        milkSubcategoriesRef.child("Сливки взбитые").setValue(true);

        DatabaseReference meatSubcategoriesRef = categoriesRef.child("Мясо").child("subcategories");
        meatSubcategoriesRef.child("Свинина").setValue(true);
        meatSubcategoriesRef.child("Говядина").setValue(true);
        meatSubcategoriesRef.child("Кролик").setValue(true);
        meatSubcategoriesRef.child("Баранина").setValue(true);
        meatSubcategoriesRef.child("Полуфабрикаты из мяса").setValue(true);
        meatSubcategoriesRef.child("Субпродукты из мяса").setValue(true);

        DatabaseReference birdSubcategoriesRef = categoriesRef.child("Птица").child("subcategories");
        birdSubcategoriesRef.child("Курица").setValue(true);
        birdSubcategoriesRef.child("Цыплёнок").setValue(true);
        birdSubcategoriesRef.child("Индейка").setValue(true);
        birdSubcategoriesRef.child("Утка").setValue(true);
        birdSubcategoriesRef.child("Полуфабрикаты из птицы").setValue(true);
        birdSubcategoriesRef.child("Субпродукты из птицы").setValue(true);

        DatabaseReference eggSubcategoriesRef = categoriesRef.child("Яйцо").child("subcategories");
        eggSubcategoriesRef.child("Куриное").setValue(true);
        eggSubcategoriesRef.child("Перепелиное").setValue(true);

        DatabaseReference meat_productsSubcategoriesRef = categoriesRef.child("Мясные продукты").child("subcategories");
        meat_productsSubcategoriesRef.child("Колбаса вареная").setValue(true);
        meat_productsSubcategoriesRef.child("Сосиски").setValue(true);
        meat_productsSubcategoriesRef.child("Сардельки").setValue(true);
        meat_productsSubcategoriesRef.child("Колбаса варено-копченая").setValue(true);
        meat_productsSubcategoriesRef.child("Колбаса сырокопченая").setValue(true);
        meat_productsSubcategoriesRef.child("Копчености").setValue(true);
        meat_productsSubcategoriesRef.child("Паштет").setValue(true);
        meat_productsSubcategoriesRef.child("Буженина").setValue(true);
        meat_productsSubcategoriesRef.child("Карбонад").setValue(true);
        meat_productsSubcategoriesRef.child("Ветчина").setValue(true);
        meat_productsSubcategoriesRef.child("Колбаски").setValue(true);

        DatabaseReference fishSubcategoriesRef = categoriesRef.child("Рыба").child("subcategories");
        fishSubcategoriesRef.child("Деликатесная рыба").setValue(true);
        fishSubcategoriesRef.child("Охлажденная рыба").setValue(true);
        fishSubcategoriesRef.child("Морепродукты").setValue(true);
        fishSubcategoriesRef.child("Сельдь").setValue(true);
        fishSubcategoriesRef.child("Икра").setValue(true);
        fishSubcategoriesRef.child("Копчёная рыба").setValue(true);

        DatabaseReference mayonnaise_and_saucesSubcategoriesRef = categoriesRef.child("Майонез и соусы").child("subcategories");
        mayonnaise_and_saucesSubcategoriesRef.child("Майонез").setValue(true);
        mayonnaise_and_saucesSubcategoriesRef.child("Кетчуп").setValue(true);
        mayonnaise_and_saucesSubcategoriesRef.child("Горчица").setValue(true);
        mayonnaise_and_saucesSubcategoriesRef.child("Соусы").setValue(true);

        DatabaseReference groceriesSubcategoriesRef = categoriesRef.child("Бакалея").child("subcategories");
        groceriesSubcategoriesRef.child("Масло растительное").setValue(true);
        groceriesSubcategoriesRef.child("Макароны").setValue(true);
        groceriesSubcategoriesRef.child("Крупы").setValue(true);
        groceriesSubcategoriesRef.child("Чипсы и снеки").setValue(true);
        groceriesSubcategoriesRef.child("Орехи и семечки").setValue(true);
        groceriesSubcategoriesRef.child("Сухофрукты и цукаты").setValue(true);
        groceriesSubcategoriesRef.child("Мёд").setValue(true);
        groceriesSubcategoriesRef.child("Варенье и сиропы").setValue(true);
        groceriesSubcategoriesRef.child("Соль, сахар").setValue(true);
        groceriesSubcategoriesRef.child("Приправы").setValue(true);
        groceriesSubcategoriesRef.child("Кондитерские пф").setValue(true);
        groceriesSubcategoriesRef.child("Мука, смеси, компоненты для выпечки").setValue(true);
        groceriesSubcategoriesRef.child("Диетические продукты").setValue(true);
        groceriesSubcategoriesRef.child("Какао").setValue(true);
        groceriesSubcategoriesRef.child("Сухие завтраки").setValue(true);
        groceriesSubcategoriesRef.child("Бульонные кубики").setValue(true);
        groceriesSubcategoriesRef.child("Протеиновые батончики").setValue(true);

        DatabaseReference coffee_and_teaSubcategoriesRef = categoriesRef.child("Кофе и чай").child("subcategories");
        coffee_and_teaSubcategoriesRef.child("Кофе растворимый").setValue(true);
        coffee_and_teaSubcategoriesRef.child("Кофе в зёрнах").setValue(true);
        coffee_and_teaSubcategoriesRef.child("Кофе молотый").setValue(true);
        coffee_and_teaSubcategoriesRef.child("Кофе в капсулах").setValue(true);
        coffee_and_teaSubcategoriesRef.child("Чай").setValue(true);

        DatabaseReference confectionerySubcategoriesRef = categoriesRef.child("Кондитерские изделия").child("subcategories");
        confectionerySubcategoriesRef.child("Конфеты").setValue(true);
        confectionerySubcategoriesRef.child("Шоколад").setValue(true);
        confectionerySubcategoriesRef.child("Вафли").setValue(true);
        confectionerySubcategoriesRef.child("Торты").setValue(true);
        confectionerySubcategoriesRef.child("Кексы и рулеты").setValue(true);
        confectionerySubcategoriesRef.child("Мармелад").setValue(true);
        confectionerySubcategoriesRef.child("Зефир, суфле и маршмеллоу").setValue(true);
        confectionerySubcategoriesRef.child("Пастила").setValue(true);
        confectionerySubcategoriesRef.child("Пирожные").setValue(true);
        confectionerySubcategoriesRef.child("Печенье").setValue(true);
        confectionerySubcategoriesRef.child("Пряники").setValue(true);
        confectionerySubcategoriesRef.child("Восточные сладости").setValue(true);
        confectionerySubcategoriesRef.child("Жевательная резинка").setValue(true);
        confectionerySubcategoriesRef.child("Сладкая выпечка").setValue(true);
        confectionerySubcategoriesRef.child("Баранки и сушки").setValue(true);

        DatabaseReference fruits_and_vegetablesSubcategoriesRef = categoriesRef.child("Фрукты, овощи").child("subcategories");
        fruits_and_vegetablesSubcategoriesRef.child("Фрукты свежие").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Овощи").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Зелень").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Соленья").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Зелёные салаты").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Грибы свежие").setValue(true);
        fruits_and_vegetablesSubcategoriesRef.child("Ягоды свежие").setValue(true);

        DatabaseReference breadSubcategoriesRef = categoriesRef.child("Хлеб").child("subcategories");
        breadSubcategoriesRef.child("Батон").setValue(true);
        breadSubcategoriesRef.child("Пшеничный хлеб").setValue(true);
        breadSubcategoriesRef.child("Ржаной хлеб").setValue(true);
        breadSubcategoriesRef.child("Зерновой хлеб").setValue(true);
        breadSubcategoriesRef.child("Хлебцы хрустящие").setValue(true);
        breadSubcategoriesRef.child("Пшенично-ржаной").setValue(true);

        DatabaseReference frozenSubcategoriesRef = categoriesRef.child("Замороженные продукты").child("subcategories");
        frozenSubcategoriesRef.child("Готовые замороженные блюда").setValue(true);
        frozenSubcategoriesRef.child("Замороженные овощи").setValue(true);
        frozenSubcategoriesRef.child("Замороженные фрукты и ягоды").setValue(true);
        frozenSubcategoriesRef.child("Замороженная рыба").setValue(true);
        frozenSubcategoriesRef.child("Замороженное мясо").setValue(true);
        frozenSubcategoriesRef.child("Замороженная птица").setValue(true);
        frozenSubcategoriesRef.child("Замороженные морепродукты").setValue(true);
        frozenSubcategoriesRef.child("Мороженое").setValue(true);
        frozenSubcategoriesRef.child("Сладкое").setValue(true);
        frozenSubcategoriesRef.child("Замороженные полуфабрикаты").setValue(true);
        frozenSubcategoriesRef.child("Пельмени").setValue(true);
        frozenSubcategoriesRef.child("Вареники").setValue(true);
        frozenSubcategoriesRef.child("Замороженные грибы").setValue(true);
        frozenSubcategoriesRef.child("Тесто замороженное").setValue(true);

        DatabaseReference canned_foodSubcategoriesRef = categoriesRef.child("Консервы").child("subcategories");
        canned_foodSubcategoriesRef.child("Мясные консервы").setValue(true);
        canned_foodSubcategoriesRef.child("Рыбные консервы").setValue(true);
        canned_foodSubcategoriesRef.child("Овощные консервы").setValue(true);
        canned_foodSubcategoriesRef.child("Грибные консервы").setValue(true);
        canned_foodSubcategoriesRef.child("Фруктовые консервы").setValue(true);
        canned_foodSubcategoriesRef.child("Готовые блюда (консервы)").setValue(true);

        DatabaseReference cookerySubcategoriesRef = categoriesRef.child("Кулинария").child("subcategories");
        cookerySubcategoriesRef.child("Готовые салаты").setValue(true);
        cookerySubcategoriesRef.child("Горячие блюда").setValue(true);
        cookerySubcategoriesRef.child("Закуски").setValue(true);
        cookerySubcategoriesRef.child("Суши").setValue(true);

        DatabaseReference drinksSubcategoriesRef = categoriesRef.child("Напитки").child("subcategories");
        drinksSubcategoriesRef.child("Соки, нектары и сокосодержащие напитки").setValue(true);
        drinksSubcategoriesRef.child("Газированная вода").setValue(true);
        drinksSubcategoriesRef.child("Морс, компот, кисель").setValue(true);
        drinksSubcategoriesRef.child("Квас").setValue(true);
        drinksSubcategoriesRef.child("Холодный чай").setValue(true);
        drinksSubcategoriesRef.child("Вода").setValue(true);
    }

    public static void uploadImage(String category, String subcategory, Bitmap bitmap) {
        DatabaseReference productRef = database.getReference("applicationx5/products/");
        DatabaseReference categoryRef = database.getReference("applicationx5/products/categories/" + category + "/subcategories/" + subcategory + "/products");
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to save image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                        categoryRef.child(String.valueOf(barcode)).updateChildren(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
                                Data.addUser(2);
                                Data.addPoints(2, "За добавление изображения");
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
        DatabaseReference userRef = database.getReference("applicationx5/users").child(String.valueOf(userId));

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

    public static void addPoints(int numPoints, String description) {
        DatabaseReference userPointsRef = database.getReference("applicationx5/points_history/" + userId);
        String pointId = userPointsRef.push().getKey();

        Map<String, Object> point = new HashMap<>();
        point.put("id", pointId);
        point.put("user_id", userId);
        point.put("date", ServerValue.TIMESTAMP);
        point.put("points", numPoints);
        point.put("description", description);

        assert pointId != null;
        DatabaseReference pointsRef = userPointsRef.child(pointId).child("points");
        pointsRef.setValue(ServerValue.increment(numPoints));

        userPointsRef.child(pointId).setValue(point);
    }

    public Product findProduct() {
        DatabaseReference productRef = database.getReference("applicationx5/products");
        Query productQuery = productRef.orderByChild("barcode").equalTo(barcode);
        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        assert product != null;
                        String productString = product.getBarcode();
                        Log.d(TAG, "onDataChange: Found product: " + product.getBarcode());
                        foundProduct = product;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "findProduct onCancelled: " + error.getMessage());
            }
        });
        return foundProduct;
    }

    void saveProduct(String category, String subcategory) {
        Map<String, Object> product = new HashMap<>();
        product.put("barcode", barcode);
        product.put("rating", rating);
        product.put("category", category);
        product.put("subcategory", subcategory);

        Map<String, Object> productCategory = new HashMap<>();
        productCategory.put("barcode", barcode);
        productCategory.put("rating", rating);

        DatabaseReference productRef = database.getReference("applicationx5/products/");

        DatabaseReference categoryRef = database.getReference("applicationx5/products/categories/" + category + "/subcategories/" + subcategory + "/products");
        productRef.child(String.valueOf(barcode)).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Product saved successfully", Toast.LENGTH_SHORT).show();
            }
        });
        categoryRef.child(String.valueOf(barcode)).setValue(productCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void getPointsHistory(String userId, long startDate, long endDate) {
        DatabaseReference pointsRef = FirebaseDatabase.getInstance().getReference("points_history/" + userId);
        Query query = pointsRef.orderByChild("date").startAt(startDate).endAt(endDate);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Point> points = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Point point = snapshot.getValue(Point.class);
                    points.add(point);
                }
                // Обработка полученных данных
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPoints:onCancelled", databaseError.toException());
            }
        });
    }
}