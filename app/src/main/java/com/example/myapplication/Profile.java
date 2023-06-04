package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class Profile extends Activity {

    Button backBtn;
    TextView profile;
    Button addProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        backBtn = findViewById(R.id.backBtn);
        profile = findViewById(R.id.profile);
        addProfile=findViewById(R.id.addProfile);

        backBtn.setOnClickListener(v-> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}