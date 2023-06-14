package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EntryVK extends AppCompatActivity {
    public static Long userId;
    private Toolbar toolbar;

    Button logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VK.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.entry);
        logIn = (Button) findViewById(R.id.logIn);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                Toast.makeText(EntryVK.this, "Login success", Toast.LENGTH_SHORT).show();
                Data.addPoints(1);
                Data.addUser(1);
                Intent intent = new Intent(EntryVK.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            @Override
            public void onLoginFailed(int errorCode) {
                Toast.makeText(EntryVK.this, "Login fail", Toast.LENGTH_SHORT).show();
            }
        };

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static Long getUserId() {
        return userId;
    }
}