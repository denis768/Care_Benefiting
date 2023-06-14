package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    static Long userId = EntryVK.getUserId();
    TextView profile;
    Button addProfile;

    private JSONObject userObject;
    private String photoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        profile = findViewById(R.id.profile);
        addProfile = findViewById(R.id.addProfile);

        if (userId != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_ids", userId);
                jsonObject.put("fields", "photo_200_orig");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            VKRequest request = new VKRequest("users.get", "jsonObject");

            VK.execute(request, new VKApiCallback<JSONObject>() {
                @Override
                public void fail(@NonNull Exception e) {
                }

                @Override
                public void success(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("response");
                        if (jsonArray.length() > 0) {
                            userObject = jsonArray.getJSONObject(0);
                            photoUrl = userObject.optString("photo_200_orig");
                            loadProfilePhoto();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Извините, но вы не авторизованы", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Profile.this, EntryVK.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void loadProfilePhoto() {
        ImageView imageView = findViewById(R.id.profilePhoto);
        if (photoUrl != null) {
            Glide.with(this).load(photoUrl).into(imageView);
        }
    }
}