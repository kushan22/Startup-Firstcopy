package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class MyReferrals extends AppCompatActivity {

//    private TextView tv1,tv2,tv3,tv4;
//    private Button btShare;
//    private static final String REFERRAL_URL = "http://192.168.0.102/getreferral_code.php";
//    private String emailid;
//    SharedPreferences preferences;
//    String referralCode;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_referrals);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////
////        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        tv1 = findViewById(R.id.tvReferralCodeHeader);
//        tv2 = findViewById(R.id.tvReferralCodeOption);
//        tv3 = findViewById(R.id.tvReferralCode);
//        tv4 = findViewById(R.id.tvReferralCondition);
//
//        btShare = findViewById(R.id.btShareReferal);
//
//        Typeface typefaceReg = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
//        Typeface typeFaceBold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//
//        tv1.setTypeface(typefaceReg);
//        tv2.setTypeface(typefaceReg);
//        tv3.setTypeface(typeFaceBold);
//        tv4.setTypeface(typefaceReg);
//
//        btShare.setTypeface(typefaceReg);
//
//        preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
//        emailid = preferences.getString(Signin.PREF_EMAIL,"none");
//
//        new GetReferralCode().execute("");
//
//
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//    }
//
//    public class GetReferralCode extends AsyncTask<String,String,String>{
//        HttpURLConnection connection;
//        BufferedReader reader;
//        InputStream inputStream;
//        String result = "";
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            Uri uri = Uri.parse(REFERRAL_URL).buildUpon().appendQueryParameter("EMAIL",emailid).build();
//            try {
//                URL url = new URL(uri.toString());
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                connection.connect();
//
//                inputStream = connection.getInputStream();
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = "";
//                StringBuilder sb = new StringBuilder();
//
//
//                while ((line = reader.readLine()) != null){
//                    sb.append(line + "\n");
//                }
//
//
//
//
//                result = getDataFromJson(sb.toString());
//
//
//
//
//
//
//
//
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (connection != null)
//                    connection.disconnect();
//                if (reader != null)
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//            }
//
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (s != null){
//                ArrayList<String> allDetails  = new ArrayList<>(Arrays.asList(s.split(">")));
//                if (allDetails.size() == 2){
//                    referralCode = allDetails.get(0);
//                    Log.i("REFCODE",referralCode);
//                    String emails = allDetails.get(1);
//                    ArrayList<String> allEmails = new ArrayList<>(Arrays.asList(emails.split(",")));
//                    for (String email: allEmails){
//                        Log.i("EMAILS" , email);
//                    }
//                }
//            }
//        }
//    }
//
//
//    private String getDataFromJson(String jsonString){
//        String res = "";
//        String refCode = "";
//        String emails = "";
//
//        try {
//            JSONArray jsonArray = new JSONArray(jsonString);
//            for (int i = 0; i < jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                if (i == 0){
//                    refCode = refCode + jsonObject.getString("referral_code");
//                }else {
//                    if (i == 1){
//                        emails = emails + jsonObject.getString("email");
//                    }else {
//                        emails = emails + "," + jsonObject.getString("email");
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        res = refCode + ">" + emails;
//
//        return res;
//    }
//





}
