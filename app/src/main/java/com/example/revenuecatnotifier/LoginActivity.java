package com.example.revenuecatnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

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
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
        registerElements();
        setLayoutDirection();
        registerListeners();

        changeStatusBarColor("#FFFFFF");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
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
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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

    private void loginRequest(String email, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        final String url = getResources().getString(R.string.api_url);

        CustomRequest.request(LoginActivity.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                saveToStorage("email", email);
                saveToStorage("password", password);
                saveToStorage("data", response.toString());
                saveToStorage("lastCheck", new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
                navigateToHomeActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                loginError("Please make sure to fill your email and password");
            }
        });
    }

    private void loginError(String errorMessage) {
        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
        if (getFromStorage("language").equals("")) {
            saveToStorage("language", getCurrentLanguage());
        }
        if (getFromStorage("language").equals("ar") && getResources().getString(R.string.other_language).equals("عربي")) {
            switchLanguage();
            return;
        }
        if (getFromStorage("language").equals("en") && getResources().getString(R.string.other_language).equals("English")) {
            switchLanguage();
            return;
        }
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

    private void switchLanguage() {
        saveToStorage("language", getCurrentLanguage());
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