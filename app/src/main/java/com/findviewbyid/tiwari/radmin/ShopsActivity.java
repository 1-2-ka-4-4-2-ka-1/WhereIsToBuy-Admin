package com.findviewbyid.tiwari.radmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopsActivity extends AppCompatActivity implements ShopsEditDialogue.DialogueListener{


    private FirebaseDatabase databaseFirebase;

    private RecyclerView mShopsListRecyclerView;
    private ShopsRecyclerViewAdapter mShopsRecyclerViewAdapter;
    public RecyclerView.LayoutManager mShopsListlayoutManager;
    public ArrayList<ShopDetailsModel> mShopsList=null;

    private FloatingActionButton mSyncShops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);

        databaseFirebase = FirebaseDatabase.getInstance();

        mShopsList = new ArrayList<>();

        mShopsListRecyclerView = findViewById(R.id.rv_shopsLise_recyclerView);
        mShopsListlayoutManager = new LinearLayoutManager(this);
        mShopsRecyclerViewAdapter =new ShopsRecyclerViewAdapter(mShopsList);
        mShopsListRecyclerView.setLayoutManager(mShopsListlayoutManager);
        mShopsListRecyclerView.setAdapter(mShopsRecyclerViewAdapter);

        mShopsRecyclerViewAdapter.setOnShopClickedListener(new ShopsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onShopClicked(int position) {


                doEditShop(position);


            }
        });



        mSyncShops= findViewById(R.id.fb_add_shopItem);
        mSyncShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSyncShops();
            }
        });

        doGetShops();

    }


    public void doEditShop(int position){

        final ShopsEditDialogue shopsEditDialogue = new ShopsEditDialogue(mShopsList.get(position).getmShopName(),mShopsList.get(position).getmAliasName(),mShopsList.get(position).getmAddress(),mShopsList.get(position).getmArea(),mShopsList.get(position).getmLocation(),mShopsList.get(position).getmSublocation(),mShopsList.get(position).getmLandmark(),mShopsList.get(position).getmContactno(),mShopsList.get(position).getmGroup(),mShopsList.get(position).getmRating(),position);
        shopsEditDialogue.show(getSupportFragmentManager(),"Edit Shop");

    }


    public void doGetShops(){

        ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);


        if(storage.loadShops()!=null){
            if(storage.loadShops().size()!=0){
                mShopsList.addAll(storage.loadShops());
            }
        }

    }

    public void doSyncShops(){


        mShopsList.clear();
        final ShopsStorageClass storage = new ShopsStorageClass(DashBoardActivity.activity_dashBoard);
        storage.clearCachedShops();


        DatabaseReference mDataRef = databaseFirebase.getReference("data");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    DatabaseReference mShopsRef = snapshot.child("shops").getRef();
                    mShopsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()) {


                                Log.i("----------", snapshot1.getValue().toString());
                                Log.i("----------", snapshot1.child("mContactno").getValue().toString());

                                ShopDetailsModel shopDetailsModel = new ShopDetailsModel(snapshot1.child("mShopName").getValue().toString()
                                        ,snapshot1.child("mAliasName").getValue().toString()
                                        ,snapshot1.child("mAddress").getValue().toString()
                                        ,snapshot1.child("mArea").getValue().toString()
                                        ,snapshot1.child("mLocation").getValue().toString()
                                        ,snapshot1.child("mSublocation").getValue().toString()
                                        ,snapshot1.child("mLandmark").getValue().toString()
                                        ,snapshot1.child("mContactno").getValue().toString()
                                        ,snapshot1.child("mGroup").getValue().toString()
                                        ,Integer.parseInt(snapshot1.child("mRating").getValue().toString()));
                                storage.addNewShop(shopDetailsModel);
                                mShopsList.add(shopDetailsModel);
                                mShopsRecyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void getModifiedShopsData(String ShopName, String AliasName, String Address, String Area, String Location, String Sublocation, String Landmark, String Contactno, String Group, int Rating, int pos) {
        ShopDetailsModel shopModel= new ShopDetailsModel(ShopName,AliasName,Address,Area,Location,Sublocation,Landmark,Contactno,Group,Rating);
        mShopsList.set(pos,shopModel);
        mShopsRecyclerViewAdapter.notifyItemChanged(pos);
        ShopsStorageClass storage = new ShopsStorageClass(getApplicationContext());
        storage.shopDetails(mShopsList);

    }
}
