package com.findnearbyplaces.model;


/**
 * Created by Sarmila on 08-06-2018.
 */

public class UserSearchedDetail {
    public static final String TABLE_NAME = "UserSearchedDetail";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    private String searchName;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT"
                    + ")";

    public UserSearchedDetail() {
    }

    public UserSearchedDetail(String searchName) {
        this.searchName = searchName;
    }

}
