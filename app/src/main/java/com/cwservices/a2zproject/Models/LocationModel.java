package com.cwservices.a2zproject.Models;

public class LocationModel {
    double latitude,longitude;

    public LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LocationModel(){
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
