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
public class AddEditDialogue extends AppCompatDialogFragment {


    private EditText mItemDesc;
    private EditText mItemRate;
    private EditText mItemUnit;


    private String desc ;
    private String unit;
    private String rate;
    private int pos;

    private DialogueListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener= (DialogueListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



   @SuppressLint("ValidFragment")
   public AddEditDialogue(String desc, String unit, String rate , int pos){
        this.desc = desc;
        this.unit = unit;
        this.rate = rate;
        this.pos = pos;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity(), R.style.CustomDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.add_edit_item_dialogue_layout,null);

        mItemDesc = view.findViewById(R.id.ed_new_item_desc);
        mItemRate = view.findViewById(R.id.ed_new_item_rate);
        mItemUnit = view.findViewById(R.id.ed_new_item_unit);


        mItemDesc.setText(desc);
        mItemUnit.setText(unit);
        mItemRate.setText(rate);


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

                if(getTag().equals("Add Item")){
                if(mItemDesc.getText().toString().trim().equals("") || mItemUnit.getText().toString().trim().equals("") || mItemRate.getText().toString().equals("")){

                    Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                }else {
                    listener.getData(mItemDesc.getText().toString(), mItemUnit.getText().toString(), mItemRate.getText().toString());
                    builder.dismiss();
                }}
                else if(getTag().equals("Edit Item")){
                    if(mItemDesc.getText().toString().trim().equals("") || mItemUnit.getText().toString().trim().equals("") || mItemRate.getText().toString().equals("")){

                        Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                    }else {
                        listener.getModifiedData(mItemDesc.getText().toString(), mItemUnit.getText().toString(), mItemRate.getText().toString(),pos);
                        builder.dismiss();
                    }

                }

            }
        });
        builder.setContentView(view);


        return builder;
    }



    public interface DialogueListener{
        void getData(String desc,String unit, String rate);
        void getModifiedData(String desc,String unit, String rate ,int pos);
    }


}
