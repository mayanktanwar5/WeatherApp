package com.sjsu.cmpe277.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mak on 11/4/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecylcerViewHolder> {

    private static final String LOG_TAG = "RecyclerAdapter";
    // Database Helper
    WeatherAppDbHelper db;
    List<City> cities = new ArrayList<City>();
    Context ctx;

    public RecyclerAdapter(Context ctx) {
        this.ctx = ctx;

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

        RecylcerViewHolder viewHolder = new RecylcerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecylcerViewHolder holder, int position) {

        holder.cityAdress.setText(cities.get(position).getCityCountry());
        holder.cityName.setText(cities.get(position).getCityName());
        Bitmap bmp = BitmapFactory.decodeByteArray(cities.get(position).getCityImage(), 0, cities.get(position).getCityImage().length);

        holder.cityImage.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class RecylcerViewHolder extends RecyclerView.ViewHolder {

        public int currentItem;
        public ImageView cityImage;
        public TextView cityName;
        public TextView cityAdress;

        public RecylcerViewHolder(View itemView) {

            super(itemView);

            cityImage = (ImageView) itemView.findViewById(R.id.card_image);
            cityName = (TextView) itemView.findViewById(R.id.card_city_name);
            cityAdress = (TextView) itemView.findViewById(R.id.card_city_address);
        }


    }
}
