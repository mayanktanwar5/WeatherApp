package com.sjsu.cmpe277.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mak on 11/3/17.
 */

public class ViewPagerHandler implements Serializable {

    private static final String LOG_TAG = "ViewPagerHandler";
    private final Typeface weatherFont;
    private final Typeface fontawesome;
    private ViewPager pager = null;
    private MainPageAdapter pagerAdapter = null;
    Context ctx;
    LayoutInflater inflater;
    Activity activity;
    private List<City> allCities;
    WeatherAppDbHelper db;
    ScrollView v0;
    ActionBar actionBar;
    SimpleDateFormat fmtOut;
    Date date;

    public ViewPagerHandler(Activity activity, Context ctx, ActionBar actionBar) {

        this.ctx = ctx;
        this.activity = activity;
        this.actionBar = actionBar;
        pagerAdapter = new MainPageAdapter();
        pager = (ViewPager) activity.findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weather.ttf");
        fontawesome = Typeface.createFromAsset(activity.getAssets(), "fonts/fontawesome-webfont.ttf");

        fmtOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        getAllCities();
    }

    public void getAllCities() {

        db = new WeatherAppDbHelper(ctx);

        allCities = db.getAllCities();

        for (int i = 0; i < allCities.size(); i++) {

            addCityView(allCities.get(i), i);
        }

        pagerAdapter.notifyDataSetChanged();
    }


    public void addCityView(City city, int position) {

        Log.e(LOG_TAG, "BEFORE ADDING VIEW");
        pagerAdapter.addView(createCityView(city), position);


    }


    public void convertUnits(boolean check) {

        if (check) {


            for (int i = 0; i < pagerAdapter.getCount(); i++) {


                View view = pagerAdapter.getView(i);

                TextView cityTemp = (TextView) view.findViewById(R.id.cityTemp);
                TextView minTempValue = (TextView) view.findViewById(R.id.minTempValue);
                TextView cityMinTemp = (TextView) view.findViewById(R.id.cityMinTemp);
                TextView cityMaxTemp = (TextView) view.findViewById(R.id.cityMaxTemp);
                TextView maxTempValue = (TextView) view.findViewById(R.id.maxTempValue);

                RecyclerView todayWeather = (RecyclerView) view.findViewById(R.id.todayForecastRecycler);
                todayWeather.setAdapter(todayWeather.getAdapter());

                RecyclerView forecastWeather = (RecyclerView) view.findViewById(R.id.futureForecastRecycler);
                forecastWeather.setAdapter(forecastWeather.getAdapter());

                Log.e(LOG_TAG, "COnverting from Celsius to farrheinhit");

                String cityTempValue = cityTemp.getText().toString();
                cityTempValue = cityTempValue.substring(0, cityTempValue.length() - 1);


                String minTempValue1 = minTempValue.getText().toString();
                minTempValue1 = minTempValue1.substring(0, minTempValue1.length() - 1);

                String maxTempValue1 = maxTempValue.getText().toString();
                maxTempValue1 = maxTempValue1.substring(0, maxTempValue1.length() - 1);


                cityTemp.setText(celsiusToFahrenheit(Double.parseDouble(cityTempValue)).intValue() + "°");

                cityMinTemp.setText(celsiusToFahrenheit(Double.parseDouble(minTempValue1)).intValue() + "°");
                cityMaxTemp.setText(celsiusToFahrenheit(Double.parseDouble(maxTempValue1)).intValue() + "°");
                minTempValue.setText(celsiusToFahrenheit(Double.parseDouble(minTempValue1)).intValue() + "°");
                maxTempValue.setText(celsiusToFahrenheit(Double.parseDouble(maxTempValue1)).intValue() + "°");

            }


        } else {


            for (int i = 0; i < pagerAdapter.getCount(); i++) {


                View view = pagerAdapter.getView(i);

                TextView cityTemp = (TextView) view.findViewById(R.id.cityTemp);
                TextView minTempValue = (TextView) view.findViewById(R.id.minTempValue);
                TextView cityMinTemp = (TextView) view.findViewById(R.id.cityMinTemp);
                TextView cityMaxTemp = (TextView) view.findViewById(R.id.cityMaxTemp);
                TextView maxTempValue = (TextView) view.findViewById(R.id.maxTempValue);


                RecyclerView todayWeather = (RecyclerView) view.findViewById(R.id.todayForecastRecycler);
                todayWeather.setAdapter(todayWeather.getAdapter());

                RecyclerView forecastWeather = (RecyclerView) view.findViewById(R.id.futureForecastRecycler);
                forecastWeather.setAdapter(forecastWeather.getAdapter());

                Log.e(LOG_TAG, "COnverting from Farheniet to Celsius");

                String cityTempValue = cityTemp.getText().toString();
                cityTempValue = cityTempValue.substring(0, cityTempValue.length() - 1);


                String minTempValue1 = minTempValue.getText().toString();
                minTempValue1 = minTempValue1.substring(0, minTempValue1.length() - 1);

                String maxTempValue1 = maxTempValue.getText().toString();
                maxTempValue1 = maxTempValue1.substring(0, maxTempValue1.length() - 1);


                cityTemp.setText(fahrenheitToCelsius(Double.parseDouble(cityTempValue)).intValue() + "°");

                cityMinTemp.setText(fahrenheitToCelsius(Double.parseDouble(minTempValue1)).intValue() + "°");
                cityMaxTemp.setText(fahrenheitToCelsius(Double.parseDouble(maxTempValue1)).intValue() + "°");
                minTempValue.setText(fahrenheitToCelsius(Double.parseDouble(minTempValue1)).intValue() + "°");
                maxTempValue.setText(fahrenheitToCelsius(Double.parseDouble(maxTempValue1)).intValue() + "°");

            }
        }
    }

