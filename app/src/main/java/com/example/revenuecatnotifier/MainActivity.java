package com.example.revenuecatnotifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        registerElements();
        setLayoutDirection();
        registerListeners();
    }

    private void registerListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Starting the api call");
                loginFormView.setVisibility(View.INVISIBLE);
                loginWarningView.setVisibility(View.INVISIBLE);
                loginWaitingView.setVisibility(View.VISIBLE);
                final String url = "https://fadfadah.net/ksu-gpa/getRevenueCatInformation2";

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "fadfadahChat@gmail.com");
                params.put("password", "12345");
                params.put("hash", "AwzXlHcpcv-FW7aEyA2Idfadfadah_199_1w_3d0$2.13aPXogm9czt-zHKEZjELznfadfadah_499_1m_1w0TrialFdOl66zFy0-jNGcL4eDbcfadfadah_199_1w_3d0Trial");

                CustomRequest.request(MainActivity.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loginFormView.setVisibility(View.VISIBLE);
                            loginWarningView.setVisibility(View.VISIBLE);
                            loginWaitingView.setVisibility(View.INVISIBLE);

                            Log.e(" result", (String.valueOf(response)));
                            System.out.println(String.valueOf(response.get("generalStats")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginFormView.setVisibility(View.VISIBLE);
                        loginWarningView.setVisibility(View.VISIBLE);
                        loginWaitingView.setVisibility(View.INVISIBLE);
                    }
                });
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
        getWindow().getDecorView().setLayoutDirection(getCurrentLanguage() == "ar" ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);
    }

    private void registerElements() {
        changeLanguageButton = findViewById(R.id.button_change_language);
        loginButton = findViewById(R.id.button_login);
        loginWarningView = findViewById(R.id.view_login_warning);
        loginFormView = findViewById(R.id.view_login_form);
        loginWaitingView = findViewById(R.id.view_login_loading);
        loginWaitingView.setVisibility(View.INVISIBLE);
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