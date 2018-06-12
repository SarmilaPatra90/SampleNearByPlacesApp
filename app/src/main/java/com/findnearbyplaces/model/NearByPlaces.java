package com.findnearbyplaces.model;

public class NearByPlaces  {
    private String name ;
    private String address ;
    private boolean isCurrentlyOpen ;
    private float rating ;
    private boolean isFavourite ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCurrentlyOpen() {
        return isCurrentlyOpen;
    }

    public void setCurrentlyOpen(boolean currentlyOpen) {
        isCurrentlyOpen = currentlyOpen;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
