package com.sjsu.cmpe277.weatherapp.weatherApi;

import com.sjsu.cmpe277.weatherapp.City;

import org.json.JSONException;

import java.util.Date;
import java.util.List;

/**
 * Created by vivek on 11/3/2017.
 */

public interface WeatherService {
    public City getCurrentWeather(String city, String country) throws JSONException;
    //public City[] getOneDayForecast(String city, String country, String timeZone);
    //public City[] getFiveDayForecast(String city, String country, String timeZone);
    //public String getCity(double latitude, double longitude);
}
