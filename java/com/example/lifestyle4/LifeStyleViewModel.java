package com.example.lifestyle4;

import android.app.Application;
import android.widget.Toast;

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

    public void setLocation(String location){

        mRepo.setLocation(location);
    }

    public void setWeightMod(double weightMod) {
        UserData userDataCopy = mUserData.getValue();
        userDataCopy.setWeightMod(weightMod);
        setUserData(userDataCopy);
    }

    public void setUserActivity(boolean isActive){
        UserData userDataCopy = mUserData.getValue();
        userDataCopy.setIsActive(isActive);
        setUserData(userDataCopy);
    }

    public void setWeightModGoal(boolean isGaining, boolean isLosing, boolean isActive) {
        if (isGaining && isLosing) {
            Toast.makeText(getApplication(), "Cannot gain and lose weight at the same time", Toast.LENGTH_SHORT).show();
        }
        UserData userDataCopy = mUserData.getValue();
        userDataCopy.setIsGainingWeight(isGaining);
        userDataCopy.setIsLosingWeight(isLosing);
        userDataCopy.setIsActive(isActive);
        if ((!isGaining) && (!isLosing))
            userDataCopy.setWeightMod(0);
        setUserData(userDataCopy);
    }
}
