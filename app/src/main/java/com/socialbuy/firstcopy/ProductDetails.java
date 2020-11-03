package com.socialbuy.firstcopy;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.*;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    private String userName,dateOfCreation,imageUrl,productDesc,profileLink,sellerBio,sellerPhoneNumber;
    private TextView tv1,tv2,tv3,tv4,tv5;
    private ImageView iv1;
    private com.android.volley.toolbox.ImageLoader imageLoader;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageButton ib1,ib2;
    private String activity,query;

    private Button btShowSeller,btBuyNow,btEnquirePrice;
    private String phoneNumber,numberofDays,userPhoneNumber,dateAdded;
    private FirebaseAnalytics firebaseAnalytics;
    private static final String URLPHONENUMBER = "http://firstcopy.co.in/firstcopy/addusernumber.php";
    private Typeface typeface;
    private Typeface typeface1;
    private  View view;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);




        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar1);
        collapsingToolbarLayout.setTitle("");
        tv1=  (TextView) findViewById(R.id.tvUsername);
        tv2 = (TextView) findViewById(R.id.tvDate);
        tv3 = (TextView) findViewById(R.id.tvDescription);
        tv4 = (TextView) findViewById(R.id.tvDescriptionTag);
        tv5 = findViewById(R.id.tvWarning);
        view = findViewById(R.id.viewProductDetails);

        ib1 = (ImageButton) findViewById(R.id.ibNoBookmark);
        ib2 = (ImageButton) findViewById(R.id.ibBookmark);

        iv1 =  (ImageView) findViewById(R.id.ivProductImage);
        btShowSeller =  (Button) findViewById(R.id.btShowSeller);
//        btContactSeller = (Button) findViewById(R.id.btContactSeller);
        btBuyNow = (Button) findViewById(R.id.btBuyNow);
        btEnquirePrice =(Button) findViewById(R.id.btEnquirePrice);

       // View v = findViewById(R.id.separator);






        typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");

        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

        Intent receiver = getIntent();
        userName = receiver.getStringExtra("USERNAME");
        dateOfCreation = receiver.getStringExtra("DATEOFCREATION");
        imageUrl = receiver.getStringExtra("IMAGEURL");
        productDesc = receiver.getStringExtra("DESC");
        profileLink = receiver.getStringExtra("PROFILE_LINK");
        sellerBio = receiver.getStringExtra("SELLER_BIO");
        sellerPhoneNumber = receiver.getStringExtra("PHONE");
       // Toast.makeText(getApplicationContext(),sellerPhoneNumber,Toast.LENGTH_SHORT).show();
        if (sellerPhoneNumber.length() == 10){
            phoneNumber = "91" + sellerPhoneNumber;
           // Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();
        }

        if (sellerPhoneNumber.equals("nothing")){
            phoneNumber = "nothing";
        }
        activity = receiver.getStringExtra("ACTIVITY");
        if (activity.equals("1")) {
            query = receiver.getStringExtra("QUERY");
            numberofDays = receiver.getStringExtra("NUMBEROFDAYS");
            if (Integer.parseInt(numberofDays) > 30){

                btBuyNow.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);
                tv5.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
                //btContactSeller.setVisibility(View.INVISIBLE);
                btShowSeller.setVisibility(View.INVISIBLE);
                btEnquirePrice.setVisibility(View.VISIBLE);
               // v.setVisibility(View.INVISIBLE);

            }
        } else if (activity.equals("2")){
            numberofDays = receiver.getStringExtra("NUMBEROFDAYS");
            if (Integer.parseInt(numberofDays) > 30){

                btBuyNow.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);
                tv5.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
               // btContactSeller.setVisibility(View.INVISIBLE);
                btShowSeller.setVisibility(View.INVISIBLE);
                btEnquirePrice.setVisibility(View.VISIBLE);
               // v.setVisibility(View.INVISIBLE);

            }
        } else if (activity.equals("4")){
            long differenceofDays = getNumberofDaysBetweenDates(dateOfCreation);
            //numberofDays = "nill";
            if (differenceofDays > 30){
                numberofDays = String.valueOf(differenceofDays);
                btBuyNow.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
                tv1.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);
                tv5.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
              //  btContactSeller.setVisibility(View.INVISIBLE);
                btShowSeller.setVisibility(View.INVISIBLE);
                btEnquirePrice.setVisibility(View.VISIBLE);
                //v.setVisibility(View.INVISIBLE);
                //RelativeLayout relativeLayout = findViewById(R.id.relativeLayout1);

            }
        }
            //numberofDays = "nill";
