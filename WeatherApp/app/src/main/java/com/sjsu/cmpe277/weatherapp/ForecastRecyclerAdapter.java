package com.sjsu.cmpe277.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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

public class ForecastRecyclerAdapter extends RecyclerView.Adapter<ForecastRecyclerAdapter.RecylcerViewHolder> {

    WeatherAppDbHelper db;
    List<City> cities = new ArrayList<City>();
    Context ctx;
    String cityName;
    Activity activity;

    public ForecastRecyclerAdapter(Context ctx, String cityName, Activity activity) {
        this.ctx = ctx;
        this.cityName = cityName;
        db = new WeatherAppDbHelper(ctx);
        this.activity=activity;

        Log.e("ForecastRecyclerAdapter","CAME HERE CONSTRUCTOR");
        getForecastDataForCity(cityName);
    }

    public void getForecastDataForCity(String cityName) {

        cities = db.getTodayWeather(cityName);

        Log.e("ForecastRecyclerAdapter","NUmber of cities are "+cities.size());
    }


    @Override
    public ForecastRecyclerAdapter.RecylcerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.today_forecast, parent, false);

        ForecastRecyclerAdapter.RecylcerViewHolder viewHolder = new ForecastRecyclerAdapter.RecylcerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastRecyclerAdapter.RecylcerViewHolder holder, int position) {

        holder.weatherIcon.setText(setWeatherIcon(cities.get(position).getWeatherId().intValue(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        holder.dayTime.setText(cities.get(position).getTempHour());
        holder.weatherDaily.setText(cities.get(position).getCityTemp().intValue()+"°");


        Log.e("ForecastRecyclerAdapter","CAME HERE binding view"+ cities.get(position).getCityTemp() );
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

        TextView dayTime;
        TextView weatherIcon;
        TextView weatherDaily;
        Typeface weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weather.ttf");

        public RecylcerViewHolder(View itemView) {
            super(itemView);
            Log.e("ForecastRecyclerAdapter","CAME HERE CONSTRUCTOR  view holder");
            dayTime = (TextView) itemView.findViewById(R.id.dayTime);
            weatherIcon = (TextView) itemView.findViewById(R.id.weather_icon_daily);
            weatherDaily = (TextView) itemView.findViewById(R.id.weather_temp_daily);
            weatherIcon.setTypeface(weatherFont);
        }
    }

}
