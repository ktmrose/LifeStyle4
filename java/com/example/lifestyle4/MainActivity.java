package com.example.lifestyle4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SetFitGoals.FitnessGoalsDataPasser, WeightModFrag.WeightModDataPasser {

    private LifeStyleViewModel mViewModel;
    private ImageButton mSetFitGoalsBtn, mSettingsBtn, mWeatherBtn;

    //from settings data
    private int mDailyCalories;
    private double mBmr, mWeightModGoal; //default value is 0
    private String mUserName, mImgPath;
    private boolean mIsActive; //default value is false

    //fragments dynamically displayed
    SetFitGoals mSetFitGoals;
    WeightModFrag mWeightMod;
    DashDisplayFrag mDashDisplay;
    SettingsFrag mSettings;
    WeatherFrag mWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //grab xml objects
        mSetFitGoalsBtn = findViewById(R.id.fitness_goals_btn);
        mSettingsBtn = findViewById(R.id.settings_btn);
        mWeatherBtn = findViewById(R.id.weather_btn);

        mSetFitGoalsBtn.setOnClickListener(this);
        mSettingsBtn.setOnClickListener(this);
        mWeatherBtn.setOnClickListener(this);

        DashDisplayFrag dashDisp = new DashDisplayFrag();
        if (savedInstanceState != null) {

            savedInstanceState.getInt("DAILY_CALORIES", NutritionCalcsUtil.getDailyCalories(mBmr, mIsActive, mWeightModGoal));
            savedInstanceState.getDouble("USER_BMR");
            savedInstanceState.getDouble("MOD_GOAL", 0);
            savedInstanceState.getString("IMAGE_PATH");
            savedInstanceState.getString("USER_NAME", "Karen");
            savedInstanceState.getBoolean("USER_ACTIVE", false);

            Bundle bundle = new Bundle();
            bundle.putDouble("USER_BMR", mBmr);
            bundle.putDouble("MOD_GOAL", mWeightModGoal);
            bundle.putString("USER_NAME", mUserName);
            bundle.putString("IMAGE_PATH", mImgPath);
            bundle.putInt("DAILY_CALORIES", mDailyCalories);
            dashDisp.setArguments(bundle);
        }

        mDashDisplay = dashDisp;
        Intent recievedIntent = getIntent(); // from Settings
        Bundle recievedBundle = recievedIntent.getBundleExtra("SETTINGS_BUNDLE");
        if (recievedBundle != null) {
            mUserName = recievedBundle.getString("USER_NAME");
            mImgPath = recievedBundle.getString("IMAGE_PATH");
            mBmr = recievedBundle.getDouble("USER_BMR");

            Bundle bundle = new Bundle();
            bundle.putDouble("USER_BMR", mBmr);
            bundle.putDouble("MOD_GOAL", mWeightModGoal);
            bundle.putString("USER_NAME", mUserName);
            bundle.putString("IMAGE_PATH", mImgPath);
            bundle.putInt("DAILY_CALORIES", mDailyCalories);
            dashDisp.setArguments(bundle);
        }

        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.ph_dash_display, dashDisp, "dash_display");
        fTrans.commit();

        //Create the view model
        mViewModel = ViewModelProviders.of(this).get(LifeStyleViewModel.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fitness_goals_btn: {

                SetFitGoals currentFrag = (SetFitGoals) getSupportFragmentManager().findFragmentByTag("set_fit_goals");
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                if (currentFrag != null && currentFrag.isVisible()) {

                    Bundle bundle = new Bundle();
                    bundle.putDouble("USER_BMR", mBmr);
                    bundle.putDouble("MOD_GOAL", mWeightModGoal);
                    bundle.putString("USER_NAME", mUserName);
                    bundle.putString("IMAGE_PATH", mImgPath);
                    bundle.putInt("DAILY_CALORIES", mDailyCalories);

                    mDashDisplay = new DashDisplayFrag();
                    mDashDisplay.setArguments(bundle);
                    fTrans.replace(R.id.ph_dash_display, mDashDisplay, "dash_display");
                } else {

                    mSetFitGoals = new SetFitGoals();
                    fTrans.replace(R.id.ph_dash_display, mSetFitGoals, "set_fit_goals");
                }

                fTrans.commit();

                //make sure other buttons are visible
                mSettingsBtn.setVisibility(View.VISIBLE);
                mWeatherBtn.setVisibility(View.VISIBLE);
                break;
            }
            case  R.id.weather_btn: {

                mWeather = new WeatherFrag();
                mWeather.setRetainInstance(true);
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.ph_dash_display, mWeather, "weather");
                fTrans.commit();

                //hide buttons
                mSettingsBtn.setVisibility(View.INVISIBLE);
                mWeatherBtn.setVisibility(View.INVISIBLE);

                break;
            }
            case R.id.settings_btn: {

                mSettings = new SettingsFrag();
                FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
                fTrans.replace(R.id.ph_dash_display, mSettings, "settings");
                fTrans.commit();

                //hide buttons
                mWeatherBtn.setVisibility(View.INVISIBLE);
                mSettingsBtn.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("DAILY_CALORIES", mDailyCalories);
        outState.putDouble("USER_BMR", mBmr);
        outState.putDouble("MOD_GOAL", mWeightModGoal);
        outState.putString("USER_NAME", mUserName);
        outState.putString("IMAGE_PATH", mImgPath);
        outState.putBoolean("USER_ACTIVE", mIsActive);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.getInt("DAILY_CALORIES");
        savedInstanceState.getDouble("USER_BMR");
        savedInstanceState.getDouble("MOD_GOAL", 0);
        savedInstanceState.getString("IMAGE_PATH");
        savedInstanceState.getString("USER_NAME", "Karen");
        savedInstanceState.getBoolean("USER_ACTIVE", false);
    }

    @Override
    public void fitGoalsData(boolean isActive, double weightMod, int fragmentCode) {
        mIsActive = isActive;
        mWeightModGoal = weightMod;
        mDailyCalories = NutritionCalcsUtil.getDailyCalories(mBmr, mIsActive, mWeightModGoal);

        Bundle modDisplayBundle = new Bundle();
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

        if (fragmentCode == 1) { // lose weight

            mWeightMod = new WeightModFrag();
            modDisplayBundle.putBoolean("GAIN_WEIGHT", false);
            mWeightMod.setArguments(modDisplayBundle);
            fTrans.replace(R.id.ph_dash_display, mWeightMod, "weight_loss_fragment");
        } else if (fragmentCode == 2) { // gain weight

            mWeightMod = new WeightModFrag();
            modDisplayBundle.putBoolean("GAIN_WEIGHT", true);
            mWeightMod.setArguments(modDisplayBundle);
            fTrans.replace(R.id.ph_dash_display, mWeightMod, "weight_gain_Fragment");
        } else {
            System.out.println("Something went wrong when displaying fragments in Main Activity");
        }

        fTrans.commit();
    }

    @Override
    public void onWeightModData(double weightMod) {
        
        mWeightModGoal = weightMod;

        if (mDailyCalories < 1000) {
            Toast.makeText(this, "I think you should update your settings", Toast.LENGTH_LONG).show();

            //opens settings panel
            mSettings = new SettingsFrag();
            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.ph_dash_display, mSettings, "settings");
            fTrans.commit();

            //hide buttons
            mWeatherBtn.setVisibility(View.INVISIBLE);
            mSettingsBtn.setVisibility(View.INVISIBLE);
            return;
        }
        
        mDailyCalories = NutritionCalcsUtil.getDailyCalories(mBmr, mIsActive, mWeightModGoal);
        if (mDailyCalories < 1000)
            Toast.makeText(this, "If you did that, you'd be eating less than 1000 calories! That's not sustainable!", Toast.LENGTH_LONG).show();
        else {
            //send to display fragment
            mDashDisplay = new DashDisplayFrag();
            Bundle bundle = new Bundle();
            bundle.putDouble("USER_BMR", mBmr);
            bundle.putDouble("MOD_GOAL", mWeightModGoal);
            bundle.putString("USER_NAME", mUserName);
            bundle.putString("IMAGE_PATH", mImgPath);
            bundle.putInt("DAILY_CALORIES", mDailyCalories);
            mDashDisplay.setArguments(bundle);

            FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.ph_dash_display, mDashDisplay, "dash_display");
            fTrans.commit();
        }
    }
}