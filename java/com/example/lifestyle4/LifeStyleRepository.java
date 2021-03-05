package com.example.lifestyle4;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class LifeStyleRepository implements LocationListener {
    private final MutableLiveData<UserData> mUserData = new MutableLiveData<>();
    private final MutableLiveData<WeatherData> mWeatherData = new MutableLiveData<>();

    //for weather
    private String mLocation;
    private String mJsonWeather;

    private LocationManager mLocationManager;
    private final Context mContext;

    private WeatherDAO mWeatherDao;
    private UserDAO mUserDao;

    public MutableLiveData<UserData> getUserData() {
        return mUserData;
    }

    public MutableLiveData<WeatherData> getWeatherData() {
        return mWeatherData;
    }

    LifeStyleRepository(Application application) {
        LifeStyleDatabase db = LifeStyleDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mWeatherDao = db.weatherDao();
        mContext = application;

        mLocationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        if (mLocation != null)
            loadWeatherData();
        else {
            mLocation = "Salt Lake City";
            loadWeatherData();
        }

    }

    public void setUserData(UserData userData) {
        mUserData.setValue(userData);
        UserDataTable userTable = new UserDataTable(userData);
        new insertUserTask(mUserDao).execute(userTable);
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //grab location name
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                mLocation = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    private void insertWeather(){
        WeatherDataTable wdt = new WeatherDataTable(mLocation, mJsonWeather);
        new insertWeatherTask(mWeatherDao).execute(wdt);
    }

    private void loadWeatherData() {
        String locationNameQuery = mLocation.replaceAll(" ", "&");
        new FetchWeatherTask(this).execute(locationNameQuery);
    }

    //TODO: Turn AsyncTask into WorkManager
    private static class FetchWeatherTask extends AsyncTask<String, Void, String> {

        private WeakReference<LifeStyleRepository> mRepo;

        private FetchWeatherTask(LifeStyleRepository repo) {
            mRepo = new WeakReference<>(repo);
        }

        @Override
        protected String doInBackground(String... strings) {
            String location = strings[0];
            URL urlQuery = WeatherNetworkUtil.buildURL(location);
            String jsonWeatherData;
            try {
                jsonWeatherData = WeatherNetworkUtil.getDataFromURL(urlQuery);
                return jsonWeatherData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonWeatherData) {
            super.onPostExecute(jsonWeatherData);
            LifeStyleRepository localRepo = mRepo.get();
            if (jsonWeatherData != null) {

                localRepo.mJsonWeather = jsonWeatherData;
                localRepo.insertWeather();
                try {
                    localRepo.mWeatherData.setValue(JSONWeatherUtil.getWeatherData(jsonWeatherData, localRepo.mLocation));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class insertWeatherTask extends AsyncTask<WeatherDataTable, Void, Void> {

        private WeatherDAO mAsyncWeatherDao;
        insertWeatherTask(WeatherDAO dao) {mAsyncWeatherDao = dao;}
        @Override
        protected Void doInBackground(WeatherDataTable... weatherDataTables) {
            mAsyncWeatherDao.insert(weatherDataTables[0]);
            return null;
        }
    }

    private static class insertUserTask extends AsyncTask<UserDataTable, Void, Void> {

        private UserDAO mAsyncUserDao;
        insertUserTask(UserDAO dao) {mAsyncUserDao = dao;}
        @Override
        protected Void doInBackground(UserDataTable... userDataTables) {
            mAsyncUserDao.insert(userDataTables[0]);
            return null;
        }
    }
}

