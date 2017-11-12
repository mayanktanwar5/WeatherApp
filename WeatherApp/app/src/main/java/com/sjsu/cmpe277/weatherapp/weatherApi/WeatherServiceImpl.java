package com.sjsu.cmpe277.weatherapp.weatherApi;

import android.os.AsyncTask;
import android.util.Log;

import com.sjsu.cmpe277.weatherapp.City;

import net.aksingh.owmjapis.DailyForecast;
import net.aksingh.owmjapis.OpenWeatherMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by vivek on 11/4/2017.
 */

public class WeatherServiceImpl implements WeatherService {

    SimpleDateFormat mSimpleDatFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    OpenWeatherMap.Units units = (true)
            ? OpenWeatherMap.Units.METRIC
            : OpenWeatherMap.Units.IMPERIAL;


    OpenWeatherMap owm = new OpenWeatherMap(units, "647f4536db207983f6f2345572c9492f");

    @Override
    public City getCurrentWeather(String city, String country,String timezone) throws JSONException {
        DailyWeather dailyWeather = null;
        JSONObject weatherInfo = weatherInfo("http://api.openweathermap.org/data/2.5/weather?q=" + getSuitableLocation(city) + "," + getSuitableLocation(country) + "&units=metric" + "&appid=647f4536db207983f6f2345572c9492f", true);

        City cityObj = currentWeatherJsonParser(weatherInfo);
        cityObj.setCityCountry(country);
        Log.e("Setting timezone"," TIMEZONE get is "+timezone);
        cityObj.setTimeZone(timezone);

        return cityObj;


    }

