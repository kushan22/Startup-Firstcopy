package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.instamojo.android.Instamojo;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.Order;
import com.instamojo.android.network.Request;
import com.socialbuy.firstcopy.adapters.AllAddressesAdapter;
import com.socialbuy.firstcopy.authentication.Signin;
import com.socialbuy.firstcopy.pojo.Address;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class OrderSummaryandCheckout extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBuyerName,tvBuyerAddress,tvBuyerPhoneNumber,tvBuyerPincode;
    private TextView tvProductName,tvProductPrice,tvProductSize;
    private ImageView ivProductImage;
    private Button btChangeorAddAddress;
    private static final String LATEST_ADDR_URL = "http://www.firstcopy.co.in/firstcopy/getLatestAddress.php";
    private String email = "",productName="",productPrice="",productSize="",productImageUrl="",productLink="";
    private ArrayList<com.socialbuy.firstcopy.pojo.Address> addresses = new ArrayList<>();
    private SharedPreferences addressPref;
    private TextView tvToPay,tvSubtotalHeader,tvsubTotal,tvDeliveryCostHeader,tvDeliveryCost,tvTotalHeader,tvTotal;
    private Button btProceedtoCheckout;
    private static final int PERMISSION_REQUESTCODE = 200;
    private static final String PAYMENTGATEWAY_URL = "https://api.instamojo.com/";
    private static final String URLSTRING = "http://www.firstcopy.co.in/firstcopy/getaccesstoken.php";
    private static final String PAYMENT_URL = "http://www.firstcopy.co.in/firstcopy/getpaymentdetails.php";
    private static final String ORDER_URL = "http://www.firstcopy.co.in/firstcopy/addorder.php";
    private String orderName,orderEmail,orderPhone,orderTransactionId,orderPaymentId,orderStatus,orderDateCreated,orderAmount;
    private String accessToken, transactionId;
    Typeface typeface,typeFaceBold;
    private PopupWindow popupWindow1;
    private FirebaseAnalytics firebaseAnalytics;
    private ProgressBar progressBar,progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summaryand_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
        typeFaceBold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");

        Instamojo.setBaseUrl(PAYMENTGATEWAY_URL);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progressBar = findViewById(R.id.progressBar1);
        progressBar1 = findViewById(R.id.progressBar2);

        tvBuyerName = findViewById(R.id.tvBuyerName);
        tvBuyerAddress = findViewById(R.id.tvBuyerAddress);
        tvBuyerPhoneNumber = findViewById(R.id.tvBuyerNumber);
        tvBuyerPincode = findViewById(R.id.tvBuyerPincode);
        tvBuyerName.setTypeface(typeFaceBold);
        tvBuyerAddress.setTypeface(typeface);
        tvBuyerPhoneNumber.setTypeface(typeface);
        tvBuyerPincode.setTypeface(typeface);

        tvProductName = findViewById(R.id.tvProductNameOrderAndSummary);
        tvProductPrice = findViewById(R.id.tvProductPriceOrderandSummary);
        tvProductSize = findViewById(R.id.tvSizeOrderandSummary);

        tvProductName.setTypeface(typeFaceBold);
        tvProductPrice.setTypeface(typeface);
        tvProductSize.setTypeface(typeface);

        tvToPay = findViewById(R.id.tvToPayHeader);
        tvSubtotalHeader = findViewById(R.id.tvSubTotalHeader);
        tvsubTotal = findViewById(R.id.tvSubTotal);
        tvDeliveryCostHeader = findViewById(R.id.tvDeliveryCostHeader);
        tvDeliveryCost = findViewById(R.id.tvDeliveryCost);
        tvTotalHeader = findViewById(R.id.tvTotalHeader);
        tvTotal = findViewById(R.id.tvTotal);

        tvToPay.setTypeface(typeFaceBold);
        tvSubtotalHeader.setTypeface(typeface);
        tvsubTotal.setTypeface(typeface);
        tvDeliveryCostHeader.setTypeface(typeface);
        tvDeliveryCost.setTypeface(typeface);
        tvTotalHeader.setTypeface(typeface);
        tvTotal.setTypeface(typeface);

        btProceedtoCheckout = findViewById(R.id.btProceedtoCheckout);
        btProceedtoCheckout.setTypeface(typeface);

        ivProductImage = findViewById(R.id.ivProductImageOrderandSummary);

        SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
        email = preferences.getString(Signin.PREF_EMAIL,"none");

        SharedPreferences productPreference = getSharedPreferences(FactoryproductDetails.PRODUCT_PREF,Context.MODE_PRIVATE);
        productName = productPreference.getString("PRODUCTNAME","none");
        productPrice = productPreference.getString("PRICE","none");
        productSize = productPreference.getString("PRODUCT_SIZE","none");
        productImageUrl = productPreference.getString("PRODUCTIMAGE","none");
        productLink = productPreference.getString("PRODUCTLINK","none");

        tvProductName.setText(productName);
        tvProductPrice.setText(getResources().getString(R.string.Rs) + productPrice);
        tvProductSize.setText(productSize);

        tvsubTotal.setText(getResources().getString(R.string.Rs) + productPrice);
        tvDeliveryCost.setText(getResources().getString(R.string.Rs) + 0);
        tvTotal.setText(getResources().getString(R.string.Rs) + productPrice);

        Picasso.with(OrderSummaryandCheckout.this).load(productImageUrl).
                config(Bitmap.Config.RGB_565).
                placeholder(R.drawable.stub).
                error(R.mipmap.ic_launcher).into(ivProductImage);


        btChangeorAddAddress = findViewById(R.id.btChangeorAddaddress);

        btChangeorAddAddress.setOnClickListener(this);
        btProceedtoCheckout.setOnClickListener(this);

        addressPref = getSharedPreferences(AllAddressesAdapter.PREF_ADDRESS,Context.MODE_PRIVATE);
        String buyerName = addressPref.getString(AllAddressesAdapter.PREF_NAME,"none");
        String addressLine = addressPref.getString(AllAddressesAdapter.PREF_ADDRESSLINE,"none");
        String phoneNumber = addressPref.getString(AllAddressesAdapter.PREF_NUMBER,"none");
        String pincode = addressPref.getString(AllAddressesAdapter.PREF_PINCODE,"none");

        if (buyerName.equals("none") || addressLine.equals("none") || phoneNumber.equals("none") || pincode.equals("none")){
            //Toast.makeText(getApplicationContext(),buyerName,Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            new GetLatestAddress().execute("");
        }
        else {
            tvBuyerName.setText(buyerName);
            tvBuyerAddress.setText(addressLine);
            tvBuyerPhoneNumber.setText(phoneNumber);
            tvBuyerPincode.setText(pincode);
        }



        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,String.valueOf(btProceedtoCheckout.getId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Checkout");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(OrderSummaryandCheckout.this,FactoryproductDetails.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btChangeorAddaddress:
                Intent intent = new Intent(OrderSummaryandCheckout.this,AllAddresses.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btProceedtoCheckout:
                if (checkPermission()){
                    doOnCheckoutTask();
                }else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(OrderSummaryandCheckout.this);
                    View view = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_checkout,null,false);
                    alertBuilder.setView(view);

                    TextView tvPer = view.findViewById(R.id.tvPermissions);
                    tvPer.setTypeface(typeface);

                    alertBuilder.setCancelable(true).setPositiveButton("View Permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermission();
                        }
                    });


                    AlertDialog dialog = alertBuilder.create();
                    dialog.show();


                }

                break;
        }
    }


    private void doOnCheckoutTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderSummaryandCheckout.this);
        View view = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_payment, null, false);
        builder.setView(view);

        final RadioGroup radioGroup = view.findViewById(R.id.rgPaymentGroup);

        builder.setCancelable(true).setPositiveButton("Checkout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(radioGroup.getCheckedRadioButtonId() == R.id.rbOnline) {
                    progressBar1.setVisibility(View.VISIBLE);
                    new FetchAccessTokenAndTransactionID().execute("");




                }else if (radioGroup.getCheckedRadioButtonId() == R.id.rbCod) {
                    progressBar1.setVisibility(View.VISIBLE);
                    new AddCodOrder().execute("");

                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Select Payment Mode", Toast.LENGTH_SHORT).show();
                }

            }
        });





        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    class FetchAccessTokenAndTransactionID extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String res = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(URLSTRING).buildUpon().build();
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



                res = getTokensFromJson(sb.toString());



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (reader != null)
                        reader.close();
                    if (connection != null)
                        connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                ArrayList<String> allDetails = new ArrayList<>(Arrays.asList(s.split(">")));
                if (allDetails.size() == 2) {
                    accessToken = allDetails.get(0);
                    transactionId = allDetails.get(1);

                    //     Log.i("TOKENS", "ACCESS_TOKEN: " + accessToken);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            initiateOrder(accessToken, transactionId);
                        }
                    };

                    runnable.run();

                }else {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Unable to connect to our server",Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.INVISIBLE);
                }

            }else {
                progressBar1.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Unable to connect to our server",Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.INVISIBLE);
            }

        }
    }

    private String getTokensFromJson(String jsonString){
        String res;
        String accessToken = "",transactionId = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            accessToken = jsonObject.getString("access_token");
            transactionId = jsonObject.getString("transactionid");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        res = accessToken + ">" + transactionId;

        return res;
    }

    private void initiateOrder(String accessToken, String transactionId){

        Order order = new Order(accessToken,transactionId,tvBuyerName.getText().toString(),email,tvBuyerPhoneNumber.getText().toString(),productPrice,"Payment for " + productName);
        //Validate the Order
        if (!order.isValid()){
            //oops order validation failed. Pinpoint the issue(s).

            if (!order.isValidName()){
                Log.e("App", "Buyer name is invalid");
            }

            if (!order.isValidEmail()){
                Log.e("App", "Buyer email is invalid");
            }

            if (!order.isValidPhone()){
                Log.e("App", "Buyer phone is invalid");
            }

            if (!order.isValidAmount()){
                Log.e("App", "Amount is invalid");
            }

            if (!order.isValidDescription()){
                Log.e("App", "description is invalid");
            }

            if (!order.isValidTransactionID()){
                Log.e("App", "Transaction ID is invalid");
            }

            if (!order.isValidRedirectURL()){
                Log.e("App", "Redirection URL is invalid");
            }

            if (!order.isValidWebhook()) {
                Toast.makeText(getApplicationContext(),"Webhook URL is invalid",Toast.LENGTH_SHORT).show();
            }

            return;
        }

        Request request = new Request(order, new OrderRequestCallBack() {
            @Override
            public void onFinish(Order order, Exception error) {
                if (error != null) {
                    if (error instanceof Errors.ConnectionError) {
                        Log.e("App", "No internet connection");
                    } else if (error instanceof Errors.ServerError) {
                        Log.e("App", "Server Error. Try again");
                    } else if (error instanceof Errors.AuthenticationError){
                        Log.e("App", "Access token is invalid or expired");
                    } else if (error instanceof Errors.ValidationError){
                        // Cast object to validation to pinpoint the issue
                        Errors.ValidationError validationError = (Errors.ValidationError) error;
                        if (!validationError.isValidTransactionID()) {
                            Log.e("App", "Transaction ID is not Unique");
                            return;
                        }
                        if (!validationError.isValidRedirectURL()) {
                            Log.e("App", "Redirect url is invalid");
                            return;
                        }


                        if (!validationError.isValidWebhook()) {
                            Toast.makeText(getApplicationContext(),"Webhook URL is invalid",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!validationError.isValidPhone()) {
                            Log.e("App", "Buyer's Phone Number is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidEmail()) {
                            Log.e("App", "Buyer's Email is invalid/empty");
                            return;
                        }
                        if (!validationError.isValidAmount()) {
                            Log.e("App", "Amount is either less than Rs.9 or has more than two decimal places");
                            return;
                        }
                        if (!validationError.isValidName()) {
                            Log.e("App", "Buyer's Name is required");
                            return;
                        }
                    } else {
                        Log.e("App", error.getMessage());
                    }
                    return;
                }

                startPreCreatedUI(order);
            }
        });

        request.execute();

    }

    private void startPreCreatedUI(Order order){
        //Using Pre created UI

        Intent intent = new Intent(getBaseContext(), PaymentDetailsActivity.class);
        intent.putExtra(Constants.ORDER, order);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    class GetPaymentDetails extends AsyncTask<String,String,String> {

        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        String result;

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(PAYMENT_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                outputStream = connection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("access_token", "UTF-8") + "=" + URLEncoder.encode(accessToken, "UTF-8") + "&"
                        + URLEncoder.encode("order_id", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8") + "&"
                        + URLEncoder.encode("transaction_id", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                // Log.i("JSONSTRING",sb.toString());
                result = getPaymentDataFromJson(sb.toString());

                reader.close();
                inputStream.close();
                connection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayList<String> allDetails = new ArrayList<>(Arrays.asList(s.split(">")));
            if (!allDetails.isEmpty()){
                orderName = allDetails.get(0);
                orderEmail = allDetails.get(1);
                orderPhone = allDetails.get(2);
                orderTransactionId = allDetails.get(3);
                orderPaymentId = allDetails.get(4);
                orderStatus = allDetails.get(5);
                orderDateCreated = allDetails.get(6);
                orderAmount = allDetails.get(7);

                if (orderStatus.equals("failed")){
                    Toast.makeText(getApplicationContext(),"Oops! Transaction was cancelled",Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                }else {
                    progressBar1.setVisibility(View.VISIBLE);
                    new AddOrder().execute("");
                }

            }

        }
    }

    private String getPaymentDataFromJson(String jsonString){
        String result = "";

        String name="",email="",phoneNumber="",transactionid="",paymentid = "", status="",datecreated="",amount="";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            name  = jsonObject.getString("name");
            email = jsonObject.getString("email");
            phoneNumber = jsonObject.getString("phone");
            transactionid = jsonObject.getString("transaction_id");
            datecreated = jsonObject.getString("created_at");
            amount = jsonObject.getString("amount");

            JSONArray jsonArray = new JSONArray(jsonObject.getString("payments"));
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            status = jsonObject1.getString("status");
            paymentid = jsonObject1.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result = name + ">" + email + ">" + phoneNumber + ">" + transactionid + ">" + paymentid + ">" + status + ">" + datecreated + ">" + amount;


        return result;
    }


    private class GetLatestAddress extends AsyncTask<String, String, ArrayList<Address>> {
        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader reader;
        ArrayList<Address> result;

        @Override
        protected ArrayList<Address> doInBackground(String... strings) {

            Uri uri = Uri.parse(LATEST_ADDR_URL).buildUpon().appendQueryParameter("email", email).build();
            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";


                while ((line = reader.readLine()) != null)
                    sb.append(line + "\n");


                result = getLatestAddressFromJson(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Address> s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.size() > 0) {
                    if (s.get(0).getAddressLine().equals("none")){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Problem while fetching the address",Toast.LENGTH_SHORT).show();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        tvBuyerName.setText(s.get(0).getFullName());
                        tvBuyerAddress.setText(s.get(0).getAddressLine() + "city :" + s.get(0).getCity() + " State: " + s.get(0).getState());
                        tvBuyerPhoneNumber.setText(s.get(0).getPhoneNumber());
                        tvBuyerPincode.setText(s.get(0).getPincode());
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Problem while connecting to the server",Toast.LENGTH_SHORT).show();
                }
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Problem while connecting to the server",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<Address> getLatestAddressFromJson(String jsonString){
        String res;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
            if (res.equals("404")) {
                Address address = new Address();
                address.setFullName("none");
                address.setAddressLine("none");
                address.setCity("none");
                address.setState("none");
                address.setPincode("none");
                address.setPhoneNumber("none");

                addresses.add(address);
            }
        } catch (JSONException e) {
          //  e.printStackTrace();
            if (e.toString().startsWith("org.json.JSONException")) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Address address = new Address();
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

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(OrderSummaryandCheckout.this,"android.permission.ACCESS_NETWORK_STATE");
        int result2 = ContextCompat.checkSelfPermission(OrderSummaryandCheckout.this,"android.permission.READ_PHONE_STATE");
        int result3 = ContextCompat.checkSelfPermission(OrderSummaryandCheckout.this,"android.permission.READ_SMS");
        int result4 = ContextCompat.checkSelfPermission(OrderSummaryandCheckout.this,"android.permission.RECEIVE_SMS");
        //int result5 = ContextCompat.checkSelfPermission(Shippinginfo.this,"android.permission.ACCESS_FINE_LOCATION");

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;



    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(OrderSummaryandCheckout.this,new String[]{"android.permission.ACCESS_NETWORK_STATE","android.permission.READ_PHONE_STATE","android.permission.READ_SMS","android.permission.RECEIVE_SMS","android.permission.ACCESS_FINE_LOCATION"},PERMISSION_REQUESTCODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if (grantResults.length > 0){
                    boolean networkPer = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean phonestatePer = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readsmsPer = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean receivesmsPer= grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    //boolean locationPer = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (networkPer && phonestatePer && readsmsPer && receivesmsPer){
                        // Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                        doOnCheckoutTask();

                    }else {
                        //Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();

                        if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")){
                            showMessageOKCancel("You need to allow access to both the permission", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"},PERMISSION_REQUESTCODE);
                                }
                            });
                            finish();

                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(OrderSummaryandCheckout.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && data != null) {
            String orderID = data.getStringExtra(Constants.ORDER_ID);
            String transactionID = data.getStringExtra(Constants.TRANSACTION_ID);
            String paymentID = data.getStringExtra(Constants.PAYMENT_ID);

            // Check transactionID, orderID, and orderID for null before using them to check the Payment status.
            if (orderID != null && transactionID != null && paymentID != null) {
                //Check for Payment status with Order ID or Transaction ID
                //   Log.i("DETAILSPAYMENT","ORDERID " + orderID + " TRANSID " + transactionID + "PAYMENTID " + paymentID);
                progressBar1.setVisibility(View.VISIBLE);
                new GetPaymentDetails().execute(orderID,transactionID);

            } else {
                //Oops!! Payment was cancelled
                progressBar1.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Oops Payment was cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AddOrder extends AsyncTask<String,String,String> {
        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result;

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(ORDER_URL).buildUpon().appendQueryParameter("orderName", orderName).
                    appendQueryParameter("orderEmail", orderEmail).appendQueryParameter("orderPhone", orderPhone).
                    appendQueryParameter("orderTransactionId", orderTransactionId).
                    appendQueryParameter("orderPaymentId", orderPaymentId).
                    appendQueryParameter("orderStatus", orderStatus).appendQueryParameter("orderSize", productSize).
                    appendQueryParameter("orderDateCreated", orderDateCreated).appendQueryParameter("productName", productName).
                    appendQueryParameter("productLink", productLink).appendQueryParameter("productImage", productImageUrl).
                    appendQueryParameter("address", tvBuyerAddress.getText().toString() + " Pincode " + tvBuyerPincode.getText().toString()).
                    appendQueryParameter("price", orderAmount).build();

            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";


                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }


                result = getOrderStatusFromJson(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("200")){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Order is Successfull",Toast.LENGTH_SHORT).show();
                View addView = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_order, null, false);

                RelativeLayout relativeLayout = addView.findViewById(R.id.relativeLayoutOrder);
                ImageButton ib2,ib3;
                TextView tv1;
                Button btHome;


                ib2 = addView.findViewById(R.id.ibOrderDone);
                ib3 = addView.findViewById(R.id.ibOrderNotdone);
                tv1 = addView.findViewById(R.id.tvOrderStatus);
                btHome = addView.findViewById(R.id.btGotoHome);

                tv1.setTypeface(typeface);
                tv1.setText("Hurray! Order is successfully placed");
                btHome.setTypeface(typeface);

                popupWindow1 = new PopupWindow(addView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow1.setAnimationStyle(R.style.AnimationPopup);
                popupWindow1.setFocusable(true);
                popupWindow1.update();
                popupWindow1.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                btHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderSummaryandCheckout.this,Basehome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });



            }else if (s.equals("404")){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Order is Unsuccessful",Toast.LENGTH_SHORT).show();
                View addView = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_order, null, false);

                RelativeLayout relativeLayout = addView.findViewById(R.id.relativeLayoutOrder);
                ImageButton ib2,ib3;
                TextView tv1;
                Button btHome;


                ib2 = addView.findViewById(R.id.ibOrderDone);
                ib3 = addView.findViewById(R.id.ibOrderNotdone);
                tv1 = addView.findViewById(R.id.tvOrderStatus);
                btHome = addView.findViewById(R.id.btGotoHome);

                tv1.setTypeface(typeface);
                tv1.setText("Oops! Order could not be placed successfully");
                btHome.setTypeface(typeface);
                ib2.setVisibility(View.INVISIBLE);
                ib3.setVisibility(View.VISIBLE);

                popupWindow1 = new PopupWindow(addView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow1.setAnimationStyle(R.style.AnimationPopup);
                popupWindow1.setFocusable(true);
                popupWindow1.update();
                popupWindow1.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                btHome.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderSummaryandCheckout.this,Basehome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(),"Problem while connecting with our server",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private String getOrderStatusFromJson(String jsonString){
        String res = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    class AddCodOrder extends AsyncTask<String,String,String> {
        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result;

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(ORDER_URL).buildUpon().appendQueryParameter("orderName", tvBuyerName.getText().toString()).
                    appendQueryParameter("orderEmail", email).appendQueryParameter("orderPhone", tvBuyerPhoneNumber.getText().toString()).
                    appendQueryParameter("orderTransactionId", getTransactionId()).
                    appendQueryParameter("orderPaymentId", "COD").
                    appendQueryParameter("orderStatus", "Successful").appendQueryParameter("orderSize", productSize).
                    appendQueryParameter("orderDateCreated", getCurrentDate()).appendQueryParameter("productName", productName).
                    appendQueryParameter("productLink", productLink).appendQueryParameter("productImage", productImageUrl).
                    appendQueryParameter("address", tvBuyerAddress.getText().toString() + " Pincode " + tvBuyerPincode.getText().toString()).
                    appendQueryParameter("price", productPrice).build();

            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";


                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }


                result = getCodOrderStatusFromJson(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {


                if (s.equals("200")) {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Order is Successfull", Toast.LENGTH_SHORT).show();
                    View addView = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_order, null, false);

                    RelativeLayout relativeLayout = addView.findViewById(R.id.relativeLayoutOrder);
                    ImageButton ib2, ib3;
                    TextView tv1;
                    Button btHome;


                    ib2 = addView.findViewById(R.id.ibOrderDone);
                    ib3 = addView.findViewById(R.id.ibOrderNotdone);
                    tv1 = addView.findViewById(R.id.tvOrderStatus);
                    btHome = addView.findViewById(R.id.btGotoHome);

                    tv1.setTypeface(typeface);
                    tv1.setText("Hurray! Order is successfully placed");
                    btHome.setTypeface(typeface);

                    popupWindow1 = new PopupWindow(addView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow1.setAnimationStyle(R.style.AnimationPopup);
                    popupWindow1.setFocusable(true);
                    popupWindow1.update();
                    popupWindow1.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                    btHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OrderSummaryandCheckout.this, Basehome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });


                } else if (s.equals("404")) {
                    progressBar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Order is Unsuccessful", Toast.LENGTH_SHORT).show();
                    View addView = LayoutInflater.from(OrderSummaryandCheckout.this).inflate(R.layout.custom_order, null, false);

                    RelativeLayout relativeLayout = addView.findViewById(R.id.relativeLayoutOrder);
                    ImageButton ib2, ib3;
                    TextView tv1;
                    Button btHome;


                    ib2 = addView.findViewById(R.id.ibOrderDone);
                    ib3 = addView.findViewById(R.id.ibOrderNotdone);
                    tv1 = addView.findViewById(R.id.tvOrderStatus);
                    btHome = addView.findViewById(R.id.btGotoHome);

                    tv1.setTypeface(typeface);
                    tv1.setText("Oops! Order could not be placed successfully");
                    btHome.setTypeface(typeface);
                    ib2.setVisibility(View.INVISIBLE);
                    ib3.setVisibility(View.VISIBLE);

                    popupWindow1 = new PopupWindow(addView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    popupWindow1.setAnimationStyle(R.style.AnimationPopup);
                    popupWindow1.setFocusable(true);
                    popupWindow1.update();
                    popupWindow1.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                    btHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OrderSummaryandCheckout.this, Basehome.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Problem while connecting with our server", Toast.LENGTH_SHORT).show();
                    progressBar1.setVisibility(View.GONE);
                }
            }else {
                progressBar1.setVisibility(View.GONE);
            }
        }
    }

    private String getCodOrderStatusFromJson(String jsonString){
        String res = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;

    }



    private String getCurrentDate(){
        String dateString = "";
        Calendar c = Calendar.getInstance();
        Log.i("TIME",c.getTime().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        dateString = simpleDateFormat.format(c.getTime());


        return dateString;
    }

    private String getTransactionId(){
        String transactionId = "";

        long t = System.currentTimeMillis();
        transactionId = "COD" + String.valueOf(t);


        return transactionId;
    }


}
