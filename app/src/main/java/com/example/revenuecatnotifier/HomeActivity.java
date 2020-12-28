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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private TextView logoutButton;
    private TextView switchLanguageButton;
    private TextView trailsValue;
    private TextView subscriptionsValue;
    private TextView mRRValue;
    private TextView revenueValue;
    private TextView activeUsersValue;
    private TextView installsValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_home);
        registerElements();
        registerListeners();
        setLayoutDirection();
        fillData();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void fillData() {
        fillGeneralStats();
    }

    private JsonObject getData() {
        return JsonParser.parseString(getFromStorage("data")).getAsJsonObject();
    }

    private void fillGeneralStats() {
        JsonArray generalStats = getData().get("generalStats").getAsJsonArray();

        String activeTrails = generalStats.get(0).getAsJsonObject().get("Active Trials").getAsString();
        String activeSubscriptions = generalStats.get(1).getAsJsonObject().get("Active Subscriptions").getAsString();
        String mRR = generalStats.get(2).getAsJsonObject().get("MRR").getAsString();
        String revenue = generalStats.get(3).getAsJsonObject().get("Revenue").getAsString();
        String installs = generalStats.get(4).getAsJsonObject().get("Installs").getAsString();
        String activeUsers = generalStats.get(5).getAsJsonObject().get("Active Users").getAsString();

        trailsValue.setText(activeTrails);
        subscriptionsValue.setText(activeSubscriptions);
        mRRValue.setText(mRR);
        revenueValue.setText(revenue);
        installsValue.setText(installs);
        activeUsersValue.setText(activeUsers);
    }

    private String getFromStorage(String savedDataTitle) {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences_name), MODE_PRIVATE);
        return sharedPreferences.getString(savedDataTitle, "");
    }

    private void saveToStorage(String savedDataTitle, String savedDataValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_preferences_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedDataTitle, savedDataValue);
        editor.apply();
    }

    private void registerElements() {
        logoutButton = findViewById(R.id.button_logout);
        switchLanguageButton = findViewById(R.id.button_home_switch_langauge);
        trailsValue = findViewById(R.id.text_stats_trails_body);
        subscriptionsValue = findViewById(R.id.text_stats_subscriptions_body);
        mRRValue = findViewById(R.id.text_stats_mrr_body);
        subscriptionsValue = findViewById(R.id.text_stats_subscriptions_body);
        revenueValue = findViewById(R.id.text_stats_revenue_body);
        activeUsersValue = findViewById(R.id.text_stats_active_users_body);
        installsValue = findViewById(R.id.text_stats_installs_body);
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
