package com.socialbuy.firstcopy.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.Browse;
import com.socialbuy.firstcopy.CategoryAdapter;
import com.socialbuy.firstcopy.LatestPostAdapter;
import com.socialbuy.firstcopy.R;
import com.socialbuy.firstcopy.RecyclerViewItemListener;
import com.socialbuy.firstcopy.SearchActivity;
import com.socialbuy.firstcopy.pojo.Categories;

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
public class Instashop extends Fragment implements View.OnClickListener {


    private Button btSearch;
    private RecyclerView rvCategories,rvLatestPosts,rvCategories1;
    private TextView tvCategoryHeader,tvLatestPostHeader;
    private LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    private StaggeredGridLayoutManager sgm;
    private LatestPostAdapter mAdapter;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Categories> categories  = new ArrayList<>();
    private ArrayList<Categories> categories1 = new ArrayList<>();
    private static final String URL_LATEST_POST = "http://www.firstcopy.co.in/firstcopy/latest_posts.php";
    private ArrayList<String> latestPostsUsername;
    private ArrayList<String> latestPostsImageUrl;
    private ArrayList<String> latestPostsProfileLink;
    private ArrayList<String> latestPostsProductDesc;
    private ArrayList<String> latestPostsDateofCreation;
    private ArrayList<String> latestPostsNumberofDays;
    private ArrayList<String> latesPostSellerBio,latestPostSellerPhoneNumber;
    private CollapsingToolbarLayout collapsingToolbarLayout;
   // private ProgressDialog dialog;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;


    public static Instashop newInstance() {
        Instashop fragment = new Instashop();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_instashop,container,false);

        addCategories();
        addCategories1();
        frameLayout = v.findViewById(R.id.frameLayout);
        btSearch = v.findViewById(R.id.btgoToSearch);
        tvCategoryHeader = v.findViewById(R.id.tvCategoriesHeading);
        tvLatestPostHeader = v.findViewById(R.id.tvLatestPostHeader);
        progressBar = v.findViewById(R.id.progressBar1);
        rvCategories = v.findViewById(R.id.rvCategories);
        rvCategories.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvCategories.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(getContext(),categories);
        rvCategories.setAdapter(categoryAdapter);


        rvCategories1 = v.findViewById(R.id.rvCategories1);
        rvCategories1.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvCategories1.setLayoutManager(linearLayoutManager1);

        categoryAdapter = new CategoryAdapter(getContext(),categories1);
        rvCategories1.setAdapter(categoryAdapter);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Regular.ttf");


      //  tvCategoryHeader.setPaintFlags(tvCategoryHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvCategoryHeader.setText("Browse Categories");
        tvCategoryHeader.setTypeface(typeface);
        btSearch.setTypeface(typeface);
        tvLatestPostHeader.setTypeface(typeface);

        rvLatestPosts = v.findViewById(R.id.rvLatestPost);
        rvLatestPosts.setHasFixedSize(true);
        sgm = new StaggeredGridLayoutManager(2,1);
        rvLatestPosts.setLayoutManager(sgm);

        progressBar.setVisibility(View.VISIBLE);


        new FetchLatestPosts().execute("");

        btSearch.setOnClickListener(this);

