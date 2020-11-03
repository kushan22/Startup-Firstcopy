package com.socialbuy.firstcopy;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.socialbuy.firstcopy.authentication.Signin;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, View.OnClickListener {

    private Button btGetCurrentLocation,btSave;
    private Location myLocation;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_SETTING = 1;
    private static final int REQUEST_PERMISSION = 2;
    private EditText etCity,etLocality,etFlatno,etPincode,etState,etLandmark,etName,etPhone;
    private String city,locality,flatno,pinCode,state,landmark,name,phone,email;
    private static final String ADD_ADDR_URL = "http://www.firstcopy.co.in/firstcopy/add_address.php";
    private ProgressBar progressBar;
    private TextView tvTaptoAutoFill;
    Typeface typefaceReg,typefaceBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        typefaceReg = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
        typefaceBold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");

        btGetCurrentLocation = findViewById(R.id.btGetCurrentLocation);
        btSave = findViewById(R.id.btSaveAddress);
        btGetCurrentLocation.setTypeface(typefaceReg);
        btSave.setTypeface(typefaceReg);
        progressBar = findViewById(R.id.progressBar1);

        etCity = findViewById(R.id.etCity);
        etLocality = findViewById(R.id.etLocality);
        etFlatno = findViewById(R.id.etFlatNo);
        etPincode = findViewById(R.id.etPincode);
        etState = findViewById(R.id.etState);
        etLandmark = findViewById(R.id.etLandmark);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhoneNumber);
        tvTaptoAutoFill = findViewById(R.id.tvTaptoAutoFill);
        etCity.setTypeface(typefaceReg);
        etLocality.setTypeface(typefaceReg);
        etFlatno.setTypeface(typefaceReg);
        etPincode.setTypeface(typefaceReg);
        etState.setTypeface(typefaceReg);
        etLandmark.setTypeface(typefaceReg);
        etName.setTypeface(typefaceReg);
        etPhone.setTypeface(typefaceReg);
        tvTaptoAutoFill.setTypeface(typefaceReg);

        btGetCurrentLocation.setOnClickListener(this);
        btSave.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(Signin.PREF_NAME, Context.MODE_PRIVATE);
        email = preferences.getString(Signin.PREF_EMAIL,"none");





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
    public void onLocationChanged(Location location) {
        myLocation = location;
        if (myLocation != null){
            Double latitude = myLocation.getLatitude();
            Double longitude = myLocation.getLongitude();

            googleApiClient.disconnect();
            getAddress(latitude,longitude);

        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
            checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btGetCurrentLocation:
                tvTaptoAutoFill.setText("Getting location. Please wait!");
                progressBar.setVisibility(View.VISIBLE);
                setUpGClient();
                break;
            case R.id.btSaveAddress:
                city = etCity.getText().toString();
                locality = etLocality.getText().toString();
                flatno = etFlatno.getText().toString();
                pinCode = etPincode.getText().toString();
                state = etState.getText().toString();
                landmark = etLandmark.getText().toString();
                name = etName.getText().toString();
                phone = etPhone.getText().toString();

                if (city.isEmpty() || locality.isEmpty() || flatno.isEmpty() || pinCode.isEmpty() || state.isEmpty() || name.isEmpty() || phone.isEmpty())
                    Toast.makeText(getApplicationContext(),"All fields except landmark are required",Toast.LENGTH_SHORT).show();
                else if (!isValidPhone(phone))
                    Toast.makeText(getApplicationContext(),"Please enter a valid 10 digit phone number",Toast.LENGTH_SHORT).show();
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    new AddAddress().execute("");
                }
                break;
        }

    }



    private synchronized void setUpGClient(){
        googleApiClient = new GoogleApiClient.Builder(Address.this).
                           addConnectionCallbacks(Address.this).
                           addOnConnectionFailedListener(Address.this).
                           addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(Address.this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION);
            }
        }else{
            getMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(Address.this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }else {
            if (shouldShowRequestPermissionRationale("android.permission.ACCESS_FINE_LOCATION")){
                showMessageOKCancel("You need to allow access to both the permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"},REQUEST_PERMISSION);
                    }
                });
                finish();

            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Address.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void getMyLocation(){
        if (googleApiClient != null){
            if (googleApiClient.isConnected()){
                int permissionLocation = ContextCompat.checkSelfPermission(Address.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED){
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

                    builder.setAlwaysShow(true);


                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

                    final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                            final Status status = locationSettingsResult.getStatus();
                            final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
                            switch (status.getStatusCode()){
                                case LocationSettingsStatusCodes.SUCCESS:
                                    int permissionLocation = ContextCompat.checkSelfPermission(Address.this,Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED){
                                        myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                    }

                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(Address.this,REQUEST_SETTING);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;

                            }
                        }
                    });



                }else {

                }
            }else {
                Toast.makeText(Address.this,"Disconnected",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(Address.this,"Null",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_SETTING:
                switch (resultCode){
                    case RESULT_OK:
                        getMyLocation();
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

    private void getAddress(Double lat,Double longt){

        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(Address.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat,longt,1);
            String addressLine = addresses.get(0).getAddressLine(0);

            String city = addresses.get(0).getLocality();
            String pincode = addresses.get(0).getPostalCode();
            String state = addresses.get(0).getAdminArea();
            progressBar.setVisibility(View.GONE);
            etCity.setText(city);
            etState.setText(state);
            etPincode.setText(pincode);
            etLocality.setText(addressLine);
            tvTaptoAutoFill.setText("Tap to auto fill the address fields");
            Log.i("PINCODE",pincode);
        } catch (IOException e) {
            progressBar.setVisibility(View.GONE);
            tvTaptoAutoFill.setText("Tap to auto fill the address fields");
            e.printStackTrace();
        }

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

    private class AddAddress extends AsyncTask<String,String,String>{
        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result;

        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(ADD_ADDR_URL).buildUpon().appendQueryParameter("email", email).
                    appendQueryParameter("name", name).appendQueryParameter("phoneNumber",phone).
                    appendQueryParameter("address", "Locality : " + locality + " Flat no: " + flatno + " Landmark: " + landmark ).
                    appendQueryParameter("city", city).
                    appendQueryParameter("state", state).
                    appendQueryParameter("pincode", pinCode).build();

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


                result = getAddressStatusFromJson(sb.toString());




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
            if (s != null){
                if (s.equals("200")){

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Address added Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Address.this,OrderSummaryandCheckout.class);
                    startActivity(intent);
                    finish();
                }else if (s.equals("404")){

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Problem while adding address",Toast.LENGTH_SHORT).show();
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


    private String getAddressStatusFromJson(String jsonString){
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
