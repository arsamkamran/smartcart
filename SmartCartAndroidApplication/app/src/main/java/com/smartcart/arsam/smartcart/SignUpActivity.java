package com.smartcart.arsam.smartcart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.smartcart.arsam.smartcart.Cashier.CashierMainActivity;
import com.smartcart.arsam.smartcart.Utility.SharedPref;
import com.smartcart.arsam.smartcart.VolleyController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pDialog = new ProgressDialog(this);
        Button btn_login, btn_signup;

        btn_login = (Button) findViewById(R.id.btn_login_signup);
        btn_signup = (Button) findViewById(R.id.btn_signup_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormEditText nameEditText = (FormEditText) findViewById(R.id.et_name_signup),
                        emailEditText = (FormEditText) findViewById(R.id.et_email_signup),
                        passwordEditText = (FormEditText) findViewById(R.id.et_password_signup),
                        confirmEditText = (FormEditText) findViewById(R.id.et_c_password_signup);

                FormEditText[] allFields = {nameEditText, emailEditText, passwordEditText, confirmEditText};
                boolean allValid = true;
                for (FormEditText field : allFields) {
                    allValid = field.testValidity() && allValid;
                }

                if (allValid) {
                    if (passwordEditText.getText().toString().length() < 5) {
                        passwordEditText.setError("Password cannot be less than 5");
                    } else {
                        if (passwordEditText.getText().toString().equals(confirmEditText.getText().toString())) {
                            signupCall(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                        } else {
                            confirmEditText.setError("Password does not match");
                        }
                    }
                }
            }
        });
    }

    //________________________________________________________________________________________________________________________
    //signup call

    public void signupCall(String name, final String email , final String password){

        String url = Server.IP + "/signup?name=" + name + "&email="+ email + "&password=" + password;
        url = url.replaceAll(" ", "%20");

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("1")){
                    Toast.makeText(SignUpActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();
                    loginCall(email,password);
                    finish();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
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

    public void loginCall(String email, String password){

        String url = Server.IP + "/login?email=" + email + "&pass=" + password;

        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.print(response);
                Log.d("response:getProduct", response);

                if (response.equals("user not found")){
                    Toast.makeText(SignUpActivity.this, "Incorrect Details", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {

                        JSONObject user = new JSONObject(response);
                        JSONArray products = user.getJSONArray("user");
                        JSONObject us = products.getJSONObject(0);

                        SharedPref sp = new SharedPref(SignUpActivity.this);
                        sp.putIntPref("user_id",us.getInt("id"));
                        sp.putPref("user_name",us.getString("name"));
                        sp.putPref("user_email",us.getString("email"));
                        sp.putIntPref("user_role_id",us.getInt("role_id"));

                        int role_id = us.getInt("role_id");
                        if (role_id==1){
                            //Admin
                            //Intent admin
                        }
                        else if(role_id==2){
                            //User
                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            //Cashier
                            Intent i = new Intent(SignUpActivity.this,CashierMainActivity.class);
                            startActivity(i);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Exception in login", Toast.LENGTH_SHORT).show();
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



}
