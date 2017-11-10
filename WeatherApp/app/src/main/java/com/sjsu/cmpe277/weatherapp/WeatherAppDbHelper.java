package com.sjsu.cmpe277.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mak on 10/29/17.
 */

public class WeatherAppDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "WeatherAppDbHelper";
    public static final String DATABASE_NAME = "weatherApp.db";

    private static final int DATABASE_VERSION = 12;

    // Table Names
    private static final String TABLE_WEATHER = "weather";
    private static final String TABLE_TODAY_WEATHER = "weather_today";
    private static final String TABLE_FORECAST_WEATHER = "weather_forecast";

    // Column names
    private static final String OW_CITY_ID = "ow_id";
    private static final String GOOGLE_CITY_ID = "id";
    private static final String CITY_NAME = "city_name";
    private static final String CITY_COUNTRY = "city_country";
    private static final String CITY_LONGITUDE = "longitude";
    private static final String CITY_LATITUDE = "latitude";
    private static final String CITY_TEMP = "city_temp";
    private static final String WEATHER_ID = "weather_id";
    private static final String WEATHER_MAIN = "weather_main";
    private static final String WEATHER_DESCRIPTION = "weather_description";
    private static final String WEATHER_ICON = "weather_icon";
    private static final String MIN_TEMP = "min_temp";
    private static final String MAX_TEMP = "max_temp";
    private static final String TEMP_METRIC = "metric";
    private static final String HUMIDITY = "humidity";
    private static final String PRESSURE = "pressure";
    private static final String WIND_SPEED = "wind_speed";
    private static final String DEGREES = "degrees";
    private static final String TEMP_HOUR = "temp_hour";
    private static final String TEMP_DAY = "temp_day";
    private static final String TEMP_MONTH_DAY = "temp_month_day";
    private static final String CITY_IMAGE = "city_image";
    private static final String ROW_CREATED_AT = "create_date";
    private static final String UPDATE_DATE = "update_date";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // Table Create Statements
    // Weather Table table create statement
    private static final String CREATE_TABLE_WEATHER = "CREATE TABLE "
            + TABLE_WEATHER
            + "("
            + GOOGLE_CITY_ID + " TEXT ,"
            + OW_CITY_ID + " TEXT,"
            + CITY_NAME + " TEXT PRIMARY KEY,"
            + CITY_COUNTRY + " TEXT,"
            + CITY_LONGITUDE + " DOUBLE,"
            + CITY_LATITUDE + " DOUBLE,"
            + CITY_TEMP + " DOUBLE,"
            + WEATHER_DESCRIPTION + " TEXT,"
            + WEATHER_ICON + " TEXT,"
            + WEATHER_MAIN + " TEXT,"
            + WEATHER_ID + " INTEGER,"
            + MIN_TEMP + " DOUBLE,"
            + MAX_TEMP + " DOUBLE,"
            + TEMP_METRIC + "TEXT,"
            + HUMIDITY + " DOUBLE,"
            + PRESSURE + " DOUBLE,"
            + WIND_SPEED + " DOUBLE,"
            + DEGREES + " DOUBLE,"
            + CITY_IMAGE + " BLOB,"
            + ROW_CREATED_AT + " DATETIME,"
            + UPDATE_DATE + "DATETIME"
            + ")";


    // Table Create Statements
    // Weather Table table create statement
    private static final String CREATE_TABLE_TODAY_WEATHER = "CREATE TABLE "
            + TABLE_TODAY_WEATHER
            + "("
            + OW_CITY_ID + " TEXT,"
            + CITY_NAME + " TEXT ,"
            + CITY_COUNTRY + " TEXT,"
            + CITY_TEMP + " DOUBLE,"
            + WEATHER_ID + " INTEGER,"
            + MIN_TEMP + " DOUBLE,"
            + MAX_TEMP + " DOUBLE,"
            + HUMIDITY + " DOUBLE,"
            + PRESSURE + " DOUBLE,"
            + WIND_SPEED + " DOUBLE,"
            + TEMP_HOUR + " INTEGER,"
            + ROW_CREATED_AT + " DATETIME,"
            + UPDATE_DATE + "DATETIME"
            + ")";



    // Table Create Statements
    // Weather Table table create statement
    private static final String CREATE_TABLE_FORECAST_WEATHER = "CREATE TABLE "
            + TABLE_FORECAST_WEATHER
            + "("
            + OW_CITY_ID + " TEXT,"
            + CITY_NAME + " TEXT ,"
            + CITY_COUNTRY + " TEXT,"
            + CITY_TEMP + " DOUBLE,"
            + WEATHER_ID + " INTEGER,"
            + MIN_TEMP + " DOUBLE,"
            + MAX_TEMP + " DOUBLE,"
            + HUMIDITY + " DOUBLE,"
            + PRESSURE + " DOUBLE,"
            + WIND_SPEED + " DOUBLE,"
            + TEMP_HOUR + " TEXT,"
            + TEMP_DAY + " TEXT,"
            + TEMP_MONTH_DAY + " TEXT,"
            + ROW_CREATED_AT + " DATETIME,"
            + UPDATE_DATE + "DATETIME"
            + ")";



    public WeatherAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(LOG_TAG, "CREATING DB");
        db.execSQL(CREATE_TABLE_WEATHER);
        db.execSQL(CREATE_TABLE_TODAY_WEATHER);
        db.execSQL(CREATE_TABLE_FORECAST_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(LOG_TAG, "Upgrading DB");
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODAY_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORECAST_WEATHER);
        // create new tables
        onCreate(db);
    }

    public long createCity(City city) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(city!=null){

        }
        if (city.getGoogleCityId()!=null) {
            values.put(GOOGLE_CITY_ID, city.getGoogleCityId());
        }
        if (city.getCityCountry()!=null) {
            values.put(CITY_COUNTRY, city.getCityCountry());
        }
        if (city.getCityName()!=null) {
            values.put(CITY_NAME, city.getCityName());
        }
        if (city.getCityLatitude() != null) {
            values.put(CITY_LATITUDE, city.getCityLatitude());
        }

        if(city.getCityLongitude()!=null){
            values.put(CITY_LONGITUDE, city.getCityLongitude());
        }


        values.put(CITY_IMAGE, city.getCityImage());

        values.put(CITY_TEMP, city.getCityTemp());
        Log.e(LOG_TAG,"Inserting Description"+city.getWeatherDescription());
        values.put(WEATHER_DESCRIPTION, city.getWeatherDescription());

        Log.e(LOG_TAG,"Inserting weather ID"+city.getWeatherId());
        values.put(WEATHER_ID, city.getWeatherId());
        Log.e(LOG_TAG,"Inserting ICON"+city.getWeatherIcon());
        values.put(WEATHER_ICON, city.getWeatherIcon());
        Log.e(LOG_TAG,"Inserting Main weather"+city.getWeatherMain());
        values.put(WEATHER_MAIN, city.getWeatherMain());
        Log.e(LOG_TAG,"Inserting humidiirt"+city.getCityHumididty());
        values.put(HUMIDITY, city.getCityHumididty());
        Log.e(LOG_TAG,"Inserting Pressure"+city.getCityPressure());
        values.put(PRESSURE, city.getCityPressure());


        Log.e(LOG_TAG,"Inserting temp MAX"+city.getCityMaxTemp());
        Log.e(LOG_TAG,"Inserting temp MIN"+city.getCityMinTemp());
        values.put(MIN_TEMP, city.getCityMinTemp());
        values.put(MAX_TEMP, city.getCityMaxTemp());
        values.put(ROW_CREATED_AT, dateFormat.format(new Date()));


        // insert row
        long city_row_id = db.insert(TABLE_WEATHER, null, values);

        return city_row_id;
    }





    public long createTodayWeather(List<City>
                                           cities) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long city_row_id=0;

        try {
            ContentValues values = new ContentValues();
            for (City city : cities) {

                values.put(CITY_TEMP, city.getCityTemp());

                Log.e(LOG_TAG,"Inserting TODAY weather ID"+city.getWeatherId());
                values.put(WEATHER_ID, city.getWeatherId());

                Log.e(LOG_TAG,"Inserting TODAY humidiirt"+city.getCityHumididty());
                values.put(HUMIDITY, city.getCityHumididty());
                Log.e(LOG_TAG,"Inserting TODAY Pressure"+city.getCityPressure());
                values.put(PRESSURE, city.getCityPressure());
                values.put(TEMP_HOUR,city.getTempHour());
                values.put(MIN_TEMP, city.getCityMinTemp());
                values.put(MAX_TEMP, city.getCityMaxTemp());
                city_row_id= db.insert(TABLE_TODAY_WEATHER, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return city_row_id;
    }


    public long createForecastWeather(List<City> cities) {


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long city_row_id=0;

        try {
            ContentValues values = new ContentValues();
            for (City city : cities) {

                values.put(CITY_TEMP, city.getCityTemp());
                values.put(WEATHER_ID, city.getWeatherId());
                Log.e(LOG_TAG,"Inserting TODAY humidiirt"+city.getCityHumididty());
                values.put(HUMIDITY, city.getCityHumididty());
                Log.e(LOG_TAG,"Inserting TODAY Pressure"+city.getCityPressure());
                values.put(PRESSURE, city.getCityPressure());
                values.put(TEMP_HOUR,city.getTempHour());
                values.put(TEMP_DAY,city.getTempDay());
                values.put(TEMP_MONTH_DAY,city.getTempDay());
                values.put(MIN_TEMP, city.getCityMinTemp());
                values.put(MAX_TEMP, city.getCityMaxTemp());
                city_row_id= db.insert(TABLE_FORECAST_WEATHER, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return city_row_id;
    }

    /*
 * get single city
 */

    public City getCity(int googleCityId) {


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER + " WHERE "
                + GOOGLE_CITY_ID + " = " + googleCityId;

        Log.e(LOG_TAG, "  RESULT OF THE Query " + selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        City city = new City(c.getString(c.getColumnIndex(CITY_NAME))
                , c.getString(c.getColumnIndex(CITY_COUNTRY))
                , c.getString(c.getColumnIndex(GOOGLE_CITY_ID))
                , c.getDouble(c.getColumnIndex(CITY_LONGITUDE))
                , c.getDouble(c.getColumnIndex(CITY_LATITUDE))
                , c.getBlob(c.getColumnIndex(CITY_IMAGE))
        );

        return city;

    }


    /*
* check  city ByName
*/
    public boolean getCityByName(String cityName) {

        Log.e("AA gya", "AAA GYAAAAAAA");
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER + " WHERE "
                + CITY_NAME + " = ? ;";

        Log.e(LOG_TAG, "  RESULT OF THE Query " + selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[]{cityName});

        Log.e("value ", "value of cursor is " + c + "count ==>" + c.getCount());
        if (c.getCount() <= 0)
            return false;


        return true;

    }




        /*
 * get all cities
 */

    public List<City> getAllCities() {

        List<City> allCities = new ArrayList<City>();

        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER;


        Log.e(LOG_TAG, " ALL THE CITIES " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                City city = new City(c.getString(c.getColumnIndex(CITY_NAME))
                        , c.getString(c.getColumnIndex(CITY_COUNTRY))
                        , c.getString(c.getColumnIndex(GOOGLE_CITY_ID))
                        , c.getDouble(c.getColumnIndex(CITY_LONGITUDE))
                        , c.getDouble(c.getColumnIndex(CITY_LATITUDE))
                        , c.getBlob(c.getColumnIndex(CITY_IMAGE))
                );

                city.setWeatherIcon(c.getString(c.getColumnIndex(WEATHER_ICON)));
                city.setWeatherMain(c.getString(c.getColumnIndex(WEATHER_MAIN)));
                city.setWeatherId(c.getDouble(c.getColumnIndex(WEATHER_ID)));
                city.setWeatherDescription(c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)));
                city.setCityTemp(c.getDouble(c.getColumnIndex(CITY_TEMP)));
                city.setCityMinTemp(c.getDouble(c.getColumnIndex(MIN_TEMP)));
                city.setCityMaxTemp(c.getDouble(c.getColumnIndex(MAX_TEMP)));
                // adding to todo list
                allCities.add(city);
            } while (c.moveToNext());
        }

        return allCities;
    }




    public List<City> getTodayWeather(String cityName) {

        List<City> allCities = new ArrayList<City>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODAY_WEATHER + " WHERE "
                + CITY_NAME + " = ? ;";


        Log.e(LOG_TAG, " ALL THE CITIES " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{cityName});

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                City city = new City(c.getString(c.getColumnIndex(CITY_NAME))
                        , c.getString(c.getColumnIndex(CITY_COUNTRY))
                        , c.getString(c.getColumnIndex(GOOGLE_CITY_ID))
                        , c.getDouble(c.getColumnIndex(CITY_LONGITUDE))
                        , c.getDouble(c.getColumnIndex(CITY_LATITUDE))
                        , c.getBlob(c.getColumnIndex(CITY_IMAGE))
                );

                city.setWeatherIcon(c.getString(c.getColumnIndex(WEATHER_ICON)));
                city.setWeatherMain(c.getString(c.getColumnIndex(WEATHER_MAIN)));
                city.setWeatherId(c.getDouble(c.getColumnIndex(WEATHER_ID)));
                city.setWeatherDescription(c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)));
                city.setCityTemp(c.getDouble(c.getColumnIndex(CITY_TEMP)));
                city.setCityMinTemp(c.getDouble(c.getColumnIndex(MIN_TEMP)));
                city.setCityMaxTemp(c.getDouble(c.getColumnIndex(MAX_TEMP)));

                city.setTempHour(c.getString(c.getColumnIndex(TEMP_HOUR)));

                // adding to todo list
                allCities.add(city);
            } while (c.moveToNext());
        }

        return allCities;
    }



    public List<City> getForecastWeather(String cityName) {

        List<City> allCities = new ArrayList<City>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODAY_WEATHER + " WHERE "
                + CITY_NAME + " = ? ;";



        Log.e(LOG_TAG, " ALL THE CITIES " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, new String[]{cityName});

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                City city = new City(c.getString(c.getColumnIndex(CITY_NAME))
                        , c.getString(c.getColumnIndex(CITY_COUNTRY))
                        , c.getString(c.getColumnIndex(GOOGLE_CITY_ID))
                        , c.getDouble(c.getColumnIndex(CITY_LONGITUDE))
                        , c.getDouble(c.getColumnIndex(CITY_LATITUDE))
                        , c.getBlob(c.getColumnIndex(CITY_IMAGE))
                );

                city.setWeatherIcon(c.getString(c.getColumnIndex(WEATHER_ICON)));
                city.setWeatherMain(c.getString(c.getColumnIndex(WEATHER_MAIN)));
                city.setWeatherId(c.getDouble(c.getColumnIndex(WEATHER_ID)));
                city.setWeatherDescription(c.getString(c.getColumnIndex(WEATHER_DESCRIPTION)));
                city.setCityTemp(c.getDouble(c.getColumnIndex(CITY_TEMP)));
                city.setCityMinTemp(c.getDouble(c.getColumnIndex(MIN_TEMP)));
                city.setCityMaxTemp(c.getDouble(c.getColumnIndex(MAX_TEMP)));

                city.setTempHour(c.getString(c.getColumnIndex(TEMP_HOUR)));
                city.setTempDay(c.getString(c.getColumnIndex(TEMP_DAY)));
                city.setTempMonthDay(c.getString(c.getColumnIndex(TEMP_MONTH_DAY)));

                // adding to todo list
                allCities.add(city);
            } while (c.moveToNext());
        }

        return allCities;
    }



        /*
 * Updating a City
 */


    public int updateCityWeather(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MIN_TEMP, city.getCityMinTemp());
        values.put(MAX_TEMP, city.getCityMaxTemp());
        values.put(TEMP_METRIC, city.getCityTempMetric());
        values.put(HUMIDITY, city.getCityHumididty());
        values.put(PRESSURE, city.getCityMinTemp());
        values.put(WIND_SPEED, city.getCityWindSpeed());
        values.put(DEGREES, city.getCityDegrees());
        values.put(UPDATE_DATE, dateFormat.format(new Date()));

        // updating row
        return db.update(TABLE_WEATHER, values, GOOGLE_CITY_ID + " = ?",
                new String[]{String.valueOf(city.getGoogleCityId())});
    }

    /*
 * Deleting a City
 */
    public void deleteToDo(long googleCityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER, GOOGLE_CITY_ID + " = ?",
                new String[]{String.valueOf(googleCityId)});
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
