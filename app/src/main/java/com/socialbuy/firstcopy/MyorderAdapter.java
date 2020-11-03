package com.socialbuy.firstcopy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.pojo.OrderDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kushansingh on 09/11/17.
 */

public class MyorderAdapter extends RecyclerView.Adapter<MyorderAdapter.MyorderHolder> {

    private Context context;
    private ArrayList<OrderDetails> orderDetails;
    private static final String CANCEL_URL = "http://www.firstcopy.co.in/firstcopy/cancelorder.php";
    private ProgressBar progressBar;


    public MyorderAdapter(Context context, ArrayList<OrderDetails> orderDetails, ProgressBar progressBar) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.progressBar = progressBar;

    }

    @Override
    public MyorderHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_myorders,parent,false);
        MyorderHolder myorderHolder = new MyorderHolder(v);
        return myorderHolder;
    }

    @Override
    public void onBindViewHolder(MyorderHolder holder, final int position) {
        String date = "";
        final OrderDetails order = orderDetails.get(position);

        final Typeface typeface  = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        final Typeface typeFaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/Raleway-Bold.ttf");

        holder.tvStatus.setTypeface(typeFaceBold);
        holder.tvProductName.setTypeface(typeface);
        holder.tvDateCreated.setTypeface(typeface);
        holder.tvPrice.setTypeface(typeface);
        holder.tvOrderid.setTypeface(typeface);

        String dateString = order.getDate();
        if (dateString.length() <= 12){
            date = dateString;
        }else {
            int index = dateString.indexOf("T");
            date = dateString.substring(0, index);
        }

        if (order.getStatus().equals("0") || order.getStatus().equals("2")){
            holder.tvStatus.setText("In Process");
        }else if (order.getStatus().equals("1")){
            holder.tvStatus.setText("Delivered");
        }

        holder.tvOrderid.setText("ORDER NO: " + order.getOrderid());
        holder.tvPrice.setText(context.getResources().getString(R.string.Rs) + order.getPrice());
        holder.tvDateCreated.setText(date);
        holder.tvProductName.setText(order.getProductName());
        holder.tvOrderSize.setText(order.getSize());

        ImageView iv = holder.ivproductImage;

        Picasso.with(context).load(order.getProductImage()).
                config(Bitmap.Config.RGB_565).
                placeholder(R.drawable.stub).
                error(R.mipmap.ic_launcher).into(iv);

        if (order.getStatus().equals("2")){
            holder.btCancel.setVisibility(View.INVISIBLE);
        }

        holder.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.custom_settings,null,false);
                builder.setView(view);

                TextView tvHeading,tvContent;
                tvHeading = view.findViewById(R.id.tvCustomHeading);
                tvContent = view.findViewById(R.id.tvCustomContent);

                tvHeading.setTypeface(typeFaceBold);
                tvContent.setTypeface(typeface);

                tvHeading.setText("Confirm Cancellation");
                tvContent.setText("Would You like to cancel your order ?");

                builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        new CancelOrder().execute(order.getOrderid(),order.getSize(),order.getProductName());
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            }
        });

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    class MyorderHolder extends RecyclerView.ViewHolder{

        private TextView tvStatus,tvOrderid,tvPrice,tvDateCreated,tvProductName,tvOrderSize;
        private ImageView ivproductImage;
        private Button btCancel;

        public MyorderHolder(View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderid = itemView.findViewById(R.id.tvOrderNumber);
            tvPrice = itemView.findViewById(R.id.tvPriceofItem);
            tvDateCreated = itemView.findViewById(R.id.tvDatePlaced);
            tvProductName = itemView.findViewById(R.id.tvMyorderProductName);
            ivproductImage = itemView.findViewById(R.id.ivOrderImage);
            btCancel = itemView.findViewById(R.id.btCancelOrder);
            tvOrderSize = itemView.findViewById(R.id.tvMyOrderProductSize);
        }
    }


    public class CancelOrder extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        BufferedReader reader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            Uri uri = Uri.parse(CANCEL_URL).buildUpon().appendQueryParameter("ORDERID",strings[0]).
                                                        appendQueryParameter("ORDERSIZE",strings[1]).
                                                        appendQueryParameter("PRODUCTNAME",strings[2]).build();

            try {
                URL url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder sb = new StringBuilder();

                while ((line=reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                result = getCancelOrderDataFromJson(sb.toString());



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
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context,"Order Cancelled Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,Basehome.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }else if (s.equals("404")){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context,"Unable to cancel Order",Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context,"Problem while Connecting with our server",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private String getCancelOrderDataFromJson(String jsonString){
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
