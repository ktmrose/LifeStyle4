package com.example.lifestyle4;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.DataStoreException;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class LifeStyleRepository extends BroadcastReceiver{
    private final MutableLiveData<UserData> mUserData = new MutableLiveData<>();
    private final MutableLiveData<WeatherData> mWeatherData = new MutableLiveData<>();

    //for weather
    private String mLocation;
    private String mJsonWeather;

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

        if (mLocation != null)
            loadWeatherData();
        else {
            mLocation = "Salt Lake City";
            loadWeatherData();
        }
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(application);

            Log.i("LifeStyleApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("LifeStyleApp", "Could not initialize Amplify", error);
        }
    }

    public void setUserData(UserData userData) {
        mUserData.setValue(userData);
        UserDataTable userTable = new UserDataTable(userData);
        new insertUserTask(mUserDao).execute(userTable);
        //upload to AWS
        try {
            uploadToCloud();
        } catch (AmplifyException e) {
            Toast.makeText(mContext, "Amplify error", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void insertWeather(){
        WeatherDataTable wdt = new WeatherDataTable(mLocation, mJsonWeather);
        new insertWeatherTask(mWeatherDao).execute(wdt);
    }

    private void loadWeatherData() {
        String locationNameQuery = mLocation.replaceAll(" ", "&");
        new FetchWeatherTask(this).execute(locationNameQuery);
    }

    public void setLocation(String location) {
        mLocation = location;
        UserData userData = mUserData.getValue();
        if (userData != null) {
            userData.setLocation(location);
        }
        loadWeatherData();
    }

    /*
    Monitors device connection to power and pushes to s3 bucket
        //TODO: connect to Wifi instead of power
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        File userFile = userDataToFile();
        Amplify.Storage.uploadFile("User_Data",
                userFile,
                result ->  Toast.makeText(mContext, "Upload successful", Toast.LENGTH_LONG).show(),
//                        Log.i("LifeStyleApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Toast.makeText(mContext, "Upload failed", Toast.LENGTH_LONG).show());
//                        Log.e("LifeStyleApp", "Upload failed", storageFailure));


        File weatherFile = weatherDataToFile();
        Amplify.Storage.uploadFile("Weather_Data",
                userFile,
                result -> Log.i("LifeStyleApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("LifeStyleApp", "Upload failed", storageFailure));
    }

     private void uploadToCloud() throws AmplifyException {
        File userFile = userDataToFile();

        Amplify.Storage.uploadFile("User_Data",
                userFile,
                result -> Log.i("LifeStyleApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("LifeStyleApp", "Upload failed", storageFailure));


        File weatherFile = weatherDataToFile();
        Amplify.Storage.uploadFile("Weather_Data",
                userFile,
                result -> Log.i("LifeStyleApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("LifeStyleApp", "Upload failed", storageFailure));
    }

    private File userDataToFile() {

        File userFile = new File(mContext.getFilesDir(), "UserData");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            writer.append(getUserData().getValue().toJson());
            writer.close();
        } catch (Exception exception) {
            Toast.makeText(mContext, "Upload failed" + exception, Toast.LENGTH_LONG).show();
            Log.e("LifeStyleApp", "Upload failed", exception);
        }
        return userFile;
    }

    private File weatherDataToFile() {

        File userFile = new File(mContext.getFilesDir(), "WeatherData");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            writer.append(getUserData().getValue().toJson());
            writer.close();
        } catch (Exception exception) {
            Log.e("LifeStyleApp", "Upload failed", exception);
        }
        return userFile;
    }

    //TODO: Turn AsyncTask into WorkManager that fetches weather data.
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

    /*
    Methods below add weather and user data respectively to Room database

     */
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

