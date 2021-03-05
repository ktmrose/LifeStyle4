package com.example.lifestyle4;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WeatherDataTable.class, UserDataTable.class}, version = 1, exportSchema = false)
public abstract class LifeStyleDatabase extends RoomDatabase {

    private static volatile LifeStyleDatabase mInstance;
    public abstract UserDAO userDao();
    public abstract WeatherDAO weatherDao();

    static synchronized LifeStyleDatabase getDatabase(final Context context) {
        if (mInstance==null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), LifeStyleDatabase.class, "lifestyle.db").build();
        }
        return mInstance;
    }

    private static Callback sLifeStyleDataBaseCallback = new Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //TODO: Async task call to populate db
        }
    };

    private static class PopulateWeatherAsync extends AsyncTask<WeatherDataTable, Void, Void> {
        private WeatherDAO mWeatherDao;

        PopulateWeatherAsync(WeatherDAO dao) { mWeatherDao = dao; }
        @Override
        protected Void doInBackground(WeatherDataTable... weatherDataTables) {
            //TODO: add calls to populate weather table
            return null;
        }
    }

    private static class PopulateUserAsync extends AsyncTask<UserDataTable, Void, Void> {
        private UserDAO mUserDao;

        @Override
        protected Void doInBackground(UserDataTable... userDataTables) {
            //TODO: add calls to populate user table
            return null;
        }
    }
}
