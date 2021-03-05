package com.example.lifestyle4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

public class WeatherFrag extends Fragment implements View.OnClickListener {

    private Button mRefresh;
    private TextView mWeatherCondition, mLocationDiplay, mHighTemp, mLowTemp, mUpdatedTimeStamp, mCurrentTemp;
    private ProgressBar mPb;
    private ImageView mWeatherImg;

    String mLocationName;
    WeatherData mWeatherData;

    LifeStyleViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.weather_frag, container, false);

        //grab xml
        mRefresh = view.findViewById(R.id.refresh_weather);
        mPb = view.findViewById(R.id.progress_Bar);
        mWeatherCondition = view.findViewById(R.id.weather_condition);
        mLocationDiplay = view.findViewById(R.id.current_location);
        mHighTemp = view.findViewById(R.id.high_temp);
        mLowTemp = view.findViewById(R.id.low_temp);
        mUpdatedTimeStamp = view.findViewById(R.id.timestamp);
        mCurrentTemp = view.findViewById(R.id.current_temperature);
        mWeatherImg = view.findViewById(R.id.weather_image);

        mPb.setVisibility(View.INVISIBLE);
        mRefresh.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);
        mViewModel.getWeatherData().observe(getActivity(), new Observer<WeatherData>() {
            @Override
            public void onChanged(WeatherData weatherData) {

                if (weatherData != null) {
                    mWeatherCondition.setText("" + "Current weather condition: " + weatherData.getWeatherDescription());
                    mLocationDiplay.setText("" + weatherData.getLocationName());
                    mCurrentTemp.setText("" + "Current temperature: " + Math.round(weatherData.getTemperature().getCurrentTemp()) + "˚F");
                    mHighTemp.setText("" + "Today's high: " + Math.round(weatherData.getTemperature().getMaxTemp()) + "˚F");
                    mLowTemp.setText("" + "Today's low: " + Math.round(weatherData.getTemperature().getMinTemp()) + "˚F");
                    mUpdatedTimeStamp.setText("" + "Last updated: " + Calendar.getInstance().getTime());
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {//since there is only one button I'm not bothering with the switch statements

        //TODO: maybe get rid of button and fix view in constructor
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
//
//        getWeatherData();
    }

}