//        if (userName.contains("%")){
//
//            userName = userName.replaceAll("%","Percent");
//        }
//
//        String text =  String.format("<a href=\\\"%s\\\">" + userName + "</a>", profileLink);

        tv1.setText(userName);
        //tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv1.setTypeface(typeface);
        btShowSeller.setTypeface(typeface1);
      //  btContactSeller.setTypeface(typeface1);
        btBuyNow.setTypeface(typeface1);

//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = newProfileIntent(getApplicationContext().getPackageManager(),profileLink);
//                startActivity(Intent.createChooser(intent,"Choose"));
//            }
//        });

        btShowSeller.setOnClickListener(this);


        btBuyNow.setOnClickListener(this);
    //    btContactSeller.setOnClickListener(this);
        btEnquirePrice.setOnClickListener(this);
        iv1.setOnClickListener(this);


        tv2.setText(dateOfCreation);
        tv2.setTypeface(typeface);

        tv3.setText(productDesc);
        tv3.setTypeface(typeface);

//        imageLoader = new ImageLoader(ProductDetails.this);
//        imageLoader.DisplayImage(imageUrl,iv1);
//        imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
//        imageLoader.get(imageUrl, com.android.volley.toolbox.ImageLoader.getImageListener(iv1,R.drawable.stub,R.mipmap.ic_launcher_round));
//        iv1.setImageUrl(imageUrl,imageLoader);

        Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.stub).error(R.mipmap.ic_launcher).into(iv1);


        ib1.setOnClickListener(this);
        ib2.setOnClickListener(this);

        WishListDatabase db = new WishListDatabase(ProductDetails.this);
        db.open();

        boolean isWishlist = db.checkForWishList(imageUrl);


        db.close();

        if (isWishlist){
            ib1.setVisibility(View.INVISIBLE);
            ib2.setVisibility(View.VISIBLE);
        }else {
            ib1.setVisibility(View.VISIBLE);
            ib2.setVisibility(View.INVISIBLE);
        }



        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,String.valueOf(btBuyNow.getId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Buy Now");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

        //Bundle bundle1 = new Bundle();
       // bundle1.putString(FirebaseAnalytics.Param.ITEM_ID,String.valueOf(btContactSeller.getId()));
       // bundle1.putString(FirebaseAnalytics.Param.ITEM_NAME,"Enquire Price");
       // firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle1);





    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null)
            alertDialog.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    public static boolean isValidPhone(String phone)
    {
        String expression = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
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

    private Intent newProfileIntent(PackageManager pm, String url){

        Intent intent = new Intent(Intent.ACTION_VIEW);

        try {
            if (pm.getPackageInfo("com.instagram.android",0) != null){

                if (url.endsWith("/")){
                    url = url.substring(0,url.length()-1);
                }
                String userName = url.substring(url.lastIndexOf("/") + 1);
                intent.setType("*/*");

                intent.setData(Uri.parse("http://instagram.com/_u/" + userName));
                intent.setPackage("com.instagram.android");
                return intent;

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        intent.setData(Uri.parse(url));
        return intent;


    }

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
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ibNoBookmark:

                WishListDatabase db1  = new WishListDatabase(ProductDetails.this);
                db1.open();

                long res = db1.insert(userName,imageUrl,profileLink,productDesc,dateOfCreation,sellerBio,sellerPhoneNumber);

                if (res > 0){
                    Toast.makeText(getApplicationContext(),"Added to WishList",Toast.LENGTH_SHORT).show();
                    ib1.setVisibility(View.INVISIBLE);
                    ib2.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getApplicationContext(),"Cannot add to wishlist",Toast.LENGTH_SHORT).show();
                }




                db1.close();


                break;
            case R.id.ibBookmark:

                WishListDatabase db2 = new WishListDatabase(ProductDetails.this);

                db2.open();

                boolean isRemoved = db2.removeFromWishList(imageUrl);

                db2.close();

                if (isRemoved){
                    Toast.makeText(getApplicationContext(),"Successfully Removed From Your Wishlist",Toast.LENGTH_SHORT).show();
                    ib1.setVisibility(View.VISIBLE);
                    ib2.setVisibility(View.INVISIBLE);
                }

                break;
            case R.id.btShowSeller:
                Intent intent = newProfileIntent(getApplicationContext().getPackageManager(), profileLink);
                startActivity(Intent.createChooser(intent, "Choose"));
                break;
            case R.id.btBuyNow:
                if (phoneNumber.equals("nothing")){
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ProductDetails.this);
                    View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_settings,null,false);
                    alertBuilder.setView(v);

                    TextView tvHeading,tvContent;
                    tvHeading = v.findViewById(R.id.tvCustomHeading);
                    tvContent = v.findViewById(R.id.tvCustomContent);

                    tvHeading.setText("Contact Not Found");
                    tvContent.setText("Please view seller profile for more information");
                    tvHeading.setTypeface(typeface);
                    tvContent.setTypeface(typeface);

                    alertBuilder.setCancelable(false).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                }else {
                    String whatsappApi = "https://api.whatsapp.com/send?phone=" + phoneNumber  + "&text=" + imageUrl + "\n" +  "Hi, I want to buy this product. Is it available? ";

                    Intent contactIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappApi));
                    startActivity(contactIntent);
                }
                break;

            case R.id.btEnquirePrice:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_phonenumber,null,false);
                builder.setView(v);

                final EditText etNumber = v.findViewById(R.id.etPhoneNumber);

                builder.setCancelable(false).setPositiveButton("Get Best Price", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getApplicationContext(),"Request Otp",Toast.LENGTH_SHORT).show();
                        userPhoneNumber = etNumber.getText().toString();
                        if (userPhoneNumber.equals(""))
                            Toast.makeText(getApplicationContext(),"You need to enter phone number",Toast.LENGTH_SHORT).show();
                        else if (isValidPhone(userPhoneNumber)) {

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            int month = calendar.get(Calendar.MONTH);
                            int correctedMonth = month + 1;
                            dateAdded = "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + correctedMonth + "/" + calendar.get(Calendar.YEAR);

                            //Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                            new AddUserNumber().execute("");
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid Phone Number",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.ivProductImage:

               // iv1.startAnimation(animation);
                break;
        }

    }


