package com.smartcart.arsam.smartcart;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.smartcart.arsam.smartcart.HomeFragments.CartFragment;
import com.smartcart.arsam.smartcart.HomeFragments.HomeFragment;
import com.smartcart.arsam.smartcart.Models.CartItemsModel;
import com.smartcart.arsam.smartcart.Receipts.ReceiptActivity;
import com.smartcart.arsam.smartcart.Scanner.ScanningActivity;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PagerAdapter mPagerAdapter;
    FragmentTransaction ft;
    HomeFragment homeFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {R.drawable.ic_home, R.drawable.ic_cart};
    private int[] tabSelectedIcons = {R.drawable.ic_home_white, R.drawable.ic_cart_white};
    String scanCode;
    ProgressDialog pDialog;
    public static float total;

    public static List<CartItemsModel> cartItemsModels = new ArrayList<>();
    public static List<CartItemsModel> cashierCartItemModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        pDialog = new ProgressDialog(MainActivity.this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(tabSelectedIcons[0]);
                        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(tabSelectedIcons[1]);
                        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        homeFragment = new HomeFragment();
//
//        BottomBar bottomBar = findViewById(R.id.bottomBara);
//
//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                if (tabId == R.id.tab_homes) {
//
//                    Toast.makeText(MainActivity.this, "sadfkadskjf", Toast.LENGTH_SHORT).show();
//                    ft = getSupportFragmentManager().beginTransaction();
//
//                    if (homeFragment.isAdded()) {
//                        ft.show(homeFragment);
//                    } else {
//                        ft.add(R.id.fragment_container, homeFragment);
//                    }
//                    //hideFragment(ft, cartFragment);
//
//                    ft.commit();
//                }
//                else if (tabId == R.id.tab_carts) {
//                    Toast.makeText(MainActivity.this, "sadfkadskjf", Toast.LENGTH_SHORT).show();
//
//                    // The tab with id R.id.tab_favorites was selected,
//                    // change your content accordingly.
//                }
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(MainActivity.this,ScanningActivity.class);
//                startActivity(i);
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
        TextView headerName = header.findViewById(R.id.name_navbar);
        TextView headerEmail = header.findViewById(R.id.email_navbar);

        SharedPref sp = new SharedPref(this);
        headerName.setText(sp.getPref("user_name",null));
        headerEmail.setText(sp.getPref("user_email",null));

    }
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
                addToRecyclerCart();    //call api to add product
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //______________________________________________________________________________________________settings
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabSelectedIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity.ViewPagerAdapter adapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new CartFragment(), "Cart");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void hideFragment(FragmentTransaction ft, Fragment f) {
        if (f.isAdded()) {
            ft.hide(f);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_history) {
            Intent i = new Intent(MainActivity.this, ReceiptActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_faq) {
            Intent i = new Intent(MainActivity.this, Faq.class);
            startActivity(i);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:03105026468"));
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            SplashScreen.logged = false;
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(scanCode);
        builder.setMessage("This is an alert with no consequence");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You don't have to do anything here if you just
                // want it dismissed when clicked
            }
        });

    }

    //______________________________________________________________________________________________implementation of apis
    private void addToRecyclerCart() {
        getProductInformation(scanCode);                //api call to get product information
    }

    private boolean itemAlreadyAdded(int id){
        for (int i=0;i<cartItemsModels.size();i++){
            if (cartItemsModels.get(i).getId() == id)
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

//__________________________________________________________________________________________________api call
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
                    Toast.makeText(MainActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                            JSONObject product = new JSONObject(response);
                            JSONArray products = product.getJSONArray("product");
                            JSONObject pd = products.getJSONObject(0);

                            if (itemAlreadyAdded(pd.getInt("id"))){
                                Toast.makeText(MainActivity.this, "Item already added", Toast.LENGTH_SHORT).show();
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

                                MainActivity.cartItemsModels.add(item);
                                CartFragment.adapterCartItems.notifyDataSetChanged();

                                for (int i =0;i<cartItemsModels.size();i++)
                                {
                                    total = total + cartItemsModels.get(i).getTotal();
                                }
                                CartFragment.tvtotal.setText(String.valueOf(total));
                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Exception in get product", Toast.LENGTH_SHORT).show();
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






//end
}
