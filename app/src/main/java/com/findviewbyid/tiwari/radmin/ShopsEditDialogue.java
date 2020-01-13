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
import android.widget.RatingBar;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class ShopsEditDialogue extends AppCompatDialogFragment {

    private EditText mShopName;
    private EditText mAliasName;
    private EditText mAddress;
    private EditText mArea;
    private EditText mLocation;
    private EditText mSublocation;
    private EditText mLandmark;
    private EditText mContactno;
    private EditText mGroup;

    private RatingBar mRating;


    private String ShopName ;
    private String AliasName ;
    private  String Address ;
    private String Area ;
    private  String Location ;
    private String Sublocation ;
    private  String Landmark;
    private  String Contactno ;
    private String Group ;;
    private  int Rating;
    private int pos;



    private ShopsEditDialogue.DialogueListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener= (ShopsEditDialogue.DialogueListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @SuppressLint("ValidFragment")
    public ShopsEditDialogue( String mShopName ,String mAliasName , String mAddress,String mArea ,String mLocation ,String mSublocation ,String mLandmark ,String mContactno ,String mGroup ,int mRating, int pos){

        this.ShopName = mShopName;
        this.AliasName = mAliasName;
        this.Address= mAddress ;
        this.Area = mArea;
        this.Location = mLocation;
        this.Sublocation= mSublocation ;
        this.Landmark = mLandmark;
        this.Contactno = mContactno;
        this.Group = mGroup;
        this.Rating = mRating;
        this.pos = pos;


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity(), R.style.CustomDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.edit_shops_dialogue,null);

        mShopName = view.findViewById(R.id.ed_shop_name);
        mAliasName =  view.findViewById(R.id.ed_alias_name);
        mAddress =  view.findViewById(R.id.ed_address);
        mArea =  view.findViewById(R.id.ed_area);
        mLocation =  view.findViewById(R.id.ed_location);
        mSublocation =  view.findViewById(R.id.ed_sublocation);
        mLandmark =  view.findViewById(R.id.ed_landmark);
        mContactno =  view.findViewById(R.id.ed_contact);
        mGroup =  view.findViewById(R.id.ed_group);

        mRating =  view.findViewById(R.id.sb_rating);



          mShopName.setText(ShopName);
          mAliasName.setText(AliasName);
          mAddress.setText(Address);
          mArea.setText(Area);
          mLocation.setText(Location);
          mSublocation.setText(Sublocation);
          mLandmark.setText(Landmark);
          mContactno.setText(Contactno);
          mGroup.setText(Group);
          mRating.setRating(Rating);


        mRating.setStepSize(1.0f);
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = (int)(rating);
            }
        });



        Button cancel= view.findViewById(R.id.btn_cancel_dialogue_shop);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button ok = view.findViewById(R.id.btn_ok_dialogue_shop);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if(mShopName.getText().toString().trim().equals("") || mAddress.getText().toString().trim().equals("") || mArea.getText().toString().equals("") || mLandmark.getText().toString().equals("") || mContactno.getText().toString().equals("")){

                        Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                    }else {
                        listener.getModifiedShopsData(mShopName.getText().toString(),mAliasName.getText().toString(),mAddress.getText().toString(),mArea.getText().toString(),mLocation.getText().toString(),mSublocation.getText().toString(),mLandmark.getText().toString(),mContactno.getText().toString(),mGroup.getText().toString(),Rating,pos);
                        builder.dismiss();
                    }


            }
        });
        builder.setContentView(view);


        return builder;
    }



    public interface DialogueListener{
        
        void getModifiedShopsData( String ShopName ,String  AliasName ,String  Address ,String  Area ,String  Location ,String  Sublocation ,String  Landmark ,String  Contactno ,String Group ,int  Rating ,int pos);
    }

}
