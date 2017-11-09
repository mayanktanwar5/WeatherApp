package com.sjsu.cmpe277.weatherapp.weatherApi;

import java.util.Date;

/**
 * Created by vivek on 11/4/2017.
 */

public class DailyWeather {
    private float currentTemperature;
    private float minTemperature;
    private float maxTemperature;
    private Date timestamp;
    private String city;
    private String weatherStatus;

    public DailyWeather(float currentTemperature, float minTemperature, float maxTemperature, Date timestamp, String city, String weatherStatus) {
        this.currentTemperature = currentTemperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.timestamp = new Date(timestamp.getTime());
        this.city = city;
        this.weatherStatus = weatherStatus;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherStatus() {
        return weatherStatus;
    }

    public void setWeatherStatus(String weatherStatus) {
        this.weatherStatus = weatherStatus;
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "currentTemperature=" + currentTemperature +
                ", minTemperature=" + minTemperature +
                ", maxTemperature=" + maxTemperature +
                ", timestamp=" + timestamp +
                ", city='" + city + '\'' +
                ", weatherStatus='" + weatherStatus + '\'' +
                '}';
    }
}