        rvCategories.addOnItemTouchListener(new RecyclerViewItemListener(getContext(), new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent1 = new Intent(getContext(), Browse.class);
                intent1.putExtra("CATEGORY", categories.get(position).getCategoryName());
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
            }
        }));

        rvCategories1.addOnItemTouchListener(new RecyclerViewItemListener(getContext(), new RecyclerViewItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent1 = new Intent(getContext(), Browse.class);
                if (categories1.get(position).getCategoryName().equals("Gadgets")){
                    intent1.putExtra("CATEGORY", "tech");
                }else {
                    intent1.putExtra("CATEGORY", categories1.get(position).getCategoryName());
                }

                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
            }
        }));








        return v;
    }



    private void addCategories(){


        String[] names = new String[] {"Footwear","Eyewear","Bag","Watch"};
        int[] images = new int[] {R.drawable.footwear,R.drawable.eyewear,R.drawable.bag,R.drawable.watch};

        for (int i = 0; i < names.length; i++){
            Categories category = new Categories();
            category.setCategoryName(names[i]);
            category.setCategoryImage(images[i]);

            categories.add(category);
        }



    }

    private void addCategories1(){
        String[] names = new String[] {"Accessories","Grooming","Clothing","Gadgets"};
        int[] images = new int[] {R.drawable.accessories, R.drawable.grooming, R.drawable.clothing,R.drawable.gadgets};
        for (int i = 0; i < names.length; i++){
            Categories categories = new Categories();
            categories.setCategoryName(names[i]);
            categories.setCategoryImage(images[i]);

            categories1.add(categories);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(getContext(),SearchActivity.class);
        startActivity(intent1);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
    }

    private class FetchLatestPosts extends AsyncTask<String,String,String>{

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
                    String usernames = allPosts.get(1);
                    String profileLinks = allPosts.get(2);
                    String productDesc = allPosts.get(3);
                    String dates = allPosts.get(4);
                    String sellerBios = allPosts.get(5);
                    String sellerPhoneNumbers = allPosts.get(6);
                    // String numberofDays = allPosts.get(5);

                    latestPostsImageUrl = new ArrayList<>(Arrays.asList(imageUrls.split(">")));
                    latestPostsUsername= new ArrayList<>(Arrays.asList(usernames.split(">")));
                    latestPostsProfileLink = new ArrayList<>(Arrays.asList(profileLinks.split(">")));
                    latestPostsProductDesc = new ArrayList<>(Arrays.asList(productDesc.split(">")));
                    latestPostsDateofCreation = new ArrayList<>(Arrays.asList(dates.split(">")));
                    latesPostSellerBio = new ArrayList<>(Arrays.asList(sellerBios.split(">")));

                    latestPostSellerPhoneNumber = new ArrayList<>(Arrays.asList(sellerPhoneNumbers.split(">")));
                    //Toast.makeText(getApplicationContext()," " +  latestPostSellerPhoneNumber.size(),Toast.LENGTH_SHORT).show();
                    //latestPostsNumberofDays = new ArrayList<>(Arrays.asList(numberofDays.split(">")));

                    mAdapter = new LatestPostAdapter(getContext(),latestPostsImageUrl,latestPostsUsername,latestPostsProfileLink,latestPostsProductDesc,latestPostsDateofCreation,latesPostSellerBio,latestPostSellerPhoneNumber);
                    rvLatestPosts.setAdapter(mAdapter);
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
        String userName = "";
        String profileLink = "";
        String productDesc = "";
        String dateOfCreation = "";
        String sellerBio = "";
        String sellerPhoneNumber = "";
        // String numberOfdays = "";
        String result = "";


        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 0)
                {
                    imageLinks = imageLinks + jsonObject.getString("image_link");
                    userName = userName + jsonObject.getString("user_name");
                    profileLink = profileLink + jsonObject.getString("profile_link");
                    productDesc = productDesc + jsonObject.getString("product_desc");
                    String creation_time = jsonObject.getString("creation_time");
                    dateOfCreation = dateOfCreation + getDate(Long.parseLong(creation_time));
                    sellerBio = sellerBio + jsonObject.getString("biography");
                    sellerPhoneNumber = sellerPhoneNumber + jsonObject.getString("phone_number");

                }
                else
                {
                    imageLinks = imageLinks + ">" + jsonObject.getString("image_link");
                    userName = userName + ">" + jsonObject.getString("user_name");
                    profileLink = profileLink + ">" +  jsonObject.getString("profile_link");
                    productDesc = productDesc + ">" +  jsonObject.getString("product_desc");
                    String creation_time = jsonObject.getString("creation_time");
                    dateOfCreation = dateOfCreation + ">" +  getDate(Long.parseLong(creation_time));
                    sellerBio = sellerBio + ">" +  jsonObject.getString("biography");
                    sellerPhoneNumber = sellerPhoneNumber + ">" + jsonObject.getString("phone_number");


                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result = imageLinks + "<" + userName + "<"  + profileLink + "<" + productDesc + "<" + dateOfCreation + "<" + sellerBio + "<" + sellerPhoneNumber;
        return result;
    }

    public static String getDate(long timeInMillis)
    {
        // Create a DateFormatter object for displaying date in specified format.
        String result = "";
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis * 1000);
        int month = calendar.get(Calendar.MONTH);
        int correctedMonth = month + 1;
        String date = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + correctedMonth + "/" + calendar.get(Calendar.YEAR);
        long numberOfDays = getNumberofDaysBetweenDates(date);
        result = date + "<" + String.valueOf(numberOfDays);
        return date;
    }

    public static long getNumberofDaysBetweenDates(String originalDateString){

        long days = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String currentDateString = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = sdf.parse(currentDateString);
            Date originalDate = sdf.parse(originalDateString);

            long diff = currentDate.getTime() - originalDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = (hours / 24) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;

    }


}
