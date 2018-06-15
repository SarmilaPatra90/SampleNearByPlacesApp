package com.findnearbyplaces.shownearbyplaceslist;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.findnearbyplaces.model.NearByPlaces;
import com.findnearbyplaces.nearbyplacesmap.GetNearbyPlacesData;
import com.findnearbyplaces.nearbyplacesmap.MapsActivity;
import com.findnearbyplaces.util.Constant;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import sms.sarmila.ele.com.findnearbyplaces.R;

public class ShowNearByPlacesActivity extends AppCompatActivity {
    private List<NearByPlaces> nearByPlacesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShowNearByPlacesListAdapter mAdapter;
    private String enteredText;
    private double latitude, longitude;
    protected GeoDataClient geoDataClient;
    protected PlaceDetectionClient placeDetectionClient;
    public static final String TAG = "ShowNearByPlaces";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_places_list);

        initViews();
        getExtras();


       /* mAdapter = new ShowNearByPlacesListAdapter(nearByPlacesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);*/
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        placeDetectionClient = Places.getPlaceDetectionClient(this, null);

        getCurrentPlaceData();

        //prepareData();
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_nearByPlaces);
    }

    private void getCurrentPlaceData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<PlaceLikelihoodBufferResponse> placeResult = placeDetectionClient.getCurrentPlace(null);
        placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                Log.d(TAG, "current location places info");
                List<Place> placesList = new ArrayList<Place>();
                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    placesList.add(placeLikelihood.getPlace().freeze());
                }
                likelyPlaces.release();

              /*  ShowNearByPlacesListAdapter recyclerViewAdapter = new
                        ShowNearByPlacesListAdapter(placesList);
                recyclerView.setAdapter(recyclerViewAdapter);*/
            }
        });
    }
    private void getExtras() {
            enteredText = getIntent().getStringExtra(Constant.ENTERED_TEXT);
            latitude = getIntent().getDoubleExtra(Constant.LATITUDE, 0);
            longitude = getIntent().getDoubleExtra(Constant.LONGITUDE, 0);
        }


    private void prepareData() {

        String url = new MapsActivity().getUrl(latitude, longitude, enteredText);
        Object[] DataTransfer = new Object[2];
       // DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);

    }
}
