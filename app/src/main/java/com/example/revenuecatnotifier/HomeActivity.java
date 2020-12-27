package com.example.revenuecatnotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private TextView logoutButton;
    private TextView switchLanguageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_home);
        registerElements();
        registerListeners();
        setLayoutDirection();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void registerElements() {
        logoutButton = findViewById(R.id.button_logout);
        switchLanguageButton = findViewById(R.id.button_home_switch_langauge);
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

        switchLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
            }
        });
    }

    private void navigateToLoginScreen() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    private void setLayoutDirection() {
        getWindow().getDecorView().setLayoutDirection(getCurrentLanguage().equals("ar") ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);
    }

    private String getCurrentLanguage() {
        if (switchLanguageButton.getText().equals("English")) {
            return "en";
        } else {
            return "ar";
        }
    }

    private void switchLanguage() {
        Locale myLocale = new Locale(getCurrentLanguage());
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }
}
