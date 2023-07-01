package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBarcode extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText barcodeEditText;
    private Spinner categorySpinner, productSpinner;
    private RatingBar ratingBar;
    private String currentPhotoPath;
    String productName;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barcode);

//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
        Button addPhoto = (Button) findViewById(R.id.addPhoto);
        Button addButton = (Button) findViewById(R.id.addButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        barcodeEditText.setText(getIntent().getStringExtra("barcode"));

//        Data.CreateDB();

        categorySpinner = findViewById(R.id.category_spinner);
        productSpinner = findViewById(R.id.product_spinner);

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("applicationx5/products/categories");
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Получаем список категорий
                List<String> categories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    categoryName = categorySnapshot.getKey();
                    categories.add(categoryName);
                }

                // Обновляем выпадающий список категорий
                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(AddBarcode.this, android.R.layout.simple_spinner_item, categories);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoriesAdapter);

                // Устанавливаем слушатель выбора категории
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Получаем выбранную категорию
                        categoryName = parent.getItemAtPosition(position).toString();

                        // Получаем ссылку на узел продуктов для выбранной категории
                        DatabaseReference productsRef = categoriesRef.child(categoryName).child("subcategories");

                        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Получаем список продуктов
                                List<String> products = new ArrayList<>();
                                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                                    productName = productSnapshot.getKey();
                                    products.add(productName);
                                }

                                // Обновляем выпадающий список продуктов
                                ArrayAdapter<String> productsAdapter = new ArrayAdapter<>(AddBarcode.this, android.R.layout.simple_spinner_item, products);
                                productsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                productSpinner.setAdapter(productsAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Обработка ошибки получения данных
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Обработка, если ничего не выбрано
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибки получения данных
            }
        });

        addButton.setOnClickListener(v -> {
            long barcode = 0;
            float rating = 0;
            barcode = Long.parseLong(barcodeEditText.getText().toString());
            rating = ratingBar.getRating();
            productName = productSpinner.getSelectedItem().toString();

            // Добавляем элемент в DB
            Data data = new Data(barcode, rating, AddBarcode.this);
            data.saveProduct(categoryName, productName);

            // Выводим сообщение об успешном добавлении
            Toast.makeText(AddBarcode.this, "Штрих-код успешно добавлен!", Toast.LENGTH_SHORT).show();
            Data.addUser(3);
            Data.addPoints(3, "За добавление штрих кода");

            // Очищаем поля ввода
            barcodeEditText.setText("");
            finish();
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Вызов камеры при помощи интента
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Создаем временный файл для хранения изображения
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Обработка ошибки создания временного файла
                        ex.printStackTrace();
                    }
                    // Продолжаем только если файл был успешно создан
                    if (photoFile != null) {
                        Uri photoUri = FileProvider.getUriForFile(AddBarcode.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        })
        ;
    }

    // Создание временного файла для хранения изображения
    private File createImageFile() throws IOException {
        // Созлаётся имя файла на основе даты и времени
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // Сохраняем путь к временному файлу
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Картинка сохранилась в файле currentPhotoPath
            File imageFile = new File(currentPhotoPath);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            productName = productSpinner.getSelectedItem().toString();
            Data.uploadImage(categoryName, productName, imageBitmap);
        }
    }
}