package com.example.arcius.livinghistory.data.repository.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.arcius.livinghistory.data.repository.local.dao.EventDao;
import com.example.arcius.livinghistory.data.repository.local.dao.LocationDao;
import com.example.arcius.livinghistory.data.repository.local.dao.PictureDao;
import com.example.arcius.livinghistory.data.repository.local.dao.SourceDao;
import com.example.arcius.livinghistory.data.repository.local.entity.Event;
import com.example.arcius.livinghistory.data.repository.local.entity.Location;
import com.example.arcius.livinghistory.data.repository.local.entity.Picture;
import com.example.arcius.livinghistory.data.repository.local.entity.Source;


@Database(entities = {Event.class, Location.class, Source.class, Picture.class}, version = 2, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract SourceDao sourceDao();
    public abstract LocationDao locationDao();
    public abstract PictureDao pictureDao();
}
