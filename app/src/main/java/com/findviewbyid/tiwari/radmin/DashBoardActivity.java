package com.findviewbyid.tiwari.radmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

public class DashBoardActivity extends AppCompatActivity {

    public static Activity activity_dashBoard;


    private ImageView mShopsActivityButton;
    private ImageView mItemsActivityButton;

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



    }



}