    private String getSuitableLocation(String location) {
        String[] locationList = location.split("\\s+");
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < locationList.length - 1; i++) {
            build.append(locationList[i]);
            build.append("%20");
        }
        if (locationList.length > 0) {
            build.append(locationList[locationList.length - 1]);
        }
        return build.toString();
    }

    @Override
    public List<City> getForecast(String city, String country, String timeZone, String forecastType) {
        JSONObject forecast = null;
        List<City> weathers = new ArrayList<>();
        int j = 0;
        Log.e("WeatherSercice","getting the forecast data");

        try {

            forecast = weatherInfo("http://api.openweathermap.org/data/2.5/forecast?q=" + getSuitableLocation(city) + "," + getSuitableLocation(country) + "&units=metric" +"&appid=647f4536db207983f6f2345572c9492f", true);

            String cityName = forecast.getJSONObject("city").getString("name");
            int cityId = forecast.getJSONObject("city").getInt("id");

            JSONArray forecastArray = forecast.getJSONArray("list");

              City cityRes;
                if (forecastType.equals("one")) {

                    Log.e("WeatherSercice","getting the forecast equals ===> ONE");

                    for (int i = 0; i < 9; i++) {
                        JSONObject jsonObject = forecastArray.getJSONObject(i);


                        cityRes = forecastWeatherJsonParser(jsonObject,cityName,cityId);

                        String[] res = TimezoneConverter(jsonObject.getString("dt"), timeZone);
                        cityRes.setTempHour(res[1] + " " + res[3].toLowerCase());
                        cityRes.setTimeZone(timeZone);
                        weathers.add(cityRes) ;
                    }

                } else if (forecastType.equals("five")) {


                    Log.e("WeatherSercice","getting the forecast equals ===> five");
                    for (int i = 0; i < forecastArray.length(); i++) {
                        JSONObject jsonObject = forecastArray.getJSONObject(i);
                        String[] res = TimezoneConverter(jsonObject.getString("dt"), timeZone);

                        if (Integer.parseInt(res[4]) > 11 && Integer.parseInt(res[4]) < 16) {



                            City temp =  getCurrentWeather(city,country,timeZone);

                            Log.e("WeatherSercice","met the condition running for ===> "+j);
                            cityRes = forecastWeatherJsonParser(jsonObject,cityName,cityId);


                            cityRes.setTempDay(res[0]);
                            cityRes.setTimeZone(timeZone);
                            cityRes.setTempMonthDay(res[2]);
                            cityRes.setCityMaxTemp(temp.getCityMaxTemp());
                            cityRes.setCityMinTemp(temp.getCityMinTemp());

                            weathers.add(cityRes);
                            j++;
                        }

                    }
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weathers;
    }


    private String[] TimezoneConverter(String timeStamp, String timeZone) {


        String[] results = new String[5];

        String dayPattern = "EEE";
        String hourPattern = "hh";
        String monthDayPattern = "MMM d";
        String am_pmPattern = "aa";
        String hourCheckPattern="HH";

        Date d = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa zzz");
        String UTCdate = dateFormat.format(d);

        Log.e("TIMZONE facts ","TIMEZONE is ===>"+timeZone+"timestamp is ===>"+timeStamp);


        Log.e("TIMZONE facts ","UTCdate++++++"+UTCdate);

        Date d1 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss aa zzz");
        dateFormat1.setTimeZone(TimeZone.getTimeZone(timeZone));
        String TimezoneConversion = dateFormat.format(d1);

        Log.e("TIMZONE facts ","TimezoneConversion++++++"+TimezoneConversion);



        Date d5 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat sam_pmPattern = new SimpleDateFormat(am_pmPattern);
        sam_pmPattern.setTimeZone(TimeZone.getTimeZone(timeZone));
        String amPm = sam_pmPattern.format(d5);

        Log.e("TIMZONE facts ","AM_PM++++++"+amPm);
        results[3] = amPm;



        Date d6 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat checkPatt = new SimpleDateFormat(hourCheckPattern);
        checkPatt.setTimeZone(TimeZone.getTimeZone(timeZone));
        String check = checkPatt.format(d6);

        Log.e("TIMZONE facts ","hourCheckPattern++++++"+check);
        results[4] = check;


        Date d3 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat shourPattern = new SimpleDateFormat(hourPattern);
        shourPattern.setTimeZone(TimeZone.getTimeZone(timeZone));
        String hour = shourPattern.format(d3);

        Log.e("TIMZONE facts ","hour++++++"+hour);
        results[1] = hour;


        Date d2 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat sdfDayPattern = new SimpleDateFormat(dayPattern);
        sdfDayPattern.setTimeZone(TimeZone.getTimeZone(timeZone));
        String day = sdfDayPattern.format(d2);

        Log.e("TIMZONE facts ","DAY++++++"+day);

        results[0] = day;



        Date d4 = new Date((long) Integer.parseInt(timeStamp) * 1000);
        SimpleDateFormat smonthDayPattern = new SimpleDateFormat(monthDayPattern);
        smonthDayPattern.setTimeZone(TimeZone.getTimeZone(timeZone));
        String monthDay = smonthDayPattern.format(d4);
        Log.e("TIMZONE facts ","MONTH-DAY++++++"+monthDay);
        results[2] = monthDay;


        return results;

    }


    //@Override
//    public DailyWeather[] getFiveDayForecast(String city, String country,String timeZone) {
//        JSONArray forecast=weatherForecastInfo("http://api.openweathermap.org/data/2.5/forecast?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&appid=647f4536db207983f6f2345572c9492f");
//        DailyWeather [] weathers=new DailyWeather[5];
//        Date lv_localDate = null;
//        int j=0;
//        try {
//            for (int i = 0; i < forecast.length(); i++) {
//                JSONObject jsonObject = new JSONObject(forecast.getJSONObject(i).get("main").toString());
//                JSONArray weatherObj = new JSONArray(forecast.getJSONObject(i).get("weather").toString());
//                JSONObject weather=new JSONObject(weatherObj.getJSONObject(0).toString());
//                System.out.println("Current Date :"+forecast.getJSONObject(i).get("dt_txt").toString());
//
//                String UTCdate=forecast.getJSONObject(i).get("dt_txt").toString()+" UTC";
//                String formatPattern = "yyyy-MM-dd hh:mm:ss zzz";
//
//                SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
//                Date date=sdf.parse(UTCdate);
//
//                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
//                formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
//
//
//
//// Prints the date in the CET timezone
//                System.out.println(timeZone+" :"+formatter.format(date));
//
//                int hours=Integer.parseInt(formatter.format(date).substring(12,14));
//                System.out.println("Hours :"+hours);
//                SimpleDateFormat sdfAns=new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
//                Date dateobj=sdfAns.parse(formatter.format(date));
//                if(hours>11 && hours<16){
//                    DailyWeather dailyWeather = new DailyWeather(Float.parseFloat(jsonObject.get("temp").toString()),
//                            Float.parseFloat(jsonObject.get("temp_min").toString()),
//                            Float.parseFloat(jsonObject.get("temp_max").toString()),
//                            dateobj,
//                            city,
//                            weather.get("main").toString());
//                            weathers[j++]=dailyWeather;
//                }
//            }
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//        }
//        System.out.println("Weather length :"+weathers.length);
//        return weathers;
//    }


    private JSONObject weatherInfo(String url, boolean mainData) throws JSONException {

        JSONObject jsonObject = null;
        try {
            jsonObject = new NetworkUtil(mainData).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        Log.e("WEATHERSRVICE", "received the json data" + jsonObject);
        return jsonObject;
    }

    private City currentWeatherJsonParser(JSONObject jsonObject) throws JSONException {

        JSONObject mainObj = new JSONObject(jsonObject.getString("main"));
        JSONObject weatherObj = jsonObject.getJSONArray("weather").getJSONObject(0);

        String cityName = jsonObject.getString("name");
        int cityId = jsonObject.getInt("id");
        City city = new City(cityName, cityId);

        city.setCityTemp(Double.parseDouble(mainObj.getString("temp")));
        city.setCityHumididty(Double.parseDouble(mainObj.getString("humidity")));
        city.setCityMinTemp(Double.parseDouble(mainObj.getString("temp_min")));
        city.setCityMaxTemp(Double.parseDouble(mainObj.getString("temp_max")));
        city.setCityPressure(Double.parseDouble(mainObj.getString("pressure")));
        city.setWeatherDescription(weatherObj.getString("description"));
        city.setWeatherId(Double.parseDouble(weatherObj.getString("id")));
        city.setWeatherMain(weatherObj.getString("main"));
        city.setWeatherIcon(weatherObj.getString("icon"));


        return city;
    }

    private City forecastWeatherJsonParser(JSONObject jsonObject, String cityName, int cityId) throws JSONException {

        JSONObject mainObj = new JSONObject(jsonObject.getString("main"));
        JSONObject weatherObj = jsonObject.getJSONArray("weather").getJSONObject(0);


        Log.e("L","Inside forecastWeatherJsonParser service forecast");
        City city = new City(cityName, cityId);

        city.setCityTemp(Double.parseDouble(mainObj.getString("temp")));
        city.setCityHumididty(Double.parseDouble(mainObj.getString("humidity")));
        city.setCityMinTemp(Double.parseDouble(mainObj.getString("temp_min")));
        city.setCityMaxTemp(Double.parseDouble(mainObj.getString("temp_max")));
        city.setCityPressure(Double.parseDouble(mainObj.getString("pressure")));
        city.setWeatherDescription(weatherObj.getString("description"));
        city.setWeatherId(Double.parseDouble(weatherObj.getString("id")));
        city.setWeatherMain(weatherObj.getString("main"));
        city.setWeatherIcon(weatherObj.getString("icon"));


        return city;
    }



}
