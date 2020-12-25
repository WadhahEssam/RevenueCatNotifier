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

public class MainActivity extends AppCompatActivity {

    private TextView changeLanguageButton;
    private Button loginButton;
    private LinearLayout loginWarningView;
    private LinearLayout loginFormView;
    private LinearLayout loginWaitingView;
    private EditText emailTextEdit;
    private EditText passwordTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUserLoggedIn()) {
            navigateToHomeActivity();
        } else {
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            registerElements();
            setLayoutDirection();
            registerListeners();
        }
    }

    private boolean isUserLoggedIn() {
        String email = getFromStorage("email");
        return !email.equals("");
    }

    private void navigateToHomeActivity() {
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    private String getFromStorage(String savedDataTitle) {
        SharedPreferences sharedPreferences = getSharedPreferences("revenue_cat_shared_preferences", MODE_PRIVATE);
        return sharedPreferences.getString(savedDataTitle, "");
    }

    private void saveToStorage(String savedDataTitle, String savedDataValue) {
        SharedPreferences sharedPreferences = getSharedPreferences("revenue_cat_shared_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(savedDataTitle, savedDataValue);
        editor.apply();
    }

    private void loginRequest(String email, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        final String url = "https://fadfadah.net/ksu-gpa/getRevenueCatInformation2";

        CustomRequest.request(MainActivity.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    saveToStorage("email", email);
                    saveToStorage("password", password);
                    loginFormView.setVisibility(View.VISIBLE);
                    loginWarningView.setVisibility(View.VISIBLE);
                    loginWaitingView.setVisibility(View.INVISIBLE);
                    Log.e(" result", (String.valueOf(response)));
                    System.out.println(String.valueOf(response.get("generalStats")));
                    navigateToHomeActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginError("Please make sure to fill your email and password");
            }
        });
    }

    private void loginError(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        loginFormView.setVisibility(View.VISIBLE);
        loginWarningView.setVisibility(View.VISIBLE);
        loginWaitingView.setVisibility(View.INVISIBLE);
    }

    private void registerListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();

                if (email.equals("") || password.equals("")) {
                    loginError(getResources().getString(R.string.login_error_empty_fields));
                } else {
                    loginFormView.setVisibility(View.INVISIBLE);
                    loginWarningView.setVisibility(View.INVISIBLE);
                    loginWaitingView.setVisibility(View.VISIBLE);
                    loginRequest(email, password);
                }
            }
        });

        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
            }
        });
    }

    private void setLayoutDirection() {
        getWindow().getDecorView().setLayoutDirection(getCurrentLanguage().equals("ar") ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);
    }

    private void registerElements() {
        changeLanguageButton = findViewById(R.id.button_change_language);
        loginButton = findViewById(R.id.button_login);
        loginWarningView = findViewById(R.id.view_login_warning);
        loginFormView = findViewById(R.id.view_login_form);
        loginWaitingView = findViewById(R.id.view_login_loading);
        loginWaitingView.setVisibility(View.INVISIBLE);
        emailTextEdit = findViewById(R.id.edit_text_email);
        passwordTextEdit = findViewById(R.id.edit_text_password);
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