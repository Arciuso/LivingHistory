package com.example.arcius.livinghistory.data.repository.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.arcius.livinghistory.data.repository.local.entity.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event WHERE date= :date")
    List<Event> getAllByDate(String date);

    @Query("SELECT * FROM event WHERE eventID= :eventID")
    Event getEvent(int eventID);

    @Query("SELECT date FROM event")
    List<String> getAllDates();

    @Query("SELECT eventID FROM event WHERE date= :date")
    List<Integer> getEventIDsByDate(String date);

    @Insert
    void insertEvent(Event event);
}
