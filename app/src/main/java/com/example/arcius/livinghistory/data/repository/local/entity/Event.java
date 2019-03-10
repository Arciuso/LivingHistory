package com.example.arcius.livinghistory.data.repository.local.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

@Entity
public class Event {

    @Ignore
    public Event(Integer eventID, String date, String time, String mainTitle) {
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.date = date;
    }

    @Ignore
    public Event(Integer eventID, String date, String time, String mainTitle, String fullText) {
        this.eventID = eventID;
        this.time = time;
        this.mainTitle = mainTitle;
        this.fullText = fullText;
        this.date = date;
    }

    public Event() {

    }

    @PrimaryKey
    public Integer eventID;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "mainTitle")
    public String mainTitle;

    @ColumnInfo(name = "index")
    public Integer index;

    @ColumnInfo(name = "locationID")
    public Integer locationID;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "fullText")
    public String fullText;

    @ColumnInfo(name = "pictureID")
    public Integer pictureID;

    public void log() {
        Log.d("Local-db EVENT","\t\teventID : " + eventID);
        Log.d("Local-db EVENT", "\t\tdate : " + date);
        Log.d("Local-db EVENT", "\t\tmainTitle : " + mainTitle);
        Log.d("Local-db EVENT", "\t\tindex : " + index);
        Log.d("Local-db EVENT", "\t\tlocationID : " + locationID);
        Log.d("Local-db EVENT", "\t\ttime : " + time);
        Log.d("Local-db EVENT", "\t\tfullText : " + fullText);
        Log.d("Local-db EVENT", "\t\tpictureID : " + pictureID);
    }
}
