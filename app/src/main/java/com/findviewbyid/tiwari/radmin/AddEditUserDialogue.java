package com.findviewbyid.tiwari.radmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AddEditUserDialogue extends AppCompatDialogFragment {

    private EditText mUserId;
    private EditText mUserPassword;


    private String id ;
    private String password;
    private int pos;


    private AddEditUserDialogue.DialogueListener listener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener= (AddEditUserDialogue.DialogueListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @SuppressLint("ValidFragment")
    public AddEditUserDialogue(String id, String pass, int pos){
        this.id = id;
        this.password = pass;
        this.pos = pos;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity(), R.style.CustomDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.add_edit_user_dialogue,null);

        mUserId = view.findViewById(R.id.ed_new_user_id);
        mUserPassword = view.findViewById(R.id.ed_new_user_password);



        mUserId.setText(id);
        mUserPassword.setText(password);



        Button cancel= view.findViewById(R.id.btn_cancel_dialogue);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button ok = view.findViewById(R.id.btn_ok_dialogue);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getTag().equals("Add User")){
                    if(mUserId.getText().toString().trim().equals("") || mUserPassword.getText().toString().trim().equals("")){

                        Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                    }else {
                        listener.getUserData(mUserId.getText().toString(), mUserPassword.getText().toString());
                        builder.dismiss();
                    }}
                else if(getTag().equals("Edit User")){
                    if(mUserId.getText().toString().trim().equals("") || mUserPassword.getText().toString().trim().equals("") ){

                        Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                    }else {
                        listener.getModifiedUserData(mUserId.getText().toString(), mUserPassword.getText().toString(),pos);
                        builder.dismiss();
                    }

                }

            }
        });
        builder.setContentView(view);


        return builder;
    }



    public interface DialogueListener{
        void getUserData(String id,String pass);
        void getModifiedUserData(String id,String pass,int pos);
    }

}
