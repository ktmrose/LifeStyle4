package com.example.lifestyle4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_table")
public class WeatherDataTable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Location")
    private String location;

    @NonNull
    @ColumnInfo(name = "WeatherData")
    private String weatherJson;

    public WeatherDataTable(@NonNull String location, @NonNull String data) {
        this.location = location;
        this.weatherJson = data;
    }

    public WeatherDataTable() {}

    @NonNull
    public String getLocation() {
        return location;
    }

    public void setLocation(@NonNull String location) {
        this.location = location;
    }

    @NonNull
    public String getWeatherJson() {
        return weatherJson;
    }

    public void setWeatherJson(@NonNull String weatherJson) {
        this.weatherJson = weatherJson;
    }
}
