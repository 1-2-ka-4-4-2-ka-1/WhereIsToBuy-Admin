package com.findviewbyid.tiwari.radmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String  dbname="_bills_db";
    private static final String  _all_bills_tb="_all_bills_tb";
    private static final String  _all_shops_tb="_all_shops_tb";
    private static final String  _mapping_tb="_mapping_tb";


    private static final int version=1;


    public DataBaseHelper(Context context){

        super(context,dbname,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS _all_bills_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _bill_id VARCHAR ,_amount VARCHAR , _desc VARCHAR, _id_label VARCHAR , _qty VARCHAR, _rate VARCHAR, _unit VARCHAR ,_count VARCHAR, _date VARCHAR, _shop_id VARCHAR)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS _all_shops_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _shop_name VARCHAR,_alias VARCHAR ,_address VARCHAR, _area VARCHAR , _location VARCHAR, _sublocation VARCHAR , _landmark VARCHAR , _contact INTEGER , _ratiing VARCHAR ,_group VARCHAR,_shop_id VARCHAR)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS _mapping_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT, _mbill_id VARCHAR, _count VARCHAR, _date VARCHAR, _shopname VARCHAR, _shop_id VARCHAR)";
        db.execSQL(sql);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertBill(String _bill_id  ,String _amount  ,String _desc ,String _id_label  ,String _qty ,String _rate ,String _unit,String _shop_id  ,String _date  ,String _count , SQLiteDatabase database )
    {
        ContentValues bill=new ContentValues();

        bill.put("_bill_id",_bill_id);
        bill.put("_amount",_amount);
        bill.put("_desc",_desc);
        bill.put("_id_label",_id_label);
        bill.put("_qty",_qty);
        bill.put("_rate",_rate);
        bill.put("_unit",_unit);
        bill.put("_count",_count);
        bill.put("_date",parseDateToddMMyyyy(_date));
        bill.put("_shop_id",_shop_id);
        database.insert(_all_bills_tb,null, bill);

    }


    public void insertShop(String  _shop_name ,String _alias  ,String  _address ,String _area  ,String _location ,String _sublocation  ,String _landmark  ,String _contact  ,String _ratiing  ,String _group ,String _shop_id, SQLiteDatabase database )
    {
        ContentValues shop=new ContentValues();

        shop.put("_shop_name",_shop_name);
        shop.put("_alias",_alias);
        shop.put("_address",_address);
        shop.put("_area",_area);
        shop.put("_location",_location);
        shop.put("_sublocation",_sublocation);
        shop.put("_landmark",_landmark);
        shop.put("_contact",String.valueOf(Long.parseLong(_contact)%1000));
        shop.put("_ratiing",_ratiing);
        shop.put("_shop_id",_shop_id);
        shop.put("_group",_group);

        database.insert(_all_shops_tb,null, shop);

    }


    public void insertMapping(String  _mbill_id ,String _count ,String _date ,String _shopname ,String _shop_id   , SQLiteDatabase database )
    {
        ContentValues mapping=new ContentValues();

        mapping.put("_mbill_id",_mbill_id);
        mapping.put("_count",_count);
        mapping.put("_date",parseDateToddMMyyyy(_date));
        mapping.put("_shopname",_shopname);
        mapping.put("_shop_id",_shop_id);

        database.insert(_mapping_tb,null, mapping);

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(time,str);
        return str;
    }

    public void clearBills(SQLiteDatabase database){

        database.execSQL("delete from "+ "_all_bills_tb");

    }


    public void deleteBillsHavingId(int id, SQLiteDatabase database){

        database.execSQL("delete from "+ "_all_bills_tb where _id_label ="+id);
    }

    public void deleteMappingHavingId(int id, SQLiteDatabase database){

        database.execSQL("delete from "+ "_mapping_tb where _mbill_id ="+id);
    }

    public void updateBill(BillItem billItem,BillItem prevBill,int id, SQLiteDatabase database){
        ContentValues bill=new ContentValues();

        bill.put("_amount",billItem.getMitem_amount());
        bill.put("_desc",billItem.getMitem_desc());
        bill.put("_qty",billItem.getMitem_qty());
        bill.put("_rate",billItem.getMitem_rate());


        database.update(_all_bills_tb,bill,"_id_label =(?) AND _desc=(?) AND _qty=(?)  ",new String[]{String.valueOf(id),prevBill.getMitem_desc(),String.valueOf(prevBill.getMitem_qty())});
    }



    public void clearShops(SQLiteDatabase database){

        database.execSQL("delete from "+ "_all_shops_tb");

    }

}
