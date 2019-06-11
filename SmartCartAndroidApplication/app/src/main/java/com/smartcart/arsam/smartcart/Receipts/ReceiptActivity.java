package com.smartcart.arsam.smartcart.Receipts;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartcart.arsam.smartcart.Adapters.CartItemsRecycler;
import com.smartcart.arsam.smartcart.Adapters.ReceiptItemsRecycler;
import com.smartcart.arsam.smartcart.Cashier.CashierMainActivity;
import com.smartcart.arsam.smartcart.MainActivity;
import com.smartcart.arsam.smartcart.Models.CartItemsModel;
import com.smartcart.arsam.smartcart.Models.ReceiptItemsModel;
import com.smartcart.arsam.smartcart.R;
import com.smartcart.arsam.smartcart.Server;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceiptActivity extends AppCompatActivity {

    RecyclerView recyclerViewReceipt;
    ReceiptItemsRecycler adapterReceiptItems;
    List<ReceiptItemsModel> receiptItemsModel = new ArrayList<>();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase Receipts");

        pDialog = new ProgressDialog(ReceiptActivity.this);



        RecyclerView.LayoutManager layoutManagerCart = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerViewReceipt = findViewById(R.id.recycler_receipt_activity);
        recyclerViewReceipt.setLayoutManager(layoutManagerCart);


        getReceipts();

    }

    //______________________________________________________________________________________________________________
    //api call for getting items
    private void getReceipts(){
        SharedPref sp = new SharedPref(ReceiptActivity.this);

        String url = Server.IP + "/getreceipts?userid=" + sp.getIntPref("user_id",1);

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getReceipts", response);

                if (response.equals("receipts not found")){
                    Toast.makeText(ReceiptActivity.this, "No Receipts Found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        JSONArray receipts = null;
                        try {
                            receipts = new JSONArray(response);

                            for (int i =0;i<receipts.length();i++)
                            {
                                JSONObject rc = receipts.getJSONObject(i);

                                ReceiptItemsModel item = new ReceiptItemsModel(rc.getInt("id"),
                                        rc.getDouble("total"),
                                        rc.getString("date"));

                                receiptItemsModel.add(item);
                            }

                            adapterReceiptItems = new ReceiptItemsRecycler(ReceiptActivity.this, receiptItemsModel);
                            recyclerViewReceipt.setAdapter(adapterReceiptItems);
                            adapterReceiptItems.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReceiptActivity.this, "Exception in getting receipts", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
