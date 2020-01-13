package com.findviewbyid.tiwari.radmin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        String sql = "CREATE TABLE IF NOT EXISTS _all_bills_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _bill_id VARCHAR ,_amount VARCHAR , _desc VARCHAR, _id_label VARCHAR , _qty VARCHAR, _rate VARCHAR, _unit VARCHAR )";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS _all_shops_tb(_id INTEGER PRIMARY KEY AUTOINCREMENT, _shop_name VARCHAR,_alias VARCHAR ,_address VARCHAR, _area VARCHAR , _location VARCHAR, _sublocation VARCHAR , _landmark VARCHAR , _contact INTEGER , _ratiing VARCHAR ,_group VARCHAR)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS _mapping_tb (_id INTEGER PRIMARY KEY AUTOINCREMENT, _mbill_id VARCHAR, _count VARCHAR, _date VARCHAR, _shopname VARCHAR, _shop_id VARCHAR)";
        db.execSQL(sql);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertBill(String _bill_id  ,String _amount  ,String _desc ,String _id_label  ,String _qty ,String _rate ,String _unit, SQLiteDatabase database )
    {
        ContentValues bill=new ContentValues();

        bill.put("_bill_id",_bill_id);
        bill.put("_amount",_amount);
        bill.put("_desc",_desc);
        bill.put("_id_label",_id_label);
        bill.put("_qty",_qty);
        bill.put("_rate",_rate);
        bill.put("_unit",_unit);

        database.insert(_all_bills_tb,null, bill);

    }


    public void insertShop(String  _shop_name ,String _alias  ,String  _address ,String _area  ,String _location ,String _sublocation  ,String _landmark  ,String _contact  ,String _ratiing  ,String _group , SQLiteDatabase database )
    {
        ContentValues shop=new ContentValues();

        shop.put("_shop_name",_shop_name);
        shop.put("_alias",_alias);
        shop.put("_address",_address);
        shop.put("_area",_area);
        shop.put("_location",_location);
        shop.put("_sublocation",_sublocation);
        shop.put("_landmark",_landmark);
        shop.put("_contact",_contact);
        shop.put("_ratiing",_ratiing);
        shop.put("_group",_group);

        database.insert(_all_shops_tb,null, shop);

    }


    public void insertMapping(String  _mbill_id ,String _count ,String _date ,String _shopname ,String _shop_id   , SQLiteDatabase database )
    {
        ContentValues mapping=new ContentValues();

        mapping.put("_mbill_id",_mbill_id);
        mapping.put("_count",_count);
        mapping.put("_date",_date);
        mapping.put("_shopname",_shopname);
        mapping.put("_shop_id",_shop_id);

        database.insert(_mapping_tb,null, mapping);

    }


}
