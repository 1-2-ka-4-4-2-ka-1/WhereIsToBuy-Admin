package com.findviewbyid.tiwari.radmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoardActivity extends AppCompatActivity {

    public static Activity activity_dashBoard;


    private ImageView mShopsActivityButton;
    private ImageView mItemsActivityButton;
    private ImageView mBillsActivityButton;
    private ImageView mNotification;


    private FirebaseDatabase databaseFirebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        activity_dashBoard = this;

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
    }


}
