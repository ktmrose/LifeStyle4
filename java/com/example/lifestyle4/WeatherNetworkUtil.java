package com.example.lifestyle4;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherNetworkUtil {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String APPIDQUERY = "&appid=";
    private static final String app_id="7aae9aeef640550636ed5114b87cf70e";
    
    public static URL buildURL(String location) {
        URL  url = null;
        try {
            url = new URL(BASE_URL + location + APPIDQUERY + app_id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getDataFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner sc = new Scanner(inputStream);
            sc.useDelimiter("\\A");
            boolean hasInput= sc.hasNext();
            if (hasInput)
                return sc.next();
            else
                return null;
        } finally {
            urlConnection.disconnect();
        }

    }
}
