package com.taxi.passenger.ChangePasswordMvp.Model;

/**
 * Created by Abhilasha Yadav on 10/30/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequestModel {

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("uid")
    @Expose
    private String uid;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}