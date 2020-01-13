package com.findviewbyid.tiwari.radmin;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase databaseFirebase;


    private ArrayList<BillItem> mAllBillsList;

    private RecyclerView mBillsListRecyclerView;
    private BillItemsRecyclerViewAdapter mBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mBillsListlayoutManager;
    public ArrayList<BillItem> mBillItems;



    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseFirebase = FirebaseDatabase.getInstance();
        mAllBillsList = new ArrayList<>();


        mBillItems = new ArrayList<>();
        mBillsListRecyclerView = findViewById(R.id.rv_bill_tem_recyclerView);
        mBillsListlayoutManager = new LinearLayoutManager(this);
        mBillsRecyclerViewAdapter = new BillItemsRecyclerViewAdapter(mBillItems);
        mBillsListRecyclerView.setLayoutManager(mBillsListlayoutManager);
        mBillsListRecyclerView.setAdapter(mBillsRecyclerViewAdapter);


        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();



//        LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new LoadeBillsDataAsyncTask();
//        lodeItemsDataAsyncTask.execute();


//        LoadMappingData lodeItemsDataAsyncTask = new LoadMappingData();
//        lodeItemsDataAsyncTask.execute();

//        Cursor cursor = database.rawQuery("SELECT * FROM _mapping_tb ", new String[]{});
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                     Log.i("song Name",cursor.getString(0)+"--- "+cursor.getString(1)+"---- "+cursor.getString(2)+" ---"+cursor.getString(3)+" ---"+cursor.getString(4));
//
//
//
//
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
//
//        }





    }


//    public void doSyncShops(){
//        mAllBillsList.clear();
////        final ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);
////        storage.clearCachedShops();
//        DatabaseReference mDataRef = databaseFirebase.getReference("data");
//        mDataRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    DatabaseReference mShopsRef = snapshot.child("bills").getRef();
//                    mShopsRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            int i=0;
//                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
//
//
//                                Log.i("----------", snapshot1.getValue().toString());
//                                //Log.i("----------", snapshot1.child("mitem_desc").getValue().toString());
//
//                                BillItem billItem = new BillItem(snapshot1.child("bill_id").getValue().toString()
//
//                                        ,snapshot1.child("mitem_desc").getValue().toString()
//                                        ,snapshot1.child("mitem_unit").getValue().toString()
//                                        ,Double.parseDouble(snapshot1.child("mitem_qty").getValue().toString())
//                                        ,Double.parseDouble(snapshot1.child("mitem_rate").getValue().toString())
//                                        ,Double.parseDouble(snapshot1.child("mitem_amount").getValue().toString())
//                                      );
//                              //  storage.addNewShop(billItem);
//                                mBillItems.add(billItem);
//                                mBillsRecyclerViewAdapter.notifyItemInserted(i);
//                                i++;
//
//                               // mShopsRecyclerViewAdapter.notifyDataSetChanged();
//                            }
//
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    mBillsRecyclerViewAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }


    public class LoadeBillsDataAsyncTask extends AsyncTask<ArrayList<BillItem>, Void, ArrayList<BillItem>> {
        @Override
        protected void onPostExecute(ArrayList<BillItem> billItems) {
            super.onPostExecute(billItems);
            if (billItems != null) {
                Toast.makeText(MainActivity.this, "Loading....", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "some thing went wrong", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected ArrayList<BillItem> doInBackground(ArrayList<BillItem>... arrayLists) {

            mAllBillsList.clear();
            DatabaseReference mDataRef = databaseFirebase.getReference("data");
            mDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        DatabaseReference mShopsRef = snapshot.child("bills").getRef();
                        mShopsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    BillItem billItem = new BillItem(snapshot1.child("bill_id").getValue().toString()

                                            , snapshot1.child("mitem_desc").getValue().toString()
                                            , snapshot1.child("mitem_unit").getValue().toString()
                                            , Double.parseDouble(snapshot1.child("mitem_qty").getValue().toString())
                                            , Double.parseDouble(snapshot1.child("mitem_rate").getValue().toString())
                                            , Double.parseDouble(snapshot1.child("mitem_amount").getValue().toString())
                                    );
                                    mBillItems.add(billItem);
                                    mBillsRecyclerViewAdapter.notifyItemInserted(i);
                                    i++;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mBillsRecyclerViewAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return mBillItems;
        }
    }


    public class LoadMappingData extends AsyncTask<ArrayList<BillItem>, Void ,Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(ArrayList... arrayLists) {


        DatabaseReference mDataRef = databaseFirebase.getReference("data");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    DatabaseReference mShopsRef = snapshot.child("mapping").getRef();
                    mShopsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {

                               Log.i("----------",snapshot1.getValue().toString());

                                dataBaseHelper.insertMapping(snapshot1.child("mBillId").getValue().toString(),
                                        snapshot1.child("mCount").getValue().toString(),
                                        snapshot1.child("mDate").getValue().toString(),
                                        snapshot1.child("mName").getValue().toString(),
                                        snapshot1.child("mShopId").getValue().toString(),
                                        database
                                        );
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
