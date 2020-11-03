package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



/**
 * Created by kushansingh on 04/09/17.
 */

public class LatestPostAdapter extends RecyclerView.Adapter<LatestPostAdapter.LatestPostViewHolder> {

    Context context;
    ArrayList<String> latestPostsImageUrl,latestPostsUsername,latestPostsProfileLink,latestPostsProductDesc,latestPostsDateofCreation,latestPostSellerBio,latestPostSellerPhoneNumber;
    //com.android.volley.toolbox.ImageLoader imageLoader;
   // private ImageView imageViewError;

    public LatestPostAdapter(Context context, ArrayList<String> latestPostsImageUrl, ArrayList<String> latestPostsUsername, ArrayList<String> latestPostsProfileLink, ArrayList<String> latestPostsProductDesc, ArrayList<String> latestPostsDateofCreation, ArrayList<String> latesPostSellerBio, ArrayList<String> latestPostSellerPhoneNumber) {

        this.context = context;
        this.latestPostsImageUrl = latestPostsImageUrl;
        this.latestPostsUsername = latestPostsUsername;
        this.latestPostsProfileLink = latestPostsProfileLink;
        this.latestPostsProductDesc = latestPostsProductDesc;
        this.latestPostsDateofCreation = latestPostsDateofCreation;
        this.latestPostSellerBio = latesPostSellerBio;
        this.latestPostSellerPhoneNumber = latestPostSellerPhoneNumber;

       // this.imageLoader = new ImageLoader(context);
    }



    @Override
    public LatestPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false);

        LatestPostViewHolder holder = new LatestPostViewHolder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(LatestPostViewHolder holder, final int position) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size  = new Point();
        display.getSize(size);



        final ImageView imageView = holder.iv;
        Picasso.with(context).load(latestPostsImageUrl.get(position)).placeholder(R.drawable.stub).error(R.mipmap.ic_launcher).into(imageView);
       // imageLoader.DisplayImage(latestPostsImageUrl.get(position),imageView);
//        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
//        imageLoader.get(latestPostsImageUrl.get(position), com.android.volley.toolbox.ImageLoader.getImageListener(imageView,R.drawable.stub,R.mipmap.ic_launcher_round));
//        imageView.setImageUrl(latestPostsImageUrl.get(position),imageLoader);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetails.class);
                intent.putExtra("USERNAME",latestPostsUsername.get(position));
                intent.putExtra("DATEOFCREATION",latestPostsDateofCreation.get(position));
                intent.putExtra("IMAGEURL",latestPostsImageUrl.get(position));
                intent.putExtra("DESC",latestPostsProductDesc.get(position));
                intent.putExtra("PROFILE_LINK",latestPostsProfileLink.get(position));
                intent.putExtra("SELLER_BIO",latestPostSellerBio.get(position));
                intent.putExtra("PHONE",latestPostSellerPhoneNumber.get(position));
                intent.putExtra("ACTIVITY","3");

                context.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return latestPostsImageUrl.size();
    }


    public class LatestPostViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public LatestPostViewHolder(View itemView) {
            super(itemView);


            iv = itemView.findViewById(R.id.ivLatestProductImage);


        }
    }



}
