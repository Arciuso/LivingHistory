package com.example.arcius.livinghistory.data.repository.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

@Entity
public class Location {

    @Ignore
    public Location(Integer locationID, float latitude, float longitude, String country, String name) {
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.name = name;
    }

    public Location() {

    }

    @PrimaryKey
    public Integer locationID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "latitude")
    public float latitude;

    @ColumnInfo(name = "longitude")
    public float longitude;

    @ColumnInfo(name = "country")
    public String country;

    public void log() {
        Log.d("Local-db LOCATION","\t\tlocationID : " + locationID);
        Log.d("Local-db LOCATION", "\t\tname : " + name);
        Log.d("Local-db LOCATION", "\t\tlatitude : " + latitude);
        Log.d("Local-db LOCATION", "\t\tlongitude : " + longitude);
        Log.d("Local-db LOCATION", "\t\tlocationID : " + locationID);
        Log.d("Local-db LOCATION", "\t\tcountry : " + country);
    }

}
