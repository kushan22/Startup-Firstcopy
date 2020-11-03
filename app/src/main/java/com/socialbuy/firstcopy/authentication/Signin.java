package com.socialbuy.firstcopy.authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.Basehome;
import com.socialbuy.firstcopy.R;

import org.json.JSONException;
import org.json.JSONObject;

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

public class Signin extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail,etPassword;
    private Button btSignin,btForgotPass,btgoToRegister;
    private String email,pass;
    private static final String authenticate_url = "http://firstcopy.co.in/firstcopy/authenticate_user.php";
    private static final String resetpassword_url = "http://firstcopy.co.in/firstcopy/resetpassword.php";
    public static final String PREF_NAME="loginpref";
    public static final String PREF_EMAIL = "loginemail";
    public static final String PREF_PASS = "loginpass";
    public static final String PREF_FULLNAME = "fullname";
    private String status = "none";
    private String userNameEmail;
    PopupWindow popupWindow;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressBar = findViewById(R.id.progressBar1);
        etEmail = findViewById(R.id.etSigninEmail);
        etPassword = findViewById(R.id.etSigninPassword);
        btSignin = findViewById(R.id.btSignin);
        btForgotPass = findViewById(R.id.btForgotPassword);
        btgoToRegister = findViewById(R.id.btgoToRegister);


        btSignin.setOnClickListener(this);
        btForgotPass.setOnClickListener(this);
        btgoToRegister.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (popupWindow != null)
            popupWindow.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null)
            popupWindow.dismiss();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btSignin:
                email = etEmail.getText().toString();
                pass = etPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty())
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    new AuthenticateUser().execute("");
                }

                break;
            case R.id.btForgotPassword:

                View forgotPasswordView = LayoutInflater.from(Signin.this).inflate(R.layout.custom_forgotpassword,null,false);
                RelativeLayout relativeLayout = forgotPasswordView.findViewById(R.id.relativeLayoutForgotPassword);

                ImageButton ibClose;
                final EditText etUserEmail;
                Button btResetPassword;

                ibClose = forgotPasswordView.findViewById(R.id.ibCloseForgotPassword);
                etUserEmail = forgotPasswordView.findViewById(R.id.etEnteremail);
                btResetPassword = forgotPasswordView.findViewById(R.id.btResetPassword);

                popupWindow = new PopupWindow(forgotPasswordView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setAnimationStyle(R.style.AnimationPopup);
                popupWindow.setFocusable(true);
                popupWindow.update();
                if (popupWindow != null)
                    popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

                btResetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userNameEmail = etUserEmail.getText().toString();

                        progressBar.setVisibility(View.VISIBLE);
                        new PasswordReset().execute("");
                    }
                });

                ibClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                break;
            case R.id.btgoToRegister:
                Intent intent = new Intent(Signin.this,Register.class);
                startActivity(intent);
                break;
        }

    }

    public class AuthenticateUser extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(authenticate_url);
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("POST");
                request.setDoOutput(true);
                request.setDoInput(true);

                outputStream = request.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = request.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line  = "";

                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line + "\n");
                }

                if (sb.length() == 0)
                    return "Network Problem";

                result  = getDataFromJson(sb.toString());


                bufferedReader.close();
                inputStream.close();
                request.disconnect();



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
            if (s.equals("404")){
                Toast.makeText(getApplicationContext(),"Problem while logging in",Toast.LENGTH_SHORT).show();

               progressBar.setVisibility(View.INVISIBLE);
              //  finish();
            }else if (s.startsWith("F:")){

                Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_SHORT).show();
                String replace = s.replace("F:","");
                String fullName = replace.replace("L:","");
                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_EMAIL,email);
                editor.putString(PREF_PASS,pass);
                editor.putString(PREF_FULLNAME,fullName);
                editor.commit();


                Intent intent = new Intent(Signin.this, Basehome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

               progressBar.setVisibility(View.INVISIBLE);

            }else {
                Toast.makeText(getApplicationContext(),s + "Problem while connecting with our server",Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }


    private String getDataFromJson(String jsonString){
        String res = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    class PasswordReset extends AsyncTask<String, String, String>{

        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result  = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(resetpassword_url).buildUpon().appendQueryParameter("email",userNameEmail).build();
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


                result = getResetPasswordDataFromJson(sb.toString());



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (reader == null){
                        return "None";
                    }
                    if (reader != null)
                        reader.close();
                    if (inputStream != null)
                        inputStream.close();
                    if (connection != null)
                        connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.equals("200")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                    View view = LayoutInflater.from(Signin.this).inflate(R.layout.custom_rating,null,false);
                    builder.setView(view);

                    TextView tvHeader,tvContent;
                    tvHeader = view.findViewById(R.id.tvReviewHeader);
                    tvContent = view.findViewById(R.id.tvReviewContent);

                    tvHeader.setText("Forgot Password");
                    tvContent.setText("An email has been sent to you on your registered email address. Click on the link provided to reset your password. Please check your spam folders too in case you don't get an email in your inbox.");

                    builder.setPositiveButton("Home", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Signin.this,Basehome.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    //Toast.makeText(getApplicationContext(), "An email has been sent to you on your registered email address. Click on the link provided to reset your password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (s.equals("404")) {
                    Toast.makeText(getApplicationContext(), "This email is not registered", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else if (s.equals("None")){
                    Toast.makeText(getApplicationContext(),"Unable to connect to our server",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }else {
                Toast.makeText(getApplicationContext(),"Unable to connect to our server",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
    }


    private String getResetPasswordDataFromJson(String jsonString){

        String res = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
          //  Log.i("RESULT",res);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res;

    }


}
