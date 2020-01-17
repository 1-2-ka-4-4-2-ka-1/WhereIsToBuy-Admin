package com.findviewbyid.tiwari.radmin;

public class BillItem {


    private int bill_id;

    private String mitem_id_label;
    private String mitem_desc;
    private String mitem_unit;
    private double mitem_rate;
    private double mitem_qty;
    private double mitem_amount;
    public Long mshop_Id;
    public String mdate;
    public int mcount;




    public BillItem(String mitem_id_label, String mitem_desc, String mitem_unit, double mitem_rate, double mitem_qty, double mitem_amount , Long mshop_Id, String mdate, int mcount) {

        this.mitem_id_label = mitem_id_label;
        this.mitem_desc = mitem_desc;
        this.mitem_unit = mitem_unit;
        this.mitem_rate = mitem_rate;
        this.mitem_qty = mitem_qty;
        this.mitem_amount = mitem_amount;
        this.mshop_Id = mshop_Id;
        this.mdate = mdate;
        this.mcount = mcount;
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

    public Long getMshop_Id() {
        return mshop_Id;
    }



    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public int getMcount() {
        return mcount;
    }

    public void setMcount(int mcount) {
        this.mcount = mcount;
    }

    public int getBill_id() {
        return bill_id;
    }

    public String getMitem_id_label() {
        return mitem_id_label;
    }

    public void setMitem_id_label(String mitem_id_label) {
        this.mitem_id_label = mitem_id_label;
    }

    public String getMitem_desc() {
        return mitem_desc;
    }

    public String getMitem_unit() {
        return mitem_unit;
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




    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }
}


