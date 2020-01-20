package com.findviewbyid.tiwari.radmin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

public class RegisteredUsersActivity extends AppCompatActivity implements AddEditUserDialogue.DialogueListener{


    private RecyclerView mUsersRecyclerView;
    private RegisteredUsersRecyclerViewAdapter mUsersAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<UsersModel> mUsersList;
    private FloatingActionButton mAddNewUser;

   private FirebaseDatabase database;
   private DatabaseReference useraRef;

   private String prevId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users);


        mUsersList = new ArrayList<>();

        mUsersRecyclerView = findViewById(R.id.rv_registered_users);
        mLayoutManager = new LinearLayoutManager(RegisteredUsersActivity.this);
        mUsersAdapter = new RegisteredUsersRecyclerViewAdapter(mUsersList);
        mUsersRecyclerView.setLayoutManager(mLayoutManager);
        mUsersRecyclerView.setAdapter(mUsersAdapter);


        mUsersAdapter.setOnUserClickedListener(new RegisteredUsersRecyclerViewAdapter.onUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                doEditUser(position);
            }
        });


        mAddNewUser = findViewById(R.id.fb_add_new_user);
        mAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddUser();
            }
        });


        database = FirebaseDatabase.getInstance();
        useraRef = database.getReference("users");


        doGetUsers();

    }

    public void doGetUsers(){
        UsersStorageClass storage = new UsersStorageClass(RegisteredUsersActivity.this);


        if(storage.loadUsers()!=null){
            if(storage.loadUsers().size()!=0){
                mUsersList.addAll(storage.loadUsers());
            }
        }

        mUsersAdapter.notifyDataSetChanged();
    }


    public void doEditUser(int pos){

        prevId = mUsersList.get(pos).getusername();
        final AddEditUserDialogue addEditUserDialogue = new AddEditUserDialogue(mUsersList.get(pos).getusername(),mUsersList.get(pos).getpassword(),pos);
        addEditUserDialogue.show(getSupportFragmentManager(),"Edit User");

        }


    public void doAddUser(){

        final AddEditUserDialogue addEditUserDialogue = new AddEditUserDialogue("","",0);
        addEditUserDialogue.show(getSupportFragmentManager(),"Add User");


    }


    @Override
    public void getUserData(final String id, String pass) {
        final UsersModel UserModel= new UsersModel(id,pass);


        useraRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(id)){
                    Toast.makeText(RegisteredUsersActivity.this,"User Already exists",Toast.LENGTH_LONG).show();
                }else {
                    useraRef.child(id).setValue(UserModel);
                    mUsersList.add(UserModel);
                    mUsersAdapter.notifyItemInserted(mUsersList.size());

                    UsersStorageClass storage = new UsersStorageClass(getApplicationContext());
                    storage.addNewUser(UserModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void getModifiedUserData(final  String id,final String pass,final int pos) {
        final UsersModel UserModel= new UsersModel(id,pass);

        useraRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    Toast.makeText(RegisteredUsersActivity.this,"User Already exists",Toast.LENGTH_LONG).show();
                }else {
                    mUsersList.set(pos,UserModel);
                    mUsersAdapter.notifyItemChanged(pos);

                    UsersStorageClass storage = new UsersStorageClass(getApplicationContext());
                    storage.storeUsers(mUsersList);

                    useraRef.child(prevId).removeValue();
                    useraRef.child(id).setValue(UserModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
