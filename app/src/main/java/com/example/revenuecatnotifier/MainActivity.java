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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView changeLanguageButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        registerElements();
        setLayoutDirection();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Starting the api call");
                final String url = "https://fadfadah.net/ksu-gpa/getRevenueCatInformation2";

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "fadfadahChat@gmail.com");
                params.put("password", "12345");
                params.put("hash", "AwzXlHcpcv-FW7aEyA2Idfadfadah_199_1w_3d0$2.13aPXogm9czt-zHKEZjELznfadfadah_499_1m_1w0TrialFdOl66zFy0-jNGcL4eDbcfadfadah_199_1w_3d0Trial");

                CustomRequest.request(MainActivity.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e(" result", (String.valueOf(response)));
                            System.out.println(String.valueOf(response.get("generalStats")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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