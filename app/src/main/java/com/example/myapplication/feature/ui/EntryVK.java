package com.example.myapplication.feature.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class EntryVK extends AppCompatActivity {
    public static Long userId = 0L;
    SharedPreferences sharedPreferences;
    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("vk_id", 0);

        setContentView(R.layout.entry);
        logIn = findViewById(R.id.logIn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        logIn.setBackgroundColor(Color.RED);

        logIn.setOnClickListener(view -> {
            int appId = getResources().getInteger(R.integer.com_vk_sdk_AppId);
            VK.login(EntryVK.this, Arrays.asList(VKScope.WALL, VKScope.PHOTOS));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(VKAccessToken token) {
                userId = (long) token.getUserId();
                sharedPreferences.edit().putLong("vk_id", userId).apply();
                Toast.makeText(EntryVK.this, "Вы успешно вошли", Toast.LENGTH_SHORT).show();
                if (userId != 0) {
//                    Data.addPoints(1, "За авторизацию");
//                    Data.addUser(1);
                }
                Intent intent = new Intent(EntryVK.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(int errorCode) {
                Toast.makeText(EntryVK.this, "Возникла ошибка", Toast.LENGTH_SHORT).show();
            }
        };

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}