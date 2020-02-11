package com.findviewbyid.tiwari.radmin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SendNoteActivity extends AppCompatActivity {

    private Button mSendNote;
    private Spinner mSelectUser;
    private EditText mNote;

    private ArrayAdapter<String> mSelectUserAdapter;


    private List<String> mUsersList;
    private ArrayList<UsersModel> mUsers;


    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mUsers = new ArrayList<>();
        mUsersList = new ArrayList<>();
        mNote  = findViewById(R.id.ed_note_send);


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference();


        doGetUsers();

        mSelectUser = findViewById(R.id.spnr_select_user);
        mSelectUserAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mUsersList);
        mSelectUserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectUser.setAdapter(mSelectUserAdapter);
        mSelectUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectUser.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectUser.setSelection(0);
            }
        });


        mNote = findViewById(R.id.ed_note_send);

        mSendNote = findViewById(R.id.btn_send_note);
        mSendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = mNote.getText().toString().trim();
                String to = mSelectUser.getSelectedItem().toString();
                doSendNote(note,to);
            }
        });

    }



    public void doSendNote(String note, String to){

      if(to.equals("All")){

          for(int i=0;i<mUsers.size();i++){
              String username = mUsers.get(i).getusername();
              mDatabaseRef.child("data").child("salesmen_"+username).child("notifications").push().setValue(note);
          }

      }
      else {
          mDatabaseRef.child("data").child("salesmen_"+to).child("notifications").push().setValue(note);
      }

        Toast.makeText(SendNoteActivity.this,"Note Send",Toast.LENGTH_LONG).show();

    }


    public void doGetUsers(){

        UsersStorageClass storage = new UsersStorageClass(SendNoteActivity.this);

        mUsersList.add("All");

        if(storage.loadUsers()!=null){
            if(storage.loadUsers().size()!=0){
                mUsers.addAll(storage.loadUsers());
            }
        }

        for(int i = 0 ;i< mUsers.size();i++){
            mUsersList.add(mUsers.get(i).getusername());
        }

    }

}
