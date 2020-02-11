package com.findviewbyid.tiwari.radmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UtilitiesActivity extends AppCompatActivity {

    private ImageView mRegisteredUsers;
    private  ImageView mSendNote;
    private ImageView mClearBills;


    public static DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;

    private FirebaseDatabase databaseFirebase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        databaseFirebase = FirebaseDatabase.getInstance();
        databaseReference = databaseFirebase.getReference().child("data");


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


        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();

        mClearBills = findViewById(R.id.iv_clear_bills);
        mClearBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UtilitiesActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Clear Bills")
                        .setMessage("You can clear from device/server")
                        .setPositiveButton("Device", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataBaseHelper.clearBills(database);
                                Toast.makeText(getApplicationContext(),"Cleared",Toast.LENGTH_LONG).show();

                            }

                        })
                        .setNegativeButton("Server", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference.setValue("");
                            }
                        })
                        .show();
            }
        });

    }
}
