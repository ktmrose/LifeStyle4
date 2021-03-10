package com.example.lifestyle4;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeatherFrag extends Fragment implements View.OnClickListener, LocationListener {

    private Button mRefresh;
    private TextView mWeatherCondition, mLocationDiplay, mHighTemp, mLowTemp, mUpdatedTimeStamp, mCurrentTemp;
    private ProgressBar mPb;
    private ImageView mWeatherImg;

    String mLocationName;
    LocationManager mLocationManager;

    LifeStyleViewModel mViewModel;

    public WeatherFrag() {
    }


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

        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);


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
    public void onClick(View v) {

        //TODO: maybe get rid of button and fix view in constructor

        if (mLocationName != null)
            mViewModel.setLocation(mLocationName);
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        //grab location name
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                mLocationName = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        super.onPause();
        //TODO: unregister location manager
        mLocationManager.unregisterGnssMeasurementsCallback(mGnssCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        //TODO: register location manager
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.registerGnssMeasurementsCallback(mGnssCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private GnssMeasurementsEvent.Callback mGnssCallback = new GnssMeasurementsEvent.Callback() {
        @Override
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
            super.onGnssMeasurementsReceived(eventArgs);
        }

        @Override
        public void onStatusChanged(int status) {
            super.onStatusChanged(status);
        }
    };

}

