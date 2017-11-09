package com.sjsu.cmpe277.weatherapp.weatherApi;

import java.util.Date;
import java.util.List;

/**
 * Created by vivek on 11/3/2017.
 */

public interface WeatherService {
    public DailyWeather getCurrentWeather(String city, String country);
    public DailyWeather[] getOneDayForecast(String city, String country, String timeZone);
    public DailyWeather[] getFiveDayForecast(String city, String country, String timeZone);
    public String getCity(double latitude, double longitude);
}
