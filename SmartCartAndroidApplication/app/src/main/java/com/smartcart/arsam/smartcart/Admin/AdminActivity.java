package com.smartcart.arsam.smartcart.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.method.KeyListener;
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
import com.smartcart.arsam.smartcart.Cashier.CashierMainActivity;
import com.smartcart.arsam.smartcart.LoginActivity;
import com.smartcart.arsam.smartcart.MainActivity;
import com.smartcart.arsam.smartcart.R;
import com.smartcart.arsam.smartcart.Server;
import com.smartcart.arsam.smartcart.SplashScreen;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btn_add_cashier, btn_remove_cashier, btn_add_product, btn_remove_product, btn_edit_product;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialization();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        btn_add_cashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                    LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.add_cashier_layout_admin, null);
                    dialogBuilder.setView(dialogView);

                    final EditText name = (EditText) dialogView.findViewById(R.id.edt_name_add_cashier_layout);
                    final EditText email = (EditText) dialogView.findViewById(R.id.edt_email_add_cashier_layout);
                    final EditText password = (EditText) dialogView.findViewById(R.id.edt_password_add_cashier_layout);

                    dialogBuilder.setTitle("Add Cashier");
                    dialogBuilder.setMessage("Enter Details");
                    dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //send api call with data
                            addCashier(name.getText().toString(),email.getText().toString(),password.getText().toString());

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
        });

        btn_remove_cashier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Remove Cashier");
                dialogBuilder.setMessage("Enter Email");
                dialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //send api call with data
                        removeCashier(edt.getText().toString()); //get email from text box and pass to api call
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
        });

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_product_layout_admin, null);
                dialogBuilder.setView(dialogView);

                final EditText name = (EditText) dialogView.findViewById(R.id.edt_name_add_product);
                final EditText desc = (EditText) dialogView.findViewById(R.id.edt_desc_add_product);
                final EditText price = (EditText) dialogView.findViewById(R.id.edt_price_add_product);
                final EditText quantity = (EditText) dialogView.findViewById(R.id.edt_quantity_add_product);
                final EditText barcode = (EditText) dialogView.findViewById(R.id.edt_barcode_add_product);

                dialogBuilder.setTitle("Add Product");
                dialogBuilder.setMessage("Enter details");
                dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //send api call with data
                        addProduct(name.getText().toString(),
                                desc.getText().toString(),
                                Float.parseFloat(price.getText().toString()),
                                Integer.parseInt(quantity.getText().toString()),
                                Long.parseLong(barcode.getText().toString()));
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
        });

        btn_remove_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Remove Product");
                dialogBuilder.setMessage("Enter Barcode");
                dialogBuilder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //send api call with data
                        removeProduct(Long.parseLong(edt.getText().toString()));
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
        });

        btn_edit_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Search Product");
                dialogBuilder.setMessage("Enter Barcode");
                dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getProductInformation(edt.getText().toString());
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();


