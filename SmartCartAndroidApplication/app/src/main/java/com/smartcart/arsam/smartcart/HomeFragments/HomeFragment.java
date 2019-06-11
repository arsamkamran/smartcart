package com.smartcart.arsam.smartcart.HomeFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartcart.arsam.smartcart.Adapters.DiscountItemsRecycler;
import com.smartcart.arsam.smartcart.Models.DiscountItemsModel;
import com.smartcart.arsam.smartcart.R;
import com.smartcart.arsam.smartcart.Receipts.ReceiptActivity;
import com.smartcart.arsam.smartcart.Server;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

/**
 * Created by Arsam on 15/01/2018.
 */

public class HomeFragment extends Fragment{

    List<DiscountItemsModel> discountItemsModel = new ArrayList<>();
    List<DiscountItemsModel> topSellingModel = new ArrayList<>();
    ProgressDialog pDialog;
    DiscountItemsRecycler adapterTopSelling;
    RecyclerView recyclerViewTopSelling;

    public static HomeFragment newInstance(String imageUrl) {

        final HomeFragment mf = new HomeFragment ();

//        final Bundle args = new Bundle();
//        args.putString("somedata", "somedata");
//        mf.setArguments(args);

        return mf;
    }

    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String data = getArguments().getString("somedata");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate and locate the main ImageView
        final View v = inflater.inflate(R.layout.home_fragment_view, container, false);

        pDialog = new ProgressDialog(getContext());
        BannerSlider bannerSlider = (BannerSlider) v.findViewById(R.id.banner_slider1);
        List<Banner> banners=new ArrayList<>();
        //add banner using image url
        //banners.add(new RemoteBanner("Put banner image url here ..."));
        //add banner using resource drawable
        banners.add(new DrawableBanner(R.drawable.banner1));
        banners.add(new DrawableBanner(R.drawable.banner2));
        banners.add(new DrawableBanner(R.drawable.banner3));
        bannerSlider.setBanners(banners);

        getReceipts();
        InitializationOfModels();

//        //Recycler Implementation of Discount Items
//        RecyclerView.LayoutManager layoutManagerDiscounts = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView recyclerViewDiscounts = (RecyclerView) v.findViewById(R.id.recycler_discount_items);
//        recyclerViewDiscounts.setLayoutManager(layoutManagerDiscounts);
//        DiscountItemsRecycler adapterDiscountItems = new DiscountItemsRecycler(getContext(), discountItemsModel);
//        recyclerViewDiscounts.setAdapter(adapterDiscountItems);
//        adapterDiscountItems.notifyDataSetChanged();

        //Recycler Implementation of Top Selling Items
        RecyclerView.LayoutManager layoutManagerTopSelling = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTopSelling = (RecyclerView) v.findViewById(R.id.recycler_top_selling_items);
        recyclerViewTopSelling.setLayoutManager(layoutManagerTopSelling);



        return v;
    }

    public void InitializationOfModels() {

//        DiscountItemsModel items1 = new DiscountItemsModel(R.drawable.ic_item1, "Protein Bar", "10% off");
//
//        discountItemsModel.add(items1);
//        discountItemsModel.add(items1);
//        discountItemsModel.add(items1);
//        discountItemsModel.add(items1);
//        discountItemsModel.add(items1);
//
//        DiscountItemsModel items2 = new DiscountItemsModel(R.drawable.ic_item2, "Pillow", "Rs. 120");
//
//        topSellingModel.add(items2);
//        topSellingModel.add(items2);
//        topSellingModel.add(items2);

    }

    //_____________________________________________________________________________________________________________________________________
    private void getReceipts(){
        SharedPref sp = new SharedPref(getContext());

        String url = Server.IP + "/getallproducts";

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getReceipts", response);

                if (response.equals("products not found")){
                    Toast.makeText(getContext(), "No Products Found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONArray topselling = null;
                    try {
                        topselling = new JSONArray(response);

                        for (int i =0;i<topselling.length();i++)
                        {
                            JSONObject rc = topselling.getJSONObject(i);

                            DiscountItemsModel item = new DiscountItemsModel(
                                    rc.getString("name"),
                                    rc.getString("price"));

                            topSellingModel.add(item);
                        }

                        adapterTopSelling = new DiscountItemsRecycler(getContext(), topSellingModel);
                        recyclerViewTopSelling.setAdapter(adapterTopSelling);
                        adapterTopSelling.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Exception in getting receipts", Toast.LENGTH_SHORT).show();
                    }}


                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
                pDialog.dismiss();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


}
