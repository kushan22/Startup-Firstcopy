package com.socialbuy.firstcopy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.authentication.Signin;
import com.socialbuy.firstcopy.fragments.FactoryShop;
import com.socialbuy.firstcopy.fragments.Instashop;
import com.socialbuy.firstcopy.fragments.Profile;

import java.lang.reflect.Field;

public class Basehome extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean doubleBackToExitPressedOnce = false;
    private BottomNavigationView bottomNavigationView;
    int count;
    Menu menu;
    private TextView tvToolbarTitle;
    Typeface typeface,typeface1;
    String email;
    private ImageButton ib1;
    private TextView tv1;
    private Button bt1;
  //  private MyCountDownTimer myCountDownTimer;
   // private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basehome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");

        SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME,Context.MODE_PRIVATE);
        email = preferences.getString(Signin.PREF_EMAIL,"1");

        tvToolbarTitle  = findViewById(R.id.toolbar_title);
        tvToolbarTitle.setTypeface(typeface);
        tvToolbarTitle.setText("Home");
        bottomNavigationView = findViewById(R.id.bottomnavigation);

        ib1 = findViewById(R.id.ibNoNetworkHome);
        tv1 = findViewById(R.id.tvNoNetworkHome);
        bt1 = findViewById(R.id.btRetryHome);



//        preferences = getSharedPreferences("RATINGPREF",Context.MODE_PRIVATE);
//        String ratingStatus = preferences.getString("RATINGSTATUS","none");

//        if (ratingStatus.equals("none")) {
//
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    myCountDownTimer = new MyCountDownTimer(30000, 1000);
//                    myCountDownTimer.start();
//                }
//            };
//
//            runnable.run();
//        }



        doAllTasks();

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAllTasks();
            }
        });









    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (myCountDownTimer != null)
//            myCountDownTimer.cancel();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (myCountDownTimer != null)
//            myCountDownTimer.cancel();
//    }



    public void doAllTasks(){

        boolean isConnected = checkForNetwork();
        if (isConnected) {
            ib1.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
            layoutParams.setBehavior(new BottomNavigationViewBehavior());



            //BottomNavigationViewHelper.enableShiftMode(bottomNavigationView);

            // coordinatorLayout = findViewById(R.id.bottomNavCoordinatorLayout);


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
//                        case R.id.instahop:
//                            count = 1;
//                            selectedFragment = Instashop.newInstance();


                        case R.id.factoryshop:
                            count = 2;
                            selectedFragment = FactoryShop.newInstance();

                            break;
                        case R.id.profile:
                            count = 3;
                            Intent intent = new Intent(Basehome.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }

                    if (count == 1 || count == 2) {
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.replace(R.id.containerView, selectedFragment).commit();
                    }


                    return true;
                }
            });

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(R.id.containerView, FactoryShop.newInstance()).disallowAddToBackStack().commit();
        }else if (!isConnected){
            ib1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

    }

    private boolean checkForNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean networkConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return networkConnected;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu;
        updateMenuItemTitle();
        return true;
    }

    private void updateMenuItemTitle(){
        MenuItem menuItem = menu.findItem(R.id.logout);
        if (email.equals("1")){
          menuItem.setTitle("Profile");
          menuItem.setVisible(false);
        }else {
            menuItem.setTitle("Logout");
            menuItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String title = item.getTitle().toString();
//        if (id == R.id.showWishList){
//            Intent intent = new Intent(Basehome.this,WishList.class);
//            startActivity(intent);
//        }else if (id == R.id.logout){
            if (title.equals("Logout")) {
                SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }else if (title.equals("Profile")){
//                Intent intent = new Intent(Basehome.this,ProfileActivity.class);
//                startActivity(intent);
            }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }





    public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

        private int height;

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
            height = child.getHeight();
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View directTargetChild, View target, int nestedScrollAxes) {
            return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, BottomNavigationView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyConsumed > 0) {
                slideDown(child);
            } else if (dyConsumed < 0) {
                slideUp(child);
            }
        }

        private void slideUp(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(0).setDuration(200);
        }

        private void slideDown(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(height).setDuration(200);
        }

    }


//    public class MyCountDownTimer extends CountDownTimer{
//
//        /**
//         * @param millisInFuture    The number of millis in the future from the call
//         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
//         *                          is called.
//         * @param countDownInterval The interval along the way to receive
//         *                          {@link #onTick(long)} callbacks.
//         */
//        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
//            super(millisInFuture, countDownInterval);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//        }
//
//        @Override
//        public void onFinish() {
//            AlertDialog.Builder builder = new AlertDialog.Builder(Basehome.this);
//            View view = LayoutInflater.from(Basehome.this).inflate(R.layout.custom_rating,null,false);
//            builder.setView(view);
//
//            TextView tvHeader,tvContent;
//            tvHeader = view.findViewById(R.id.tvReviewHeader);
//            tvContent = view.findViewById(R.id.tvReviewContent);
//
//            tvHeader.setTypeface(typeface);
//            tvContent.setTypeface(typeface1);
//
//            builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    final String appPackageName = getPackageName();
//                    try{
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                        preferences = getSharedPreferences("RATINGPREF",Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("RATINGSTATUS","never");
//                        editor.commit();
//                        dialog.dismiss();
//                    } catch (android.content.ActivityNotFoundException e){
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                        preferences = getSharedPreferences("RATINGPREF",Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("RATINGSTATUS","never");
//                        editor.commit();
//                        dialog.dismiss();
//                    }
//
//
//                }
//            }).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    preferences = getSharedPreferences("RATINGPREF",Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("RATINGSTATUS","never");
//                    editor.commit();
//                    dialog.dismiss();
//
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//
//        }
//    }




}
