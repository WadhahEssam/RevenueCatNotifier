package com.example.revenuecatnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        if (isUserLoggedIn()) {
            navigateToHomeActivity();
        } else {
            navigateToLoginActivity();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private boolean isUserLoggedIn() {
        String email = getFromStorage("email");
        return !email.equals("");
    }

    private void navigateToHomeActivity() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    private void navigateToLoginActivity() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }


    private String getFromStorage(String savedDataTitle) {
        SharedPreferences sharedPreferences = getSharedPreferences("revenue_cat_shared_preferences", MODE_PRIVATE);
        return sharedPreferences.getString(savedDataTitle, "");
    }
}