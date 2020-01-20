package com.findviewbyid.tiwari.radmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UtilitiesActivity extends AppCompatActivity {

    private ImageView mRegisteredUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);

        mRegisteredUsers = findViewById(R.id.iv_registered_users);
        mRegisteredUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UtilitiesActivity.this,RegisteredUsersActivity.class);
                startActivity(intent);
            }
        });



    }
}
