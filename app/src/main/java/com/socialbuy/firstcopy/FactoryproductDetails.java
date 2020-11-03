package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socialbuy.firstcopy.adapters.AllAddressesAdapter;
import com.socialbuy.firstcopy.adapters.MyCustomPagerAdapter;
import com.socialbuy.firstcopy.authentication.Signin;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FactoryproductDetails extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivFactoryImage;
    private TextView tvFactoryProductName,tvFactoryProductDesc,tvFactoryProductPrice,tvProductDetailsHeader,tvSizeTitle,tvReturnPolicy;
    private Button btBuyNow;
    private RecyclerView rvSize;
    private LinearLayoutManager linearLayoutManager;
    private SizeAdapter mAdapter;
    private ArrayList<String> sizes,stocks;
    String productName,productImageLink,productDesc,productPrice,productSizes,productLink;
    private int position = -1;
    private String status = "none";
    private SharedPreferences userLoginPref;
    private SharedPreferences.Editor userLoginEditor;
    private FirebaseAnalytics firebaseAnalytics;
    private ArrayList<String> sizeAndStock;
    private ArrayList<String> buyerDetails = new ArrayList<>();
    private ViewPager viewPager;
    private MyCustomPagerAdapter myCustomPagerAdapter;
    private static int currentPage = 0;
    private static int num_of_pages = 0;
    private static final String RANDOM_NAME_URL = "http://www.firstcopy.co.in/firstcopy/getrandomnames.php";
    private PopupWindow popupWindow;
    private Typeface typeface;
    private Typeface typeface1;
    private static final String ADDR_URL = "http://www.firstcopy.co.in/firstcopy/get_address.php";
    private ArrayList<com.socialbuy.firstcopy.pojo.Address> addresses = new ArrayList<>();
    private String email;
    public static final String PRODUCT_PREF = "productpref";
    private String sizeCheck = "";
    private String size;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factoryproduct_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     //   addBuyerDetails();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progressBar = findViewById(R.id.progressBar1);

        SharedPreferences emailPref = getSharedPreferences(Signin.PREF_NAME,Context.MODE_PRIVATE);
        email = emailPref.getString(Signin.PREF_EMAIL,"none");

        SharedPreferences addressPref = getSharedPreferences(AllAddressesAdapter.PREF_ADDRESS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editorAddr = addressPref.edit();
        editorAddr.clear();
        editorAddr.commit();

//            Intent receiver = getIntent();
//            productName = receiver.getStringExtra("NAME");
//            productImageLink = receiver.getStringExtra("IMAGE");
//            productDesc = receiver.getStringExtra("DESC");
//            productPrice = receiver.getStringExtra("PRICE");
//            productSizes = receiver.getStringExtra("SIZES");
//            productLink = receiver.getStringExtra("product_link");
        SharedPreferences productPref = getSharedPreferences("productPref",Context.MODE_PRIVATE);
        productName = productPref.getString("NAME","none");
        productImageLink = productPref.getString("IMAGE","none");
        productDesc = productPref.getString("DESC","none");
        productPrice = productPref.getString("PRICE","none");
        productSizes = productPref.getString("SIZES","none");
        productLink = productPref.getString("product_link","none");


        SharedPreferences preferences = getSharedPreferences("SIZEPREF",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();

        btBuyNow = findViewById(R.id.btBuyNowFactoryProduct);


      //  String dummySize = "onesize,";
        //Toast.makeText(getApplicationContext(),productName,Toast.LENGTH_SHORT).show();
        sizeAndStock = new ArrayList<>(Arrays.asList(productSizes.split(":")));
        if (sizeAndStock.size() == 1){

        }else {
            String sizesString = sizeAndStock.get(0);
            String stocksString = sizeAndStock.get(1);

            sizes = new ArrayList<>(Arrays.asList(sizesString.split(",")));
            stocks = new ArrayList<>(Arrays.asList(stocksString.split(",")));

            ArrayList<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < stocks.size(); i++){
                if (stocks.get(i).equals("0")){
                    indexes.add(i);
                }
            }



            for (int j = sizes.size() - 1; j >= 0; j--){
                int index = sizes.indexOf(sizes.get(j));
                if (indexes.contains(index)){
                    sizes.remove(index);
                }
            }

            for (String item: sizes){
                Log.i("SIZES",item);
            }

            if (sizes.isEmpty()){
                btBuyNow.setText("Out of Stock");
                btBuyNow.setEnabled(false);
            }else {
                btBuyNow.setEnabled(true);
            }

        }










//
        ivFactoryImage = findViewById(R.id.ivFactoryImageDetails);
        tvFactoryProductName = findViewById(R.id.tvFactoryNameDetails);
        tvFactoryProductDesc = findViewById(R.id.tvFactoryDescDetails);
        tvFactoryProductPrice = findViewById(R.id.tvFactoryProductPrice);
        tvProductDetailsHeader = findViewById(R.id.tvProductDetailsHeader);
        tvSizeTitle = findViewById(R.id.tvSizeTitle);
        tvReturnPolicy = findViewById(R.id.tvReturnPolicy);


         typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
         typeface1 = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");

        tvFactoryProductName.setTypeface(typeface);
        tvFactoryProductPrice.setTypeface(typeface1);
        btBuyNow.setTypeface(typeface);
        tvFactoryProductDesc.setTypeface(typeface);
        tvProductDetailsHeader.setTypeface(typeface1);
        tvSizeTitle.setTypeface(typeface1);
        tvReturnPolicy.setTypeface(typeface);
        tvReturnPolicy.setOnClickListener(this);

        tvFactoryProductName.setText(productName);
        String rupeeSymbol = getResources().getString(R.string.Rs);
        tvFactoryProductPrice.setText( rupeeSymbol + productPrice);
        tvFactoryProductDesc.setText(productDesc);

        Picasso.with(FactoryproductDetails.this).load(productImageLink).
                config(Bitmap.Config.RGB_565).
                placeholder(R.drawable.stub).
                error(R.mipmap.ic_launcher).into(ivFactoryImage);

        rvSize = findViewById(R.id.rvSize);

        if (sizeAndStock.size() == 1){
            tvSizeTitle.setVisibility(View.INVISIBLE);
            rvSize.setVisibility(View.INVISIBLE);
        }else {


            rvSize.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(FactoryproductDetails.this, LinearLayoutManager.HORIZONTAL, false);
            rvSize.setLayoutManager(linearLayoutManager);
            mAdapter = new SizeAdapter(FactoryproductDetails.this, sizes, rvSize);
            rvSize.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }


//        rvSize.addOnItemTouchListener(new RecyclerViewItemListener(FactoryproductDetails.this, new RecyclerViewItemListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                for (int i = 0; i < rvSize.getChildCount(); i++){
//                    rvSize.getChildAt(i)
//                }
//            }
//        }));

        btBuyNow.setOnClickListener(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,String.valueOf(btBuyNow.getId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Buy Now Factory");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

        progressBar.setVisibility(View.VISIBLE);
        new GetRandomNames().execute("");






    }

//    private void addBuyerDetails(){
//        buyerDetails.add("Sameer bought the Product 23 min ago");
//        buyerDetails.add("Ayush bought the Product 2 min ago");
//        buyerDetails.add("Kushan bought the Product 12 min ago");
//        buyerDetails.add("Jon Snow bought the Product 4 min ago");
//    }

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
    protected void onResume() {
        super.onResume();
        onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btBuyNowFactoryProduct:

                SharedPreferences preferences1 = getSharedPreferences(Signin.PREF_NAME,Context.MODE_PRIVATE);
                String email = preferences1.getString(Signin.PREF_EMAIL,"none");

                if (email.equals("none")){

                    Intent intent = new Intent(FactoryproductDetails.this,ProfileActivity.class);
                    //intent.putExtra("STATUS","1");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {

                    if (sizeAndStock.size() == 1){
                        progressBar.setVisibility(View.VISIBLE);
                        new GetShippingAddress().execute("1");

                    }else {
                        SharedPreferences preferences = getSharedPreferences("SIZEPREF", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor;
                        size = preferences.getString("SIZE", "none");
                        if (size.equals("none")) {
                            Toast.makeText(getApplicationContext(), "Please select a size first", Toast.LENGTH_SHORT).show();
                        } else {


                            // Toast.makeText(getApplicationContext(),"" + position,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(),size,Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                            new GetShippingAddress().execute("2");


                        }
                    }



                }

                break;
            case R.id.tvReturnPolicy:

                View returnPolicyView = LayoutInflater.from(FactoryproductDetails.this).inflate(R.layout.custom_returnpolicy,null,false);
                ScrollView scrollView = returnPolicyView.findViewById(R.id.svReturnPolicy);

                TextView tvreturnPolicyHeading,tvReturnPolicyContent;
                final ImageButton ibCloseWindow;

                tvreturnPolicyHeading = returnPolicyView.findViewById(R.id.tvReturnPolicyHeading);
                tvReturnPolicyContent = returnPolicyView.findViewById(R.id.tvReturnPolicyContent);
                ibCloseWindow = returnPolicyView.findViewById(R.id.ibCloseReturnPolicy);

                tvreturnPolicyHeading.setTypeface(typeface1);
                tvReturnPolicyContent.setTypeface(typeface);

                popupWindow = new PopupWindow(returnPolicyView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setAnimationStyle(R.style.AnimationPopup);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAtLocation(scrollView, Gravity.CENTER, 0, 0);

                ibCloseWindow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });


                break;
        }
    }


    public class GetRandomNames extends AsyncTask<String,String,String>{
        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(RANDOM_NAME_URL).buildUpon().build();
            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder sb = new StringBuilder();


                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");


                result = getRandomNamesFromJson(sb.toString());











            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null)
                    connection.disconnect();
                if (reader != null){
                    try {
                        reader.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null){
                if (s.equals("404")){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"There was a problem while connecting to the server",Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.GONE);
                    ArrayList<String> allDetails = new ArrayList<>(Arrays.asList(s.split("<")));
                    if (allDetails.size() == 2){
                        String firstNames = allDetails.get(0);
                        String lastNames = allDetails.get(1);

                        ArrayList<String> firstNameList = new ArrayList<>(Arrays.asList(firstNames.split(">")));
                        ArrayList<String> lastNameList = new ArrayList<>(Arrays.asList(lastNames.split(">")));

                        ArrayList<Integer> timeInMinutes = getRandomTimeinMinutes();

                        viewPager = findViewById(R.id.viewPagerBuyers);
                        myCustomPagerAdapter = new MyCustomPagerAdapter(FactoryproductDetails.this,firstNameList,lastNameList,timeInMinutes);
                        viewPager.setAdapter(myCustomPagerAdapter);

                        num_of_pages = firstNameList.size();
                        final Handler handler = new Handler();
                        final Runnable update = new Runnable() {
                            @Override
                            public void run() {
                                if (currentPage == num_of_pages)
                                    currentPage = 0;
                                viewPager.setCurrentItem(currentPage++,true);
                            }
                        };

                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(update);
                            }
                        },3000 , 3000);


                    }
                }
            }


        }
    }


    private ArrayList<Integer> getRandomTimeinMinutes(){
        Random random = new Random();
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            times.add(i,random.nextInt(100) + 1);
        }

        return times;
    }

    private String getRandomNamesFromJson(String jsonString){
        String res = "";
        String firstname = "",lastName="";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
            return res;
        } catch (JSONException e) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (i == 0){
                        firstname = firstname + jsonObject.getString("firstname");
                        lastName = lastName + jsonObject.getString("lastname");
                    }else {
                        firstname = firstname + ">" + jsonObject.getString("firstname");
                        lastName = lastName + ">" + jsonObject.getString("lastname");
                    }


                }

                res = firstname + "<" + lastName;

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        return res;

    }
    class GetShippingAddress extends AsyncTask<String, String, ArrayList<com.socialbuy.firstcopy.pojo.Address>> {

        HttpURLConnection request;
        BufferedReader reader;
        InputStream inputStream;
        ArrayList<com.socialbuy.firstcopy.pojo.Address> result = new ArrayList<>();

        @Override
        protected ArrayList<com.socialbuy.firstcopy.pojo.Address> doInBackground(String... strings) {

            Uri uri = Uri.parse(ADDR_URL).buildUpon().appendQueryParameter("email", email).build();
            try {
                URL url = new URL(uri.toString());
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                inputStream = request.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";


                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                sizeCheck = strings[0];


                result = getDataFromJson(sb.toString());



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<com.socialbuy.firstcopy.pojo.Address> s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(), s.get(0).getCity(), Toast.LENGTH_SHORT).show();
            if (s != null){
                if (s.size() > 0){
                    if (s.get(0).getAddressLine().equals("none")){
                        progressBar.setVisibility(View.GONE);
                        SharedPreferences productPreferences = getSharedPreferences(PRODUCT_PREF,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = productPreferences.edit();
                        editor.putString("PRICE",productPrice);
                        editor.putString("PRODUCTLINK",productLink);
                        editor.putString("PRODUCTNAME",productName);
                        editor.putString("PRODUCTIMAGE",productImageLink);
                        if (sizeCheck.equals("1"))
                            editor.putString("PRODUCT_SIZE","onesize");
                        else if (sizeCheck.equals("2"))
                            editor.putString("PRODUCT_SIZE",size);
                        editor.commit();
                        Intent intent = new Intent(FactoryproductDetails.this, Address.class);
//                        intent.putExtra("PRICE", productPrice);
//                        intent.putExtra("PRODUCTLINK", productLink);
//                        intent.putExtra("PRODUCTNAME", productName);
//                        intent.putExtra("PRODUCTIMAGE",productImageLink);
//                        intent.putExtra("PRODUCT_SIZE","onesize");
                        startActivity(intent);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        SharedPreferences productPreferences = getSharedPreferences(PRODUCT_PREF,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = productPreferences.edit();
                        editor.putString("PRICE",productPrice);
                        editor.putString("PRODUCTLINK",productLink);
                        editor.putString("PRODUCTNAME",productName);
                        editor.putString("PRODUCTIMAGE",productImageLink);
                        if (sizeCheck.equals("1"))
                            editor.putString("PRODUCT_SIZE","onesize");
                        else if (sizeCheck.equals("2"))
                            editor.putString("PRODUCT_SIZE",size);
                        editor.commit();
                        Intent intent = new Intent(FactoryproductDetails.this, OrderSummaryandCheckout.class);
//                        intent.putExtra("PRICE", productPrice);
//                        intent.putExtra("PRODUCTLINK", productLink);
//                        intent.putExtra("PRODUCTNAME", productName);
//                        intent.putExtra("PRODUCTIMAGE",productImageLink);
//                        intent.putExtra("PRODUCT_SIZE","onesize");
                        startActivity(intent);
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Problem while connecting with our server",Toast.LENGTH_SHORT).show();
                }
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Problem while connecting with our server",Toast.LENGTH_SHORT).show();
            }


        }
    }

    private ArrayList<com.socialbuy.firstcopy.pojo.Address> getDataFromJson(String jsonString) {
        String res;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
            if (res.equals("404")) {
                com.socialbuy.firstcopy.pojo.Address address = new com.socialbuy.firstcopy.pojo.Address();
                address.setFullName("none");
                address.setAddressLine("none");
                address.setCity("none");
                address.setState("none");
                address.setPincode("none");
                address.setPhoneNumber("none");

                addresses.add(address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.i("JSONEXCEPTION", e.toString());
            if (e.toString().startsWith("org.json.JSONException")) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        com.socialbuy.firstcopy.pojo.Address address = new com.socialbuy.firstcopy.pojo.Address();
                        address.setFullName(object.getString("fullname"));
                        address.setAddressLine(object.getString("address"));
                        address.setCity(object.getString("city"));
                        address.setState(object.getString("state"));
                        address.setPincode(object.getString("pincode"));
                        address.setPhoneNumber(object.getString("phone_number"));

                        addresses.add(address);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }


        return addresses;
    }

}
