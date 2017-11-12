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
    private Double cityLongitude;
    private Double cityLatitude;
    private Double cityTemp;
    private Date timestamp;

    private Double cityMinTemp;
    private Double cityMaxTemp;
    private String cityTempMetric;
    private Double cityHumididty;
    private Double cityPressure;
    private Double cityWindSpeed;
    private Double cityDegrees;
    private Double weatherId;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private String tempDay;
    private String tempHour;
    private String tempMonthDay;
    private String timeZone;

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

    public void setCityLongitude(Double cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public void setCityLatitude(Double cityLatitude) {
        this.cityLatitude = cityLatitude;
    }

    public void setCityImage(byte[] cityImage) {
        this.cityImage = cityImage;
    }

    public void setOwId(int owId) {
        this.owId = owId;
    }

    public void setCityMinTemp(Double cityMinTemp) {
        this.cityMinTemp = cityMinTemp;
    }

    public void setCityMaxTemp(Double cityMaxTemp) {
        this.cityMaxTemp = cityMaxTemp;
    }

    public void setCityTempMetric(String cityTempMetric) {
        this.cityTempMetric = cityTempMetric;
    }

    public void setCityHumididty(Double cityHumididty) {
        this.cityHumididty = cityHumididty;
    }

    public void setCityPressure(Double cityPressure) {
        this.cityPressure = cityPressure;
    }

    public void setCityWindSpeed(Double cityWindSpeed) {
        this.cityWindSpeed = cityWindSpeed;
    }

    public void setCityDegrees(Double cityDegrees) {
        this.cityDegrees = cityDegrees;
    }

    public Double getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(Double cityTemp) {
        this.cityTemp = cityTemp;
    }

    public City(String cityName, String cityCountry, String googleCityId, Double cityLongitude, Double cityLatitude, byte[] cityImage) {
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

    public Double getCityLongitude() {
        return cityLongitude;
    }

    public Double getCityLatitude() {
        return cityLatitude;
    }

    public Double getCityMinTemp() {
        return cityMinTemp;
    }

    public Double getCityMaxTemp() {
        return cityMaxTemp;
    }

    public String getCityTempMetric() {
        return cityTempMetric;
    }

    public Double getCityHumididty() {
        return cityHumididty;
    }

    public Double getCityPressure() {
        return cityPressure;
    }

    public Double getCityWindSpeed() {
        return cityWindSpeed;
    }

    public Double getCityDegrees() {
        return cityDegrees;
    }

    public byte[] getCityImage() {
        return cityImage;
    }


    public void setGoogleCityId(String googleCityId) {
        this.googleCityId = googleCityId;
    }

    public Double getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Double weatherId) {
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

    public String getTempDay() {
        return tempDay;
    }

    public void setTempDay(String tempDay) {
        this.tempDay = tempDay;
    }

    public String getTempHour() {
        return tempHour;
    }

    public void setTempHour(String tempHour) {
        this.tempHour = tempHour;
    }

    public String getTempMonthDay() {
        return tempMonthDay;
    }

    public void setTempMonthDay(String tempMonthDay) {
        this.tempMonthDay = tempMonthDay;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
