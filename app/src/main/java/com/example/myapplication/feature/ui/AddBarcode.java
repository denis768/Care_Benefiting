package com.example.myapplication.feature.ui;

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

import com.example.myapplication.R;
import com.example.myapplication.data.Data;
import com.example.myapplication.data.barcode.CategoriesManager;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBarcode extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String productName;
//    String categoryName;
    private EditText barcodeEditText;
    private Spinner productSpinner;
    private RatingBar ratingBar;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barcode);

        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
        Button addPhoto = (Button) findViewById(R.id.addPhoto);
        Button addButton = (Button) findViewById(R.id.addButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        barcodeEditText.setText(getIntent().getStringExtra("barcode"));

        Spinner categorySpinner = findViewById(R.id.category_spinner);
        productSpinner = findViewById(R.id.product_spinner);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CategoriesManager.getCategoriesList());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categoryAdapter.getItem(position);
                List<String> subcategories = CategoriesManager.getSubcategories(selectedCategory);

                ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, subcategories);
                subcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                productSpinner.setAdapter(subcategoryAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddBarcode.this, "Вы ничего не выбрали", Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(v -> {
            long barcode = 0;
            float rating = 0;
            barcode = Long.parseLong(barcodeEditText.getText().toString());
            rating = ratingBar.getRating();
            productName = productSpinner.getSelectedItem().toString();

            // Добавляем элемент в DB
            Data.saveProduct();

//            Data.addUser(3);
//            Data.addPoints(3, "За добавление штрих кода");

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
//            Data.uploadImage(categoryName, productName, imageBitmap);
        }
    }
}