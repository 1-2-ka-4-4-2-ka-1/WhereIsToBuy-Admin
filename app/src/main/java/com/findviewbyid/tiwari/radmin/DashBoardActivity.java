package com.findviewbyid.tiwari.radmin;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity {

    public static Activity activity_dashBoard;


    private ImageView mShopsActivityButton;
    private ImageView mItemsActivityButton;
    private ImageView mBillsActivityButton;
    private ImageView mUtilitiesActivityButton;
    private  ImageView mBillsPreviewButton;
    private ImageView mNotification;





    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;
    private FirebaseDatabase databaseFirebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        activity_dashBoard = this;



        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();
        databaseFirebase = FirebaseDatabase.getInstance();

        mShopsActivityButton = findViewById(R.id.iv_shops_list);
        mShopsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,ShopsActivity.class);
                startActivity(intent);

            }
        });

        mItemsActivityButton = findViewById(R.id.iv_items_list);
        mItemsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,ShopItemsActivity.class);
                startActivity(intent);

            }
        });

        mBillsActivityButton = findViewById(R.id.iv_billss_activity);
        mBillsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mUtilitiesActivityButton = findViewById(R.id.iv_utils_avtivity);
        mUtilitiesActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,UtilitiesActivity.class);
                startActivity(intent);
            }
        });

        mBillsPreviewButton = findViewById(R.id.bills_preview_button);
        mBillsPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, PreviewBillsActivity.class);
                startActivity(intent);
            }
        });


        mNotification = findViewById(R.id.iv_notification_icon);
        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,NotificationsActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataReference = firebaseDatabase.getReference("admin");
        DatabaseReference  notificationRef = dataReference.child("notification").getRef();
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    mNotification.setImageResource(R.drawable.ic_notifications_active);
                }else {
                    mNotification.setImageResource(R.drawable.ic_notifications_inactive);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });





        DashBoardActivity.LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new DashBoardActivity.LoadeBillsDataAsyncTask();
        lodeItemsDataAsyncTask.execute();

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


}
