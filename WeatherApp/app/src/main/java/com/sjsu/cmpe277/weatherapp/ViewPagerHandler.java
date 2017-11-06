package com.sjsu.cmpe277.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mak on 11/3/17.
 */

public class ViewPagerHandler implements Serializable {

    private static final String LOG_TAG = "ViewPagerHandler";
    private ViewPager pager = null;
    private MainPageAdapter pagerAdapter = null;
    Context ctx;
    LayoutInflater inflater;

    private List<City> allCities;

    WeatherAppDbHelper db;

    public ViewPagerHandler(Activity activity, Context ctx) {

        this.ctx = ctx;


        pagerAdapter = new MainPageAdapter();
        pager = (ViewPager) activity.findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

//        FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.city_view, null);
//
//        TextView cityName = (TextView) v0.findViewById(R.id.cityName);
//        cityName.setText(city.getCityName());
//        TextView cityCountry = (TextView) v0.findViewById(R.id.cityAddress);
//        cityCountry.setText(city.getCityCountry());
//        TextView cityLang = (TextView) v0.findViewById(R.id.cityLang);
//        cityLang.setText(Double.toString(city.getCityLatitude()));
//        TextView cityLong = (TextView) v0.findViewById(R.id.cityLong);
//        cityLong.setText(Double.toString(city.getCityLongitude()));
//        ImageView cityImage = (ImageView) v0.findViewById(R.id.cityBgImage);
//
//        Bitmap bmp = BitmapFactory.decodeByteArray(city.getCityImage(), 0, city.getCityImage().length);
//        cityImage.setImageBitmap(bmp);

        //createCityView(city);

        Log.e(LOG_TAG, "BEFORE ADDING VIEW");
        pagerAdapter.addView(createCityView(city), position);


    }


    public View createCityView(City city) {

        FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.city_view, null);

        TextView cityName = (TextView) v0.findViewById(R.id.cityName);
        cityName.setText(city.getCityName());
        TextView cityCountry = (TextView) v0.findViewById(R.id.cityAddress);
        cityCountry.setText(city.getCityCountry());
        TextView cityLang = (TextView) v0.findViewById(R.id.cityLang);
        cityLang.setText(Double.toString(city.getCityLatitude()));
        TextView cityLong = (TextView) v0.findViewById(R.id.cityLong);
        cityLong.setText(Double.toString(city.getCityLongitude()));
        ImageView cityImage = (ImageView) v0.findViewById(R.id.cityBgImage);

        Bitmap bmp = BitmapFactory.decodeByteArray(city.getCityImage(), 0, city.getCityImage().length);
        cityImage.setImageBitmap(bmp);
        return v0;

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
