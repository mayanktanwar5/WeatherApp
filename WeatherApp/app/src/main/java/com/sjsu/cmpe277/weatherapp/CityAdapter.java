package com.sjsu.cmpe277.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

import static java.security.AccessController.getContext;

/**
 * Created by Mak on 10/28/17.
 */

    public class CityAdapter extends ArrayAdapter<CityAdapter.City> implements Filterable {
    private  LayoutInflater cityInflator;
    private static final String TAG = "CityAdapter";
    List<City> citiesList = Collections.emptyList();
    private GoogleApiClient mGoogleApiClient;
    //AutocompleteFilter filter;


    /**
     * Constructor
     * @param context  Context
     * @param resource Layout resource
     */

    public CityAdapter(Context context, int resource ) {
        super(context,resource);
        cityInflator = LayoutInflater.from(context);
        Log.i(TAG, "City Adapter construdtor context is ."+context);
    }


    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        Log.i(TAG, "Google API client sent: " + googleApiClient);
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            mGoogleApiClient = null;
        } else {
            mGoogleApiClient = googleApiClient;
        }
    }

    @Override
    public int getCount() {
        return citiesList.size();
    }

    @Override
    public City getItem(int position) {
         Log.e(TAG," cities Sizes are =>+"+citiesList.size());
        Log.e(TAG," Incoming position are =>+"+position);
        return citiesList.get(position);
    }

    private List<City> getPredictions(CharSequence constraint) {
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Executing autocomplete query for: " + constraint);

            // Build a filter to restrict for cities
            AutocompleteFilter.Builder filter = new AutocompleteFilter.Builder();
            // TYPE_FILTER_CITIES
            filter.setTypeFilter(5);
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    null, filter.build());
            // Wait for predictions, set the timeout.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(),"Error"+status.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error while getting the place predictions: " + status
                        .toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query is completed successfully. Received " + autocompletePredictions.getCount()
                    + " predictions.");
            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                resultList.add(new City((String)prediction.getFullText(null),  prediction.getPlaceId()));
            }
            // Buffer release
            autocompletePredictions.release();
            return resultList;
        }
        Log.e(TAG, "Google API client is not connected.");
        return null;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    citiesList = getPredictions(constraint);
                    if (citiesList != null) {
                        // Results
                        results.values = citiesList;
                        results.count = citiesList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    citiesList.clear();
                }
            }
        };
        return filter;
    }

    class City {

        public String description;
        public String cityId;


        public City(String description, String cityId) {
            this.description = description;
            this.cityId = cityId;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

}
