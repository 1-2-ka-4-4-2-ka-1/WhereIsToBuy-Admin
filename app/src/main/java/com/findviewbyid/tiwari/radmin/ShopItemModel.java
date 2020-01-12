package com.findviewbyid.tiwari.radmin;

public class ShopItemModel {

    private String mitem_id_label;
    private String mitem_desc;
    private String mitem_unit;
    private double mitem_rate;
    private double mitem_qty;
    private double mitem_amount;
    private Boolean mIsSelected ;


    public ShopItemModel(String mitem_id_label, String mitem_desc, String mitem_unit, double mitem_rate, double mitem_qty, double mitem_amount) {

        this.mitem_id_label = mitem_id_label;
        this.mitem_desc = mitem_desc;
        this.mitem_unit = mitem_unit;
        this.mitem_rate = mitem_rate;
        this.mitem_qty = mitem_qty;
        this.mitem_amount = mitem_amount;
        this.mIsSelected = false;

    }

    public String getMitem_id_label() {
        return mitem_id_label;
    }

    public String getMitem_desc() {
        return mitem_desc;
    }

    public String getMitem_unit() {
        return mitem_unit;
    }

    public Boolean getmIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(Boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public void setMitem_id_label(String mitem_id_label) {
        this.mitem_id_label = mitem_id_label;
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

    public void setMitem_qty(double mitem_qty) {
        this.mitem_qty = mitem_qty;
    }

    public void setMitem_amount(double mitem_amount) {
        this.mitem_amount = mitem_amount;
    }

    public double getMitem_rate() {
        return mitem_rate;
    }

    public double getMitem_qty() {
        return mitem_qty;
    }

    public double getMitem_amount() {
        return mitem_amount;
    }
}
