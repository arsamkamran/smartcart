package com.smartcart.arsam.smartcart.HomeFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcart.arsam.smartcart.Adapters.CartItemsRecycler;
import com.smartcart.arsam.smartcart.Adapters.DiscountItemsRecycler;
import com.smartcart.arsam.smartcart.CheckoutActivity;
import com.smartcart.arsam.smartcart.MainActivity;
import com.smartcart.arsam.smartcart.Models.CartItemsModel;
import com.smartcart.arsam.smartcart.Models.DiscountItemsModel;
import com.smartcart.arsam.smartcart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arsam on 17/01/2018.
 */

public class CartFragment extends Fragment {


    String scanCode;
    Button btn_checkout,btn_clearcart;
    RecyclerView recyclerViewCart;
    public static CartItemsRecycler adapterCartItems;
    public static TextView tvtotal;

    public CartFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.cart_fragment_view, container, false);

        InitializationOfModels();

        //Recycler Implementation of Cart Items
        RecyclerView.LayoutManager layoutManagerCart = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart = v.findViewById(R.id.recycler_cart_items);
        recyclerViewCart.setLayoutManager(layoutManagerCart);
        adapterCartItems = new CartItemsRecycler(getContext(), MainActivity.cartItemsModels);
        recyclerViewCart.setAdapter(adapterCartItems);

        tvtotal = v.findViewById(R.id.total_cart_fragment);


        btn_clearcart = v.findViewById(R.id.btn_cart_clearcart);
        btn_clearcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cartItemsModels.clear();
                tvtotal.setText("0");
                MainActivity.total =0;
                CartFragment.adapterCartItems.notifyDataSetChanged();
            }
        });


        btn_checkout = v.findViewById(R.id.btn_cart_checkout);
        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.cartItemsModels.size() > 0)
                {
                    Intent i = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getActivity(), "No items in cart", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }

    public void InitializationOfModels() {

        //CartItemsModel items1 = new CartItemsModel("Protein Bar", 1f,35f,35f,R.drawable.ic_minus,R.drawable.ic_plus);
        //CartItemsModel items2 = new CartItemsModel("Protein Shake", 1f,35f,35f,R.drawable.ic_minus,R.drawable.ic_plus);

        //MainActivity.cartItemsModels.add(items1);
        //MainActivity.cartItemsModels.add(items2);
    }

    //__________________________________________________________________________________________________________________
    //api call for getting product details


}
