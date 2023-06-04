package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements View.OnClickListener {
    static Button AddBtn;
    public String text;
    Button scanBtn;
    ImageButton imageBtn;
    TextView messageText, messageFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.Scan);
        messageText = findViewById(R.id.Content);
        messageFormat = findViewById(R.id.Format);
        AddBtn = findViewById(R.id.AddBtn);
        imageBtn = findViewById(R.id.imageBtn);

        scanBtn.setOnClickListener(this);
        AddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBarcode.class);
            intent.putExtra("barcode", messageFormat.getText().toString());
            startActivity(intent);
        });
        imageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Наведите камеру на штрих код");
//        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Data dat = new Data();
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Вы вышли", Toast.LENGTH_SHORT).show();
            } else {
                messageFormat.setVisibility(View.VISIBLE);
                messageFormat.setText(intentResult.getContents());
                text = messageFormat.getText().toString();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        Data.getProduct(Long.parseLong(text), this);
        String rating = Data.products.get(Long.parseLong(text));
        if (rating != null) {
            AddBtn.setVisibility(View.INVISIBLE);
            messageText.setText(rating);
            Toast.makeText(this, "Товар найден, смотрите описание", Toast.LENGTH_LONG).show();
        } else {
            AddBtn.setVisibility(View.VISIBLE);
            messageText.setText(null);
            Toast.makeText(this, "Товар не найден вы можете его добавить", Toast.LENGTH_LONG).show();
        }
    }
}