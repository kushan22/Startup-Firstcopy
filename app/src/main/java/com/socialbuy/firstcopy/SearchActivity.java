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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnFocusChangeListener, View.OnClickListener {

    private SearchView sv;

    private RequestQueue requestQueue;
    private int requestCount = 1;
    private String trimmedQuery;
    private ArrayList<Products> productses;
    private ProductAdapter mAdapter;
    private RecyclerView rv;
    private StaggeredGridLayoutManager sgm;
    //private boolean loading = true;
    private FloatingActionButton fab;
    private int lastVisibleItem;
    private String receivedQuery = "";
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12;
    private ImageButton ib1;
    private Button bt1,btFilter;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private PopupWindow popupWindow;
    private boolean isFilter = false;


    int i = 1;
    private ArrayList<String> list = new ArrayList<>();
    private String allFilters;
    private String myFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receiver = getIntent();
        if (receiver.getExtras() != null){
//            receivedQuery = receiver.getStringExtra("QUERY");
            isFilter = receiver.getBooleanExtra("FILTER",false);
            trimmedQuery = receiver.getStringExtra("QUERY");
            myFilters = receiver.getStringExtra("MYFILTER");
           // Toast.makeText(getApplicationContext(),trimmedQuery,Toast.LENGTH_SHORT).show();

        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout1);

        tv1 = (TextView) findViewById(R.id.tvTrendingSearches);
        tv2 = (TextView) findViewById(R.id.tvSuperstar);
        tv3 = (TextView) findViewById(R.id.tvAdidas);
        tv4 = (TextView) findViewById(R.id.tvJordan);
        tv5 = (TextView) findViewById(R.id.tvNike);
        tv6 = (TextView) findViewById(R.id.tvRado);
        tv7 = (TextView) findViewById(R.id.tvRayban);
        tv8 = (TextView) findViewById(R.id.tvJbl);
        tv9 = (TextView) findViewById(R.id.tvGucci);
        tv10 = (TextView) findViewById(R.id.tvRolex);
        tv11= (TextView) findViewById(R.id.tvYeezy);
        tv12 = (TextView) findViewById(R.id.tvNoNetwork);
        progressBar = findViewById(R.id.progressBar1);
        //  tv13 = findViewById(R.id.tvTotalItems);
        ib1 = (ImageButton) findViewById(R.id.ibNoNetwork);
        bt1 = (Button) findViewById(R.id.btRetry);
        btFilter = findViewById(R.id.btFilter);


        Typeface typeface1;
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        tv1.setTypeface(typeface1);
        tv2.setTypeface(typeface1);
        tv3.setTypeface(typeface1);
        tv4.setTypeface(typeface1);
        tv5.setTypeface(typeface1);
        tv6.setTypeface(typeface1);
        tv7.setTypeface(typeface1);
        tv8.setTypeface(typeface1);
        tv9.setTypeface(typeface1);
        tv10.setTypeface(typeface1);
        tv11.setTypeface(typeface1);
        tv12.setTypeface(typeface1);
        bt1.setTypeface(typeface1);
        btFilter.setTypeface(typeface1);

        if (isFilter){
            SharedPreferences sharedPreferences = getSharedPreferences("FILTERPREF",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            ib1.setVisibility(View.INVISIBLE);
            tv12.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            tv3.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
            tv5.setVisibility(View.INVISIBLE);
            tv6.setVisibility(View.INVISIBLE);
            tv7.setVisibility(View.INVISIBLE);
            tv8.setVisibility(View.INVISIBLE);
            tv9.setVisibility(View.INVISIBLE);
            tv10.setVisibility(View.INVISIBLE);
            tv11.setVisibility(View.INVISIBLE);
            btFilter.setVisibility(View.VISIBLE);

            rv = (RecyclerView) findViewById(R.id.rvProducts);
            rv.setHasFixedSize(true);
            sgm = new StaggeredGridLayoutManager(2,1);
            rv.setLayoutManager(sgm);
            rv.setVisibility(View.VISIBLE);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sgm != null)
                        sgm.scrollToPositionWithOffset(0,0);
                }
            });


            productses = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(SearchActivity.this);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView != null) {


//                    int visibleThreshold = 1;
//
//                    int totalItems = sgm.getItemCount();
//                    int[] lastVisibleItems = sgm.findLastCompletelyVisibleItemPositions(new int[sgm.getSpanCount()]);
//
//
//                    if (loading && )



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

//            sv.setQuery(trimmedQuery,false);
            onQueryTextSubmit2(trimmedQuery,true);


        }else {
           // Toast.makeText(getApplicationContext(),String.valueOf(isFilter),Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("FILTERPREF",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            doAllTasks();
        }




       // tv13.setTypeface(typeface1);





        bt1.setOnClickListener(this);
        btFilter.setOnClickListener(this);






    }

    private boolean checkForNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean networkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return networkConnected;
    }

    private void doAllTasks(){

        boolean isConnected = checkForNetwork();
        if (!isConnected){
            ib1.setVisibility(View.VISIBLE);
            tv12.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            tv3.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
            tv5.setVisibility(View.INVISIBLE);
            tv6.setVisibility(View.INVISIBLE);
            tv7.setVisibility(View.INVISIBLE);
            tv8.setVisibility(View.INVISIBLE);
            tv9.setVisibility(View.INVISIBLE);
            tv10.setVisibility(View.INVISIBLE);
            tv11.setVisibility(View.INVISIBLE);
            btFilter.setVisibility(View.INVISIBLE);
//            tv13.setVisibility(View.INVISIBLE);


        }else {

            ib1.setVisibility(View.INVISIBLE);
            tv12.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(View.VISIBLE);
            tv7.setVisibility(View.VISIBLE);
            tv8.setVisibility(View.VISIBLE);
            tv9.setVisibility(View.VISIBLE);
            tv10.setVisibility(View.VISIBLE);
            tv11.setVisibility(View.VISIBLE);
            btFilter.setVisibility(View.INVISIBLE);
//            tv13.setVisibility(View.INVISIBLE);
            rv = (RecyclerView) findViewById(R.id.rvProducts);
            rv.setHasFixedSize(true);
            sgm = new StaggeredGridLayoutManager(2,1);
            rv.setLayoutManager(sgm);
            rv.setVisibility(View.INVISIBLE);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sgm != null)
                        sgm.scrollToPositionWithOffset(0,0);
                }
            });



            productses = new ArrayList<>();
            requestQueue = Volley.newRequestQueue(SearchActivity.this);

