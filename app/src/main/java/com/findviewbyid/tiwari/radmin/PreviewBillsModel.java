package com.findviewbyid.tiwari.radmin;

public class PreviewBillsModel {

   private Long mShopId;
   private int mBillId;
   private String mDate;
   private int mCount;
   private String mName;
   private boolean isSelected;



    public PreviewBillsModel(Long mShopId, int mBillId, String mDate , int mCount, String mName ) {
        this.mShopId = mShopId;
        this.mBillId = mBillId;
        this.mDate = mDate;
        this.mCount = mCount;
        this.mName = mName;
        this.isSelected = false;
    }


    public Long getmShopId() {
        return mShopId;
    }

    public int getmBillId() {
        return mBillId;
    }


    public String getmDate() {
        return mDate;
    }


    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmCount() {
        return mCount;
    }

    public String getmName() {
        return mName;
    }

    public void setmShopId(Long mShopId) {
        this.mShopId = mShopId;
    }

    public void setmBillId(int mBillId) {
        this.mBillId = mBillId;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
