package com.socialbuy.firstcopy.authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText etFirstName,etLastName,etEmail,etPassword,etConfirmPassword;
    private Button btRegister,btgoToSigin;
    private String firstName,lastName,emailid,password,confirmPassword;
    private static final String registerUrl = "http://firstcopy.co.in/firstcopy/register_user.php";
 //   private static final String localRegisterUrl = "http://192.168.0.102/register_user.php";
    private String status = "none";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressBar = findViewById(R.id.progressBar1);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
       // etReferralCode = findViewById(R.id.etReferralCode);

        btRegister = findViewById(R.id.btRegister);
        btgoToSigin = findViewById(R.id.btGoToSignin);

        btRegister.setOnClickListener(this);
        btgoToSigin.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btRegister:
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                emailid = etEmail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
            //    referralCode = etReferralCode.getText().toString();

                if (!isValidEmail(emailid)){
                    Toast.makeText(getApplicationContext(),"Please enter a valid email address",Toast.LENGTH_SHORT).show();
                }else {

                 //   Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//                    Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
//                    Pattern lowerCasePatten = Pattern.compile("[a-z ]");
//                    Pattern digitCasePatten = Pattern.compile("[0-9 ]");

                    if (firstName.isEmpty() || lastName.isEmpty() || emailid.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    else if (!password.equals(confirmPassword))
                        Toast.makeText(getApplicationContext(), "Oops passwords didn't match", Toast.LENGTH_SHORT).show();
                    else if (password.length() < 8)
                        Toast.makeText(getApplicationContext(),"Password must be at least 8 characters long",Toast.LENGTH_SHORT).show();
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        new RegisterUser().execute("");
                    }
                }



                break;
            case R.id.btGoToSignin:
                Intent intent = new Intent(Register.this,Signin.class);
                startActivity(intent);
                break;
        }
    }


    public class RegisterUser extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        BufferedReader reader;
        InputStream is;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(registerUrl);     //Change Urls for Local and Server Accordingly
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                outputStream = connection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("firstName","UTF-8")+"="+URLEncoder.encode(firstName,"UTF-8")+"&"
                        +URLEncoder.encode("lastName","UTF-8")+"="+URLEncoder.encode(lastName,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailid,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));

                StringBuilder sb = new StringBuilder();
                String line  = "";

                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                if (sb.length() == 0)
                    return "Problem while connecting with server";

                result  = getDataFromJson(sb.toString());

                reader.close();
                is.close();
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
            if (s.equals("200")){
                Toast.makeText(getApplicationContext(),"Account Created Successfully. Please Login to continue",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this,Signin.class);
                startActivity(intent);

                progressBar.setVisibility(View.INVISIBLE);
            }else if (s.equals("404")){
                Toast.makeText(getApplicationContext(),"Problem while creating account",Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
            }else {
                Toast.makeText(getApplicationContext(),"Cannot connect to the server",Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }


    private String getDataFromJson(String jsonString){
        String res="";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            res = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res;
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

//    private String generateReferralCode(int codeLength){
//        String code = "";
//        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
//        StringBuilder sb = new StringBuilder();
//        Random random = new SecureRandom();
//        for (int i = 0; i < codeLength; i++){
//            char c = chars[random.nextInt(chars.length)];
//            sb.append(c);
//        }
//
//        code = sb.toString();
//        return code;
//    }
}
