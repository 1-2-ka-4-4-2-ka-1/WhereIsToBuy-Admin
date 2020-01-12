package com.findviewbyid.tiwari.radmin;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopsRecyclerViewAdapter extends RecyclerView.Adapter<ShopsRecyclerViewAdapter.mShopRecycllerViewHolder> {


    private ArrayList<ShopDetailsModel> mShopsList;





    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onShopClicked(int position);

    }


    public void setOnShopClickedListener(onItemClickListener listener){
        mListener = listener;
    }




    public static class  mShopRecycllerViewHolder extends RecyclerView.ViewHolder{

        private TextView mShopName;
        private TextView mAliasName;
        private TextView mGroup;
        private TextView mRating;
        private ImageView mColorCode1;
        private ImageView mColorCode2;


        public mShopRecycllerViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mShopName= itemView.findViewById(R.id.tv_shop_name_list);
            mAliasName= itemView.findViewById(R.id.tv_alias_list);
            mGroup= itemView.findViewById(R.id.tv_group_list);
            mRating= itemView.findViewById(R.id.tv_rating_list);

            mColorCode1 = itemView.findViewById(R.id.iv_colorcode_list1);
            mColorCode2 = itemView.findViewById(R.id.iv_colorcode_list2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onShopClicked(position);
                        }
                    }
                }
            });



        }
    }



    public ShopsRecyclerViewAdapter(ArrayList<ShopDetailsModel>  shops_list){

        mShopsList = shops_list;

    }

    @NonNull
    @Override
    public mShopRecycllerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


       View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shops_layout,viewGroup,false);
       mShopRecycllerViewHolder mViewHolder= new mShopRecycllerViewHolder(v,mListener);

       return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull mShopRecycllerViewHolder mShopRecycllerViewHolder, int i) {

        ShopDetailsModel current_shop= mShopsList.get(i);

        mShopRecycllerViewHolder.mShopName.setText(current_shop.getmShopName());
        mShopRecycllerViewHolder.mAliasName.setText(current_shop.getmAliasName());
        mShopRecycllerViewHolder.mGroup.setText((current_shop.getmGroup()));
        mShopRecycllerViewHolder.mRating.setText(Double.toString(current_shop.getmRating()));

        float contact  = Float.parseFloat(current_shop.getmContactno());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mShopRecycllerViewHolder.mColorCode1.setColorFilter(Color.rgb(contact/45,contact/787,contact/8300));
            mShopRecycllerViewHolder.mColorCode2.setColorFilter(Color.rgb(contact/45,contact/787,contact/8300));
        }


    }




    @Override
    public int getItemCount() {
        return mShopsList.size();
    }
}
