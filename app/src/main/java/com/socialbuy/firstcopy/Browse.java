package com.socialbuy.firstcopy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Browse extends AppCompatActivity {

    private String category;
    private RequestQueue requestQueue;
    private int requestCount = 1;
    private ArrayList<Products> productses;
    private ProductBrowseAdapter mAdapter;
    private RecyclerView rv;
    private StaggeredGridLayoutManager sgm;
    private int lastVisibleItem;
    private TextView tv,tv1;
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageButton ib1;
    private Button bt1;
    private ProgressBar progressBar;
    int i = 1;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.clBrowse);

        Intent receiver = getIntent();
      //  Log.i("INTENT_MSG",receiver.toString());
        if (receiver.getExtras() != null){
            category = receiver.getStringExtra("CATEGORY").toLowerCase();
            SharedPreferences sharedPreferences = getSharedPreferences("MYPREF", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("category",category);
            editor.commit();
        }else{

            SharedPreferences preferences = getSharedPreferences("MYPREF",Context.MODE_PRIVATE);
            category = preferences.getString("category","null");
            //Toast.makeText(getApplicationContext(),category,Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor myEditor = preferences.edit();
            myEditor.clear();

        }


        Typeface typeface;
        typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");


        tv = (TextView) findViewById(R.id.tvBrowseIntro);
        tv.setText("You are Browsing " + category);
        tv.setTypeface(typeface);

        tv1 = (TextView) findViewById(R.id.tvNoNetwork);
        tv1.setTypeface(typeface);
        ib1 = (ImageButton) findViewById(R.id.ibNoNetwork);
        bt1 = (Button) findViewById(R.id.btRetry);

        progressBar = findViewById(R.id.progressBar1);


        getAllData();




        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllData();
            }
        });







    }




    private boolean checkForNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean networkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return networkConnected;
    }

    private void getAllData(){
        boolean isConnected = checkForNetwork();
        if (!isConnected){
            tv1.setVisibility(View.VISIBLE);
            ib1.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
            tv.setVisibility(View.INVISIBLE);
        }else {
            tv1.setVisibility(View.INVISIBLE);
            ib1.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.VISIBLE);
            rv = (RecyclerView) findViewById(R.id.rvBrowse);
            rv.setHasFixedSize(true);
            sgm = new StaggeredGridLayoutManager(2,1);
            rv.setLayoutManager(sgm);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sgm != null)
                        sgm.scrollToPositionWithOffset(0,0);
                }
            });

            productses = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(Browse.this);

            getData();

            mAdapter = new ProductBrowseAdapter(Browse.this,productses);
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
    }

    private void hideViews(){

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.BROWSE_URL + category + "&page=" +   String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                 //       Log.i("RESPONSE_JSON",response.toString());
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
                            Snackbar.make(coordinatorLayout,"Could not connect to the Server",Snackbar.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else if (error.toString().startsWith("com.android.volley.ParseError")) {
                            if (i == 1) {
                                Snackbar.make(coordinatorLayout, "No more products", Snackbar.LENGTH_LONG).show();
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
            Products superHero = new Products();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                long timeInMilliSeconds = Long.parseLong(json.getString(Config.TAG_DATE_OF_CREATION));
             //   Log.i("TIME"," " + timeInMilliSeconds + " : " + System.currentTimeMillis() );
                String allResults = getDate(timeInMilliSeconds);
                String dateofCreation = "";
                String numberOfdays = "";
                ArrayList<String> dateandDay = new ArrayList<>(Arrays.asList(allResults.split("<")));
                if (dateandDay.size() > 0){
                    dateofCreation = dateandDay.get(0);
                    numberOfdays = dateandDay.get(1);
                }

                //Adding data to the superhero object
                superHero.setUserName(json.getString(Config.TAG_USER_NAME));
                superHero.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                superHero.setProfileLink(json.getString(Config.TAG_PROFILE_LINK));
                superHero.setProductDesc(json.getString(Config.TAG_PRODUCT_DESC));
                superHero.setDateofcreation(dateofCreation);
                superHero.setNumberofdays(numberOfdays);
                superHero.setSellerBio(json.getString(Config.TAG_SELLER_BIO));
                superHero.setSellerPhoneNumber(json.getString(Config.TAG_PHONE_NUMBER));
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
        long numberofDays = getNumberofDaysBetweenDates(date);
        result = date + "<" + String.valueOf(numberofDays);
        return result;
    }

    public static long getNumberofDaysBetweenDates(String originalDateString){

        long days = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int month = calendar.get(Calendar.MONTH);
        int correctedMonth = month + 1;
        String currentDateString = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + correctedMonth + "/" + calendar.get(Calendar.YEAR);
  //     Log.i("CURRENT_DATE",currentDateString);
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
