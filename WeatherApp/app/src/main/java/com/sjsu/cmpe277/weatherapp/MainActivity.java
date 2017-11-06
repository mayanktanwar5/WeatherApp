package com.sjsu.cmpe277.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
//    private ViewPager pager = null;
//    private MainPageAdapter pagerAdapter = null;
    private static final String LOG_TAG = "MainActivity";
    private List<City> allCities;
    LayoutInflater inflater;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerViewAdapter;
    private ViewPagerHandler viewPagerHandler;

    // Database Helper
    WeatherAppDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.toggle_open,R.string.toggle_close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPagerHandler = new ViewPagerHandler(this,this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerAdapter(getApplicationContext(),drawerLayout,viewPagerHandler);
        recyclerView.setAdapter(recyclerViewAdapter);



//        pagerAdapter = new MainPageAdapter();
//        pager = (ViewPager) findViewById (R.id.view_pager);
//        pager.setAdapter (pagerAdapter);
//
//        inflater = getLayoutInflater();
//
//        getAllCities();




    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG_TAG,"On restart");
        //viewPagerHandler.getAllCities();
        recyclerViewAdapter.setAllCities();
        recyclerViewAdapter.notifyDataSetChanged();
    }


//    public void getAllCities(){
//
//        db = new WeatherAppDbHelper(getApplicationContext());
//
//        allCities= db.getAllCities();
//
//        for(int i=0; i< allCities.size();i++){
//
//            addCityView(allCities.get(i),i);
//        }
//
//        pagerAdapter.notifyDataSetChanged();
//    }
//
//    public void addCityView(City city, int position){
//
//        FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.city_view,null);
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
//
//
//
//        Log.e(LOG_TAG,"BEFORE ADDING VIEW");
//        pagerAdapter.addView (v0, position);
//
//
//    }

//
//    public void addView (View newPage)
//    {
//        Log.e(LOG_TAG,"ADDING VIEW");
//        int pageIndex = pagerAdapter.addView (newPage);
//        // You might want to make "newPage" the currently displayed page:
//        pager.setCurrentItem (pageIndex, true);
//    }
//
//    public void removeView (View defunctPage)
//    {
//        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
//        // You might want to choose what page to display, if the current page was "defunctPage".
//        if (pageIndex == pagerAdapter.getCount())
//            pageIndex--;
//        pager.setCurrentItem (pageIndex);
//    }
//
//    public View getCurrentPage ()
//    {
//        return pagerAdapter.getView (pager.getCurrentItem());
//    }
//
//    public void setCurrentPage (View pageToShow)
//    {
//        Log.e(LOG_TAG,"Setting Currentpage ==>" +pagerAdapter.getItemPosition (pageToShow));
//        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(mToggle.onOptionsItemSelected(item)){

            return true;
        }
        if(id==R.id.action_add_city){
            Intent i = new Intent(this,AddCityActivity.class);
           // i.putExtra("ViewPagerHandler", viewPagerHandler);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
