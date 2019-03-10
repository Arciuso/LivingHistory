package com.example.arcius.livinghistory.data.repository.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

@Entity
public class Picture {

    @Ignore
    public Picture(Integer pictureID, String link, String source, String title) {
        this.pictureID = pictureID;
        this.link = link;
        this.source = source;
        this.title = title;
    }

    public Picture() {

    }

    @PrimaryKey
    public Integer pictureID;

    @ColumnInfo(name = "link")
    public String link;

    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "title")
    public String title;

    public void log() {
        Log.d("Local-db PICTURE","\t\tpictureID : " + pictureID);
        Log.d("Local-db PICTURE", "\t\tlink : " + link);
        Log.d("Local-db PICTURE", "\t\tsource : " + source);
        Log.d("Local-db PICTURE", "\t\ttitle : " + title);
    }

}