    public Double celsiusToFahrenheit(Double celcius) {
        return celcius * 1.8 + 32;
    }

    public Double fahrenheitToCelsius(Double fahrenheit) {
        return (fahrenheit - 32) / 1.8;
    }


    public View createCityView(City city) {

        //actionBar.setTitle(city.getCityName());

        v0 = (ScrollView) inflater.inflate(R.layout.city_view, null);

        TextView cityTemp = (TextView) v0.findViewById(R.id.cityTemp);
        TextView cityDesc = (TextView) v0.findViewById(R.id.cityDesc);
        TextView cityName = (TextView) v0.findViewById(R.id.cityName);
        TextView weatherIcon = (TextView) v0.findViewById(R.id.weather_icon_main);
        TextView cityMinTemp = (TextView) v0.findViewById(R.id.cityMinTemp);
        TextView cityMaxTemp = (TextView) v0.findViewById(R.id.cityMaxTemp);
        TextView upArrowIcon = (TextView) v0.findViewById(R.id.cityIconMaxTemp);
        TextView downArrowIcon = (TextView) v0.findViewById(R.id.cityIconMinTemp);
        TextView lastUpdated = (TextView) v0.findViewById(R.id.last_refreshed);
        TextView humidityIcon = (TextView) v0.findViewById(R.id.humidityIcon);
        TextView humidityValue = (TextView) v0.findViewById(R.id.humidityValue);
        TextView pressureIcon = (TextView) v0.findViewById(R.id.pressureIcon);
        TextView pressureValue = (TextView) v0.findViewById(R.id.pressureValue);
        TextView minTempIcon = (TextView) v0.findViewById(R.id.minTempIcon);
        TextView minTempValue = (TextView) v0.findViewById(R.id.minTempValue);
        TextView maxTempIcon = (TextView) v0.findViewById(R.id.maxTempIcon);
        TextView maxTempValue = (TextView) v0.findViewById(R.id.maxTempValue);

        RecyclerView todayWeather = (RecyclerView) v0.findViewById(R.id.todayForecastRecycler);
        RecyclerView forecastWeather = (RecyclerView) v0.findViewById(R.id.futureForecastRecycler);

        Log.e("Creating City View", "TEMP====>" + city.getCityTemp());
        Log.e("Creating City View", "DEsC====>" + city.getWeatherDescription());


        cityDesc.setText(city.getWeatherDescription());
        cityName.setText(city.getCityName());


        Log.e(LOG_TAG, "Checkin city temp" + city.getCityMinTemp());

        boolean isCelsius = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("isCelsius", true);

        if (!isCelsius){

            cityTemp.setText(celsiusToFahrenheit(city.getCityTemp()).intValue() + "°");
            cityMinTemp.setText(celsiusToFahrenheit(city.getCityMinTemp()).intValue() + "°");
            cityMaxTemp.setText(celsiusToFahrenheit(city.getCityMaxTemp()).intValue() + "°");
            minTempValue.setText(celsiusToFahrenheit(city.getCityMinTemp()).intValue()+"°");
            maxTempValue.setText(celsiusToFahrenheit(city.getCityMaxTemp()).intValue()+"°");




        } else{

            cityTemp.setText(Integer.toString(city.getCityTemp().intValue()) + "°");
            cityMinTemp.setText(Integer.toString(city.getCityMinTemp().intValue()) + "°");
            cityMaxTemp.setText(Integer.toString(city.getCityMaxTemp().intValue()) + "°");
            minTempValue.setText(Integer.toString(city.getCityMinTemp().intValue())+"°");
            maxTempValue.setText(Integer.toString(city.getCityMaxTemp().intValue())+"°");



        }



        //Details part
        pressureValue.setText(Integer.toString(city.getCityPressure().intValue()));
        humidityValue.setText(Integer.toString(city.getCityHumididty().intValue()));



        //Today Later weather
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        todayWeather.setLayoutManager(layoutManager);

        ForecastRecyclerAdapter mForecastRecyclerAdapter = new ForecastRecyclerAdapter(ctx, city.getCityName(), activity);

        todayWeather.setAdapter(mForecastRecyclerAdapter);

        //Forecast Later weather
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
        forecastWeather.setLayoutManager(layoutManager1);

        FiveDayForecastRecyclerAdapter mFiveDayForecastRecyclerAdapter = new FiveDayForecastRecyclerAdapter(ctx, city.getCityName(), activity);

        forecastWeather.setAdapter(mFiveDayForecastRecyclerAdapter);

        // show current city message

        String currentCity = PreferenceManager.getDefaultSharedPreferences(ctx).getString("currentCity", "");

        if (currentCity.equals(city.getCityName())) {

            TextView isCurrentCity = (TextView) v0.findViewById(R.id.isCurrentCity);

            isCurrentCity.setVisibility(View.VISIBLE);

            updateOtherCities();
        }


        // set all the icons

        weatherIcon.setTypeface(weatherFont);
        downArrowIcon.setTypeface(fontawesome);
        upArrowIcon.setTypeface(fontawesome);
        pressureIcon.setTypeface(weatherFont);
        humidityIcon.setTypeface(weatherFont);
        minTempIcon.setTypeface(weatherFont);
        maxTempIcon.setTypeface(weatherFont);


        // update the last refresh flag

        String lastRefreshed = PreferenceManager.getDefaultSharedPreferences(ctx).getString("lastRefreshed", "");

        Log.e(LOG_TAG, "Last refershed value is " + lastRefreshed);


        try {
            date = fmtOut.parse(lastRefreshed);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e(LOG_TAG, "day is" + date.getDay() + " Month is " + date.getMonth() + "  date");
        lastUpdated.setText(date.getMonth() + "/" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes());
        weatherIcon.setText(setWeatherIcon(city.getWeatherId().intValue(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

        return v0;

    }

    public void notifyDataChanged() {

        pagerAdapter.notifyDataSetChanged();
    }


    public void updateOtherCities() {


        for (int i = 0; i < pagerAdapter.getCount(); i++) {

            View view = pagerAdapter.getView(i);

            TextView item = (TextView) view.findViewById(R.id.isCurrentCity);
            Log.e(LOG_TAG, "Setting the current city false for previous city");

            item.setVisibility(View.INVISIBLE);


        }

    }


    private String setWeatherIcon(int actualId, int hourOfDay) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            if (hourOfDay >= 7 && hourOfDay < 20) {
                icon = activity.getString(R.string.weather_sunny);
            } else {
                icon = activity.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = activity.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = activity.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = activity.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = activity.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = activity.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = activity.getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
    }


    public void addView(View newPage) {
        Log.e(LOG_TAG, "ADDING VIEW");
        int pageIndex = pagerAdapter.addView(newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem(pageIndex, true);
    }

    public void removeView(View defunctPage) {
        int pageIndex = pagerAdapter.removeView(pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem(pageIndex);
    }

    public int getViewCount() {

        return pagerAdapter.getCount();
    }

    public View getCurrentPage() {
        return pagerAdapter.getView(pager.getCurrentItem());
    }

    public View getViewPageAtPosition(int position) {
        return pagerAdapter.getView(position);
    }

    public void setCurrentPage(View pageToShow) {
        Log.e(LOG_TAG, "Setting Currentpage ==>" + pagerAdapter.getItemPosition(pageToShow));
        pager.setCurrentItem(pagerAdapter.getItemPosition(pageToShow), true);
    }

}
