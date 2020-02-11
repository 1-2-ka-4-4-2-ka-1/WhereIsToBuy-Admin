package com.findviewbyid.tiwari.radmin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView mNotificationsRecyclerView;
    private NotificationRecyclerViewAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<NotificationModel> mNotificationsLisr;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dataReference ;
    private DatabaseReference  notificationRef;

    public  ArrayList<String> keys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mNotificationsLisr= new ArrayList<>();
        keys=new ArrayList<>();


        mNotificationsRecyclerView=findViewById(R.id.rv_notifications_recyclerView);
        mLayoutManager = new LinearLayoutManager(NotificationsActivity.this);
        mAdapter = new NotificationRecyclerViewAdapter(mNotificationsLisr);
        mNotificationsRecyclerView.setAdapter(mAdapter);
        mNotificationsRecyclerView.setLayoutManager(mLayoutManager);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataReference = firebaseDatabase.getReference("admin");
        notificationRef = dataReference.child("notification").getRef();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                notificationRef.child(keys.get(viewHolder.getAdapterPosition())).removeValue();
                mNotificationsLisr.remove((viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(),"removed",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(mNotificationsRecyclerView);


        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d1 : dataSnapshot.getChildren()){
                    mNotificationsLisr.add(new NotificationModel(d1.getValue().toString()));
                    keys.add(d1.getKey());
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void finish() {

        super.finish();
    }
}
