package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Entry extends Activity {
    private long userId;
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
        logIn.setOnClickListener(view -> {
            int appId = getResources().getInteger(R.integer.com_vk_sdk_AppId);
            VK.login(Entry.this, Arrays.asList(VKScope.WALL, VKScope.PHOTOS));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(VKAccessToken token) {
                String userId = String.valueOf(token.getUserId());
                Toast.makeText(Entry.this, "Login success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Entry.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(int errorCode) {
                Toast.makeText(Entry.this, "Login fail", Toast.LENGTH_SHORT).show();
            }
        };

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public long getUserId() {
        return userId;
    }
}