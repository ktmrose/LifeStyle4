package com.example.lifestyle4;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherDataTable weatherTable);

    @Query("SELECT * from weather_table")
    LiveData<List<WeatherDataTable>> getAll();
}
