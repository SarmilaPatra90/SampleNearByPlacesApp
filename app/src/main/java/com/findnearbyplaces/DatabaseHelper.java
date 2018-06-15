package com.findnearbyplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.findnearbyplaces.model.NearByPlaces;
import com.findnearbyplaces.model.UserSearchedDetail;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "UserDetail_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserSearchedDetail.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserSearchedDetail.TABLE_NAME);

        onCreate(db);
    }

    public long insertSearchedName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserSearchedDetail.COLUMN_NAME, name);

        long id = db.insert(UserSearchedDetail.TABLE_NAME, null, values);

        db.close();

        return id;
    }
    public List<String> getSearchedListFromDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> searchNameList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + UserSearchedDetail.COLUMN_NAME + " FROM " +  UserSearchedDetail.TABLE_NAME , null );
        while (cursor.moveToFirst()){
            searchNameList.add(cursor.getString(0));
        }
        cursor.close();

        db.close();

        return searchNameList ;
    }

    public UserSearchedDetail getSearchedNameById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserSearchedDetail.TABLE_NAME,
                new String[]{UserSearchedDetail.COLUMN_ID, UserSearchedDetail.COLUMN_NAME},
                UserSearchedDetail.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        UserSearchedDetail searchedDetail = new UserSearchedDetail(

                cursor.getString(cursor.getColumnIndex(UserSearchedDetail.COLUMN_NAME)));

        cursor.close();

        return searchedDetail;
    }

    public List<UserSearchedDetail> getAllList() {
        List<UserSearchedDetail> userSearchedDetailList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + UserSearchedDetail.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserSearchedDetail searchedDetail = new UserSearchedDetail();
                searchedDetail.setSearchName(cursor.getString(cursor.getColumnIndex(UserSearchedDetail.COLUMN_NAME)));

                userSearchedDetailList.add(searchedDetail);
            } while (cursor.moveToNext());
        }

        db.close();

        return userSearchedDetailList;
    }


}
