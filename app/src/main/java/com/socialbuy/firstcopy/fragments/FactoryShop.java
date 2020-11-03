package com.socialbuy.firstcopy.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.socialbuy.firstcopy.Browse;
import com.socialbuy.firstcopy.CategoryAdapter;
import com.socialbuy.firstcopy.Config;
import com.socialbuy.firstcopy.FactoryAdapter;
import com.socialbuy.firstcopy.FactoryShopBrowse;
import com.socialbuy.firstcopy.FactoryproductDetails;
import com.socialbuy.firstcopy.HidingScrollListener;
import com.socialbuy.firstcopy.LatestPostAdapter;
import com.socialbuy.firstcopy.Products;
import com.socialbuy.firstcopy.R;
import com.socialbuy.firstcopy.RecyclerViewItemListener;
import com.socialbuy.firstcopy.adapters.FactoryShopLatestPostAdapter;
import com.socialbuy.firstcopy.pojo.Categories;
import com.socialbuy.firstcopy.pojo.FactoryProducts;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FactoryShop extends Fragment {

    private TextView tvFactoryShopHeader,tvCategoriesHeader,tvLatestPostHeader;

    private RecyclerView rvCategories1,rvCategories2,rvLatestPost;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    private StaggeredGridLayoutManager sgm;
    private FactoryShopLatestPostAdapter mAdapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Categories> categories  = new ArrayList<>();
    private ArrayList<Categories> categories1 = new ArrayList<>();
    private static final String URL_LATEST_POST = "http://www.firstcopy.co.in/firstcopy/factoryshop_latestpost.php";
    private ArrayList<String> latesPostProductName;
    private ArrayList<String> latestPostsImageUrl;
    private ArrayList<String> latestPostsProductDesc;
    private ArrayList<String> latestPostPrices;
    private ArrayList<String> latestPostSizes;
    private ArrayList<String> latestPostProductLinks;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    // private ProgressDialog dialog;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;



    public static FactoryShop newInstance() {
        FactoryShop fragment = new FactoryShop();
        return fragment;
    }

//    public FactoryShop() {
//        // Required empty public constructor
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_factory_shop,container,false);

        Typeface typefaceRegular = Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Regular.ttf");
        Typeface typeFaceBold = Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Bold.ttf");

        addCategories();
        addCategories1();
        frameLayout = v.findViewById(R.id.frameLayout);
        tvFactoryShopHeader = v.findViewById(R.id.tvFactoryShopHeading);
        tvFactoryShopHeader.setTypeface(typeFaceBold);
        tvCategoriesHeader = v.findViewById(R.id.tvFactoryShopCategoriesHeading);
        tvCategoriesHeader.setText("Browse Categories");
        tvLatestPostHeader = v.findViewById(R.id.tvFactoryShopLatestPostHeader);

        progressBar = v.findViewById(R.id.progressBar1);

        tvFactoryShopHeader.setTypeface(typefaceRegular);
        tvCategoriesHeader.setTypeface(typeFaceBold);
        tvLatestPostHeader.setTypeface(typeFaceBold);

        rvCategories1 = v.findViewById(R.id.rvFactoryShopCategories);
        rvCategories1.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvCategories1.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(getContext(),categories);
        rvCategories1.setAdapter(categoryAdapter);

        rvCategories2 = v.findViewById(R.id.rvFactoryShopCategories1);
        rvCategories2.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvCategories2.setLayoutManager(linearLayoutManager1);
        categoryAdapter = new CategoryAdapter(getContext(),categories1);
        rvCategories2.setAdapter(categoryAdapter);

        rvLatestPost = v.findViewById(R.id.rvFactoryShopLatestPost);
        rvLatestPost.setHasFixedSize(true);
        sgm = new StaggeredGridLayoutManager(2,1);
        rvLatestPost.setLayoutManager(sgm);

        progressBar.setVisibility(View.VISIBLE);

        new FetchLatestPosts().execute("");


        rvCategories1.addOnItemTouchListener(new RecyclerViewItemListener(getContext(), new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SharedPreferences preferences = getContext().getSharedPreferences("CATEGORYPREF",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("CATEGORY",categories.get(position).getCategoryName());
                editor.commit();
                Intent intent1 = new Intent(getContext(), FactoryShopBrowse.class);
               // intent1.putExtra("CATEGORY", categories.get(position).getCategoryName());
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
            }
        }));

        rvCategories2.addOnItemTouchListener(new RecyclerViewItemListener(getContext(), new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPreferences preferences = getContext().getSharedPreferences("CATEGORYPREF",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (categories1.get(position).getCategoryName().equals("Gadgets")){
                    editor.putString("CATEGORY","tech");
                    editor.commit();
                   // intent1.putExtra("CATEGORY", "tech");
                }else {
                    editor.putString("CATEGORY",categories1.get(position).getCategoryName());
                    editor.commit();
                    //intent1.putExtra("CATEGORY", categories1.get(position).getCategoryName());
                }
                Intent intent1 = new Intent(getContext(), FactoryShopBrowse.class);

                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
            }
        }));






        return v;
    }

    private void addCategories(){


        String[] names = new String[] {"Footwear","Eyewear","Watch","Bag"};
        int[] images = new int[] {R.drawable.footwear,R.drawable.eyewear,R.drawable.watch,R.drawable.bag};

        for (int i = 0; i < names.length; i++){
            Categories category = new Categories();
            category.setCategoryName(names[i]);
            category.setCategoryImage(images[i]);

            categories.add(category);
        }



    }

    private void addCategories1(){
        String[] names = new String[] {"Accessories","Clothing"};
        int[] images = new int[] {R.drawable.accessories, R.drawable.clothing};
        for (int i = 0; i < names.length; i++){
            Categories categories = new Categories();
            categories.setCategoryName(names[i]);
            categories.setCategoryImage(images[i]);

            categories1.add(categories);
        }

    }

    private class FetchLatestPosts extends AsyncTask<String,String,String> {

        HttpURLConnection request;
        BufferedReader reader;
        InputStream is;
        String result = "";


        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(URL_LATEST_POST).buildUpon().build();
            try {
                URL latest_posts_url = new URL(uri.toString());
                request = (HttpURLConnection) latest_posts_url.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                is = request.getInputStream();
                if (is == null)
                    return "Network Problem";

                reader = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "/n");
                }

                if (sb.length() == 0)
                    return "Problem while connecting to server";


                result = getDataFromJson(sb.toString());

                return result;









            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (request != null)
                    request.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (s != null) {
                ArrayList<String> allPosts = new ArrayList<>(Arrays.asList(s.split("<")));
                if (!allPosts.isEmpty()){
                    String imageUrls = allPosts.get(0);
                    String productNames = allPosts.get(1);
                    String productDesc = allPosts.get(2);
                    String prices = allPosts.get(3);
                    String sizes = allPosts.get(4);
                    String productLinks = allPosts.get(5);


                    latestPostsImageUrl = new ArrayList<>(Arrays.asList(imageUrls.split(">")));
                    latesPostProductName= new ArrayList<>(Arrays.asList(productNames.split(">")));
                    latestPostsProductDesc = new ArrayList<>(Arrays.asList(productDesc.split(">")));
                    latestPostPrices = new ArrayList<>(Arrays.asList(prices.split(">")));
                    latestPostSizes = new ArrayList<>(Arrays.asList(sizes.split(">")));
                    latestPostProductLinks = new ArrayList<>(Arrays.asList(productLinks.split(">")));

                    //Toast.makeText(getApplicationContext()," " +  latestPostSellerPhoneNumber.size(),Toast.LENGTH_SHORT).show();
                    //latestPostsNumberofDays = new ArrayList<>(Arrays.asList(numberofDays.split(">")));

                    mAdapter = new FactoryShopLatestPostAdapter(getContext(),latestPostsImageUrl,latesPostProductName,latestPostsProductDesc,latestPostPrices,latestPostSizes,latestPostProductLinks);
                    rvLatestPost.setAdapter(mAdapter);
                    progressBar.setVisibility(View.INVISIBLE);


                } else {
                    Snackbar.make(frameLayout, "No Items", Snackbar.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }else {
                Toast.makeText(getContext(),"Unable to connect to our server",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
    }



    private String getDataFromJson(String jsonString)
    {

        String imageLinks = "";
        String productName = "";
        String productDesc = "";
        String price = "";
        String size = "";
        String productLink = "";
        String result = "";


        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 0)
                {
                    imageLinks = imageLinks + jsonObject.getString("product_image");
                    productName = productName + jsonObject.getString("product_name");
                    productDesc = productDesc + jsonObject.getString("product_description");
                    price = price + jsonObject.getString("price");
                    size = size + jsonObject.getString("sizes");
                    productLink = productLink + jsonObject.getString("product_link");


                }
                else
                {
                    imageLinks = imageLinks + ">" + jsonObject.getString("product_image");
                    productName = productName + ">" + jsonObject.getString("product_name");
                    productDesc = productDesc + ">" +  jsonObject.getString("product_description");
                    price = price + ">" + jsonObject.getString("price");
                    size = size + ">" +  jsonObject.getString("sizes");
                    productLink = productLink + ">" +  jsonObject.getString("product_link");


                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result = imageLinks + "<" + productName +  "<" + productDesc + "<" + price + "<" + size + "<" + productLink;
        return result;
    }

//    public static String getDate(long timeInMillis)
//    {
//        // Create a DateFormatter object for displaying date in specified format.
//        String result = "";
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeInMillis * 1000);
//        int month = calendar.get(Calendar.MONTH);
//        int correctedMonth = month + 1;
//        String date = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + correctedMonth + "/" + calendar.get(Calendar.YEAR);
//        long numberOfDays = getNumberofDaysBetweenDates(date);
//        result = date + "<" + String.valueOf(numberOfDays);
//        return date;
//    }
//
//    public static long getNumberofDaysBetweenDates(String originalDateString){
//
//        long days = 0;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        String currentDateString = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            Date currentDate = sdf.parse(currentDateString);
//            Date originalDate = sdf.parse(originalDateString);
//
//            long diff = currentDate.getTime() - originalDate.getTime();
//            long seconds = diff / 1000;
//            long minutes = seconds / 60;
//            long hours = minutes / 60;
//            days = (hours / 24) + 1;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        return days;
//
//    }







}
