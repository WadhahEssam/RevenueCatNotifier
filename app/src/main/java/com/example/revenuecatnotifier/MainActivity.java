package com.example.revenuecatnotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button changeLanguageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        changeLanguageButton = findViewById(R.id.button_change_language);
        getWindow().getDecorView().setLayoutDirection(getCurrentLanguage() == "ar" ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);


        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello there");
                switchLanguage();
            }
        });
    }

    public String getCurrentLanguage() {
        if (changeLanguageButton.getText().equals("English")) {
            return "en";
        } else {
            return "ar";
        }
    }

    public void switchLanguage() {
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