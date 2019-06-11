package com.smartcart.arsam.smartcart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smartcart.arsam.smartcart.Models.ReceiptItemsModel;
import com.smartcart.arsam.smartcart.R;
import com.smartcart.arsam.smartcart.SeeMoreActivity;

import java.util.List;

/**
 * Created by arsam on 05/05/2018.
 */

public class ReceiptItemsRecycler extends RecyclerView.Adapter<ReceiptItemsRecycler.ViewHolder>{

    List<ReceiptItemsModel> myModel;
    Context context;
    int position;

    public ReceiptItemsRecycler(Context context, List<ReceiptItemsModel> cartItemsModel) {
        this.context = context;
        this.myModel = cartItemsModel;
    }

    @Override
    public ReceiptItemsRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.model_receipt_items,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent() show items activity
                Intent i = new Intent(context, SeeMoreActivity.class);
                context.startActivity(i);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReceiptItemsRecycler.ViewHolder holder, int position) {

        holder.receiptid.setText(String.valueOf(myModel.get(position).getId()));
        holder.date.setText(myModel.get(position).getDate());

        String totals= String.valueOf((myModel.get(position).getTotal()));      //convert to string for textview
        holder.total.setText(totals);
    }

    @Override
    public int getItemCount() {return myModel.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView receiptid;
        TextView date;
        TextView total;
        Button seemore;

        public ViewHolder(View itemView) {
            super(itemView);

            receiptid = itemView.findViewById(R.id.id_receipt_item);
            date = (TextView) itemView.findViewById(R.id.date_receipt_item);
            total = (TextView) itemView.findViewById(R.id.total_receipt_item);
            seemore = itemView.findViewById(R.id.btn_receipt_item);
        }
    }
}
