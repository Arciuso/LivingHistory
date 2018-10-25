package com.example.arcius.livinghistory.data;

import java.io.Serializable;

public class Location implements Serializable {

    private int locationID;

    private float latitude;
    private float longitude;

    private String country;
    private String name;

    public Location(int locationID, float latitude, float longitude, String country, String name) {
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
