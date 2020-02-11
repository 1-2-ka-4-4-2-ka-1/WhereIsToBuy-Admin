package com.findviewbyid.tiwari.radmin;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopItemsActivity extends AppCompatActivity implements AddEditDialogue.DialogueListener {


    private FirebaseDatabase databaseFirebase;
    private DatabaseReference dataReference ;
    private DatabaseReference  notificationRef;

    private RecyclerView mItemsListRecyclerView;
    private ShopItemsRecyclerViewAdapter mItemsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mItemsListlayoutManager;
    public ArrayList<ShopItemModel> mItemssList=null;

    private FloatingActionButton mUploadItems;
    private FloatingActionButton mAddItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_items);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        databaseFirebase = FirebaseDatabase.getInstance();

        mItemssList = new ArrayList<>();


        mItemsListRecyclerView = findViewById(R.id.rv_shopsLise_recyclerView1);
        mItemsListlayoutManager = new LinearLayoutManager(this);
        mItemsRecyclerViewAdapter =new ShopItemsRecyclerViewAdapter(mItemssList);
        mItemsListRecyclerView.setLayoutManager(mItemsListlayoutManager);
        mItemsListRecyclerView.setAdapter(mItemsRecyclerViewAdapter);

        mItemsRecyclerViewAdapter.setOnItemClickedListener(new ShopItemsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                doEditItem(position);
            }
        });


        mUploadItems= findViewById(R.id.fb_upload_shopItem);
        mUploadItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirmUpload();
            }
        });

        mAddItem= findViewById(R.id.fb_add_shopItem);
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddItem();
            }
        });





        doLoadItems();


    }


    public void doAddItem(){

        final AddEditDialogue addEditDialogue = new AddEditDialogue("","","",0);
        addEditDialogue.show(getSupportFragmentManager(),"Add Item");

    }

    public void doEditItem(int position){
        final AddEditDialogue addEditDialogue = new AddEditDialogue(mItemssList.get(position).getMitem_desc(),mItemssList.get(position).getMitem_unit(),Double.toString(mItemssList.get(position).getMitem_rate()),position);
        addEditDialogue.show(getSupportFragmentManager(),"Edit Item");
    }







    public void doUploadItems(){

        databaseFirebase = FirebaseDatabase.getInstance();
        DatabaseReference shopItemsRef= databaseFirebase.getReference("Shop-items");



        for (int i=0;i<mItemssList.size();i++){
            shopItemsRef.child(i+"").child("item").setValue(mItemssList.get(i).getMitem_desc());
            shopItemsRef.child(i+"").child("rate").setValue(mItemssList.get(i).getMitem_rate());
            shopItemsRef.child(i+"").child("unit").setValue(mItemssList.get(i).getMitem_unit());
        }

        Toast.makeText(ShopItemsActivity.this,"Done!",Toast.LENGTH_LONG);

    }


    public void doConfirmUpload(){

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_store_blue)
                .setTitle("Confirm Send")
                .setMessage("Confirm send and notify?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doUploadItems();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void doLoadItems() {


        ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
        ArrayList<ShopItemModel> shop_items = storage.loadItems();

        if (shop_items != null) {
            if (shop_items.size() != 0) {
                mItemssList.addAll(shop_items);

            }
        } else {
            Toast.makeText(ShopItemsActivity.this, "Loading Items ...", Toast.LENGTH_SHORT).show();
            LodeItemsDataAsyncTask lodeItemsDataAsyncTask = new LodeItemsDataAsyncTask();
            lodeItemsDataAsyncTask.execute();

        }
        mItemsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void getData(String desc, String unit, String rate) {


        ShopItemModel shopItemModel= new ShopItemModel(desc,unit,Double.parseDouble(rate));
        mItemssList.add(shopItemModel);
        mItemsRecyclerViewAdapter.notifyItemInserted(mItemssList.size());

        ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
        storage.addNewShop(shopItemModel);

        doSendNotification(desc);

    }
    @Override
    public void getModifiedData(String desc, String unit, String rate, int pos) {

        ShopItemModel shopItemModel= new ShopItemModel(desc,unit,Double.parseDouble(rate));
        mItemssList.set(pos,shopItemModel);
        mItemsRecyclerViewAdapter.notifyItemChanged(pos);

        ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
        storage.storeItems(mItemssList);

    }


    public void doSendNotification(final String desc){

        dataReference = databaseFirebase.getReference("data");
        notificationRef = dataReference.child("salesmen_1").child("notifications").getRef();

        notificationRef.push().setValue("New item added"+desc);


    }

    public class  LodeItemsDataAsyncTask extends AsyncTask<Void , Void , Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            doLoadShopItemsData();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            Toast.makeText(ShopItemsActivity.this,"Done",Toast.LENGTH_LONG);
            mItemsRecyclerViewAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }


    public void doLoadShopItemsData(){

        databaseFirebase = FirebaseDatabase.getInstance();
       DatabaseReference shopItemsRef= databaseFirebase.getReference("Shop-items");
        shopItemsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int itemCount = (int)dataSnapshot.getChildrenCount();
                itemCount--;

                String itemDesc="";
                String itemRate="";
                String itemUnit="";


                while (itemCount-- != 0)
                {
                    itemDesc=  dataSnapshot.child(Integer.toString(itemCount)).child("item").getValue().toString();
                    itemRate= dataSnapshot.child(Integer.toString(itemCount)).child("rate").getValue().toString();
                    itemUnit=  dataSnapshot.child(Integer.toString(itemCount)).child("unit").getValue().toString();

                    mItemssList.add(new ShopItemModel(itemDesc,itemUnit,Double.parseDouble(itemRate)));
                }

                ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
                storage.storeItems(mItemssList);

                mItemsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShopItemsActivity.this,"Failed",Toast.LENGTH_LONG);
            }

        });



    }


}
