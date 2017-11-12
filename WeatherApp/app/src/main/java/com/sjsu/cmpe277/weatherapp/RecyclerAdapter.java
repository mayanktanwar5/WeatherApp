package com.sjsu.cmpe277.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mak on 11/4/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecylcerViewHolder> {

    private static final String LOG_TAG = "RecyclerAdapter";
    // Database Helper
    WeatherAppDbHelper db;
    public List<City> cities = new ArrayList<City>();
    Context ctx;
    private   DrawerLayout mDrawerLayout;
    private ViewPagerHandler mViewPageHandler;
    Activity activity;

    public RecyclerAdapter(Context ctx, DrawerLayout  drawerLayout, ViewPagerHandler viewPagerHandler, Activity activity) {
        this.ctx = ctx;
        this.mDrawerLayout =drawerLayout;
        this.mViewPageHandler=viewPagerHandler;

        this.activity=activity;

        Log.e(LOG_TAG,"ViewPagehandler page Recylcelradapter"+viewPagerHandler);
        Log.e(LOG_TAG,"Constructor called for RecyclerAdapter");
        db = new WeatherAppDbHelper(ctx);
        setAllCities();

    }

    public void setAllCities(){

        cities=db.getAllCities();
    }

    @Override
    public RecylcerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_card, parent, false);

        RecylcerViewHolder viewHolder = new RecylcerViewHolder(view, mDrawerLayout,mViewPageHandler,activity,cities);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecylcerViewHolder holder, int position) {

        holder.cityTemp.setText(Integer.toString(cities.get(position).getCityTemp().intValue())+"°");
        holder.cityName.setText(cities.get(position).getCityName());
        holder.weatherIcon.setText(setWeatherIcon(cities.get(position).getWeatherId().intValue(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

        boolean isCelsius = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("isCelsius", true);

        if(!isCelsius){
            holder.cityTemp.setText(celsiusToFahrenheit(cities.get(position).getCityTemp()).intValue() + "°");
        }
        else{
            holder.cityTemp.setText(cities.get(position).getCityTemp().intValue()+"°");
        }


        boolean isEditClicked = PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("isEdit", false);

        if(isEditClicked){

            holder.trashicon.setVisibility(View.VISIBLE);
        }else{

            holder.trashicon.setVisibility(View.GONE);
        }

    }


    public Double celsiusToFahrenheit(Double celcius) {
        return celcius * 1.8 + 32;
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



    @Override
    public int getItemCount() {
        return cities.size();
    }

//    public View getViewAtPosition(int position){
//
//
//    }

    public  class RecylcerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public int currentItem;
        public TextView cityName;
        public TextView cityTemp;
        public TextView weatherIcon;
        TextView trashicon;
        private   DrawerLayout mDrawerLayout;
        private ViewPagerHandler mViewPagerHandler;
        Typeface fontawesome;


        public RecylcerViewHolder(View itemView, DrawerLayout drawerLayout , ViewPagerHandler viewPagerHandler, Activity activity, final List<City> cities)  {

            super(itemView);

            this.mDrawerLayout=drawerLayout;
            this.mViewPagerHandler=viewPagerHandler;
            Typeface weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weather.ttf");
            fontawesome = Typeface.createFromAsset(activity.getAssets(), "fonts/fontawesome-webfont.ttf");
            cityTemp = (TextView) itemView.findViewById(R.id.cardWeatherTemp);
            cityName = (TextView) itemView.findViewById(R.id.cardWeatherCityName);
            weatherIcon = (TextView) itemView.findViewById(R.id.cardWeatherIcon);
            trashicon =(TextView)itemView.findViewById(R.id.deleteButton);
            weatherIcon.setTypeface(weatherFont);
            trashicon.setTypeface(fontawesome);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.e(LOG_TAG,"Item clicked position is "+position);

                    if(v.findViewById(R.id.deleteButton).getVisibility()==View.GONE){

                        Log.e(LOG_TAG,"ViewPagehandler page"+mViewPagerHandler);
                        mViewPagerHandler.setCurrentPage(mViewPagerHandler.getViewPageAtPosition(position));
                        mDrawerLayout.closeDrawer(Gravity.START,false);
                    } else if(v.findViewById(R.id.deleteButton).getVisibility()==View.VISIBLE){

                       cities.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,cities.size());

                        mViewPagerHandler.removeView(mViewPagerHandler.getViewPageAtPosition(position));

                        TextView cityN = (TextView) v.findViewById(R.id.cardWeatherCityName);
                        db.deleteCity(cityN.getText().toString());
                        db.deleteTodayForecastWeather(cityN.getText().toString());
                        db.deleteTodayWeather(cityN.getText().toString());


                    }


                }
            });
        }


        @Override
        public void onClick(View v) {
        }
    }
}
