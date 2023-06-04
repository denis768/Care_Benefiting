package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddBarcode extends Activity {
    private EditText barcodeEditText;
    private Button addButton;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barcode);

        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
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
    }
}