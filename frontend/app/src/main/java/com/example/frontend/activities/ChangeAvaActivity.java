package com.example.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.frontend.R;

public class ChangeAvaActivity extends AppCompatActivity {

    private Button btnBack, btnChangeAva;

    private ImageView avatarImageView;
    private String avatarUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_ava);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            avatarUrl = intent.getStringExtra("avatarUrl");
        }

        // Load avatar image using Glide
        if (avatarUrl != null) {
            Glide.with(this)
                    .load(avatarUrl)
                    .into(avatarImageView);
        } else {
            // Load default avatar if avatarUrl is null
            Glide.with(this)
                    .load(R.drawable.default_avatar)
                    .into(avatarImageView);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to the previous activity or fragment
            }
        });
    }

    private void initView() {
        btnBack = findViewById(R.id.button_back);
        btnChangeAva = findViewById(R.id.button_change_avatar);
        avatarImageView = findViewById(R.id.image_avatar);
    }


}