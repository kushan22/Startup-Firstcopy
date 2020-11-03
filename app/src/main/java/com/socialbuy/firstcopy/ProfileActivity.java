package com.socialbuy.firstcopy;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socialbuy.firstcopy.authentication.Register;
import com.socialbuy.firstcopy.authentication.Signin;
import com.socialbuy.firstcopy.fragments.Profile;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivBackgroundone,ivBackground2;
   // private Button btCreateAccount;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RelativeLayout rlWishList,rlRegister;
    private AppBarLayout appBarLayout;
    private TextView tvName;
    private FloatingActionButton fabOrders;
    private String status="none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
        String email = preferences.getString(Signin.PREF_EMAIL,"0");
        String password = preferences.getString(Signin.PREF_PASS,"1");
        String fullName = preferences.getString(Signin.PREF_FULLNAME,"2");
        if (email.equals("0") || password.equals("1")){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_profile);
            rlWishList = findViewById(R.id.rlWishList);
            rlRegister = findViewById(R.id.rlRegister);
            appBarLayout = findViewById(R.id.AppBarLayout);
            rlWishList.setVisibility(View.INVISIBLE);
            rlRegister.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
        }else {
            setContentView(R.layout.activity_profile);
            Toolbar toolbar = findViewById(R.id.toolbarProfile);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);

            toolbar.setTitle("");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            rlWishList = findViewById(R.id.rlWishList);
            rlRegister = findViewById(R.id.rlRegister);
            rlWishList.setVisibility(View.VISIBLE);
            rlRegister.setVisibility(View.INVISIBLE);
            Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
            tvName = findViewById(R.id.tvProfileName);
            tvName.setTypeface(typeface);
            tvName.setText("Welcome, " + fullName);


        }








        if (email.equals("0") || password.equals("1")){

            ivBackgroundone = findViewById(R.id.ivBackgroundImage);
            ivBackground2 =  findViewById(R.id.ivBackgroundImage2);
          //  btCreateAccount = findViewById(R.id.btCreateAccount);



            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(60000L);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float progress = (float) valueAnimator.getAnimatedValue();
                    float height = ivBackgroundone.getHeight();
                    float transationY = height * progress;
                    ivBackgroundone.setTranslationY(transationY);
                    ivBackground2.setTranslationY(transationY - height);
                }
            });

            valueAnimator.start();

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            View v = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.custom_dialog,null,false);
            builder.setView(v);

            Button btCreateAccount,btSignin;
            TextView tvHeader;
            btCreateAccount = v.findViewById(R.id.btDialogCreateAccount);
            btSignin = v.findViewById(R.id.btCustomSignin);
            tvHeader = v.findViewById(R.id.tvAlreadyHeader);
            Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
            tvHeader.setTypeface(typeface);
            btSignin.setTypeface(typeface);
            btCreateAccount.setTypeface(typeface);




            builder.setCancelable(true);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                       Intent intent = new Intent(ProfileActivity.this, Basehome.class);
                       startActivity(intent);
                       finish();
                }

            });

            btCreateAccount.setOnClickListener(this);
            btSignin.setOnClickListener(this);


            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();



           // btCreateAccount.setOnClickListener(this);
        } else {
            //fabWishlist = findViewById(R.id.fbWishList);
            fabOrders = findViewById(R.id.fbOrders);
          //  fabReferrals = findViewById(R.id.fbReferrals);
            //fabWishlist.setOnClickListener(this);
            fabOrders.setOnClickListener(this);
           // fabReferrals.setOnClickListener(this);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(ProfileActivity.this,Basehome.class);
                startActivity(intent);
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ProfileActivity.this,Basehome.class);
        startActivity(intent);
        finish();

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
//    private Intent getParentActivityIntentImpl(){
//        Intent i = null;
//        i = new Intent(ProfileActivity.this,Basehome.class);
//
//
//
//        return i;
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btDialogCreateAccount:
                Intent intent = new Intent(ProfileActivity.this,Register.class);
                startActivity(intent);
                break;
            case R.id.btCustomSignin:
                Intent intent1 = new Intent(ProfileActivity.this,Signin.class);
                startActivity(intent1);
                break;
//            case R.id.fbWishList:
//                Intent wishListIntent = new Intent(ProfileActivity.this,WishList.class);
//                startActivity(wishListIntent);
//                break;
            case R.id.fbOrders:
                Intent ordersIntent = new Intent(ProfileActivity.this,Myorders.class);
                startActivity(ordersIntent);
                break;
//            case R.id.fbReferrals:
//                Intent referralIntent = new Intent(ProfileActivity.this,MyReferrals.class);
//                startActivity(referralIntent);
//                break;


        }

    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btDialogCreateAccount:
//                Intent intent = new Intent(ProfileActivity.this,Register.class);
//                startActivity(intent);
//                break;
//            case R.id.btCustomSignin:
//                Intent intent1 = new Intent(ProfileActivity.this,Signin.class);
//                startActivity(intent1);
//                break;
//        }
//    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        int keyCode = event.getKeyCode();
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            finish();
//            return true;
//        }
//
//        return super.dispatchKeyEvent(event);
//
//    }
}
