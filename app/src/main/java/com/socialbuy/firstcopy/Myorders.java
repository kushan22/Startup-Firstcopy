package com.socialbuy.firstcopy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.socialbuy.firstcopy.authentication.Signin;
import com.socialbuy.firstcopy.pojo.OrderDetails;

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

public class Myorders extends AppCompatActivity {

    private RecyclerView rvOrders;
    private LinearLayoutManager linearLayoutManager;
    private MyorderAdapter mAdapter;
    private static final String MYORDER_URL = "http://www.firstcopy.co.in/firstcopy/getMyOrders.php";
    private ArrayList<OrderDetails> orderDetails = new ArrayList<>();
    private String email;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar1);


        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Myorders.this);
        rvOrders.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
        email = preferences.getString(Signin.PREF_EMAIL,"none");

        progressBar.setVisibility(View.VISIBLE);

        new GetOrderDetails().execute("");


    }



    class GetOrderDetails extends AsyncTask<String, String, ArrayList<OrderDetails>> {

        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        ArrayList<OrderDetails>  result = new ArrayList<>();

        @Override
        protected ArrayList<OrderDetails> doInBackground(String... strings) {

            Uri uri = Uri.parse(MYORDER_URL).buildUpon().appendQueryParameter("EMAIL",email).build();
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

                result = getDataFromJson(sb.toString());





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderDetails> s) {
            super.onPostExecute(s);
            if (!s.isEmpty()){
                if (s.get(0).getProductName().equals("none")){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"No orders yet",Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    mAdapter = new MyorderAdapter(Myorders.this,orderDetails,progressBar);
                    rvOrders.setAdapter(mAdapter);
                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Problem while connecting with our server",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private ArrayList<OrderDetails> getDataFromJson(String jsonString){

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String status = jsonObject.getString("status");
            if (status.equals("404")){
                OrderDetails details = new OrderDetails();
                details.setProductName("none");
                details.setProductImage("none");
                details.setDate("none");
                details.setOrderid("none");
                details.setPrice("none");
                details.setStatus("none");
                details.setSize("none");

                orderDetails.add(details);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (e.toString().startsWith("org.json.JSONException")){
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        OrderDetails details = new OrderDetails();
                        details.setProductName(object.getString("product_name"));
                        details.setProductImage(object.getString("product_image"));
                        details.setStatus(object.getString("status"));
                        details.setPrice(object.getString("price"));
                        details.setOrderid(object.getString("transaction_id"));
                        details.setDate(object.getString("order_time"));
                        details.setSize(object.getString("product_size"));

                        orderDetails.add(details);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return orderDetails;
    }


}
