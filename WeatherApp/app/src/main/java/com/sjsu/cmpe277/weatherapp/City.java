package com.sjsu.cmpe277.weatherapp;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mak on 10/28/17.
 */

public class City implements Serializable{
    private static final String LOG_TAG = "CityClass";
    private String cityName;
    private String cityCountry;
    private String googleCityId;
    private int owId;
    private double cityLongitude;
    private double cityLatitude;
    private double cityTemp;
    private Date timestamp;

    private double cityMinTemp;
    private double cityMaxTemp;
    private String cityTempMetric;
    private double cityHumididty;
    private double cityPressure;
    private double cityWindSpeed;
    private double cityDegrees;
    private double weatherId;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;


    private byte[] cityImage;

    public City(String googleCityId) {
        this.googleCityId = googleCityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public void setCityLongitude(double cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public void setCityLatitude(double cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    public void setCityImage(byte[] cityImage) {
        this.cityImage = cityImage;
    }

    public void setOwId(int owId) {
        this.owId = owId;
    }

    public void setCityMinTemp(double cityMinTemp) {
        this.cityMinTemp = cityMinTemp;
    }

    public void setCityMaxTemp(double cityMaxTemp) {
        this.cityMaxTemp = cityMaxTemp;
    }

    public void setCityTempMetric(String cityTempMetric) {
        this.cityTempMetric = cityTempMetric;
    }

    public void setCityHumididty(double cityHumididty) {
        this.cityHumididty = cityHumididty;
    }

    public void setCityPressure(double cityPressure) {
        this.cityPressure = cityPressure;
    }

    public void setCityWindSpeed(double cityWindSpeed) {
        this.cityWindSpeed = cityWindSpeed;
    }

    public void setCityDegrees(double cityDegrees) {
        this.cityDegrees = cityDegrees;
    }

    public double getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(double cityTemp) {
        this.cityTemp = cityTemp;
    }

    public City(String cityName, String cityCountry, String googleCityId, double cityLongitude, double cityLatitude, byte[] cityImage) {
        this.cityName = cityName;
        this.cityCountry = cityCountry;
        this.googleCityId = googleCityId;
        this.cityLongitude = cityLongitude;
        this.cityLatitude = cityLatitude;
        this.cityImage = cityImage;

    }

    public City(String cityName, int owId) {
        this.cityName = cityName;
        this.cityCountry = cityCountry;
        this.owId = owId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public String getGoogleCityId() {
        return googleCityId;
    }

    public int getOwId() {
        return owId;
    }

    public double getCityLongitude() {
        return cityLongitude;
    }

    public double getCityLatitude() {
        return cityLatitude;
    }

    public double getCityMinTemp() {
        return cityMinTemp;
    }

    public double getCityMaxTemp() {
        return cityMaxTemp;
    }

    public String getCityTempMetric() {
        return cityTempMetric;
    }

    public double getCityHumididty() {
        return cityHumididty;
    }

    public double getCityPressure() {
        return cityPressure;
    }

    public double getCityWindSpeed() {
        return cityWindSpeed;
    }

    public double getCityDegrees() {
        return cityDegrees;
    }

    public byte[] getCityImage() {
        return cityImage;
    }


    public void setGoogleCityId(String googleCityId) {
        this.googleCityId = googleCityId;
    }

    public double getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(double weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
