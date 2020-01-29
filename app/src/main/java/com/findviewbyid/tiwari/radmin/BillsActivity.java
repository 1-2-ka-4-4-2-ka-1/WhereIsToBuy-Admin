package com.findviewbyid.tiwari.radmin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BillsActivity extends AppCompatActivity {



    private RecyclerView mBillsListRecyclerView;
    private BillItemsRecyclerViewAdapter mBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mBillsListlayoutManager;
    public ArrayList<BillItem> mBillItems;

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;
    private FirebaseDatabase databaseFirebase;

    private FloatingActionButton mSharePdfButton;


    private String date;
    private int id;


    public static final int EDIT_NOTE_REQUEST = 2;
    private int pos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();
        databaseFirebase = FirebaseDatabase.getInstance();








        mSharePdfButton = findViewById(R.id.fb_share_pdf_button);
        mSharePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mBillItems = new ArrayList<>();
        mBillsListRecyclerView = findViewById(R.id.rv_bills_recyclerView);
        mBillsListlayoutManager = new LinearLayoutManager(this);
        mBillsRecyclerViewAdapter = new BillItemsRecyclerViewAdapter(mBillItems);
        mBillsListRecyclerView.setLayoutManager(mBillsListlayoutManager);
        mBillsListRecyclerView.setAdapter(mBillsRecyclerViewAdapter);



        date = getIntent().getStringExtra("date");
        id = getIntent().getIntExtra("id",-1);



//        BillsActivity.LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new BillsActivity.LoadeBillsDataAsyncTask();
//        lodeItemsDataAsyncTask.execute();



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


        mBillsRecyclerViewAdapter.setOnItemClickedListener(new BillItemsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent intent = new Intent(BillsActivity.this, EditBillActivity.class);

                pos = position;
                intent.putExtra(EditBillActivity.EXTRA_ID_LABLE,mBillItems.get(position).getMitem_id_label());
                intent.putExtra(EditBillActivity.EXTRA_ID,id);
                intent.putExtra(EditBillActivity.EXTRA_DESC,mBillItems.get(position).getMitem_desc());
                intent.putExtra(EditBillActivity.EXTRA_UNIT,mBillItems.get(position).getMitem_unit());
                intent.putExtra(EditBillActivity.EXTRA_RATE,mBillItems.get(position).getMitem_rate());
                intent.putExtra(EditBillActivity.EXTRA_QTY,mBillItems.get(position).getMitem_qty());
                intent.putExtra(EditBillActivity.EXTRA_AMOUNT,mBillItems.get(position).getMitem_amount());

                startActivityForResult(intent,EDIT_NOTE_REQUEST);

            }
        });


    }










    public class LoadeBillsDataAsyncTask extends AsyncTask<ArrayList<BillItem>, Void, ArrayList<BillItem>> {
        @Override
        protected void onPostExecute(ArrayList<BillItem> billItems) {
            super.onPostExecute(billItems);

        }

        @Override
        protected ArrayList<BillItem> doInBackground(ArrayList<BillItem>... arrayLists) {


            DatabaseReference mDataRef = databaseFirebase.getReference("data");
            mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot salesmen : dataSnapshot.getChildren()){

                        DatabaseReference mBillsref = salesmen.child("bills").getRef();
                        mBillsref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot bills : dataSnapshot.getChildren()){

                                    DatabaseReference mBill = bills.getRef();
                                    mBill.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for(DataSnapshot bill : dataSnapshot.getChildren()) {

                                                if (bill.child("bill_id").getValue().toString().equals("--")) {


                                                } else {
                                                    Log.i("changed", bill.getValue().toString());
                                                    BillItem billItem = new BillItem(bill.child("mitem_id_label").getValue().toString()
                                                            , bill.child("mitem_desc").getValue().toString()
                                                            , bill.child("mitem_unit").getValue().toString()
                                                            , Double.parseDouble(bill.child("mitem_rate").getValue().toString())
                                                            , Double.parseDouble(bill.child("mitem_qty").getValue().toString())
                                                            , Double.parseDouble(bill.child("mitem_amount").getValue().toString())
                                                            , Long.parseLong(bill.child("mShopId").getValue().toString())
                                                            , bill.child("mDate").getValue().toString()
                                                            , Integer.parseInt(bill.child("mCount").getValue().toString())
                                                    );

                                                   if(bill.child("mitem_id_label").getValue().toString().equals(String.valueOf(id))){

                                                       mBillItems.add(billItem);
                                                       mBillsRecyclerViewAdapter.notifyItemInserted(mBillItems.size());
                                                   }

                                                    dataBaseHelper.insertBill(String.valueOf(billItem.getBill_id()), String.valueOf(billItem.getMitem_amount()),
                                                            billItem.getMitem_desc(), billItem.getMitem_id_label(), String.valueOf(billItem.getMitem_qty()),
                                                            String.valueOf(billItem.getMitem_rate()), billItem.getMitem_unit(), billItem.mshop_Id.toString(), billItem.getMdate(), String.valueOf(billItem.getMcount()), database);


                                                    bill.child("bill_id").getRef().setValue("--");

                                                }
                                            }

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return null;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){


            int id = data.getIntExtra(EditBillActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(getApplicationContext(),"Can't be updated",Toast.LENGTH_LONG).show();
                return;
            }

            String item_id_label = data.getStringExtra(EditBillActivity.EXTRA_ID_LABLE);
            String item_desc = data.getStringExtra(EditBillActivity.EXTRA_DESC);
            String item_unit = data.getStringExtra(EditBillActivity.EXTRA_UNIT);
            double item_rate = data.getDoubleExtra(EditBillActivity.EXTRA_RATE,0);
            double item_qty = data.getDoubleExtra(EditBillActivity.EXTRA_QTY,0);
            double item_amount = data.getDoubleExtra(EditBillActivity.EXTRA_AMOUNT,0);


            BillItem billItem = new BillItem(item_id_label,item_desc,item_unit,item_rate,item_qty,item_amount,mBillItems.get(pos).getMshop_Id(),mBillItems.get(pos).getMdate(),mBillItems.get(pos).getBill_id());
            BillItem prevBill = mBillItems.get(pos);

            dataBaseHelper.updateBill(billItem,prevBill,id,database);


            mBillItems.remove(pos);
            mBillItems.add(pos,billItem);

            mBillsRecyclerViewAdapter.notifyItemChanged(pos);


            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not Saved",Toast.LENGTH_LONG).show();
        }
    }



}
