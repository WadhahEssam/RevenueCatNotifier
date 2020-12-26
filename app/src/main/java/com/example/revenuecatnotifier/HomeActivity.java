package com.example.revenuecatnotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private Button logoutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_home);
        registerElements();
        registerListeners();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void registerElements() {
        logoutButton = findViewById(R.id.button_logout);
    }

    private void registerListeners() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.shared_preferences_name), MODE_PRIVATE);
                settings.edit().clear().apply();
                navigateToLoginScreen();
            }
        });
    }

    private void navigateToLoginScreen() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }
}
