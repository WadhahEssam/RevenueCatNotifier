package com.example.revenuecatnotifier;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CustomRequest {
    public static void request(Context context, int method, String url, Map params, Response.Listener responseListener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest theRequest = new JsonObjectRequest(method, url, new JSONObject(params),
                responseListener
                ,
                errorListener
        );
        requestQueue.add(theRequest);
    }
}
