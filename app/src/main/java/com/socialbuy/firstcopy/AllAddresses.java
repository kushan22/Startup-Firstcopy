package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.adapters.AllAddressesAdapter;
import com.socialbuy.firstcopy.authentication.Signin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class AllAddresses extends AppCompatActivity implements View.OnClickListener {

    private static final String ALLADDRESSES_URL = "http://www.firstcopy.co.in/firstcopy/getalladdresses.php";
    private RecyclerView rvAddresses;
    private AllAddressesAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<String> nameList,addressList,phoneNumberList,pincodeList;
    private TextView tvAddAddress;
    private ImageButton ibAddAddress;
    private String email;
    private Button btDeliverHere;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_addresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        progressBar = findViewById(R.id.progressBar1);
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
        tvAddAddress = findViewById(R.id.tvAddAddress);
        tvAddAddress.setTypeface(typefaceBold);
        ibAddAddress = findViewById(R.id.ibAddAddress);

        btDeliverHere = findViewById(R.id.btDeliverHere);

        rvAddresses = findViewById(R.id.rvAllAddresses);
        rvAddresses.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(AllAddresses.this);
        rvAddresses.setLayoutManager(linearLayoutManager);


        tvAddAddress.setOnClickListener(this);
        ibAddAddress.setOnClickListener(this);
        btDeliverHere.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
        email = preferences.getString(Signin.PREF_EMAIL,"none");

        progressBar.setVisibility(View.VISIBLE);
        new GetAllAddresses().execute("");





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return true;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvAddAddress:
                Intent intent = new Intent(AllAddresses.this,Address.class);
                startActivity(intent);
                break;
            case R.id.ibAddAddress:
                Intent intent1 = new Intent(AllAddresses.this,Address.class);
                startActivity(intent1);
                break;
            case R.id.btDeliverHere:
                Intent deliverIntent = new Intent(AllAddresses.this,OrderSummaryandCheckout.class);
                startActivity(deliverIntent);
                finish();
                break;
        }

    }

    public class GetAllAddresses extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader reader;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(ALLADDRESSES_URL).buildUpon().appendQueryParameter("EMAIL",email).build();
            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");

                }

                result = getAddressesFromJson(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null){
                if (s.equals("404")){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Problem while connecting with the server",Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.GONE);
                    ArrayList<String> allDetails = new ArrayList<>(Arrays.asList(s.split("<")));
                    if (allDetails.size() == 4){
                        String buyerNames = allDetails.get(0);
                        String addresses = allDetails.get(1);
                        String phoneNumbers = allDetails.get(2);
                        String pinCodes = allDetails.get(3);

                        nameList = new ArrayList<>(Arrays.asList(buyerNames.split(">")));
                        addressList = new ArrayList<>(Arrays.asList(addresses.split(">")));
                        phoneNumberList = new ArrayList<>(Arrays.asList(phoneNumbers.split(">")));
                        pincodeList = new ArrayList<>(Arrays.asList(pinCodes.split(">")));

                        mAdapter = new AllAddressesAdapter(AllAddresses.this,nameList,addressList,phoneNumberList,pincodeList);
                        rvAddresses.setAdapter(mAdapter);


                    }
                }
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Problem while connecting with the server",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private String getAddressesFromJson(String jsonString){
        String res = "";
        String buyerName="",addressLine="",pincode="",phoneNumber="";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res=  jsonObject.getString("status");
            return res;
        } catch (JSONException e) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0){
                        buyerName = buyerName + jsonObject.getString("fullname");
                        addressLine = addressLine + jsonObject.getString("address") +  jsonObject.getString("city") + jsonObject.getString("state");
                        phoneNumber = phoneNumber + jsonObject.getString("phone_number");
                        pincode = pincode + jsonObject.getString("pincode");
                    }else{
                        buyerName = buyerName +  "> " + jsonObject.getString("fullname");
                        addressLine = addressLine + ">" +  jsonObject.getString("address") +  jsonObject.getString("city") + jsonObject.getString("state");
                        phoneNumber = phoneNumber + ">" +  jsonObject.getString("phone_number");
                        pincode = pincode + ">" + jsonObject.getString("pincode");
                    }
                }

                res = buyerName + "<" + addressLine + "<" + phoneNumber + "<" + pincode;

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


        return res;
    }
}
