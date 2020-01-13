package com.findviewbyid.tiwari.radmin;

public class ShopItemModel {


    private String mitem_desc;
    private String mitem_unit;
    private double mitem_rate;


    public ShopItemModel( String mitem_desc, String mitem_unit, double mitem_rate) {


        this.mitem_desc = mitem_desc;
        this.mitem_unit = mitem_unit;
        this.mitem_rate = mitem_rate;


    }



    public String getMitem_desc() {
        return mitem_desc;
    }

    public String getMitem_unit() {
        return mitem_unit;
    }

    public void setMitem_desc(String mitem_desc) {
        this.mitem_desc = mitem_desc;
    }

    public void setMitem_unit(String mitem_unit) {
        this.mitem_unit = mitem_unit;
    }

    public void setMitem_rate(double mitem_rate) {
        this.mitem_rate = mitem_rate;
    }
    public double getMitem_rate() {
        return mitem_rate;
    }


}
