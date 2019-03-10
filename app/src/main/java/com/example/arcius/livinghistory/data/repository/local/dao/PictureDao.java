package com.example.arcius.livinghistory.data.repository.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.arcius.livinghistory.data.repository.local.entity.Picture;

@Dao
public interface PictureDao {   //TODO

    @Query("SELECT * FROM picture WHERE pictureID= :pictureID")
    Picture getPicture(int pictureID);

    @Insert
    void insertPicture(Picture picture);
}
