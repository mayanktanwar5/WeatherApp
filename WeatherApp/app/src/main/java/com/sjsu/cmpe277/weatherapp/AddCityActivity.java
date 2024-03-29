package com.sjsu.cmpe277.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddCityActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "cityActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private CityAdapter mCityAdapter;
    private ProgressDialog progressDialog;
SharedPreferences sp;

    // Database Helper
    WeatherAppDbHelper db;

    Context context= AddCityActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add city");

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        mGoogleApiClient = new GoogleApiClient.Builder(AddCityActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        mCityAdapter = new CityAdapter(this,android.R.layout.simple_list_item_1);

        mAutocompleteTextView.setAdapter(mCityAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent();
        setResult(3,intent);
        finish();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final CityAdapter.City item = mCityAdapter.getItem(position);
            final String placeId = String.valueOf(item.cityId);

            Log.i(LOG_TAG, "Selected: " + item.description);


//            progressDialog = new ProgressDialog(getApplicationContext());
//            progressDialog.setMessage("Fetching weather data for city ");
//            progressDialog.setCancelable(false);
//            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    try {
////                            Toast.makeText(getApplicationContext(),"Request Canceled");
//                    } catch (SecurityException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            progressDialog.show();


            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);

            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

//            PendingResult<PlacePhotoMetadataResult> placePhotos= Places.GeoDataApi.getPlacePhotos(mGoogleApiClient,placeId);
//            placePhotos.setResultCallback(mUpdatePlacePhotoDetailsCallback);

            Log.i(LOG_TAG, "Fetching details for ID: " + item.cityId);


        }
    };



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            Log.e(LOG_TAG,"cityName==>"+place.getName().toString());
            Log.e(LOG_TAG,"latlng "+place.getLatLng().toString());





            SharedPreferences.Editor editor = sp.edit();

            editor.putString("cityName", place.getName().toString());
            editor.putFloat("cityLongitude", (float) place.getLatLng().longitude);
            editor.putFloat("cityLatitude",(float) place.getLatLng().latitude);
            editor.commit();

            Intent intent=new Intent();
            intent.putExtra("Lat",place.getLatLng().latitude);
            intent.putExtra ("Long",place.getLatLng().longitude);
            intent.putExtra("cityName",place.getName().toString());

            places.release();

//            progressDialog.dismiss();
            setResult(2,intent);
            finish();//finishing activity



        }
    };


//
//    private ResultCallback<PlacePhotoResult> mPhotoResults = new ResultCallback<PlacePhotoResult>() {
//        @Override
//        public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
//            Bitmap result = placePhotoResult.getBitmap();
//            mCityImage.setImageBitmap(result);
//
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            result.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            city.setCityImage(byteArray);
//
//
//            db = new WeatherAppDbHelper(getApplicationContext());
//
//            long city_id = db.createCity(city);
//            Log.e(LOG_TAG,"Inserted city return row id is "+city_id);
//
//          // ViewPagerHandler mViewPagerHandler= (ViewPagerHandler) getIntent().getSerializableExtra("ViewPagerHandler");
//
//           //mViewPagerHandler.addView(mViewPagerHandler.createCityView(city));
//
//            finish();
//        }
//    };
//    private ResultCallback<PlacePhotoMetadataResult> mUpdatePlacePhotoDetailsCallback = new ResultCallback<PlacePhotoMetadataResult>() {
//        @Override
//        public void onResult(@NonNull PlacePhotoMetadataResult photos) {
//            if (!photos.getStatus().isSuccess()) {
//                Log.e(LOG_TAG, "Place query did not complete. Error: " +
//                        photos.getStatus().toString());
//                return;
//            }
//            // Selecting the first object buffer.
//            PlacePhotoMetadataBuffer place = photos.getPhotoMetadata();
//            PlacePhotoMetadata placePhoto = place.get(0);
//            CharSequence attributions = placePhoto.getAttributions();
//            PendingResult<PlacePhotoResult> photoResult =placePhoto.getPhoto(mGoogleApiClient) ;
//            photoResult.setResultCallback(mPhotoResults);
//            place.release();
//
//        }
//    };
//

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Google Places API connected.");
        mCityAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mCityAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places connection suspended.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }
}
