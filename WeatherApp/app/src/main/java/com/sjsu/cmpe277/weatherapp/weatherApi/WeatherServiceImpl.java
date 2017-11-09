package com.sjsu.cmpe277.weatherapp.weatherApi;

import android.os.AsyncTask;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static android.provider.Settings.System.DATE_FORMAT;

/**
 * Created by vivek on 11/4/2017.
 */

public class WeatherServiceImpl    implements WeatherService  {

    OpenWeatherMap.Units units = (true)
            ? OpenWeatherMap.Units.METRIC
            : OpenWeatherMap.Units.IMPERIAL;



    OpenWeatherMap owm = new OpenWeatherMap(units, "647f4536db207983f6f2345572c9492f");
    @Override
    public DailyWeather getCurrentWeather(String city, String country) {
        DailyWeather dailyWeather=null;
        JSONObject[] weatherInfo=weatherInfo("http://api.openweathermap.org/data/2.5/weather?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&appid=647f4536db207983f6f2345572c9492f",true);
        try{
        dailyWeather=new DailyWeather(Float.parseFloat(weatherInfo[0].getString("temp")),
                Float.parseFloat(weatherInfo[0].getString("temp_min")),
                Float.parseFloat(weatherInfo[0].getString("temp_max")),
                new Date(),
                city,
                weatherInfo[1].getString("main"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return dailyWeather;
    }

    public String getCity(double latitude,double longitude){
        System.out.println("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=647f4536db207983f6f2345572c9492f");
        JSONObject[] weatherInfo=weatherInfo("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=647f4536db207983f6f2345572c9492f",false);
        try{
            System.out.println("weather :"+weatherInfo[0].toString());
            return weatherInfo[0].getString("name");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getSuitableLocation(String location) {
        String []locationList=location.split("\\s+");
        StringBuilder build=new StringBuilder();
        for(int i=0;i<locationList.length-1;i++){
            build.append(locationList[i]);
            build.append("%20");
        }
        if(locationList.length>0){
            build.append(locationList[locationList.length-1]);
        }
        return build.toString();
    }

    @Override
    public DailyWeather[] getOneDayForecast(String city, String country, String timeZone) {
        JSONArray forecast=weatherForecastInfo("http://api.openweathermap.org/data/2.5/forecast?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&appid=647f4536db207983f6f2345572c9492f");
        DailyWeather [] weathers=new DailyWeather[9];

        try {
//            DailyForecast forecastToday = owm.dailyForecastByCityName(city, forecastDays);
////            System.out.println("Weather for: " + forecastToday.getCityInstance().getCityName());
//            int numForecasts = forecastToday.getForecastCount();
//            for (int i = 0; i < numForecasts; i++) {
//                DailyForecast.Forecast dayForecast = forecastToday.getForecastInstance(i);
//                DailyForecast.Forecast.Temperature temperature = dayForecast.getTemperatureInstance();
//                System.out.println("\t" + dayForecast.getDateTime());
//                System.out.println("\tTemperature: " + temperature.getMinimumTemperature() +
//                        " to " + temperature.getMaximumTemperature() + "\n");
//            }

            Date currentDate=new Date();
            for (int i = 0; i < 9; i++) {
                JSONObject jsonObject = new JSONObject(forecast.getJSONObject(i).get("main").toString());
                JSONObject weather = new JSONObject(forecast.getJSONObject(i).getJSONArray("weather").getJSONObject(0).toString());

                String UTCdate=forecast.getJSONObject(i).get("dt_txt").toString()+" UTC";
                String formatPattern = "yyyy-MM-dd hh:mm:ss zzz";

                SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
                Date date=sdf.parse(UTCdate);

                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

                System.out.println(timeZone+" :"+formatter.format(date));
                SimpleDateFormat sdfAns=new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                Date dateobj=sdfAns.parse(formatter.format(date));

                DailyWeather dailyWeather = new DailyWeather(Float.parseFloat(jsonObject.get("temp").toString()),

                        Float.parseFloat(jsonObject.get("temp_min").toString()),
                        Float.parseFloat(jsonObject.get("temp_max").toString()),
                        dateobj,
                        city,
                        weather.get("main").toString());
                weathers[i]=dailyWeather;
                currentDate.setTime(currentDate.getTime()+3600 * 3000);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
//        catch (IOException ex){
//            ex.printStackTrace();
//        }
        return weathers;
    }

    @Override
    public DailyWeather[] getFiveDayForecast(String city, String country,String timeZone) {
        JSONArray forecast=weatherForecastInfo("http://api.openweathermap.org/data/2.5/forecast?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&appid=647f4536db207983f6f2345572c9492f");
        DailyWeather [] weathers=new DailyWeather[5];
        Date lv_localDate = null;
        int j=0;
        try {
            for (int i = 0; i < forecast.length(); i++) {
                JSONObject jsonObject = new JSONObject(forecast.getJSONObject(i).get("main").toString());
                JSONArray weatherObj = new JSONArray(forecast.getJSONObject(i).get("weather").toString());
                JSONObject weather=new JSONObject(weatherObj.getJSONObject(0).toString());
                System.out.println("Current Date :"+forecast.getJSONObject(i).get("dt_txt").toString());

                String UTCdate=forecast.getJSONObject(i).get("dt_txt").toString()+" UTC";
                String formatPattern = "yyyy-MM-dd hh:mm:ss zzz";

                SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
                Date date=sdf.parse(UTCdate);

                DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                formatter.setTimeZone(TimeZone.getTimeZone(timeZone));



// Prints the date in the CET timezone
                System.out.println(timeZone+" :"+formatter.format(date));

                int hours=Integer.parseInt(formatter.format(date).substring(12,14));
                System.out.println("Hours :"+hours);
                SimpleDateFormat sdfAns=new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
                Date dateobj=sdfAns.parse(formatter.format(date));
                if(hours>11 && hours<16){
                    DailyWeather dailyWeather = new DailyWeather(Float.parseFloat(jsonObject.get("temp").toString()),
                            Float.parseFloat(jsonObject.get("temp_min").toString()),
                            Float.parseFloat(jsonObject.get("temp_max").toString()),
                            dateobj,
                            city,
                            weather.get("main").toString());
                            weathers[j++]=dailyWeather;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Weather length :"+weathers.length);
        return weathers;
    }


    private JSONObject[] weatherInfo(String url,boolean mainData){

        JSONObject[] jsonObjects =null;

        try {
            jsonObjects= new NetworkUtil(mainData).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//        try {
//            URL openWeatherURL=new URL(url);
//            HttpURLConnection conn = (HttpURLConnection) openWeatherURL.openConnection();
//            if(conn.getResponseCode()==200){
//
//                InputStream responseBody = conn.getInputStream();
//                InputStreamReader responseBodyReader=new InputStreamReader(responseBody);
//                BufferedReader br=new BufferedReader(responseBodyReader);
//                StringBuilder buff=new StringBuilder();
//                String line="";
//
//                while((line=br.readLine())!=null)
//                    buff.append(line);
//
//                try {
//
//                    JSONObject jsonObject = new JSONObject(buff.toString());
//                    JSONObject dataObj=new JSONObject(jsonObject.getString("main"));
//                    if(!mainData){
//                        jsonObjects[0]=jsonObject;
//                        return jsonObjects;
//                    }
//                    jsonObjects[0]=dataObj;
//                    jsonObjects[1]=jsonObject.getJSONArray("weather").getJSONObject(0);
//
//                    System.out.println("Temperature :"+dataObj.getString("temp"));
//                    System.out.println("Pressure :"+dataObj.getString("pressure"));
//                    System.out.println("Humidity :"+dataObj.getString("humidity"));
//                    System.out.println("Min Temp :"+dataObj.getString("temp_min"));
//                    System.out.println("Max Temp :"+dataObj.getString("temp_max"));
//                }
//                catch (JSONException jsonException){
//                    jsonException.printStackTrace();
//                }
//                conn.disconnect();
//
//            }else{
//                System.out.println("Error processing request :"+conn.getResponseCode());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return  jsonObjects;
    }

    private JSONArray weatherForecastInfo(String url){
        try {
            URL openWeatherURL=new URL(url);
            HttpURLConnection conn = (HttpURLConnection) openWeatherURL.openConnection();
            if(conn.getResponseCode()==200){

                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader=new InputStreamReader(responseBody);
                BufferedReader br=new BufferedReader(responseBodyReader);
                StringBuilder buff=new StringBuilder();
                String line="";

                while((line=br.readLine())!=null)
                    buff.append(line);

                try {
                    JSONObject jsonObject = new JSONObject(buff.toString());
                    return jsonObject.getJSONArray("list");
                }
                catch (JSONException jsonException){
                    jsonException.printStackTrace();
                }
                conn.disconnect();

            }else{
                System.out.println("Error processing request :"+conn.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
