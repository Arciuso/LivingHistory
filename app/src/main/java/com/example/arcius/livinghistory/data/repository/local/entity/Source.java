package com.example.arcius.livinghistory.data.repository.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

@Entity
public class Source {

    @Ignore
    public Source(Integer sourceID, Integer eventID, String sourceName, String sourceLink, String sourceTitle) {
        this.sourceID = sourceID;
        this.eventID = eventID;
        this.name = sourceName;
        this.link = sourceLink;
        this.title = sourceTitle;
    }

    public Source() {

    }

    @PrimaryKey
    public Integer sourceID;

    @ColumnInfo(name = "eventID")
    public Integer eventID;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "name")
    public String name;

    public void log() {
        Log.d("Local-db SOURCE","\t\tsourceID : " + sourceID);
        Log.d("Local-db SOURCE", "\t\teventID : " + eventID);
        Log.d("Local-db SOURCE", "\t\tlink : " + link);
        Log.d("Local-db SOURCE", "\t\ttitle : " + title);
        Log.d("Local-db SOURCE", "\t\tname : " + name);
    }
}
