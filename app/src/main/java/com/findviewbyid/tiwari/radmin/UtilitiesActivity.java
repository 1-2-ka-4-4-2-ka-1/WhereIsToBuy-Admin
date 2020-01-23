package com.findviewbyid.tiwari.radmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class UtilitiesActivity extends AppCompatActivity {

    private ImageView mRegisteredUsers;
    private  ImageView mSendNote;
    private ImageView mShareBills;


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


        mSendNote = findViewById(R.id.iv_send_note);
        mSendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(UtilitiesActivity.this,SendNoteActivity.class);
                startActivity(intent);
            }
        });

        mShareBills = findViewById(R.id.iv_share_bill);
        mShareBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fileName = "1579699162716.xls";

                File file = new File(UtilitiesActivity.this.getExternalFilesDir(null), fileName);
                Intent intent = new Intent(Intent.ACTION_SEND);
                Uri uri = GenericFileProvider.getUriForFile(UtilitiesActivity.this,getApplicationContext().getPackageName()+".provider",file);
                intent.setDataAndType(uri,"application/xls");
                intent.putExtra(intent.EXTRA_STREAM,uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(intent);

            }
        });

    }
}