//    @Nullable
//    @Override
//    public Intent getSupportParentActivityIntent() {
//        return getParentActivityIntentImpl();
//    }
//
//    @Nullable
//    @Override
//    public Intent getParentActivityIntent() {
//        return getParentActivityIntentImpl();
//    }
//
//
//    private Intent getParentActivityIntentImpl(){
//        Intent i = null;
//
//        if (activity.equals("1")){
//            i = new Intent(ProductDetails.this,SearchActivity.class);
//            i.putExtra("QUERY",query);
//        }else if (activity.equals("2")){
//
//            i = new Intent(ProductDetails.this,Browse.class);
//        }else if (activity.equals("3")){
//            i = new Intent(ProductDetails.this,HomeScreen.class);
//        }else if (activity.equals("4")){
//            i = new Intent(ProductDetails.this,WishList.class);
//        }
//
//        return i;
//    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public class AddUserNumber extends AsyncTask<String,String,String> {

        HttpURLConnection request;
        BufferedReader reader;
        InputStream is;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(URLPHONENUMBER).buildUpon().appendQueryParameter("phone_number", userPhoneNumber).
                    appendQueryParameter("image_link", imageUrl).appendQueryParameter("dateAdded", dateAdded).build();

            try {
                URL phone_url = new URL(uri.toString());
                request = (HttpURLConnection) phone_url.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                is = request.getInputStream();
                if (is == null)
                    return "Network Problem";

                reader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "/n");
                }


                if (sb.length() == 0)
                    return "Network Problem";


                result = getdataFromJson(sb.toString());


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
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_settings,null,false);
                builder.setView(v);

                TextView tv1 = v.findViewById(R.id.tvCustomHeading);
                TextView tv2 = v.findViewById(R.id.tvCustomContent);
                LinearLayout linearLayout = v.findViewById(R.id.llCustom);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                tv1.setText("Get Best Price");
                tv1.setTextColor(Color.WHITE);
                tv2.setText("Request Submitted Succesfully. You will be informed about the price soon.");

                builder.setCancelable(false).setPositiveButton("Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(ProductDetails.this,Basehome.class);
                        startActivity(intent);
                        finish();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                alertDialog = builder.create();
                alertDialog.show();
            }
                //Toast.makeText(getApplicationContext(), "Request Submitted Successfully", Toast.LENGTH_SHORT).show();
            else if (s.equals("404"))
                Toast.makeText(getApplicationContext(), "Problem while adding", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Problem while connecting to our server", Toast.LENGTH_SHORT).show();

        }
    }

    private String getdataFromJson(String jsonString){
        String res = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }


}
