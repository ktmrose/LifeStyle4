package com.example.lifestyle4;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SetFitGoals extends Fragment implements View.OnClickListener{

    private Button mYesBtn, mNoBtn, mLoseWeight, mMaintainWeight, mGainWeight;
    private boolean mIsFit, mIsLosingWeight, mIsGainingWeight, mIsTablet;
    private double mWeightMod;
    private LifeStyleViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_fitness_goals, container, false);
        mIsTablet = getResources().getBoolean(R.bool.isTablet);

        mYesBtn = view.findViewById(R.id.excerciseYes);
        mNoBtn = view.findViewById(R.id.excerciseNo);
        mLoseWeight = view.findViewById(R.id.lose_weight_btn);
        mMaintainWeight = view.findViewById(R.id.maintain_weight_btn);
        mGainWeight = view.findViewById(R.id.gain_weight_btn);

        mYesBtn.setOnClickListener(this);
        mNoBtn.setOnClickListener(this);
        mLoseWeight.setOnClickListener(this);
        mMaintainWeight.setOnClickListener(this);
        mGainWeight.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);
        mViewModel.getUserData().observe(getActivity(), new Observer<UserData>() {

            @Override
            public void onChanged(UserData userData) {

                if (userData.userIsActive())
                    mIsFit = true;
                else
                    mIsFit = false;

                mWeightMod = userData.getWeightMod();
                if (mWeightMod > 0) {

                    mIsGainingWeight = true;
                    mIsLosingWeight = false;
                } else if (mWeightMod < 0) {

                    mIsLosingWeight = true;
                    mIsGainingWeight = false;
                } else {

                    mIsGainingWeight = false;
                    mIsLosingWeight = false;
                }
            }
        });

        resetButtonColors();
        return view;
    }

    private void resetButtonColors() {

        if (mIsFit) {

            mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        } else {

            mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        }

        if (mIsGainingWeight) {

            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        } else if (mIsLosingWeight) {

            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        } else {

            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.excerciseYes: {

                mIsFit = true;
                if (mViewModel.getUserData().getValue() == null) {
                    Toast.makeText(getContext(), "Please update your settings.", Toast.LENGTH_SHORT).show();
                    break;
                }
                mViewModel.setUserActivity(mIsFit);
                mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.excerciseNo: {

                mIsFit = false;
                if (mViewModel.getUserData().getValue() == null) {
                    Toast.makeText(getContext(), "Please update your settings.", Toast.LENGTH_SHORT).show();
                    break;
                }
                mViewModel.setUserActivity(mIsFit);
                mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.lose_weight_btn: {

                mIsLosingWeight = true;
                mIsGainingWeight = false;
                updateUserData();

                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.maintain_weight_btn: {

                mIsLosingWeight = false;
                mIsGainingWeight = false;
                updateUserData();

                mWeightMod = 0;
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.gain_weight_btn: {

                mIsLosingWeight = false;
                mIsGainingWeight = true;
                updateUserData();

                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
        }
    }

    private void updateUserData(){

        if (mViewModel.getUserData().getValue() == null) {
            Toast.makeText(getContext(), "Please update your settings.", Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.setWeightModGoal(mIsGainingWeight, mIsLosingWeight, mIsFit);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("WEIGHT_MOD", mWeightMod);
        outState.putBoolean("IS_ACTIVE", mIsFit);
    }
}
