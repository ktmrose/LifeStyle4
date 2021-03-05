package com.example.lifestyle4;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class SetFitGoals extends Fragment implements View.OnClickListener{

    private Button mYesBtn, mNoBtn, mLoseWeight, mMaintainWeight, mGainWeight;
    private boolean mIsFit;
    private double mWeightMod;
    private FitnessGoalsDataPasser mFitGoalsData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.set_fitness_goals, container, false);

        //grab xml stuff
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

        if (savedInstanceState != null) {

            savedInstanceState.getBoolean("IS_ACTIVE");
            savedInstanceState.getDouble("WEIGHT_MOD", 0);
            resetButtonColors();
        }
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

        if (mWeightMod > 0) {

            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        } else if (mWeightMod < 0) {

            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        } else {

            mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
            mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mFitGoalsData = (FitnessGoalsDataPasser) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + "must implement SetFitGoalsDataPasser");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.excerciseYes: {

                mIsFit = true;
                mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.excerciseNo: {

                mIsFit = false;
                mNoBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mYesBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                break;
            }
            case R.id.lose_weight_btn: {

                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));

                mFitGoalsData.fitGoalsData(mIsFit, mWeightMod*-1, 1); //sign indicates gain or loss
                break;
            }
            case R.id.maintain_weight_btn: {

                mWeightMod = 0;
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mFitGoalsData.fitGoalsData(mIsFit, mWeightMod, 0);
                break;
            }
            case R.id.gain_weight_btn: {

                mGainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mLoseWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));
                mMaintainWeight.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_accent));

                mFitGoalsData.fitGoalsData(mIsFit, mWeightMod, 2);
                break;
            }
        }
    }

    public interface FitnessGoalsDataPasser {
        public void fitGoalsData(boolean isActive, double weightMod, int fragmentCode);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("WEIGHT_MOD", mWeightMod);
        outState.putBoolean("IS_ACTIVE", mIsFit);
    }
}