//                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
//                LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
//                final View dialogView = inflater.inflate(R.layout.add_product_layout_admin, null);
//                dialogBuilder.setView(dialogView);
//
//                final EditText name = (EditText) dialogView.findViewById(R.id.edt_name_add_product);
//                final EditText desc = (EditText) dialogView.findViewById(R.id.edt_desc_add_product);
//                final EditText price = (EditText) dialogView.findViewById(R.id.edt_price_add_product);
//                final EditText quantity = (EditText) dialogView.findViewById(R.id.edt_quantity_add_product);
//                final EditText barcode = (EditText) dialogView.findViewById(R.id.edt_barcode_add_product);
//
//                dialogBuilder.setTitle("Edit Product");
//                dialogBuilder.setMessage("Enter Barcode");
//                dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //send api call with data
//                        Toast.makeText(AdminActivity.this, "send api call", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //pass
//                    }
//                });
//                AlertDialog b = dialogBuilder.create();
//                b.show();
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
        TextView headerName = header.findViewById(R.id.nav_name_admin);
        TextView headerEmail = header.findViewById(R.id.nav_email_admin);

        SharedPref sp = new SharedPref(this);
        headerName.setText(sp.getPref("user_name",null));
        headerEmail.setText(sp.getPref("user_email",null));
    }

    private void initialization() {
        btn_add_cashier = findViewById(R.id.btn_add_cashier_admin);
        btn_remove_cashier = findViewById(R.id.btn_remove_cashier_admin);
        btn_add_product = findViewById(R.id.btn_add_product_admin);
        btn_remove_product = findViewById(R.id.btn_remove_product_admin);
        btn_edit_product = findViewById(R.id.btn_edit_product_admin);

        pDialog = new ProgressDialog(AdminActivity.this);
    }

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
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout_admin) {
            Intent i = new Intent(this, LoginActivity.class);
            SplashScreen.logged = false;
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //______________________________________________________________________________________________ api calls

    private void addCashier(String name, String email, String password){

        String url = Server.IP + "/addcashier?name=" + name + "&email="+ email + "&password=" + password;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int receiptid = Integer.parseInt(response);

                //send items to cart
                Toast.makeText(AdminActivity.this, "Cashier Successfully added", Toast.LENGTH_SHORT).show();

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

    private void removeCashier(String email){

        String url = Server.IP + "/removecashier?email=" + email;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int id = Integer.parseInt(response);

                if(id == 1)
                {
                    Toast.makeText(AdminActivity.this, "Cashier Successfully removed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AdminActivity.this, "Error: No such cashier exists", Toast.LENGTH_SHORT).show();
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

    private void addProduct(String name, String description, float price, int quantity, long barcode) {

        String url = Server.IP + "/addproduct?name=" + name + "&description="+ description + "&price=" + price+ "&quantity="+ quantity+ "&barcode="+ barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int receiptid = Integer.parseInt(response);

                //send items to cart
                Toast.makeText(AdminActivity.this, "Product Successfully added", Toast.LENGTH_SHORT).show();

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

    private void removeProduct(long barcode){

        String url = Server.IP + "/removeproduct?barcode=" + barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int id = Integer.parseInt(response);

                if(id == 1)
                {
                    Toast.makeText(AdminActivity.this, "Product Successfully removed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AdminActivity.this, "Error: No such product exists", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdminActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject product = new JSONObject(response);
                        JSONArray products = product.getJSONArray("product");
                        JSONObject pd = products.getJSONObject(0);

                        //show it in alert dialog
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdminActivity.this);
                        LayoutInflater inflater = AdminActivity.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.add_product_layout_admin, null);
                        dialogBuilder.setView(dialogView);

                        final EditText name = (EditText) dialogView.findViewById(R.id.edt_name_add_product);
                        final EditText desc = (EditText) dialogView.findViewById(R.id.edt_desc_add_product);
                        final EditText price = (EditText) dialogView.findViewById(R.id.edt_price_add_product);
                        final EditText quantity = (EditText) dialogView.findViewById(R.id.edt_quantity_add_product);
                        final EditText barcode = (EditText) dialogView.findViewById(R.id.edt_barcode_add_product);

                        name.setText(pd.getString("name"));
                        desc.setText(pd.getString("description"));
                        price.setText(pd.getString("price"));
                        quantity.setText(pd.getString("quantity"));
                        barcode.setText(pd.getString("barcode"));

                        KeyListener variable;           //make edit text uneditable through this code
                        variable = barcode.getKeyListener();
                        barcode.setKeyListener(null);

                        dialogBuilder.setTitle("Edit Product");
                        dialogBuilder.setMessage("Enter Barcode");
                        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //send api call with data
                                editProduct(name.getText().toString(),
                                        desc.getText().toString(),
                                        Float.parseFloat(price.getText().toString()),
                                        Integer.parseInt(quantity.getText().toString()),
                                        Long.parseLong(barcode.getText().toString()));
                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //pass
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdminActivity.this, "Exception in get product", Toast.LENGTH_SHORT).show();
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

    private void editProduct(String name, String description, float price, int quantity, long barcode) {

        String url = Server.IP + "/editproduct?name=" + name + "&description="+ description + "&price=" + price+ "&quantity="+ quantity+ "&barcode="+ barcode;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                int receiptid = Integer.parseInt(response);

                //send items to cart
                Toast.makeText(AdminActivity.this, "Product Successfully edited", Toast.LENGTH_SHORT).show();

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
