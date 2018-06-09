package com.findnearbyplaces.placefinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.findnearbyplaces.DatabaseHelper;
import com.findnearbyplaces.model.UserSearchedDetail;

import java.util.ArrayList;
import java.util.List;

import sms.sarmila.ele.com.findnearbyplaces.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_finder);
        initViews();
        toolbar.setTitle("Place Finder");
        /*items.add("Doctor");
        items.add("Hospital");
        items.add("Restaurant");*/
        List<UserSearchedDetail> searchedDetailList;
        searchedDetailList = db.getAllList();
        for (UserSearchedDetail name : searchedDetailList) {
            items.add(name.getSearchName());
        }
        ArrayAdapter<String> searchedListAdapter =
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
    }

    @Override
    public void onClick(View view) {
        insertName(et_searchText.getText().toString());

    }

    private void insertName(String name) {
        long id = db.insertSearchedName(name);

        Toast.makeText(this, "Inserted !!!!!!!!!!" + db.getSearchedNameById(id).getSearchName(), Toast.LENGTH_LONG).show();

    }

}
