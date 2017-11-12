package com.sjsu.cmpe277.weatherapp.weatherApi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sjsu.cmpe277.weatherapp.City;
import com.sjsu.cmpe277.weatherapp.CityTimezone;
import com.sjsu.cmpe277.weatherapp.CurrentCity;
import com.sjsu.cmpe277.weatherapp.RecyclerAdapter;
import com.sjsu.cmpe277.weatherapp.ViewPagerHandler;
import com.sjsu.cmpe277.weatherapp.WeatherAppDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Mak on 11/11/17.
 */

public class DataProcessor {


    Double lat;
    Double lng;
    Activity activity;
    Context ctx;
    private ProgressDialog progressDialog;
    private static final String LOG_TAG = "DataProcessor";
    // Database Helper
    WeatherAppDbHelper db;
    private WeatherService mWeatherService;
    private ViewPagerHandler viewPagerHandler;
    private RecyclerAdapter recyclerAdapter;
    SimpleDateFormat mSimpleDateFormat;
    SharedPreferences sp;


    public DataProcessor(Double lat, Double lng, Activity activity, Context ctx, ProgressDialog progressDialog, ViewPagerHandler viewPagerHandler, RecyclerAdapter recyclerAdapter) {
        this.lat = lat;
        this.lng = lng;
        this.activity = activity;
        this.ctx = ctx;
        this.progressDialog = progressDialog;
        this.viewPagerHandler = viewPagerHandler;
        this.recyclerAdapter=recyclerAdapter;

        sp = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public void processTimeZoneData(boolean locationDetect) {



        try {

            Log.e(LOG_TAG, "CALLING current city  longitude "+lng+ " latitude "+lat);
            String s = new CurrentCity(progressDialog, ctx, activity).execute(Double.toString(lat), Double.toString(lng)).get();

            Long tsLong = System.currentTimeMillis() / 1000;
            String timeStamp = tsLong.toString();
            String timezone = new CityTimezone(progressDialog, ctx, activity).execute(Double.toString(lat), Double.toString(lng), timeStamp).get();
            String cityNameRes = "";
            String cityCountryRes = "";

            Log.e(LOG_TAG, "CALLING current city ka result is " + s);
            try {

                // City name JSON Parser

                JSONObject jsonObject = new JSONObject(s);

                JSONArray results = (JSONArray) jsonObject.get("results");

                JSONArray address_components = (JSONArray) results.getJSONObject(0).getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {

                    JSONObject item = address_components.getJSONObject(i);

                    JSONArray types = item.getJSONArray("types");

                    if (types.getString(0).equals("locality")) {
                        cityNameRes = item.getString("long_name");

                    }

                    if (types.getString(0).equals("country")) {
                        cityCountryRes = item.getString("long_name");
                    }
                }

                // Timezone JSON Parser

                JSONObject timezoneJsonObject = new JSONObject(timezone);
                String timzoneId = timezoneJsonObject.getString("timeZoneId");


                Log.e(LOG_TAG, "CALLING current city name is  " + cityNameRes + "currentCIty country " + cityCountryRes);
                if (!cityNameRes.equals("") && !cityCountryRes.equals("") && !timzoneId.equals("")) {

                    Log.e(LOG_TAG, "ENtered all matched   " + cityNameRes + "currentCIty country " + cityCountryRes);


//                    SharedPreferences sp;
                    String lastCity = PreferenceManager.getDefaultSharedPreferences(ctx).getString("currentCity", "");


                    ;
                    if(locationDetect){

                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("currentCity", cityNameRes);
                        editor.putBoolean("isCityChanged", !cityNameRes.equals(lastCity));
                        editor.commit();
                    }


                    getForecastAndAdd(cityNameRes, cityCountryRes, timzoneId);
                    progressDialog.dismiss();


                } else {
                    progressDialog.dismiss();

                    Toast.makeText(ctx, "Unexpected error please try later",
                            Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public void getForecastAndAdd(String cityNameRes, String cityCountryRes, String timzoneId) {


        db = new WeatherAppDbHelper(ctx);
        boolean x = db.getCityByName(cityNameRes);
        Log.e(LOG_TAG, "get city by name  and city name is " + cityNameRes + " found it ==>" + x);
        if (!x) {

            mWeatherService = new WeatherServiceImpl();
            try {

                City city = mWeatherService.getCurrentWeather(cityNameRes, cityCountryRes,timzoneId);

                Log.e(LOG_TAG, "city OBJECT  MIn temp" + city.getCityMinTemp());
                Log.e(LOG_TAG, "city OBJECT  MAx temp" + city.getCityMaxTemp());
                long returnRowID = db.createCity(city);

                Log.e(LOG_TAG, "added the city in DB" + returnRowID);


                List<City> oneDayForcast = mWeatherService.getForecast(cityNameRes, cityCountryRes, timzoneId, "one");

                long returnOneDayRowId = db.createTodayWeather(oneDayForcast);

                Log.e(LOG_TAG, "added the one day forecast in DB" + returnOneDayRowId);

                List<City> fiveDayForcast = mWeatherService.getForecast(cityNameRes, cityCountryRes, timzoneId, "five");

                long returnFiveDayRowId = db.createForecastWeather(fiveDayForcast);

                Log.e(LOG_TAG, "added the five day forecast in DB" + returnFiveDayRowId);

                SharedPreferences.Editor editor = sp.edit();
                mSimpleDateFormat = new SimpleDateFormat("MMM d hh:mm a");
                editor.putString("lastRefreshed", mSimpleDateFormat.format(new Date()));
                editor.commit();

                String lastRefreshed = PreferenceManager.getDefaultSharedPreferences(ctx).getString("lastRefreshed", "");

                Log.e(LOG_TAG, lastRefreshed);

                viewPagerHandler.addCityView(city, viewPagerHandler.getViewCount() );
                viewPagerHandler.notifyDataChanged();
                viewPagerHandler.setCurrentPage(viewPagerHandler.getViewPageAtPosition(viewPagerHandler.getViewCount()-1));

                recyclerAdapter.cities.add(city);
                recyclerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                db.closeDB();
            }
        } else {

            Toast.makeText(ctx, "City " + cityNameRes + " is already in View",
                    Toast.LENGTH_LONG).show();


        }
    }

}
