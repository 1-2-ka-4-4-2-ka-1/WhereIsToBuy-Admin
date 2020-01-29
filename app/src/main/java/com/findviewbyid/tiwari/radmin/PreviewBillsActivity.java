package com.findviewbyid.tiwari.radmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviewBillsActivity extends AppCompatActivity {

    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;


    private FirebaseDatabase databaseFirebase;

    public RecyclerView recyclerView;
    public PreviewBillsRecyclerViewAdapter previewBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager manager;
    public ArrayList<PreviewBillsModel> mMappedItems = null;


    public  static  int id ;
    public  static  String date ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_preview);

        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();

        databaseFirebase = FirebaseDatabase.getInstance();


        mMappedItems = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_preview_bills_recyclerView);
        manager = new LinearLayoutManager(getApplicationContext());
        previewBillsRecyclerViewAdapter = new PreviewBillsRecyclerViewAdapter(mMappedItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(previewBillsRecyclerViewAdapter);


        String query = "SELECT _shop_id,_mbill_id , _date , _count , _shopname FROM  _mapping_tb  ";
        Cursor cursor = database.rawQuery(query, new String[]{});
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {
                    PreviewBillsModel model = new PreviewBillsModel(Long.parseLong(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4));
                    mMappedItems.add(model);

                } while (cursor.moveToNext());
                previewBillsRecyclerViewAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(PreviewBillsActivity.this, "Not Found", Toast.LENGTH_LONG).show();
            }

            cursor.close();

        } else {
            Toast.makeText(PreviewBillsActivity.this, "Not Found null", Toast.LENGTH_LONG).show();
        }


        DatabaseReference databaseReference = databaseFirebase.getReference("data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot salesmen : dataSnapshot.getChildren()) {
                    final DatabaseReference mapping = salesmen.child("mapping").getRef();
                    mapping.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot group_mapping : dataSnapshot.getChildren()) {


                                DatabaseReference bill_mapping = group_mapping.getRef();
                                bill_mapping.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot params : dataSnapshot.getChildren()) {
                                            if (!params.child("mDate").getValue().toString().trim().equals("--")) {
                                                // Log.i("data",params.getValue().toString());
                                                PreviewBillsModel model = new PreviewBillsModel(Long.parseLong(params.child("mShopId").getValue().toString()), Integer.parseInt(params.child("mBillId").getValue().toString()), params.child("mDate").getValue().toString(), Integer.parseInt(params.child("mCount").getValue().toString()), params.child("mName").getValue().toString());
                                                dataBaseHelper.insertMapping(String.valueOf(model.getmBillId()), String.valueOf(model.getmCount()), model.getmDate(), model.getmName(), String.valueOf(model.getmShopId()), database);
                                                mMappedItems.add(model);
                                                params.child("mDate").getRef().setValue("--");
                                                previewBillsRecyclerViewAdapter.notifyItemInserted(mMappedItems.size());
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


        previewBillsRecyclerViewAdapter.setOnItemClickedListener(new PreviewBillsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void mItemSelectedListener(int position) {

                id = mMappedItems.get(position).getmBillId();
                date= mMappedItems.get(position).getmDate();
                mMappedItems.get(position).setSelected(true);
                recyclerView.getChildAt(position).setBackgroundColor(Color.CYAN);
                Intent intent  =new Intent(PreviewBillsActivity.this,BillsActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("date",date);
                startActivity(intent);

            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {


                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                new AlertDialog.Builder(PreviewBillsActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Remove Bill")
                        .setMessage("Are you sure you want to remove this bill ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.deleteBillsHavingId(mMappedItems.get(viewHolder.getAdapterPosition()).getmBillId(),database);
                                dataBaseHelper.deleteMappingHavingId(mMappedItems.get(viewHolder.getAdapterPosition()).getmBillId(),database);
                                mMappedItems.remove(viewHolder.getAdapterPosition());
                                previewBillsRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_LONG).show();
                                //remove from database


                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                previewBillsRecyclerViewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .show();

            }
        }).attachToRecyclerView(recyclerView);




//        LoadeBillsDataAsyncTask lodeItemsDataAsyncTask = new LoadeBillsDataAsyncTask();
//        lodeItemsDataAsyncTask.execute();


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
