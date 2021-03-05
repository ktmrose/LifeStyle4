package com.example.lifestyle4;

public class UserData {
    private String mName, mImgPath, mLocation;
    private int mAge, mHeightFt, mHeightIn, mWeight;
    private double mWeightMod;
    private boolean mIsActive, mIsFemale;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImgPath() {
        return mImgPath;
    }

    public void setImgPath(String imgPath) {
        this.mImgPath = imgPath;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        this.mAge = age;
    }

    public int getHeightFt() {
        return mHeightFt;
    }

    public void setHeightFt(int heightFt) {
        this.mHeightFt = heightFt;
    }

    public int getHeightIn() {
        return mHeightIn;
    }

    public void setHeightIn(int heightIn) {
        this.mHeightIn = heightIn;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        this.mWeight = weight;
    }

    public double getWeightMod() {
        return mWeightMod;
    }

    public void setWeightMod(int weightMod) {
        this.mWeightMod = weightMod;
    }

    public boolean userIsActive() { return mIsActive; }

    public void setIsActive(boolean isActive) {
        this.mIsActive = isActive;
    }

    public boolean userIsFemale() {
        return mIsFemale;
    }

    public void setIsFemale(boolean isFemale) {
        this.mIsFemale = isFemale;
    }


}
