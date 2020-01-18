package com.findviewbyid.tiwari.radmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.notificationsViewHolder> {



    private ArrayList<NotificationModel> mNotificationsList;

    public NotificationRecyclerViewAdapter(ArrayList<NotificationModel> mNotificationsList) {
        this.mNotificationsList = mNotificationsList;
    }

    @NonNull
    @Override
    public notificationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_layout,viewGroup,false);
        notificationsViewHolder viewHolder = new notificationsViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull notificationsViewHolder notificationsViewHolder, int i) {

        notificationsViewHolder.mNotification.setText(mNotificationsList.get(i).getmMessage());
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    public static class notificationsViewHolder extends RecyclerView.ViewHolder{


        public TextView mNotification;

        public notificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            mNotification=itemView.findViewById(R.id.tv_notification);

        }
    }



}
