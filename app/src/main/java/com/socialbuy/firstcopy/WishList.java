package com.socialbuy.firstcopy;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class WishList extends AppCompatActivity {

    private RecyclerView rv;
    private StaggeredGridLayoutManager sgm;
    private WishListAdapter mAdapter;
    private TextView tv1,tv2,tv3;
    ArrayList<String> userNames,imageUrls,productDescs,profileLinks,dateofCreations,sellerBio,sellerPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");

        tv1 = findViewById(R.id.tvWishListHeader);
        tv2 = findViewById(R.id.tvNumberofWishlistItems);
        tv3 = findViewById(R.id.tvPrivateCollection);

        tv1.setTypeface(typeface);
        tv2.setTypeface(typeface);
        tv3.setTypeface(typeface);

        rv = (RecyclerView) findViewById(R.id.rvWishList);
        rv.setHasFixedSize(true);
        sgm = new StaggeredGridLayoutManager(2,1);
        rv.setLayoutManager(sgm);

        WishListDatabase db = new WishListDatabase(WishList.this);
        db.open();

        String allDetails = db.getAllDetails();
        ArrayList<String> allDetailsList = new ArrayList<>(Arrays.asList(allDetails.split("<")));
        if (!allDetails.isEmpty()){
            if (allDetailsList.size() >=4) {
                String names = allDetailsList.get(0);
                String images = allDetailsList.get(1);
                String profiles = allDetailsList.get(2);
                String products = allDetailsList.get(3);
                String dates = allDetailsList.get(4);
                String sellerBios = allDetailsList.get(5);
                String sellerPhoneNumbers = allDetailsList.get(6);

                userNames = new ArrayList<>(Arrays.asList(names.split(">")));
                imageUrls = new ArrayList<>(Arrays.asList(images.split(">")));
                profileLinks = new ArrayList<>(Arrays.asList(profiles.split(">")));
                productDescs = new ArrayList<>(Arrays.asList(products.split(">")));
                dateofCreations = new ArrayList<>(Arrays.asList(dates.split(">")));
                sellerBio = new ArrayList<>(Arrays.asList(sellerBios.split(">")));
                sellerPhoneNumber = new ArrayList<>(Arrays.asList(sellerPhoneNumbers.split(">")));

                //Toast.makeText(getApplicationContext(), "" + userNames.size(), Toast.LENGTH_SHORT).show();
                String numberOfItems = String.valueOf(userNames.size());
                if (numberOfItems.equals("1"))
                    tv2.setText(String.valueOf(userNames.size() + " Item"));
                else
                    tv2.setText(String.valueOf(userNames.size() + " Items"));

                mAdapter = new WishListAdapter(WishList.this, userNames, imageUrls, profileLinks, productDescs, dateofCreations,sellerBio,sellerPhoneNumber);
                rv.setAdapter(mAdapter);
            }else {
                Toast.makeText(getApplicationContext(),"No Items in Your Wishlist",Toast.LENGTH_SHORT).show();
            }

        }



        db.close();








    }

}
