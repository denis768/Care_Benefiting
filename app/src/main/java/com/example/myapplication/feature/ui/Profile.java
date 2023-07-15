package com.example.myapplication.feature.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    public long userId;
    SharedPreferences sharedPreferences;
    TextView profile, balance_points, name;
    Button help;

    private JSONObject userObject;
    private String photoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        userId = sharedPreferences.getLong("vk_id", 0);

        profile = findViewById(R.id.profile);
        help = findViewById(R.id.help);
        balance_points = findViewById(R.id.balance_points);
        name = findViewById(R.id.Name);

        help.setOnClickListener(v -> {
            Intent intent = new Intent(this, Help.class);
            startActivity(intent);
        });


        if (userId != 0) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_ids", userId);
                jsonObject.put("fields", "photo_200_orig");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            VKRequest<JSONObject> request = new VKRequest<>("users.get", "jsonObject");

            VK.execute(request, new VKApiCallback<JSONObject>() {
                @Override
                public void fail(@NonNull Exception e) {
                }

                @Override
                public void success(JSONObject response) {
                    Log.d("VKResponse", response.toString());
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