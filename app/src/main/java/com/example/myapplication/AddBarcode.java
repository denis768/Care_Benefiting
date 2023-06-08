package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBarcode extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText barcodeEditText;
    private Button addButton, addPhoto;
    private RatingBar ratingBar;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barcode);

        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
        addPhoto = (Button) findViewById(R.id.addPhoto);
        addButton = (Button) findViewById(R.id.addButton);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        barcodeEditText.setText(getIntent().getStringExtra("barcode"));

        addButton.setOnClickListener(v -> {
            Data data = new Data();
            // Получаем введенные значения штрих-кода и качества
            long barcode = Long.parseLong(barcodeEditText.getText().toString());
            float rating = ratingBar.getRating();

            // Добавляем элемент в TreeMap
            data.addProduct(barcode, rating, AddBarcode.this);

            // Выводим сообщение об успешном добавлении
            Toast.makeText(AddBarcode.this, "Штрих-код успешно добавлен!", Toast.LENGTH_SHORT).show();

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
    ;}

    // Создание временного файла для хранения изображения
    private File createImageFile() throws IOException {
        // Создаем уникальное имя файла на основе даты и времени
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

    // Обработка результата вызова камеры
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Картинка сохранилась в файле currentPhotoPath
            // Используйте это место для загрузки изображения в Firebase Storage
            File imageFile = new File(currentPhotoPath);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        }
    }
}