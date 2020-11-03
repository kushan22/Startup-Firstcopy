package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kushansingh on 22/09/17.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    private Context context;
    private ArrayList<String> userNames,imageUrls,profileLinks,productDescs,dateofCreations,sellerBio,sellerPhoneNumber;
   // private ImageLoader imageLoader;


    public WishListAdapter(Context context, ArrayList<String> userNames, ArrayList<String> imageUrls, ArrayList<String> profileLinks, ArrayList<String> productDescs, ArrayList<String> dateofCreations, ArrayList<String> sellerBio, ArrayList<String> sellerPhoneNumber) {

        this.context = context;
        this.userNames = userNames;
        this.imageUrls = imageUrls;
        this.profileLinks = profileLinks;
        this.productDescs = productDescs;
        this.dateofCreations = dateofCreations;
      //  this.imageLoader = new ImageLoader(context);
        this.sellerBio = sellerBio;
        this.sellerPhoneNumber = sellerPhoneNumber;

    }

    @Override
    public WishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_products,parent,false);
        WishListViewHolder holder = new WishListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(WishListViewHolder holder, final int position) {

        final ImageView imageView = holder.iv;
       // imageLoader.DisplayImage(imageUrls.get(position),imageView);
        Picasso.with(context).load(imageUrls.get(position)).config(Bitmap.Config.RGB_565).placeholder(R.drawable.stub).error(R.mipmap.ic_launcher).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetails.class);
                intent.putExtra("USERNAME",userNames.get(position));
                intent.putExtra("DATEOFCREATION",dateofCreations.get(position));
                intent.putExtra("IMAGEURL",imageUrls.get(position));
                intent.putExtra("DESC",productDescs.get(position));
                intent.putExtra("PROFILE_LINK",profileLinks.get(position));
                intent.putExtra("SELLER_BIO",sellerBio.get(position));
                intent.putExtra("PHONE",sellerPhoneNumber.get(position));
                intent.putExtra("ACTIVITY","4");

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    class WishListViewHolder extends RecyclerView.ViewHolder{

        ImageView  iv;


        public WishListViewHolder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
