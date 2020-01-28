package com.findviewbyid.tiwari.radmin;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PreviewBillsRecyclerViewAdapter extends RecyclerView.Adapter<PreviewBillsRecyclerViewAdapter.ItemContainerRecyclerViewHolder> {


    private ArrayList<PreviewBillsModel> mMappedBillsList;

    private onItemClickListener mListener;

    public interface onItemClickListener{
        void mItemSelectedListener(int position);

    }


    public void setOnItemClickedListener(onItemClickListener listener){
        mListener = listener;
    }


    public static class  ItemContainerRecyclerViewHolder extends RecyclerView.ViewHolder{


        public TextView mshop_id;
        public TextView mitem_name;
        public TextView  mitem_date;
        public TextView  mitem_count;



        public ItemContainerRecyclerViewHolder(@NonNull View itemView, final onItemClickListener listener) {
            super(itemView);
            mshop_id= itemView.findViewById(R.id.tv_check_bill_id);
            mitem_name = itemView.findViewById(R.id.tv_check_name);
            mitem_date = itemView.findViewById(R.id.tv_check_date);
            mitem_count = itemView.findViewById(R.id.tv_check_bills_count);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.mItemSelectedListener(position);
                        }
                    }
                }
            });


        }
    }



    public PreviewBillsRecyclerViewAdapter(ArrayList<PreviewBillsModel>  bill_item_container_List){

        mMappedBillsList = bill_item_container_List;

    }

    @NonNull
    @Override
    public ItemContainerRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


       View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_preview_item_layout,viewGroup,false);
       ItemContainerRecyclerViewHolder mViewHolder= new ItemContainerRecyclerViewHolder(v,mListener);

       return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemContainerRecyclerViewHolder itemContainerRecyclerViewHolder, int i) {

        PreviewBillsModel current_bill_item= mMappedBillsList.get(i);

        itemContainerRecyclerViewHolder.mshop_id.setText(Long.toString(current_bill_item.getmShopId()%10000));
        itemContainerRecyclerViewHolder.mitem_name.setText(current_bill_item.getmName());
        itemContainerRecyclerViewHolder.mitem_date.setText(current_bill_item.getmDate());
        itemContainerRecyclerViewHolder.mitem_count.setText(Integer.toString(current_bill_item.getmCount()));


        final PreviewBillsModel current_item  = mMappedBillsList.get(i);

        if(!current_item.isSelected())
        {
            itemContainerRecyclerViewHolder.itemView.setBackgroundColor(Color.WHITE);
            //current_item.setmIsSelected(false);
        }
        else {
            itemContainerRecyclerViewHolder.itemView.setBackgroundColor(Color.CYAN);
            //current_item.setmIsSelected(true);
        }


    }

    @Override
    public int getItemCount() {
        return mMappedBillsList.size();
    }
}
