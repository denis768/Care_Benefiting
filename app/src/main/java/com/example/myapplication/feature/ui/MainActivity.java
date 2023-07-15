package com.example.myapplication.feature.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.example.myapplication.R;
import com.example.myapplication.data.Data;
import com.example.myapplication.data.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button AddBtn;
    public String text;
    Button scanBtn;
    ImageButton imageBtn;
    TextView messageText, messageFormat;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = findViewById(R.id.Scan);
        messageText = findViewById(R.id.Content);
        messageFormat = findViewById(R.id.Format);
        AddBtn = findViewById(R.id.AddBtn);
        imageBtn = findViewById(R.id.imageBtn);
        nestedScrollView = findViewById(R.id.my_nested_scroll_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        ViewCompat.setNestedScrollingEnabled(nestedScrollView, false);
        AddBtn.setVisibility(View.INVISIBLE);

        scanBtn.setOnClickListener(this);
        AddBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBarcode.class);
            intent.putExtra("barcode", messageFormat.getText().toString());
            startActivity(intent);
            AddBtn.setVisibility(View.INVISIBLE);
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

        if (data != null && data.getExtras() != null) {
            long barcode = 0;
            final float[] rating = {0};

            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    Toast.makeText(getBaseContext(), "Вы вышли", Toast.LENGTH_SHORT).show();
                } else {
                    messageFormat.setVisibility(View.VISIBLE);
                    messageFormat.setText(intentResult.getContents());
                    String textView = messageFormat.getText().toString();
                    if (!TextUtils.isEmpty(textView)) {
                        try {
                            barcode = Long.parseLong(textView);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Некорректный штрих-код", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(this, "Некорректный штрих-код", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Data.findProduct(barcode, new Callback<Barcode>() {
                @Override
                public void onResponse(@NonNull Call<Barcode> call, @NonNull Response<Barcode> response) {
                    Barcode product = response.body();
                    if (product != null) {
                        float rating = product.getRating();
                        if (rating != 0) {
                            AddBtn.setVisibility(View.INVISIBLE);
                            messageText.setVisibility(View.VISIBLE);
                            messageText.setText(Float.toString(rating));
                            Toast.makeText(getApplicationContext(), "Товар найден, смотрите описание", Toast.LENGTH_LONG).show();
                        } else {
                            AddBtn.setVisibility(View.VISIBLE);
                            messageText.setVisibility(View.INVISIBLE);
                            messageText.setText(null);
                            nestedScrollView.setNestedScrollingEnabled(false);
                            Toast.makeText(getApplicationContext(), "Товар найден, но оценки нет", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        AddBtn.setVisibility(View.VISIBLE);
                        messageText.setVisibility(View.INVISIBLE);
                        messageText.setText(null);
                        nestedScrollView.setNestedScrollingEnabled(false);
                        Toast.makeText(getApplicationContext(), "Товар не найден, Вы можете его добавить", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Barcode> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ошибка во время запроса", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "Не удалось получить данные", Toast.LENGTH_SHORT).show();
        }
    }
}