package com.example.michaelwang.capitaltogo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        final Typeface custom_font_medium = Typeface.createFromAsset(getAssets(),  "fonts/Avenir-Medium.ttf");
        final TextView choose = (TextView) findViewById(R.id.choose);
        final RelativeLayout checking = (RelativeLayout) findViewById(R.id.checking);
        final TextView checkingName = (TextView) findViewById(R.id.tvChecking);
        final TextView checkingNum = (TextView) findViewById(R.id.tvCheckingNum);
        final TextView checkingAmt = (TextView) findViewById(R.id.tvCheckingAmt);
        final RelativeLayout savings = (RelativeLayout) findViewById(R.id.savings);
        final TextView savingsName = (TextView) findViewById(R.id.tvSavings);
        final TextView savingsNum = (TextView) findViewById(R.id.tvSavingsNum);
        final TextView savingsAmt = (TextView) findViewById(R.id.tvSavingsAmt);
        final RelativeLayout emergency = (RelativeLayout) findViewById(R.id.emergency);
        final TextView emergencyName = (TextView) findViewById(R.id.tvEmergency);
        final TextView emergencyNum = (TextView) findViewById(R.id.tvEmergencyAmt);
        final TextView emergencyAmt = (TextView) findViewById(R.id.tvEmergencyNum);

        choose.setTypeface(custom_font_medium);
        checkingName.setTypeface(custom_font_medium);
        checkingNum.setTypeface(custom_font_medium);
        checkingAmt.setTypeface(custom_font_medium);
        savingsName.setTypeface(custom_font_medium);
        savingsNum.setTypeface(custom_font_medium);
        savingsAmt.setTypeface(custom_font_medium);
        emergencyName.setTypeface(custom_font_medium);
        emergencyNum.setTypeface(custom_font_medium);
        emergencyAmt.setTypeface(custom_font_medium);

        RequestQueue queue = Volley.newRequestQueue(HomeScreenActivity.this);
        String url = "http://api.reimaginebanking.com/customers/58788eb81756fc834d8eb492/accounts?key=[SECRET]";

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        String checkingAcctName = "";
                        String checkingAcctNum = "";
                        String checkingAcctAmt = "";
                        String savingsAcctName = "";
                        String savingsAcctNum = "";
                        String savingsAcctAmt = "";
                        String emergencyAcctName = "";
                        String emergencyAcctNum = "";
                        String emergencyAcctAmt = "";
                        try {
                            checkingAcctName = response.getJSONObject(0).getString("nickname");
                            checkingAcctAmt = response.getJSONObject(0).getString("balance");
                            checkingAcctNum = response.getJSONObject(0).getString("account_number");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            savingsAcctName = response.getJSONObject(1).getString("nickname");
                            savingsAcctAmt = response.getJSONObject(1).getString("balance");
                            savingsAcctNum = response.getJSONObject(1).getString("account_number");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            emergencyAcctName = response.getJSONObject(2).getString("nickname");
                            emergencyAcctAmt = response.getJSONObject(2).getString("balance");
                            emergencyAcctNum = response.getJSONObject(2).getString("account_number");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("HomeScreenActivity", response.toString());
                        checkingAmt.setText(checkingAcctAmt);
                        checkingName.setText(checkingAcctName);
                        checkingNum.setText(checkingAcctNum);

                        savingsAmt.setText(savingsAcctAmt);
                        savingsName.setText(savingsAcctName);
                        savingsNum.setText(savingsAcctNum);

                        emergencyAmt.setText(emergencyAcctAmt);
                        emergencyName.setText(emergencyAcctName);
                        emergencyNum.setText(emergencyAcctNum);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        queue.add(jsObjRequest);


        //get all name acc numbers and balances
        checking.setOnClickListener(new View.OnClickListener() {
            //pass the information too!
            @Override
            public void onClick(View v) {
                Intent choseBank = new Intent(HomeScreenActivity.this, RequestCashActivity.class);
                HomeScreenActivity.this.startActivity(choseBank);
            }
        });
        savings.setOnClickListener(new View.OnClickListener() {
            //pass the information too!
            @Override
            public void onClick(View v) {
                Intent choseBank = new Intent(HomeScreenActivity.this, RequestCashActivity.class);
                HomeScreenActivity.this.startActivity(choseBank);
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            //pass the information too!
            @Override
            public void onClick(View v) {
                Intent choseBank = new Intent(HomeScreenActivity.this, RequestCashActivity.class);
                HomeScreenActivity.this.startActivity(choseBank);
            }
        });

    }
}
