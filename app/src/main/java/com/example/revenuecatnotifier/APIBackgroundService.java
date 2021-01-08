package com.example.revenuecatnotifier;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIBackgroundService extends Service {

    private static final String TAG = APIBackgroundService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "on Create, Thread " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on Start, Thread " + Thread.currentThread().getName());
        new APIAsyncTask().execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "on Destroy, Thread " + Thread.currentThread().getName());
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "on Bind, Thread " + Thread.currentThread().getName());
        return null;
    }

    class APIAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute, Thread " + Thread.currentThread().getName());
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.i(TAG, "doInBackground, Thread " + Thread.currentThread().getName());

            int NUMBER_OF_MILLI_SECONDS_TO_RUN = 1000 * 60 * 1;
            int secondsCounter = 0; // run every
            runTheTask();
            while (true) {
                try {
                    Thread.sleep(NUMBER_OF_MILLI_SECONDS_TO_RUN);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                secondsCounter++;
                runTheTask();
                publishProgress("Time elapsed : " + NUMBER_OF_MILLI_SECONDS_TO_RUN * secondsCounter / 1000 + " seconds");
            }
        }

        private void runTheTask() {
            String email = getFromStorage("email");
            String password = getFromStorage("password");

            if (!email.equals("") && !password.equals("")) {
                loginRequest(email, password);
            }
        }

        private void loginRequest(String email, String password) {
            Log.i(TAG, "request started");

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            final String url = getResources().getString(R.string.api_url);

            CustomRequest.request(APIBackgroundService.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, response.toString());
                    saveToStorage("data", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            });
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

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate, Counter:" + values[0]+  ", Thread " + Thread.currentThread().getName());
            Toast.makeText(APIBackgroundService.this, values[0].toString(), Toast.LENGTH_SHORT).show();;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i(TAG, "onPostExecute, Thread " + Thread.currentThread().getName());
            stopSelf();
        }
    }
}
