package com.sjsu.cmpe277.weatherapp.weatherApi;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mak on 11/7/17.
 */

public class NetworkUtil extends AsyncTask<String,String,JSONObject> {

private Boolean mainData;

    public NetworkUtil(Boolean mainData) {
        this.mainData = mainData;
    }
    private static final String LOG_TAG = "NetworkUtil";

    @Override
    protected JSONObject doInBackground(String... strings) {

        String url = strings[0];
        JSONObject jsonObject=new JSONObject();

        try {
            URL openWeatherURL=new URL(url);
            HttpURLConnection conn = (HttpURLConnection) openWeatherURL.openConnection();
            if(conn.getResponseCode()==200){

                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader=new InputStreamReader(responseBody);
                BufferedReader br=new BufferedReader(responseBodyReader);
                StringBuilder buff=new StringBuilder();
                String line="";

                while((line=br.readLine())!=null)
                    buff.append(line);

                try {

                    Log.e(LOG_TAG,"Returned value for city search"+buff.toString());
                     jsonObject = new JSONObject(buff.toString());

//                    JSONObject dataObj=new JSONObject(jsonObject.getString("main"));
//                    JSONObject cityId = jsonObject.getString("id");
//
//
//
//                    if(!mainData){
//                        jsonObjects[0]=jsonObject;
//                        return jsonObjects;
//                    }
//                    jsonObjects[0]=dataObj;
//                    jsonObjects[1]=jsonObject.getJSONArray("weather").getJSONObject(0);
//                    jsonObjects[2]=cityId;
//
//                    System.out.println("Temperature :"+dataObj.getString("temp"));
//                    System.out.println("Pressure :"+dataObj.getString("pressure"));
//                    System.out.println("Humidity :"+dataObj.getString("humidity"));
//                    System.out.println("Min Temp :"+dataObj.getString("temp_min"));
//                    System.out.println("Max Temp :"+dataObj.getString("temp_max"));
                }
                catch (JSONException jsonException){
                    jsonException.printStackTrace();
                }
                conn.disconnect();

            }else{
                System.out.println("Error processing request :"+conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
