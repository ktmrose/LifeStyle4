package com.example.lifestyle4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DashDisplayFrag extends Fragment {

    private String mUserName, mFitnessGoal, mImgPath;
    private int mDailyCalories, mBMR;
    private ImageView mIvImgThumb;
    private TextView mTvUserName, mTvDailyCalories, mTvFitnessGoal, mTvBMR;
    private LifeStyleViewModel mViewModel;
    private boolean mUserIsActive;
    private double mWeightMod;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dash_display_frag, container, false);

        mTvUserName = (TextView) view.findViewById(R.id.userName);
        mTvDailyCalories = (TextView) view.findViewById(R.id.calorieGoalDynamic);
        mTvFitnessGoal = (TextView) view.findViewById(R.id.fitnessGoalDynamic);
        mTvBMR = (TextView) view.findViewById(R.id.bmrDynamic);
        mIvImgThumb = (ImageView) view.findViewById(R.id.profilePic);

        mTvUserName.setText("");
        mTvBMR.setText("" + "--");
        mTvFitnessGoal.setText("" + "--");
        mTvDailyCalories.setText("" + "--");
        if (mImgPath == null) {
            mIvImgThumb.setVisibility(View.INVISIBLE);
        } else {
            mIvImgThumb.setVisibility(View.VISIBLE);
        }

        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);
        mViewModel.getUserData().observe(getActivity(), new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                Bitmap thumbNailImg = BitmapFactory.decodeFile(userData.getImgPath());
                if (thumbNailImg != null) {
                    mImgPath = userData.getImgPath();
                    mIvImgThumb.setImageBitmap(thumbNailImg);
                    mIvImgThumb.setVisibility(View.VISIBLE);
                }

                mUserName = userData.getName();

                mTvFitnessGoal.setText("change your weight by " + userData.getWeightMod() + " pounds this week.");

                mBMR = getBmr(userData.userIsFemale(), userData.getWeight(), userData.getHeightFt(), userData.getHeightIn(), userData.getAge());

                mUserIsActive = userData.userIsActive();
                mWeightMod = userData.getWeightMod();

            }
        });
        if(mUserName != null) {
            mTvUserName.setText(mUserName);
        }
        if(mBMR > 0) {
            mDailyCalories = getCaloricNeeds(mBMR, mUserIsActive, mWeightMod);
            mTvBMR.setText("" + mBMR);
            mTvDailyCalories.setText("" + mDailyCalories);
        }

        return view;
    }

    private int getBmr(boolean isFemale, int weight, int heightFt, int heightIn, int age){
        return (int) NutritionCalcsUtil.getBmr(isFemale, weight, heightFt, heightIn, age);
    }

    private int getCaloricNeeds(double bmr, boolean isActive, double poundsMod){
        int caloricNeeds = NutritionCalcsUtil.getDailyCalories(bmr, isActive, poundsMod);
        if (caloricNeeds < 1000) {
            Toast.makeText(getContext(), "Weight management goal adjusted: Caloric intake is too little for sustainability.", Toast.LENGTH_LONG).show();
            double suggestedWeightMod = NutritionCalcsUtil.findWeightModGoal(bmr, isActive);
            mViewModel.setWeightMod(suggestedWeightMod);
            return 1000; //this is the minimum allotted caloric daily intake.
        }
        return caloricNeeds;
    }
}
