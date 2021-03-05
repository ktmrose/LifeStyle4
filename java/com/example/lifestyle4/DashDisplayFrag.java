package com.example.lifestyle4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        //TODO: set userData observer
        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);
        mViewModel.getUserData().observe(getActivity(), new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {
                Bitmap thumbNailImg = BitmapFactory.decodeFile(userData.getImgPath());
                if (thumbNailImg != null) {
                    mImgPath = userData.getImgPath();
                    mIvImgThumb.setImageBitmap(thumbNailImg);
                }

                if(userData.getName() != null) {
                    mUserName = userData.getName();
                    mTvUserName.setText(mUserName);
                }

                mTvFitnessGoal.setText("change your weight by " + userData.getWeightMod() + " pounds this week.");

                mBMR = getBmr(userData.userIsFemale(), userData.getWeight(), userData.getHeightFt(), userData.getHeightIn(), userData.getAge());
                mDailyCalories = getCaloricNeeds(mBMR, userData.userIsActive(), userData.getWeightMod());
                mTvBMR.setText("" + mBMR);
                mTvDailyCalories.setText("" + mDailyCalories);
            }
        });
        return view;
    }

    private int getBmr(boolean isFemale, int weight, int heightFt, int heightIn, int age){
        return (int) NutritionCalcsUtil.getBmr(isFemale, weight, heightFt, heightIn, age);
    }

    private int getCaloricNeeds(double bmr, boolean isActive, double poundsMod){
        return NutritionCalcsUtil.getDailyCalories(bmr, isActive, poundsMod);
    }
}
