package com.findviewbyid.tiwari.radmin;


public class ShopDetailsModel {

    private String mShopName;
    private String mAliasName;
    private String mAddress;
    private String mArea;
    private String mLocation;
    private String mSublocation;
    private String mLandmark;
    private String mContactno;
    private String mGroup;

    private int mRating;


    public ShopDetailsModel(String ShopName , String AliasName , String Address , String Area , String Location , String Sublocation , String Landmark , String Contactno ,String Group , int Rating ){

        mShopName = ShopName ;
        mAliasName =  AliasName;
        mAddress =  Address;
        mArea =  Area;
        mLocation = Location ;
        mSublocation =  Sublocation;
        mLandmark = Landmark ;
        mContactno = Contactno ;
        mGroup =  Group;
        mRating = Rating;

    }

    public String getmShopName() {
        return mShopName;
    }

    public String getmAliasName() {
        return mAliasName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmArea() {
        return mArea;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmSublocation() {
        return mSublocation;
    }

    public String getmLandmark() {
        return mLandmark;
    }

    public String getmContactno() {
        return mContactno;
    }

    public String getmGroup() {
        return mGroup;
    }

    public int getmRating() {
        return mRating;
    }

    public void setmShopName(String mShopName) {
        this.mShopName = mShopName;
    }

    public void setmAliasName(String mAliasName) {
        this.mAliasName = mAliasName;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmSublocation(String mSublocation) {
        this.mSublocation = mSublocation;
    }

    public void setmLandmark(String mLandmark) {
        this.mLandmark = mLandmark;
    }

    public void setmContactno(String mContactno) {
        this.mContactno = mContactno;
    }

    public void setmGroup(String mGroup) {
        this.mGroup = mGroup;
    }

    public void setmRating(int mRating) {
        this.mRating = mRating;
    }
}
