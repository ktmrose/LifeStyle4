package com.example.lifestyle4;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserDataTable userTable);

    @Query("SELECT * from user_table")
    LiveData<List<UserDataTable>> getAll();
}