//            if (!receivedQuery.equals("")){
//                boolean b = onQueryTextSubmit(receivedQuery);
//                if (sv != null)
//                    sv.setQuery(receivedQuery,false);
//            }

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (recyclerView != null) {


//                    int visibleThreshold = 1;
//
//                    int totalItems = sgm.getItemCount();
//                    int[] lastVisibleItems = sgm.findLastCompletelyVisibleItemPositions(new int[sgm.getSpanCount()]);
//
//
//                    if (loading && )



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

            tv2.setOnClickListener(this); tv3.setOnClickListener(this); tv4.setOnClickListener(this); tv5.setOnClickListener(this); tv6.setOnClickListener(this);
            tv7.setOnClickListener(this); tv8.setOnClickListener(this); tv9.setOnClickListener(this); tv10.setOnClickListener(this);
            tv11.setOnClickListener(this);

        }

    }

    private void hideViews(){

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = params.bottomMargin;
        fab.animate().translationY(fab.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btFilter.getLayoutParams();
        int bottomMargin =  params1.bottomMargin;
        btFilter.animate().translationY(btFilter.getHeight() + bottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();



    }

    private void showViews(){
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        btFilter.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.searchView);
        sv = (SearchView) MenuItemCompat.getActionView(item);
        sv.setQueryHint("Search Here");
        sv.setOnQueryTextListener(this);
        sv.setOnQueryTextFocusChangeListener(this);


        sv.clearFocus();

        return true;



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }





    @Override
    public boolean onQueryTextSubmit(String query) {
        i = 1;
        isFilter = false;
      //  Toast.makeText(getApplicationContext(),"Submitted",Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
      //  Toast.makeText(SearchActivity.this,"Searching...",Toast.LENGTH_SHORT).show();

        if (!productses.isEmpty()){
            productses.clear();
        }
        sgm.scrollToPositionWithOffset(0,0);
        lastVisibleItem = 0;
        requestCount = 1;

        trimmedQuery = query.trim();

        getData();
        rv.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        btFilter.setVisibility(View.VISIBLE);
  //      tv13.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        tv8.setVisibility(View.INVISIBLE);
        tv9.setVisibility(View.INVISIBLE);
        tv10.setVisibility(View.INVISIBLE);
        tv11.setVisibility(View.INVISIBLE);

        mAdapter = new ProductAdapter(SearchActivity.this,productses,trimmedQuery);
        rv.setAdapter(mAdapter);

       // tv13.setText("Items: " +  productses.size());


        return false;
    }

    private void onQueryTextSubmit2(String query,boolean isFromSearch){
        i = 1;
        if (isFromSearch)
            isFilter = true;
     //   Toast.makeText(getApplicationContext(),"Submitted",Toast.LENGTH_SHORT).show();
       progressBar.setVisibility(View.VISIBLE);

        //  Toast.makeText(SearchActivity.this,"Searching...",Toast.LENGTH_SHORT).show();

        if (!productses.isEmpty()){
            productses.clear();
        }
        sgm.scrollToPositionWithOffset(0,0);
        lastVisibleItem = 0;
        requestCount = 1;

        trimmedQuery = query.trim();

        getData();
        rv.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        btFilter.setVisibility(View.VISIBLE);
        //      tv13.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        tv4.setVisibility(View.INVISIBLE);
        tv5.setVisibility(View.INVISIBLE);
        tv6.setVisibility(View.INVISIBLE);
        tv7.setVisibility(View.INVISIBLE);
        tv8.setVisibility(View.INVISIBLE);
        tv9.setVisibility(View.INVISIBLE);
        tv10.setVisibility(View.INVISIBLE);
        tv11.setVisibility(View.INVISIBLE);

        mAdapter = new ProductAdapter(SearchActivity.this,productses,trimmedQuery);
        rv.setAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }


    private JsonArrayRequest getDataFromServer(int requestCount) {


        //Initializing ProgressBar
      //  final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
      //  progressBar.setVisibility(View.VISIBLE);
        //setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
       // Log.i("URL",Config.DATA_URL + trimmedQuery + "&page=" +   String.valueOf(requestCount));
        if (isFilter){
            Log.i("URL",Config.FILTER_URL + trimmedQuery + "&page=" + String.valueOf(requestCount) + "&filter=" + myFilters);
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.FILTER_URL + trimmedQuery + "&page=" + String.valueOf(requestCount) + "&filter=" + myFilters ,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Calling method parseData to parse the json response

                            parseData(response);
                            if (sv != null)
                                sv.clearFocus();
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
                                    Snackbar.make(coordinatorLayout, "No Products Found", Snackbar.LENGTH_LONG).show();
                                    if (sv != null)
                                        sv.clearFocus();
                                    //    Log.i("ERROR_STRING", error.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                i++;
                            } else {

                            }

                            // progressBar.setVisibility(View.GONE);
                            //If an error occurs that means end of the list has reached
                            // Toast.makeText(HomeScreen.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                        }
                    });
            return jsonArrayRequest;
        }else {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + trimmedQuery + "&page=" +   String.valueOf(requestCount),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Calling method parseData to parse the json response
                            parseData(response);
                            if (sv != null)
                                sv.clearFocus();
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
                                    Snackbar.make(coordinatorLayout, "No Products Found", Snackbar.LENGTH_LONG).show();
                                    if (sv != null)
                                        sv.clearFocus();
                                    //    Log.i("ERROR_STRING", error.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                i++;
                            } else {

                            }

                            // progressBar.setVisibility(View.GONE);
                            //If an error occurs that means end of the list has reached
                            // Toast.makeText(HomeScreen.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                        }
                    });
            return jsonArrayRequest;
        }


        //Returning the request

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
            //    Log.i("TIME"," " + timeInMilliSeconds + " : " + System.currentTimeMillis() );
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
       // tv13.setText("Items: " + productses.size());
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

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        String result = "";
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
     //   Log.i("CURRENT_DATE",currentDateString);
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


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btRetry:
                doAllTasks();
                break;

            case R.id.btFilter:
                View view1 = LayoutInflater.from(SearchActivity.this).inflate(R.layout.custom_filter,null,false);
                LinearLayout relativeLayout = view1.findViewById(R.id.rlFilter);
                final ListView lvFilter;
                Button btApplyFilters;
                FilterAdapter filterAdapter;
                ImageButton ibCloseFilter;

                String[] filters = new String[] {"Eyewear","Footwear","Bag","Watch","Accessories","Grooming","Clothing","Gadgets"};

                lvFilter = view1.findViewById(R.id.lvFilter);
                btApplyFilters = view1.findViewById(R.id.btApplyFilters);
                ibCloseFilter = view1.findViewById(R.id.ibCloseFilter);


                filterAdapter = new FilterAdapter(SearchActivity.this,filters);
                lvFilter.setAdapter(filterAdapter);

                popupWindow = new PopupWindow(view1, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setAnimationStyle(R.style.AnimationPopup);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                btApplyFilters.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences preferences = getSharedPreferences("FILTERPREF", Context.MODE_PRIVATE);
                        myFilters = preferences.getString("MYFILTERS","none");
                        Log.i("FILTERS",myFilters);
                        if (myFilters == "none")
                            popupWindow.dismiss();
                        else if (myFilters.isEmpty())
                            popupWindow.dismiss();
                        else {

                            //list.clear();



                            isFilter = true;

                            Intent filterIntent = new Intent(SearchActivity.this,SearchActivity.class);
                            filterIntent.putExtra("FILTER",isFilter);
                            filterIntent.putExtra("QUERY",trimmedQuery);
                            filterIntent.putExtra("MYFILTER",myFilters);
                            SearchActivity.this.finish();
                            startActivity(filterIntent);


                            popupWindow.dismiss();
//                            dialog = new ProgressDialog(SearchActivity.this);
//                            dialog.setTitle("");
//                            dialog.setCancelable(false);
//                            dialog.setMessage("");
//                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dialog.show();
//                            dialog.setContentView(R.layout.progress_dialog);
//
//                            //  Toast.makeText(SearchActivity.this,"Searching...",Toast.LENGTH_SHORT).show();
//
//                            if (!productses.isEmpty()){
//                                productses.clear();
//                            }
//                            sgm.scrollToPositionWithOffset(0,0);
//                            lastVisibleItem = 0;
//                            requestCount = 1;
//                            getData();
//
//                            mAdapter = new ProductAdapter(SearchActivity.this,productses,trimmedQuery);
//                            rv.setAdapter(mAdapter);
//
//                            popupWindow.dismiss();
//
//                            Log.i("FILTERS",myFilters);
                        }

                    }
                });

                ibCloseFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });



                break;

            case R.id.tvSuperstar:

                sv.setQuery(tv2.getText().toString(),false);

                onQueryTextSubmit(tv2.getText().toString());
                break;
            case R.id.tvAdidas:
                sv.setQuery(tv3.getText().toString(),false);

                onQueryTextSubmit(tv3.getText().toString());
                break;
            case R.id.tvJordan:
                sv.setQuery(tv4.getText().toString(),false);

                onQueryTextSubmit(tv4.getText().toString());
                break;
            case R.id.tvNike:
                sv.setQuery(tv5.getText().toString(),false);

                onQueryTextSubmit(tv5.getText().toString());
                break;
            case R.id.tvRado:
                sv.setQuery(tv6.getText().toString(),false);

                onQueryTextSubmit(tv6.getText().toString());
                break;
            case R.id.tvRayban:
                sv.setQuery(tv7.getText().toString(),false);

                onQueryTextSubmit(tv7.getText().toString());
                break;
            case R.id.tvJbl:
                sv.setQuery(tv8.getText().toString(),false);

                onQueryTextSubmit(tv8.getText().toString());
                break;
            case R.id.tvGucci:
                sv.setQuery(tv9.getText().toString(),false);

                onQueryTextSubmit(tv9.getText().toString());
                break;
            case R.id.tvRolex:
                sv.setQuery(tv10.getText().toString(),false);

                onQueryTextSubmit(tv10.getText().toString());
                break;
            case R.id.tvYeezy:
                sv.setQuery(tv11.getText().toString(),false);

                onQueryTextSubmit(tv11.getText().toString());
                break;

        }

    }
}
