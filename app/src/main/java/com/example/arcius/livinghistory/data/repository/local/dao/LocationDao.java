package com.example.arcius.livinghistory.data.repository.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.arcius.livinghistory.data.repository.local.entity.Location;

import java.util.List;


@Dao
public interface LocationDao {

    @Query("SELECT * FROM location WHERE locationID= :locationID")
    Location getLocation(int locationID);

    @Query("SELECT locationID FROM location")
    List<Integer> getLocationIDs();

    @Insert
    void insertLocation(Location location);
}
