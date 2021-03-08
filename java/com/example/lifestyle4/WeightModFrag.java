package com.example.lifestyle4;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class WeightModFrag extends Fragment implements View.OnClickListener{

    double mWeightMod;
    Button mSubmitMod;
    EditText mModInput;
    TextView mWeightModDisplay;
    boolean isGainingWeight;

    LifeStyleViewModel mViewModel;

    //error message
    AlertDialog.Builder mAlertBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.weight_mod_frag, container, false);

        mSubmitMod = view.findViewById(R.id.submit_btn);
        mModInput = view.findViewById(R.id.modPounds_editText);
        mSubmitMod.setOnClickListener(this);
        mWeightModDisplay = view.findViewById(R.id.modPounds_dynamic);
        mAlertBuilder = new AlertDialog.Builder(getContext());

        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null)
            isGainingWeight = bundle.getBoolean("GAIN_WEIGHT");

        if (isGainingWeight)
            mWeightModDisplay.setText("" + "gain");
        else
            mWeightModDisplay.setText("" + "lose");

        return view;
    }

//    public interface WeightModDataPasser {
//        public void onWeightModData(double weightMod);
//    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            mModDataPasser = (WeightModDataPasser) context;
//        } catch (Exception e) {
//            throw new ClassCastException(context.toString() + "must implement WeightModDataPasser");
//        }
//    }

    @Override
    public void onClick(View v) {

        if (mModInput.getText().toString().equals("")) {
            return;
        }
        double weightModValue = Double.parseDouble(mModInput.getText().toString());
        
        if (weightModValue > 2) {
//
//            mAlertBuilder.setMessage("Easy there! Try making a more maintainable goal. Try aiming for 2 pounds/week or less?");
//            mAlertBuilder.setTitle("Your weight goal is unsustainable");
//            mAlertBuilder.setCancelable(true);
//            mAlertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mWeightModDisplay.setText("" + "2");
//                }
//            });
//
//            AlertDialog alert = mAlertBuilder.create();
//            alert.show();
            Toast.makeText(getContext(), "Easy there! Try making a more maintainable goal. Try aiming for 2 pounds/week or less?", Toast.LENGTH_LONG).show();
            return;
        }

        if (isGainingWeight)
            mWeightMod = weightModValue;
        else
            mWeightMod = -1*weightModValue;

        updateUserData(mWeightMod);
    }

    private void updateUserData(double weightMod) {
        mViewModel.setWeightMod(weightMod);
    }
}
