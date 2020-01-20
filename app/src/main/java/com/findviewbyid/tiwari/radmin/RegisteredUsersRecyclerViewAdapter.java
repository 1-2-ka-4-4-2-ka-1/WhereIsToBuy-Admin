package com.findviewbyid.tiwari.radmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisteredUsersRecyclerViewAdapter extends RecyclerView.Adapter<RegisteredUsersRecyclerViewAdapter.usersViewHolder> {

    private ArrayList<UsersModel> mUsersList;



    private RegisteredUsersRecyclerViewAdapter.onUserClickListener mListener;
    public interface onUserClickListener{
        void onUserClicked(int position);

    }
    public void setOnUserClickedListener(RegisteredUsersRecyclerViewAdapter.onUserClickListener listener){
        mListener = listener;
    }
    
    
    

    public RegisteredUsersRecyclerViewAdapter(ArrayList<UsersModel> mUsersList) {
        this.mUsersList = mUsersList;
    }

    
    @NonNull
    @Override
    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.registered_user_layout,viewGroup,false);
        usersViewHolder viewHolder = new usersViewHolder(v,mListener);
        return viewHolder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull usersViewHolder usersViewHolder, int i) {

        usersViewHolder.Username.setText(mUsersList.get(i).getusername());
        usersViewHolder.Password.setText(mUsersList.get(i).getpassword());
        
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }


    public static class usersViewHolder extends RecyclerView.ViewHolder{

        
        public TextView Username;
        public TextView Password;
        
        
        public usersViewHolder(@NonNull View itemView, final RegisteredUsersRecyclerViewAdapter.onUserClickListener listener) {
            super(itemView);

            Username = itemView.findViewById(R.id.tv_user_id);
            Password = itemView.findViewById(R.id.tv_user_password);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onUserClicked(position);
                        }
                    }
                }
            });
            
        }
        
        
        
        
        
    }
    
    
    
    
}
