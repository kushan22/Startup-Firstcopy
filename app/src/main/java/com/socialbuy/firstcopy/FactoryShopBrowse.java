package com.socialbuy.firstcopy;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crashlytics.android.answers.InviteEvent;
import com.socialbuy.firstcopy.fragments.TabFragment;

public class FactoryShopBrowse extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    private String category;
    private TextView tv1;
    private ImageButton ib1;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_shop_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface typeface;
        typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");

        tv1 = (TextView) findViewById(R.id.tvNoNetwork);
        tv1.setTypeface(typeface);
        ib1 = (ImageButton) findViewById(R.id.ibNoNetwork);
        bt1 = (Button) findViewById(R.id.btRetry);


        doTask();

//        SharedPreferences preferences = getSharedPreferences("CATEGORYPREF",Context.MODE_PRIVATE);
//        category = preferences.getString("CATEGORY","none");


//        TabFragment tabFragment = new TabFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("category",category);
//        tabFragment.setArguments(bundle);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTask();
            }
        });




    }

    private boolean checkForNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean networkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return networkConnected;
    }

    private void doTask(){

        boolean isConnected = checkForNetwork();
        if (!isConnected){
            tv1.setVisibility(View.VISIBLE);
            ib1.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
        }else {
            tv1.setVisibility(View.INVISIBLE);
            ib1.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.gendertabscontainer,new TabFragment()).commit();
        }


    }

}
