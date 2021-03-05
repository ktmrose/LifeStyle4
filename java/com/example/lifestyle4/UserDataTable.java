package com.example.lifestyle4;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserDataTable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Name")
    private String username;

    @NonNull
    @ColumnInfo(name = "ImagePath")
    private String imgPath;

    @ColumnInfo(name = "Location")
    private String location;

    @ColumnInfo(name = "Age")
    private int age;

    @ColumnInfo(name = "HeightFeet")
    private int heightFt;

    @ColumnInfo(name = "HeightInches")
    private int heightIn;

    @ColumnInfo(name = "Weight")
    private int weight;

    @ColumnInfo(name = "WeightMod")
    private double weightMod;

    private boolean isActive;
    private boolean isFemale;

    public UserDataTable(@NonNull UserData user) {
        this.username = user.getName();
        this.imgPath = user.getImgPath();
        this.location = user.getLocation();
        this.age = user.getAge();
        this.heightFt = user.getHeightFt();
        this.heightIn = user.getHeightIn();
        this.weight = user.getWeight();
        this.weightMod = user.getWeightMod();
        this.isActive = user.userIsActive();
        this.isFemale = user.userIsFemale();
    }

    public UserDataTable() { }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(@NonNull String imgPath) {
        this.imgPath = imgPath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeightFt() {
        return heightFt;
    }

    public void setHeightFt(int heightFt) {
        this.heightFt = heightFt;
    }

    public int getHeightIn() {
        return heightIn;
    }

    public void setHeightIn(int heightIn) {
        this.heightIn = heightIn;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public double getWeightMod() {
        return weightMod;
    }

    public void setWeightMod(double weightMod) {
        this.weightMod = weightMod;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

}
