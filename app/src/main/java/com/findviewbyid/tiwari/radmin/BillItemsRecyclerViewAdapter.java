package com.findviewbyid.tiwari.radmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BillItemsRecyclerViewAdapter extends RecyclerView.Adapter<BillItemsRecyclerViewAdapter.mItemRecycllerViewHolder> {


    private ArrayList<BillItem> mItemsList;





    private BillItemsRecyclerViewAdapter.onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClicked(int position);

    }


    public void setOnItemClickedListener(BillItemsRecyclerViewAdapter.onItemClickListener listener){
        mListener = listener;
    }




    public static class  mItemRecycllerViewHolder extends RecyclerView.ViewHolder{

        public TextView mitem_id_label;
        public TextView mitem_desc;
        public TextView  mitem_unit;
        public TextView  mitem_rate;
        public TextView  mitem_qty;
        public TextView  mitem_amount;


        public mItemRecycllerViewHolder(@NonNull View itemView, final BillItemsRecyclerViewAdapter.onItemClickListener listener) {
            super(itemView);
            mitem_id_label= itemView.findViewById(R.id.tv_item_id);
            mitem_desc=itemView.findViewById(R.id.tv_item_desc);
            mitem_unit=itemView.findViewById(R.id.tv_item_unit);
            mitem_rate=itemView.findViewById(R.id.tv_item_rate);
            mitem_qty=itemView.findViewById(R.id.tv_item_qty);
            mitem_amount=itemView.findViewById(R.id.tv_item_amount);

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



    public BillItemsRecyclerViewAdapter(ArrayList<BillItem> billItemsList){

        mItemsList = billItemsList;

    }

    @NonNull
    @Override
    public BillItemsRecyclerViewAdapter.mItemRecycllerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_item_layout,viewGroup,false);
        BillItemsRecyclerViewAdapter.mItemRecycllerViewHolder mViewHolder= new BillItemsRecyclerViewAdapter.mItemRecycllerViewHolder(v,mListener);

        return mViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull BillItemsRecyclerViewAdapter.mItemRecycllerViewHolder mItemRecycllerViewHolder, int i) {

        BillItem current_BillItem = mItemsList.get(i);
        mItemRecycllerViewHolder.mitem_desc.setText(current_BillItem.getMitem_desc());
        mItemRecycllerViewHolder.mitem_id_label.setText(String.valueOf(current_BillItem.getMitem_id_label()));
        mItemRecycllerViewHolder.mitem_amount.setText(Double.toString(current_BillItem.getMitem_amount()));
        mItemRecycllerViewHolder.mitem_rate.setText(Double.toString(current_BillItem.getMitem_rate()));
        mItemRecycllerViewHolder.mitem_qty.setText(Double.toString(current_BillItem.getMitem_qty()));
        mItemRecycllerViewHolder.mitem_unit.setText(current_BillItem.getMitem_unit());

    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}
