package com.findnearbyplaces.placefinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sms.sarmila.ele.com.findnearbyplaces.R;

/**
 * Created by Sarmila on 09-06-2018.
 */

public class PlaceFinderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<String> items = new ArrayList<String>();
    private ListView lv_searchedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_finder);
        initViews();
        toolbar.setTitle("Place Finder");

        items.add("Doctor");
        items.add("Hospital");
        items.add("Restaurant");

        ArrayAdapter<String> searchedListAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv_searchedList.setAdapter(searchedListAdapter);

    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_searchedList = (ListView) findViewById(R.id.lv_searched_list);
    }
}
