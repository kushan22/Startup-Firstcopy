package com.socialbuy.firstcopy.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.socialbuy.firstcopy.Browse;
import com.socialbuy.firstcopy.Config;
import com.socialbuy.firstcopy.FactoryAdapter;
import com.socialbuy.firstcopy.HidingScrollListener;
import com.socialbuy.firstcopy.ProductBrowseAdapter;
import com.socialbuy.firstcopy.Products;
import com.socialbuy.firstcopy.R;
import com.socialbuy.firstcopy.pojo.FactoryProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Men extends Fragment {

    private String category;
    private RequestQueue requestQueue;
    private int requestCount = 1;
    private ArrayList<FactoryProducts> productses;
    private FactoryAdapter mAdapter;
    private RecyclerView rv;
    private StaggeredGridLayoutManager sgm;
    private int lastVisibleItem;
    private TextView tv;
    private ProgressBar progressBar;
    int i = 1;
    private FloatingActionButton fab;
    private View v;


    public Men() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_men,container,false);


        SharedPreferences preferences = getContext().getSharedPreferences("CATEGORYPREF", Context.MODE_PRIVATE);
        category = preferences.getString("CATEGORY","none");
       // Toast.makeText(getContext(),category,Toast.LENGTH_SHORT).show();

        Typeface typeface;
        typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Raleway-Regular.ttf");


        tv = (TextView) v.findViewById(R.id.tvFactoryShopBrowseIntro);
        tv.setText("You are Browsing " + category);
        tv.setTypeface(typeface);


        progressBar = v.findViewById(R.id.progressBar1);


        getAllData();










        return v;
    }



    private void getAllData(){




            progressBar.setVisibility(View.VISIBLE);
            rv = (RecyclerView) v.findViewById(R.id.rvFactoryShopBrowse);
            rv.setHasFixedSize(true);
            sgm = new StaggeredGridLayoutManager(2,1);
            rv.setLayoutManager(sgm);

            fab = (FloatingActionButton) v.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sgm != null)
                        sgm.scrollToPositionWithOffset(0,0);
                }
            });

            productses = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(getContext());

            getData();

            mAdapter = new FactoryAdapter(getContext(),productses);
            rv.setAdapter(mAdapter);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView != null) {

                        if (isLastItemDisplaying(recyclerView)) {
                            //Toast.makeText(HomeScreen.this,"Displaying",Toast.LENGTH_SHORT).show();
                            getData();
                        }
                    }
                }
            });
            rv.setOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    hideViews();
                }
                @Override
                public void onShow() {
                    showViews();
                }
            });



    }

    private void hideViews(){

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = params.bottomMargin;
        fab.animate().translationY(fab.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();



    }

    private void showViews(){
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        //  final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        //  progressBar.setVisibility(View.VISIBLE);
        //setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        // Log.i("URL",Config.DATA_URL + category + "&page=" +   String.valueOf(requestCount));
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.FACTORYSHOP_BROWSEURL + category + "&page=" +   String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("RESPONSE_JSON",response.toString());
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        //   progressBar.setVisibility(View.GONE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.toString().startsWith("com.android.volley.NoConnectionError")){
                            Toast.makeText(getContext(),"Could not connect to our server",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if (error.toString().startsWith("com.android.volley.ParseError")) {
                            if (i == 1) {
                                Toast.makeText(getContext(),"No More Products",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            i++;

                        }

                        // progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        //  Log.i("ERROR_VALUE",error.toString());
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            FactoryProducts superHero = new FactoryProducts();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);


                //Adding data to the superhero object
                superHero.setFactoryProductName(json.getString(Config.TAG_FACTORYPRODUCT_NAME));
                superHero.setFactoryProductImageLink(json.getString(Config.TAG_FACTORYPRODUCT_IMAGELINK));
                superHero.setFactoryProductLink(json.getString(Config.TAG_FACTORYPRODUCT_LINK));
                superHero.setFactoryProductDesc(json.getString(Config.TAG_FACTORYPRODUCT_DESC));
                superHero.setFactoryProductPrice(json.getString(Config.TAG_FACTORYPRODUCT_PRICE));
                superHero.setFactoryProductSizes(json.getString(Config.TAG_FACTORYPRODUCT_SIZES));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            productses.add(superHero);
        }

        //Notifying the adapter that data has been added or changed
        mAdapter.notifyDataSetChanged();
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {

        lastVisibleItem = 0;

        if (recyclerView.getAdapter().getItemCount() != 0) {
            int[] lastVisibleItemPosition = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPositions(new int[2]);
            if (sgm.getSpanCount() == 1){
                lastVisibleItem = lastVisibleItemPosition[0];
            }else if (sgm.getSpanCount() == 2)
                lastVisibleItem = Math.max(lastVisibleItemPosition[0],lastVisibleItemPosition[1]);

            if (lastVisibleItem != recyclerView.NO_POSITION && lastVisibleItem == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }






        return false;

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
//        long numberofDays = getNumberofDaysBetweenDates(date);
//        result = date + "<" + String.valueOf(numberofDays);
//        return result;
//    }
//
//    public static long getNumberofDaysBetweenDates(String originalDateString){
//
//        long days = 0;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        int month = calendar.get(Calendar.MONTH);
//        int correctedMonth = month + 1;
//        String currentDateString = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + correctedMonth + "/" + calendar.get(Calendar.YEAR);
//        //     Log.i("CURRENT_DATE",currentDateString);
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
