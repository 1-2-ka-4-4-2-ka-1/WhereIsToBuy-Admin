package com.findviewbyid.tiwari.radmin;

public class PreviewBillsModel {

   private Long mShopId;
   private int mBillId;
   private String mDate;
   private int mCount;
   private String mName;
   private boolean isSelected;
   private String mSlaesmen;
   private String mNote;


    public PreviewBillsModel(Long mShopId, int mBillId, String mDate , int mCount, String mName ,String mSlaesmen , String mNote) {
        this.mShopId = mShopId;
        this.mBillId = mBillId;
        this.mDate = mDate;
        this.mCount = mCount;
        this.mName = mName;
        this.isSelected = false;
        this.mSlaesmen = mSlaesmen;
        this.mNote = mNote;
    }

    public String getmSlaesmen() {
        return mSlaesmen;
    }

    public void setmSlaesmen(String mSlaesmen) {
        this.mSlaesmen = mSlaesmen;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
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
