package com.findviewbyid.tiwari.radmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopItemsRecyclerViewAdapter extends RecyclerView.Adapter<ShopItemsRecyclerViewAdapter.mItemRecycllerViewHolder> {


    private ArrayList<ShopItemModel> mItemsList;





    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClicked(int position);

    }


    public void setOnItemClickedListener(onItemClickListener listener){
        mListener = listener;
    }




    public static class  mItemRecycllerViewHolder extends RecyclerView.ViewHolder{

        private TextView mItemName;
        private TextView mItemUnit;
        private TextView mItemRate;
      

        public mItemRecycllerViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mItemName= itemView.findViewById(R.id.tv_item_desc);
            mItemUnit= itemView.findViewById(R.id.tv_item_unit);
            mItemRate= itemView.findViewById(R.id.tv_item_rate);
        
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemClicked(position);
                        }
                    }
                }
            });



        }
    }



    public ShopItemsRecyclerViewAdapter(ArrayList<ShopItemModel>  shops_list){

        mItemsList = shops_list;

    }

    @NonNull
    @Override
    public mItemRecycllerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


       View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item_layout,viewGroup,false);
       mItemRecycllerViewHolder mViewHolder= new mItemRecycllerViewHolder(v,mListener);

       return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull mItemRecycllerViewHolder mItemRecycllerViewHolder, int i) {

        ShopItemModel current_shop= mItemsList.get(i);

        mItemRecycllerViewHolder.mItemName.setText(current_shop.getMitem_desc());
        mItemRecycllerViewHolder.mItemUnit.setText(current_shop.getMitem_unit());
        mItemRecycllerViewHolder.mItemRate.setText(String.valueOf(current_shop.getMitem_rate()));
        
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}
