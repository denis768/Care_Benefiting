package com.example.myapplication.feature.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.myapplication.R;

import java.io.IOException;
import java.io.InputStream;

public class Help extends AppCompatActivity {

    NestedScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        scrollview = findViewById(R.id.scrollView);

        AssetManager assetManager = getAssets();
        try {
            InputStream input = assetManager.open("help.txt");
            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            String text = new String(buffer);
            TextView helpText = findViewById(R.id.help_text);
            helpText.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}