package com.sjsu.cmpe277.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mak on 11/9/17.
 */

public class CurrentCity extends AsyncTask<String, ProgressDialog, String> {


    ProgressDialog progressDialog;
    Context context;
    MainActivity activity;

    public CurrentCity(ProgressDialog progressDialog, Context context, MainActivity activity) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.activity = activity;

        Log.e("CURRENT_CITY","CALLING current city  calleddddddd");
    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response;

            String lat = params[0];
            String lng = params[1];
            try {
                response = getLatLongByURL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&result_type=locality&key=AIzaSyADzh8-Aceet-UVblUgau5y0jE7GbtN6eQ");
                Log.d("response",""+response);
                Log.e("CURRENT_CITY","RESPONSE AFTER GETTING CITY DETAILS"+response);
                return response;
            } catch (Exception e) {
                return String.valueOf(e);
            }
        }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }


    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
