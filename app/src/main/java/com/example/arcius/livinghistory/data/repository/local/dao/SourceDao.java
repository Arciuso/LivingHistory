package com.example.arcius.livinghistory.data.repository.local.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.arcius.livinghistory.data.repository.local.entity.Source;

@Dao
public interface SourceDao {

    @Query("SELECT * FROM source WHERE eventID= :eventID")
    Source getSource(int eventID);

    @Insert
    void insertSource(Source source);

}
