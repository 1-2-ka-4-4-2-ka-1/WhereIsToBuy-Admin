package com.findviewbyid.tiwari.radmin;

public class UserModel {

    private String mUserId;
    private String mPassword;


    public UserModel(String userId, String  password){

        this.mUserId = userId;
        this.mPassword = password;

    }

    public UserModel(){

    }

    public String getmUserId() {
        return mUserId;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

}
