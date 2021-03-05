package com.example.lifestyle4;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeatherUtil {
    public static WeatherData getWeatherData(String jsonData, String locationName) throws JSONException {
        WeatherData weatherData = new WeatherData();

        JSONObject jsonObject = new JSONObject(jsonData);

        //sets temperatures
        WeatherData.Temperature temps = weatherData.getTemperature();
        JSONObject jsonMain = jsonObject.getJSONObject("main");
        temps.setCurrentTemp(toFahrenheit(jsonMain.getDouble("temp")));
        temps.setMaxTemp(toFahrenheit(jsonMain.getDouble("temp_max")));
        temps.setMinTemp(toFahrenheit(jsonMain.getDouble("temp_min")));

        //sets descriptors
        weatherData.setLocationName(locationName);
        JSONObject jsonWeather =  jsonObject.getJSONArray("weather").getJSONObject(0);
        weatherData.setWeatherDescription(jsonWeather.getString("description"));
        weatherData.setWeatherImgKeyword(jsonWeather.getString("main"));

        return weatherData;
    }

    private static double toFahrenheit(Double kelvin) {
        return (kelvin - 273.15) * 9/5 + 32;
    }
}
