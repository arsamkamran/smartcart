package com.smartcart.arsam.smartcart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcart.arsam.smartcart.Models.DiscountItemsModel;
import com.smartcart.arsam.smartcart.R;

import java.util.List;

/**
 * Created by Arsam on 17/01/2018.
 */

public class DiscountItemsRecycler extends RecyclerView.Adapter<DiscountItemsRecycler.ViewHolder>{

    List<DiscountItemsModel> myModel;
    Context context;
    int position;

    public DiscountItemsRecycler(Context context, List<DiscountItemsModel> discountModels) {
        this.context = context;
        this.myModel = discountModels;
    }

    @Override
    public DiscountItemsRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_top_selling_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

//        viewHolder.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "show item", Toast.LENGTH_SHORT).show();
//            }
//        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DiscountItemsRecycler.ViewHolder holder, int position) {

        //holder.img.setImageResource(myModel.get(position).getImg());
        holder.name.setText(myModel.get(position).getName());
        holder.price.setText(myModel.get(position).getPrice());
    }

    @Override
    public int getItemCount() {return myModel.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

       // ImageView img;
        TextView name;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            //img = (ImageView) itemView.findViewById(R.id.img_discount_item);
            name = (TextView) itemView.findViewById(R.id.tv_name_top_selling);
            price = (TextView) itemView.findViewById(R.id.tv_price_top_selling);
        }
    }
}
