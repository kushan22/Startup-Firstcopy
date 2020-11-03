package com.socialbuy.firstcopy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Versioncheck extends AppCompatActivity {

    private ProgressBar progressBar;
    private String versionName;
    private Typeface typeface,typeface1;
    private SharedPreferences versionPreferences,versionCodePreferences;
    private SharedPreferences.Editor editor,versionCodeEditor;
    private PackageInfo pInfo;
    private int versionCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_versioncheck);



        progressBar = findViewById(R.id.progressBar1);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        versionCodePreferences = getSharedPreferences("VERSIONCODE",Context.MODE_PRIVATE);
        int savedVersionCode = versionCodePreferences.getInt("VERSIONCODESTATUS",-1);

        if (savedVersionCode == -1){

        } else if (savedVersionCode == versionCode){

        } else if (savedVersionCode < versionCode){
            versionPreferences = getSharedPreferences("VERSIONPREF",Context.MODE_PRIVATE);
            editor = versionPreferences.edit();
            editor.clear();
            editor.commit();
        }

        versionPreferences = getSharedPreferences("VERSIONPREF",Context.MODE_PRIVATE);
        String versionStatus = versionPreferences.getString("VERSION","none");

        if (versionStatus.equals("never")){
            Intent intent = new Intent(Versioncheck.this,Basehome.class);
            startActivity(intent);
            finish();
        }else {

            typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
            typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                versionName = pInfo.versionName;

                //Toast.makeText(getApplicationContext(),versionName,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                new CheckVersion().execute("");
                //  Intent goToNext = new Intent(FlashActivity.this,Signin.class);
                //startActivity(goToNext);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }



    private class CheckVersion extends AsyncTask<String, String, String> {

        HttpURLConnection request = null;
        BufferedReader reader = null;
        InputStream is = null;
        String res = "";

        @Override
        protected String doInBackground(String... params) {

            final String baseUrl = "http://www.firstcopy.co.in/firstcopy/version.php";
            final String QUERY_VERSION = "versioncode";
            Uri baseUri = Uri.parse(baseUrl).buildUpon().appendQueryParameter(QUERY_VERSION, versionName).build();
            try {
                URL myUrl = new URL(baseUri.toString());
                request = (HttpURLConnection) myUrl.openConnection();
                request.setRequestMethod("GET");
                request.connect();

                is = request.getInputStream();
                if (is == null)
                    return "Network Problem";

                reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }


                if (sb.length() == 0)
                    return "Network Problem";

                res = getVersionJsonData(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (request != null)
                    request.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return res;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Versioncheck.this);
                View view = LayoutInflater.from(Versioncheck.this).inflate(R.layout.custom_rating,null,false);
                builder.setView(view);

                TextView tvHeader,tvContent;
                tvHeader = view.findViewById(R.id.tvReviewHeader);
                tvContent = view.findViewById(R.id.tvReviewContent);

                tvHeader.setTypeface(typeface1);
                tvContent.setTypeface(typeface);

                tvHeader.setText("Update");
                tvContent.setText("Looks like you have older version of the app. Please update the app for better experience.");

                builder.setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        versionPreferences = getSharedPreferences("VERSIONPREF",Context.MODE_PRIVATE);
                        editor = versionPreferences.edit();
                        editor.clear();
                        editor.commit();

                        versionCodePreferences = getSharedPreferences("VERSIONCODE",Context.MODE_PRIVATE);
                        versionCodeEditor = versionCodePreferences.edit();
                        versionCodeEditor.putInt("VERSIONCODESTATUS",versionCode);
                        versionCodeEditor.commit();



                        final String appPackageName = getPackageName();
                        try {
                            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                            startActivity(playStoreIntent);
                            if (dialog != null)
                                dialog.dismiss();
                        } catch (android.content.ActivityNotFoundException e) {


                            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                            startActivity(playStoreIntent);
                            if (dialog != null)
                                dialog.dismiss();

                        }


                    }
                }).setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        versionPreferences = getSharedPreferences("VERSIONPREF", Context.MODE_PRIVATE);
                        editor = versionPreferences.edit();
                        editor.putString("VERSION","never");
                        editor.commit();

                        versionCodePreferences = getSharedPreferences("VERSIONCODE",Context.MODE_PRIVATE);
                        versionCodeEditor = versionCodePreferences.edit();
                        versionCodeEditor.putInt("VERSIONCODESTATUS",versionCode);
                        versionCodeEditor.commit();
                        if (dialog != null)
                            dialog.dismiss();

                        Intent intent = new Intent(Versioncheck.this,Basehome.class);
                        startActivity(intent);
                        finish();

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            } else if (s.equals("2")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Versioncheck.this);
                View view = LayoutInflater.from(Versioncheck.this).inflate(R.layout.custom_rating,null,false);
                builder.setView(view);

                TextView tvHeader,tvContent;
                tvHeader = view.findViewById(R.id.tvReviewHeader);
                tvContent = view.findViewById(R.id.tvReviewContent);

                tvHeader.setTypeface(typeface1);
                tvContent.setTypeface(typeface);

                tvHeader.setText("Update");
                tvContent.setText("Looks like you have older version of the app. Please update the app for better experience.");

                builder.setCancelable(false).setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String appPackageName = getPackageName();
                        try {
                            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
                            startActivity(playStoreIntent);
                            if (dialog != null)
                                dialog.dismiss();
                        } catch (android.content.ActivityNotFoundException e) {


                            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                            startActivity(playStoreIntent);
                            if (dialog != null)
                                dialog.dismiss();

                        }


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null)
                            dialog.dismiss();
                        finish();

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else if (s.equals("0")){
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Versioncheck.this,Basehome.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getApplicationContext(),"Problem while connecting with our server. Please check if your Intenet connection is active",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
    }

    private String getVersionJsonData(String jsonString) {

        Log.i("Version Check", jsonString);


        String result = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            result = jsonObject.getString("upgradeStatus");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
