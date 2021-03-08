package com.example.lifestyle4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class SettingsFrag extends Fragment implements View.OnClickListener {

    private String mUserName, mImgPath;
    private int mAge, mHeightFeet, mHeightInches, mWeight;
    private boolean isFemale;
    private EditText mEtUserName, mEtAge, mEtHeightFeet, mEtHeightInches, mEtWeight;
    private ImageButton mTakePicBtn, mMaleBtn, mFemaleBtn;
    private Button mSubmitSettings;
    private ImageView mIvProfilePic;

    LifeStyleViewModel mViewModel;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_frag, container, false);

        //grab xml objects
        mEtUserName = view.findViewById(R.id.user_name_dyn);
        mEtAge = view.findViewById(R.id.user_age_dyn);
        mEtHeightFeet = view.findViewById(R.id.user_feet);
        mEtHeightInches = view.findViewById(R.id.user_inches);
        mEtWeight = view.findViewById(R.id.user_weight_input);
        mIvProfilePic = view.findViewById(R.id.profile_pic);
        mTakePicBtn = view.findViewById(R.id.take_picture_btn);
        mMaleBtn = view.findViewById(R.id.male_btn);
        mFemaleBtn = view.findViewById(R.id.female_btn);
        mSubmitSettings = view.findViewById(R.id.submitSettings_btn);

        //set click listeners
        mTakePicBtn.setOnClickListener(this);
        mMaleBtn.setOnClickListener(this);
        mFemaleBtn.setOnClickListener(this);
        mSubmitSettings.setOnClickListener(this);

        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);

        //populate them
        mViewModel = ViewModelProviders.of(getActivity()).get(LifeStyleViewModel.class);
        mViewModel.getUserData().observe(getActivity(), new Observer<UserData>() {
            @Override
            public void onChanged(UserData userData) {

                if (userData == null)
                    return;

                if (userData.getImgPath() != null) {
                    mImgPath = userData.getImgPath();
                }

                if(userData.getName() != null) {
                    mUserName = userData.getName();
                    mEtUserName.setText(mUserName);
                }

                if (userData.userIsFemale())
                    isFemale = true;
                else
                    isFemale = false;

                mAge = userData.getAge();
                mHeightFeet = userData.getHeightFt();
                mHeightInches = userData.getHeightIn();
                mWeight = userData.getWeight();
            }
        });

        if (mUserName != null)
            mEtUserName.setText(mUserName);

        if (mAge > 0)
            mEtAge.setText("" + mAge);

        if (mHeightFeet > 0)
            mEtHeightFeet.setText("" + mHeightFeet);

        if (mHeightInches > 0)
            mEtHeightInches.setText("" + mHeightInches);

        if (mWeight > 0)
            mEtWeight.setText("" + mWeight);

        if (isFemale) {
            mFemaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mMaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.light_green));
        } else {
            mMaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
            mFemaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.light_green));
        }

        Bitmap thumbNailImg = BitmapFactory.decodeFile(mImgPath);
        if (thumbNailImg != null) {
            mIvProfilePic.setImageBitmap(thumbNailImg);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mUserName = savedInstanceState.getString("USER_NAME");
            mAge = savedInstanceState.getInt("USER_AGE");
            mHeightFeet = savedInstanceState.getInt("USER_HEIGHT_FEET");
            mHeightInches = savedInstanceState.getInt("USER_HEIGHT_INCHES");
            mWeight = savedInstanceState.getInt("USER_WEIGHT");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture_btn: {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
            case R.id.male_btn: {

                isFemale = false;

                mMaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mFemaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.light_green));
                break;
            }
            case R.id.female_btn: {

                isFemale = true;

                //button highlights
                mFemaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.gold));
                mMaleBtn.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.light_green));
                break;
            }
            case R.id.submitSettings_btn: {

                //sanitize username
                String usernameInput = mEtUserName.getText().toString();
                if (usernameInput.equals("")){
                    Toast.makeText(getContext(), "Save aborted: Please enter name", Toast.LENGTH_SHORT).show();
                    break;
                }

                usernameInput = usernameInput.trim(); //removes leading and trailing whitespace
                usernameInput = usernameInput.replaceAll("[\\W]|_", ""); //removes all non-alphabetic characters
                mUserName = usernameInput;

                //user is only allowed to enter numbers
                if (mEtAge.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Save aborted: Please enter age", Toast.LENGTH_SHORT).show();
                    break;
                }

                mAge = Integer.parseInt(mEtAge.getText().toString());
                if (mAge < 20) {
                    Toast.makeText(getContext(), "Save aborted: Nutrition calculations for persons at least 20 years old", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (mEtHeightFeet.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Save aborted: Please enter height (feet)", Toast.LENGTH_SHORT).show();
                    break;
                }
                mHeightFeet = Integer.parseInt(mEtHeightFeet.getText().toString());
                if (mHeightFeet < 7 || mHeightFeet > 4) {
                    Toast.makeText(getContext(), "Warning: Nutrition calculations for this height range may not be accurate", Toast.LENGTH_LONG).show();
                }

                if (mEtHeightInches.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Save aborted: Please enter height (inches)", Toast.LENGTH_SHORT).show();
                    break;
                }
                mHeightInches = Integer.parseInt(mEtHeightInches.getText().toString());

                if (mHeightInches <= 12) {
                    Toast.makeText(getContext(), "Warning: Did you mean to type a number greater than 12 for inches?", Toast.LENGTH_LONG).show();
                }

                if (mEtWeight.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Save aborted: Please enter weight", Toast.LENGTH_SHORT).show();
                    break;
                }
                mWeight = Integer.parseInt(mEtWeight.getText().toString());
                if (mHeightFeet < 300 || mHeightFeet > 90) {
                    Toast.makeText(getContext(), "Warning: Nutrition calculations for this weight range may not be accurate", Toast.LENGTH_LONG).show();
                }

                if (mImgPath == null) {
                    Toast.makeText(getContext(), "Save aborted: Please take image to continue", Toast.LENGTH_LONG).show();
                    break;
                }

                //save data
                UserData user = mViewModel.getUserData().getValue();
                if (user == null) {
                    user = new UserData();
                }
                user.setName(mUserName);
                user.setAge(mAge);
                user.setHeightFt(mHeightFeet);
                user.setHeightIn(mHeightInches);
                user.setWeight(mWeight);
                user.setImgPath(mImgPath);
                user.setIsFemale(isFemale);

                //pass it to view model
                setUserInfo(user);

                Toast.makeText(getContext(), "Save Successful!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void setUserInfo(UserData user) {
        mViewModel.setUserData(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap thumbNailImg = (Bitmap) extras.get("data");

            // create ImageView & populate image view
            mIvProfilePic.setImageBitmap(thumbNailImg);
            if (isExternalStorageWritable()) {

                mImgPath = saveImage(thumbNailImg);
            } else {
                Toast.makeText(getContext(), "External storage not writeable.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myLifeStyleDir = new File(root + "/saved_images");
        myLifeStyleDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Thumbnail_" + timeStamp + ".jpg";

        File file = new File(myLifeStyleDir, fname);
        if (file.exists())
            file.delete();
        try {

            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getContext(), "Picture saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_NAME", mUserName);
        outState.putInt("USER_AGE", mAge);
        outState.putInt("USER_HEIGHT_FEET", mHeightFeet);
        outState.putInt("USER_HEIGHT_INCHES", mHeightInches);
        outState.putInt("USER_WEIGHT", mWeight);
        outState.putBoolean("USER_SEX", isFemale);
        outState.putString("USER_IMAGE", mImgPath);
    }

}
