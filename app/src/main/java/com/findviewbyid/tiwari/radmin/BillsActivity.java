package com.findviewbyid.tiwari.radmin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class BillsActivity extends AppCompatActivity {



    private RecyclerView mBillsListRecyclerView;
    private BillItemsRecyclerViewAdapter mBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mBillsListlayoutManager;
    public ArrayList<BillItem> mBillItems;

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;


    private FloatingActionButton mSharePdfButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();



        mBillItems = new ArrayList<>();
        mBillsListRecyclerView = findViewById(R.id.rv_bills_recyclerView);
        mBillsListlayoutManager = new LinearLayoutManager(this);
        mBillsRecyclerViewAdapter = new BillItemsRecyclerViewAdapter(mBillItems);
        mBillsListRecyclerView.setLayoutManager(mBillsListlayoutManager);
        mBillsListRecyclerView.setAdapter(mBillsRecyclerViewAdapter);


        String date = getIntent().getStringExtra("date");
        int id = getIntent().getIntExtra("id",-1);
        Log.i("date is ---",id+"");
       String query = "SELECT B._bill_id  , B._desc , B._unit , B._rate , B._qty   ,B._amount , B._shop_id  , B._date ,_count , B._id_label FROM  _all_bills_tb B WHERE Date(B._date) = Date(?) AND B._id_label = (?)";
       Cursor cursor = database.rawQuery(query, new String[]{(date),String.valueOf(id)});
        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    Log.i("------------",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4)+"--- "+cursor.getString(5)+"--- "+cursor.getString(6)+cursor.getString(7)+cursor.getString(8)+"---------------"+cursor.getString(9));
                    mBillItems.add(new BillItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.parseDouble(cursor.getString(3)),Double.parseDouble(cursor.getString(4)),Double.parseDouble(cursor.getString(5)) , cursor.getLong(6) , cursor.getString(7),cursor.getInt(8)));

                } while (cursor.moveToNext());
                mBillsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(BillsActivity.this, "Not Found" ,Toast.LENGTH_LONG).show();
            }

            cursor.close();

        }else {
            Toast.makeText(BillsActivity.this, "Not Found null" ,Toast.LENGTH_LONG).show();
        }




        mSharePdfButton = findViewById(R.id.fb_share_pdf_button);
        mSharePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }




}
