package com.findviewbyid.tiwari.radmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class UtilitiesActivity extends AppCompatActivity {

    private ImageView mRegisteredUsers;
    private  ImageView mSendNote;
    private ImageView mShareBills;
    private ImageView mClearBills;


    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;


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


        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();

        mClearBills = findViewById(R.id.iv_clear_bills);
        mClearBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UtilitiesActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Clear Bills")
                        .setMessage("Bills will stay on server but get cleared from device !")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.clearBills(database);
                                Toast.makeText(getApplicationContext(),"Cleared",Toast.LENGTH_LONG).show();

                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }
}
