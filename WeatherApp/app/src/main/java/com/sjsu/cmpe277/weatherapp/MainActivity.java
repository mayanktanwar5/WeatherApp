package com.sjsu.cmpe277.weatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.sjsu.cmpe277.weatherapp.weatherApi.WeatherService;
import com.sjsu.cmpe277.weatherapp.weatherApi.WeatherServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private LocationManager locationManager;
    protected static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private static final String LOG_TAG = "MainActivity";
    private List<City> allCities;
    LayoutInflater inflater;
    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerViewAdapter;
    private ViewPagerHandler viewPagerHandler;
    private WeatherService mWeatherService;

    private TextView editPencil;
    private TextView unitCelsius;
    private TextView unitFharenheit;
    private Switch unitCswitch;
    private Switch unitFswitch;
    SharedPreferences sp;
    SimpleDateFormat mSimpleDateFormat;
    // Database Helper
    WeatherAppDbHelper db;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        editPencil = (TextView) drawerLayout.findViewById(R.id.editPencil);
        unitCelsius = (TextView) drawerLayout.findViewById(R.id.tempUnitCelsius);
        unitFharenheit = (TextView) drawerLayout.findViewById(R.id.tempUnitFarhenheit);

        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        editPencil.setTypeface(fontawesome);
        unitCelsius.setTypeface(weatherFont);
        unitFharenheit.setTypeface(weatherFont);


        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.toggle_open, R.string.toggle_close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String currentCity = PreferenceManager.getDefaultSharedPreferences(this).getString("currentCity", "");


        viewPagerHandler = new ViewPagerHandler(this, this, getSupportActionBar());
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerAdapter(getApplicationContext(), drawerLayout, viewPagerHandler, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        db = new WeatherAppDbHelper(getApplicationContext());




        if (db.getAllCities().size() <= 0) {

            getCityByLocation();
        }


        //unit conversion handling

        unitCswitch = (Switch) drawerLayout.findViewById(R.id.tempCSwitch);


        // check for default settings on start

        boolean isCelsius = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isCelsius", true);

        if (!isCelsius) {
            unitCswitch.setChecked(false);
        }


        unitCswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // convert to F
                if (!isChecked) {

                    viewPagerHandler.convertUnits(true);

                    recyclerView.setAdapter(recyclerViewAdapter);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isCelsius", false);
                    editor.commit();

                } else {
                    viewPagerHandler.convertUnits(false);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isCelsius", true);
                    editor.commit();
                }

            }
        });


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG_TAG, "On restart");
        //viewPagerHandler.getAllCities();
        recyclerViewAdapter.setAllCities();
        recyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        if (id == R.id.action_add_city) {
            Intent i = new Intent(this, AddCityActivity.class);
            startActivity(i);
        }

        if (id == R.id.action_add_current_city) {

            getCityByLocation();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void getCityByLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explanation not needed, since user requests this themmself

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }

        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.getting_location));
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        locationManager.removeUpdates(MainActivity.this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });
            progressDialog.show();
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.e(LOG_TAG, "In isProviderEnabled Network");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e(LOG_TAG, "In isProviderEnabled GPS_PROVIDER wfgggggg");
                Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.e(LOG_TAG, "In isProviderEnabled GPS_PROVIDER  lat ==>" + l);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } else {
            showLocationSettingsDialog();
        }
    }


    private void showLocationSettingsDialog() {

        Log.e(LOG_TAG, "In showLocationSettingsDialog");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCityByLocation();
                }
                return;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        progressDialog.setMessage("Fetching City Name");

        double lat = location.getLatitude();
        double lng = location.getLongitude();


        try {

            Log.e(LOG_TAG, "CALLING current city");
            String s = new CurrentCity(progressDialog, this, this).execute(Double.toString(lat), Double.toString(lng)).get();

            Long tsLong = System.currentTimeMillis() / 1000;
            String timeStamp = tsLong.toString();
            String timezone = new CityTimezone(progressDialog, this, this).execute(Double.toString(lat), Double.toString(lng), timeStamp).get();
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
                    progressDialog.dismiss();
                    String lastCity = PreferenceManager.getDefaultSharedPreferences(this).getString("currentCity", "");

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("currentCity", cityNameRes);
                    editor.putBoolean("isCityChanged", !cityNameRes.equals(lastCity));
                    editor.commit();

                    // check if city exist in DB

                    db = new WeatherAppDbHelper(getApplicationContext());
                    boolean x = db.getCityByName(cityNameRes);
                    Log.e(LOG_TAG, "get city by name  and city name is " + cityNameRes + " found it ==>" + x);
                    if (!x) {

                        mWeatherService = new WeatherServiceImpl();
                        try {

                            City city = mWeatherService.getCurrentWeather(cityNameRes, cityCountryRes);

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

                            editor = sp.edit();
                            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            editor.putString("lastRefreshed", mSimpleDateFormat.format(new Date()));
                            editor.commit();

                            String lastRefreshed = PreferenceManager.getDefaultSharedPreferences(this).getString("lastRefreshed", "");

                            Log.e(LOG_TAG, lastRefreshed);

                            viewPagerHandler.addCityView(city, viewPagerHandler.getViewCount());
                            viewPagerHandler.notifyDataChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Toast.makeText(this, "City " + cityNameRes + " is already in View",
                                Toast.LENGTH_LONG).show();


                    }

                } else {
                    progressDialog.dismiss();

                    Toast.makeText(this, "Unexpected error please try later",
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
