package com.example.lifestyle4;

public class WeatherData {

    private String mWeatherDescription, mWeatherImgKeyword, mLocationName;
    private Temperature mTemperature = new Temperature();

    public String getLocationName() { return mLocationName; }

    public void setLocationName(String mLocationName) { this.mLocationName = mLocationName; }

    public String getWeatherDescription() { return mWeatherDescription; }

    public void setWeatherDescription(String mWeatherDescription) { this.mWeatherDescription = mWeatherDescription; }

    public String getWeatherImgKeyword() { return mWeatherImgKeyword; }

    public void setWeatherImgKeyword(String mWeatherImgKeyword) { this.mWeatherImgKeyword = mWeatherImgKeyword; }

    public Temperature getTemperature() { return mTemperature; }

    /**
     * Temperatures are in degrees Fahrenheit
     */
    public class Temperature {

        private double mCurrentTemp, mMaxTemp, mMinTemp;

        public double getCurrentTemp() { return mCurrentTemp; }

        public void setCurrentTemp(double mCurrentTemp) { this.mCurrentTemp = mCurrentTemp; }

        public double getMaxTemp() { return mMaxTemp; }

        public void setMaxTemp(double mMaxTemp) { this.mMaxTemp = mMaxTemp; }

        public double getMinTemp() { return mMinTemp; }

        public void setMinTemp(double mMinTemp) { this.mMinTemp = mMinTemp; }
    }
}
