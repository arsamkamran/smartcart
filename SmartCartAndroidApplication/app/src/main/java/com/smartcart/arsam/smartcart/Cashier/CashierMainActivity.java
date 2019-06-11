package com.smartcart.arsam.smartcart.Cashier;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.smartcart.arsam.smartcart.Adapters.CartItemsRecycler;
import com.smartcart.arsam.smartcart.HomeFragments.CartFragment;
import com.smartcart.arsam.smartcart.LoginActivity;
import com.smartcart.arsam.smartcart.MainActivity;
import com.smartcart.arsam.smartcart.Models.CartItemsModel;
import com.smartcart.arsam.smartcart.R;
import com.smartcart.arsam.smartcart.Server;
import com.smartcart.arsam.smartcart.SignUpActivity;
import com.smartcart.arsam.smartcart.SplashScreen;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

public class CashierMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnClearCart, btnPay;
    String scanCode;
    RecyclerView recyclerViewCashierCart;
    public static CartItemsRecycler adapterCashierCartItems;
    ProgressDialog pDialog;
    public int user_id_of_scanner = 0;
    float total=0;
    TextView tvtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cashier");

        pDialog = new ProgressDialog(CashierMainActivity.this);
        tvtotal = findViewById(R.id.total_cashier);

        //Recycler Implementation of Cashier Cart Items
        RecyclerView.LayoutManager layoutManagerCart = new LinearLayoutManager(CashierMainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCashierCart = findViewById(R.id.recycler_cashier_items);
        recyclerViewCashierCart.setLayoutManager(layoutManagerCart);
        adapterCashierCartItems = new CartItemsRecycler(this, MainActivity.cashierCartItemModels);
        recyclerViewCashierCart.setAdapter(adapterCashierCartItems);

        btnPay = findViewById(R.id.btn_pay_cashier);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalDialog();
            }
        });

        btnClearCart = findViewById(R.id.btn_cart_clearcart_cashier);
        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.cashierCartItemModels.clear();
                adapterCashierCartItems.notifyDataSetChanged();
                total=0;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cashier);
        fab.setImageResource(R.drawable.ic_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        TextView headerName = header.findViewById(R.id.nav_name_cashier);
        TextView headerEmail = header.findViewById(R.id.nav_email_cashier);

        SharedPref sp = new SharedPref(this);
        headerName.setText(sp.getPref("user_name",null));
        headerEmail.setText(sp.getPref("user_email",null));
    }
//___________________________________________________________________________________________________________my functions

    private void startQRScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("");
        integrator.setOrientationLocked(false);     // to change the orientation
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "No Barcode Scanned", Toast.LENGTH_LONG).show();
            } else {
                scanCode = result.getContents();
                Toast.makeText(this, scanCode, Toast.LENGTH_LONG).show();
                //implement code to fill recycler with data

                String[] arr = scanCode.split(";");

                user_id_of_scanner = Integer.parseInt(arr[0]);
                Log.d("useriddddddddddddddddd",String.valueOf(user_id_of_scanner));

                for (int i=1;i<arr.length;i=i+4){
                    
                    int id= Integer.parseInt(arr[i]);
                    String name= arr[i+1];
                    Float quantity=Float.parseFloat(arr[i+2]);
                    Float price=Float.parseFloat(arr[i+3]);

                    CartItemsModel item = new CartItemsModel(
                            id
                            ,name,
                            quantity,
                            price,
                            quantity*price
                            ,R.drawable.ic_minus,R.drawable.ic_plus);

                    MainActivity.cashierCartItemModels.add(item);
                }

                adapterCashierCartItems.notifyDataSetChanged();
                for (int i =0; i<MainActivity.cashierCartItemModels.size();i++)
                {
                    total=total+MainActivity.cashierCartItemModels.get(i).getTotal();
                }
                tvtotal.setText(String.valueOf(total));


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Manually Add Item");
        dialogBuilder.setMessage("Enter Barcode");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(CashierMainActivity.this, edt.getText().toString(), Toast.LENGTH_SHORT).show();
                addToRecyclerCart(edt.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showTotalDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_total, null);
        dialogBuilder.setView(dialogView);

        //final EditText edt_paid = (EditText) dialogView.findViewById(R.id.edittotalpaidcd);
        //final EditText edt_received = (EditText) dialogView.findViewById(R.id.edittotalreceivedcd);

        dialogBuilder.setTitle("Are you sure?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()); //date
                float total = 0;
                for (int i=0;i<MainActivity.cashierCartItemModels.size();i++)
                {
                    total = total+ MainActivity.cashierCartItemModels.get(i).getTotal();
                }
                createReceipt(user_id_of_scanner,date, (int) total);                    //api call to create new receipt

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
//___________________________________________________________________________________________________________api calls

    private void getProductInformation(String barcode) {

        //SharedPref sp = new SharedPref(MainActivity.this);

        String url = Server.IP + "/getproduct?barcode=" + barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("product not found")){
                    Toast.makeText(CashierMainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject product = new JSONObject(response);
                        JSONArray products = product.getJSONArray("product");
                        JSONObject pd = products.getJSONObject(0);

                        if (itemAlreadyAdded(pd.getInt("id"))){
                            Toast.makeText(CashierMainActivity.this, "Item already added", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CartItemsModel item = new CartItemsModel(
                                    pd.getInt("id")
                                    ,pd.getString("name"),
                                    1f,
                                    ((float) pd.getDouble("price")),
                                    ((float) pd.getDouble("price"))
                                    ,R.drawable.ic_minus,R.drawable.ic_plus);

                            MainActivity.cashierCartItemModels.add(item);
                            adapterCashierCartItems.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CashierMainActivity.this, "Exception in get product", Toast.LENGTH_SHORT).show();
                    }
                }

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

    private void createReceipt(int userid, String date, int total){
        String url = Server.IP + "/createreceipt?userid=" + userid + "&date="+ date + "&total=" + total;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int receiptid = Integer.parseInt(response);

                //send items to cart
                Toast.makeText(CashierMainActivity.this, "Payment Successfully done", Toast.LENGTH_SHORT).show();

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

    //______________________________________________________________________________________________implementation of apis
    private void addToRecyclerCart(String barcode) {
        getProductInformation(barcode);                //api call to get product information
    }

    private boolean itemAlreadyAdded(int id){
        for (int i=0;i<MainActivity.cashierCartItemModels.size();i++){
            if (MainActivity.cashierCartItemModels.get(i).getId() == id)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;
    }

    //___________________________________________________________________________________________________________________________
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cashier_main, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_manually_add_item) {
            showDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout_cashier) {
            Intent i = new Intent(this, LoginActivity.class);
            SplashScreen.logged = false;
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
