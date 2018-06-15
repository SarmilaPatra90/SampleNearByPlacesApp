package com.findnearbyplaces.placefinder.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.findnearbyplaces.DatabaseHelper;
import com.findnearbyplaces.model.UserSearchedDetail;
import com.findnearbyplaces.nearbyplacesmap.MapsActivity;
import com.findnearbyplaces.util.Constant;

import java.util.ArrayList;
import java.util.List;

import sms.sarmila.ele.com.findnearbyplaces.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Sarmila on 09-06-2018.
 */

public class PlaceFinderActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private List<String> items = new ArrayList<String>();
    private ListView lv_searchedList;
    private EditText et_searchText;
    private Button btn_search;
    private DatabaseHelper db;
    private double latitude, longitude;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private String enteredText;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private LocationTrack locationTrack;
    private ArrayAdapter<String> searchedListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_finder);
        initViews();
        requestPermission();
        populateListAdapter();

    }

    private void requestPermission() {
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void populateListAdapter() {
        List<UserSearchedDetail> searchedDetailList;
        searchedDetailList = db.getAllList();
        for (UserSearchedDetail name : searchedDetailList) {
            items.add(name.getSearchName());
        }
        searchedListAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv_searchedList.setAdapter(searchedListAdapter);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_searchedList = (ListView) findViewById(R.id.lv_searched_list);
        et_searchText = (EditText) findViewById(R.id.et_lookingFor);
        btn_search = (Button) findViewById(R.id.bt_search);
        db = new DatabaseHelper(this);
        btn_search.setOnClickListener(this);

        toolbar.setTitle(getString(R.string.place_finder));

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        lv_searchedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                et_searchText.setText((String) adapterView.getItemAtPosition(i));
            }
        });
    }

    @Override
    public void onClick(View view) {
        enteredText = et_searchText.getText().toString();
        if (enteredText.isEmpty()) {
            Toast.makeText(PlaceFinderActivity.this, getString(R.string.edittext_validation_error_message), Toast.LENGTH_LONG).show();
        } else {
            insertNameToDB(enteredText);
            locationTrack = new LocationTrack(PlaceFinderActivity.this);

            if (locationTrack.canGetLocation()) {
                if (locationTrack.loc != null) {
                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();

                    Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    openMapsActivity();
                }
            } else {
                locationTrack.showSettingsAlert();
            }
        }
    }

    private void openMapsActivity() {
        Intent intent = new Intent(PlaceFinderActivity.this, MapsActivity.class);
        intent.putExtra(Constant.ENTERED_TEXT, enteredText);
        intent.putExtra(Constant.LATITUDE, latitude);
        intent.putExtra(Constant.LONGITUDE, longitude);
        startActivity(intent);
    }

    private void insertNameToDB(String name) {
        List<String> userSearchedDetailList = db.getSearchedListFromDB();
        if (userSearchedDetailList.isEmpty()) {
            long id = db.insertSearchedName(name);
            searchedListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Inserted !!!!!!!!!!" + db.getSearchedNameById(id).getSearchName(), Toast.LENGTH_LONG).show();

        } else {
            for (String searchName : userSearchedDetailList) {
                if (!(searchName.trim().toLowerCase().equalsIgnoreCase(name.trim().toLowerCase()))) {
                    long id = db.insertSearchedName(name);
                    searchedListAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Inserted !!!!!!!!!!" + db.getSearchedNameById(id).getSearchName(), Toast.LENGTH_LONG).show();

                }
            }
        }

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel(getString(R.string.mandatory_permission_message),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PlaceFinderActivity.this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}
