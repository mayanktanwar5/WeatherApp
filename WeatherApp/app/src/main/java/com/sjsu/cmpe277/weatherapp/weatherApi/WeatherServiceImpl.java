package com.sjsu.cmpe277.weatherapp.weatherApi;

import android.os.AsyncTask;

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

    SimpleDateFormat mSimpleDatFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    OpenWeatherMap.Units units = (true)
            ? OpenWeatherMap.Units.METRIC
            : OpenWeatherMap.Units.IMPERIAL;



    OpenWeatherMap owm = new OpenWeatherMap(units, "647f4536db207983f6f2345572c9492f");
    @Override
    public City getCurrentWeather(String city, String country) throws JSONException {
        DailyWeather dailyWeather=null;
        JSONObject weatherInfo=weatherInfo("http://api.openweathermap.org/data/2.5/weather?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&units=metric"+"&appid=647f4536db207983f6f2345572c9492f",true);

        City cityObj = currentWeatherJsonParser(weatherInfo);
        cityObj.setCityCountry(country);
        return cityObj;


//        try{
//
//            cityDailyWeather = new City()
//        dailyWeather=new DailyWeather(Float.parseFloat(weatherInfo[0].getString("temp")),
//                Float.parseFloat(weatherInfo[0].getString("temp_min")),
//                Float.parseFloat(weatherInfo[0].getString("temp_max")),
//                new Date(),
//                city,
//                weatherInfo[1].getString("main"));
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }

    }

//    public String getCity(double latitude,double longitude){
//        System.out.println("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=647f4536db207983f6f2345572c9492f");
//        JSONObject[] weatherInfo=weatherInfo("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=647f4536db207983f6f2345572c9492f",false);
//        try{
//            System.out.println("weather :"+weatherInfo[0].toString());
//            return weatherInfo[0].getString("name");
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

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

  //  @Override
//    public City[] getOneDayForecast(String city, String country, String timeZone) {
//        JSONArray forecast=weatherForecastInfo("http://api.openweathermap.org/data/2.5/forecast?q="+getSuitableLocation(city)+","+getSuitableLocation(country)+"&appid=647f4536db207983f6f2345572c9492f");
//        City [] weathers=new City[9];
//
//        try {
//
//            Date currentDate=new Date();
//            for (int i = 0; i < 9; i++) {
//                JSONObject jsonObject = new JSONObject(forecast.getJSONObject(i).get("main").toString());
//                JSONObject weather = new JSONObject(forecast.getJSONObject(i).getJSONArray("weather").getJSONObject(0).toString());
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
//                System.out.println(timeZone+" :"+formatter.format(date));
//                SimpleDateFormat sdfAns=new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
//                Date dateobj=sdfAns.parse(formatter.format(date));
//
//                DailyWeaCityther dailyWeather = new DailyWeather(Float.parseFloat(jsonObject.get("temp").toString()),
//
//                        Float.parseFloat(jsonObject.get("temp_min").toString()),
//                        Float.parseFloat(jsonObject.get("temp_max").toString()),
//                        dateobj,
//                        city,
//                        weather.get("main").toString());
//                weathers[i]=dailyWeather;
//                currentDate.setTime(currentDate.getTime()+3600 * 3000);
//            }
//        }
//        catch (Exception ex){
//            ex.printStackTrace();
//        }
////        catch (IOException ex){
////            ex.printStackTrace();
////        }
//        return weathers;
//    }

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


    private JSONObject weatherInfo(String url,boolean mainData){

        JSONObject jsonObject =null;
        try {
            jsonObject= new NetworkUtil(mainData).execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return  jsonObject;
    }

    private City currentWeatherJsonParser(JSONObject jsonObject) throws JSONException {

        JSONObject mainObj = new JSONObject(jsonObject.getString("main"));
        JSONObject weatherObj = jsonObject.getJSONArray("weather").getJSONObject(0);

        String cityName = jsonObject.getString("name");
        int cityId = jsonObject.getInt("id");
        City  city = new City(cityName,cityId);

        city.setCityTemp(mainObj.getLong("temp"));
        city.setCityHumididty(mainObj.getLong("humidity"));
        city.setCityMinTemp(mainObj.getLong("temp_min"));
        city.setCityMaxTemp(mainObj.getLong("temp_max"));
        city.setCityPressure(mainObj.getLong("pressure"));
        city.setWeatherDescription(weatherObj.getString("description"));
        city.setWeatherId(weatherObj.getInt("id"));
        city.setWeatherMain(weatherObj.getString("main"));
        city.setWeatherIcon(weatherObj.getString("icon"));


        return city;
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
