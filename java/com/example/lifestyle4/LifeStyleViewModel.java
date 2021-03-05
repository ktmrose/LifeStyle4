package com.example.lifestyle4;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LifeStyleViewModel extends AndroidViewModel {
    private MutableLiveData<UserData> mUserData;
    private MutableLiveData<WeatherData> mWeatherData;
    private LifeStyleRepository mRepo;

    public LifeStyleViewModel(@NonNull Application application) {
        super(application);
        mRepo = new LifeStyleRepository(application);
        mWeatherData = mRepo.getWeatherData();
        mUserData = mRepo.getUserData();
    }

    public LiveData<WeatherData> getWeatherData(){
        return mWeatherData;
    }

    public LiveData<UserData> getUserData() {
        return mUserData;
    }


    public void setUserData(UserData user) {
        mRepo.setUserData(user);
    }
}
