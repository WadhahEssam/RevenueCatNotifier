package com.example.revenuecatnotifier;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class APIBackgroundService extends Service {

    private static final String TAG = APIBackgroundService.class.getSimpleName();
    private static int NUMBER_OF_MILLI_SECONDS_TO_RUN = 1000 * 60 * 30;

    @Override
    public void onCreate() {
        super.onCreate();
        new APIAsyncTask().execute();
        Log.i(TAG, "on Create, Thread " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "on Start, Thread " + Thread.currentThread().getName());
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
            params.put("hash", getHash());

            Log.i(TAG, getHash());


            final String url = getResources().getString(R.string.api_url);

            CustomRequest.request(APIBackgroundService.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    saveToStorage("data", response.toString());
                    saveToStorage("lastCheck", new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date()));
                    Boolean hasNewTransactions = JsonParser.parseString(response.toString()).getAsJsonObject().get("hasNewTransactions").getAsBoolean();
                    Log.i(TAG, response.toString());
                    Log.i(TAG, getFromStorage("lastCheck"));

                    if (hasNewTransactions) {
                        sendLocalNotification("New Transactions", "You have received new transactions");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            });
        }

        private void injectNewStats(String response) {
            JsonArray newStats = getData().get("generalData").getAsJsonArray();


            for (int i = 0; i < newStats.size(); i++) {
                System.out.println(newStats.get(i).toString());
            }
        }

        private String getHash() {
            JsonArray transactionsJson = getData().get("transactions").getAsJsonArray();
            Transaction[] transactions = new Transaction[transactionsJson.size()];

            String hash = "";
            for (int i = 0; i < 3; i++) {
                String userID = transactionsJson.get(i).getAsJsonObject().get("userID").getAsString();
                String product = transactionsJson.get(i).getAsJsonObject().get("product").getAsString();
                String revenue = transactionsJson.get(i).getAsJsonObject().get("revenue").getAsString();

                hash += userID + product + revenue;
            }

            return hash;
        }

        private JsonObject getData() {
            return JsonParser.parseString(getFromStorage("data")).getAsJsonObject();
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

        private void sendLocalNotification (String title, String body) {
            Intent intent = new Intent(APIBackgroundService.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(APIBackgroundService.this, 0, intent, 0);

            NotificationCompat.Builder b = new NotificationCompat.Builder(APIBackgroundService.this, "transactionNotification");
            b.setContentTitle(title);
            b.setContentText(body);
            b.setSmallIcon(R.drawable.ic_launcher_foreground);
            b.setAutoCancel(true);
            b.setContentIntent(pendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(APIBackgroundService.this);
            manager.notify(12, b.build());
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "onProgressUpdate, Counter:" + values[0]+  ", Thread " + Thread.currentThread().getName());
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i(TAG, "onPostExecute, Thread " + Thread.currentThread().getName());
            stopSelf();
        }
    }
}
