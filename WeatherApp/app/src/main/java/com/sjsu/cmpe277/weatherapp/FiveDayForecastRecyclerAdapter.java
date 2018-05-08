package com.sjsu.cmpe277.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mak on 11/10/17.
 */

public class FiveDayForecastRecyclerAdapter extends RecyclerView.Adapter<FiveDayForecastRecyclerAdapter.RecylcerViewHolder> {

    WeatherAppDbHelper db;
    List<City> cities = new ArrayList<City>();
    Context ctx;
    String cityName;
    Activity activity;

    public FiveDayForecastRecyclerAdapter(Context ctx, String cityName, Activity activity) {
        this.ctx = ctx;
        this.cityName = cityName;
        db = new WeatherAppDbHelper(ctx);
        this.activity=activity;

        Log.e("FiveDayForecastAdapter","CAME HERE CONSTRUCTOR");
        getForecastDataForCity(cityName);
    }

    public void getForecastDataForCity(String cityName) {

        cities = db.getForecastWeather(cityName);

        Log.e("FiveDayForecastAdapter","NUmber of cities are "+cities.size());
    }


    @Override
    public FiveDayForecastRecyclerAdapter.RecylcerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fiveday_forecast, parent, false);

        FiveDayForecastRecyclerAdapter.RecylcerViewHolder viewHolder = new FiveDayForecastRecyclerAdapter.RecylcerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FiveDayForecastRecyclerAdapter.RecylcerViewHolder holder, int position) {

        holder.weatherIcon.setText(setWeatherIcon(cities.get(position).getWeatherId().intValue(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

//        holder.weatherDaily.setText(cities.get(position).getCityTemp().intValue()+"°");

        holder.weatherDay.setText(cities.get(position).getTempDay());
        holder.weatherMonth.setText(cities.get(position).getTempMonthDay());
        boolean isCelsius = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("isCelsius", true);

        if(!isCelsius){
            holder.weatherDaily.setText(celsiusToFahrenheit(cities.get(position).getCityTemp()).intValue() + "°");
            holder.weatherMaxTemp.setText(celsiusToFahrenheit(cities.get(position).getCityMaxTemp()).intValue() + "°");
            holder.weatherMinTemp.setText(celsiusToFahrenheit(cities.get(position).getCityMinTemp()).intValue() + "°");
        }
        else{
            holder.weatherDaily.setText(cities.get(position).getCityTemp().intValue()+"°");
            holder.weatherMaxTemp.setText(cities.get(position).getCityMaxTemp().intValue()+"°");
            holder.weatherMinTemp.setText(cities.get(position).getCityMinTemp().intValue()+"°");

        }

        Log.e("FiveDayForecastAdapter","CAME HERE binding view"+ cities.get(position).getCityTemp() );
    }


    public Double celsiusToFahrenheit(Double celcius) {
        return celcius * 1.8 + 32;
    }


    @Override
    public int getItemCount() {
        return cities.size();
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



    public class RecylcerViewHolder extends RecyclerView.ViewHolder {

        TextView weatherIcon;
        TextView weatherDaily;
        TextView weatherDay;
        TextView weatherMonth;
        TextView weatherMaxTemp;
        TextView weatherMinTemp;
        TextView weatherMaxTempIcon;
        TextView weatherMinIcon;

        Typeface weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weather.ttf");
        Typeface fontawesome = Typeface.createFromAsset(activity.getAssets(), "fonts/fontawesome-webfont.ttf");

        public RecylcerViewHolder(View itemView) {
            super(itemView);
            Log.e("FiveDayForecastAdapter","CAME HERE CONSTRUCTOR  view holder");
            weatherIcon = (TextView) itemView.findViewById(R.id.weather_icon_forecast);
            weatherDaily = (TextView) itemView.findViewById(R.id.weather_temp_forecast);
            weatherDay = (TextView) itemView.findViewById(R.id.dayForecast);
            weatherMonth = (TextView) itemView.findViewById(R.id.dayMonthForecast);
            weatherMaxTemp = (TextView) itemView.findViewById(R.id.weather_max_temp_forecast);
            weatherMinTemp = (TextView) itemView.findViewById(R.id.weather_min_temp_forecast);
            weatherMaxTempIcon = (TextView) itemView.findViewById(R.id.weather_max_temp_forecast_icon);
            weatherMinIcon = (TextView) itemView.findViewById(R.id.weather_min_temp_forecast_icon);

            weatherIcon.setTypeface(weatherFont);
            weatherMinIcon.setTypeface(fontawesome);
            weatherMaxTempIcon.setTypeface(fontawesome);
        }
    }

}
