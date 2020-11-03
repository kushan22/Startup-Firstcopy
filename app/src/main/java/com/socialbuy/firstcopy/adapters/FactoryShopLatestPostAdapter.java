package com.socialbuy.firstcopy.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.socialbuy.firstcopy.FactoryproductDetails;
import com.socialbuy.firstcopy.LatestPostAdapter;
import com.socialbuy.firstcopy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kushansingh on 30/12/17.
 */

public class FactoryShopLatestPostAdapter extends RecyclerView.Adapter<FactoryShopLatestPostAdapter.LatestPostHolder> {
    private Context context;
    private ArrayList<String> latestPostsImageUrl,latestPostProductName,latestPostsProductDesc,latestPostPrices,latestPostSizes,latestPostProductLinks;


    public FactoryShopLatestPostAdapter(Context context, ArrayList<String> latestPostsImageUrl, ArrayList<String> latesPostProductName, ArrayList<String> latestPostsProductDesc, ArrayList<String> latestPostPrices, ArrayList<String> latestPostSizes, ArrayList<String> latestPostProductLinks) {
        this.context = context;
        this.latestPostProductName = latesPostProductName;
        this.latestPostsImageUrl = latestPostsImageUrl;
        this.latestPostPrices = latestPostPrices;
        this.latestPostsProductDesc = latestPostsProductDesc;
        this.latestPostSizes = latestPostSizes;
        this.latestPostProductLinks = latestPostProductLinks;
    }

    @Override
    public LatestPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false);

        FactoryShopLatestPostAdapter.LatestPostHolder holder = new FactoryShopLatestPostAdapter.LatestPostHolder(v);


        return holder;

    }

    @Override
    public void onBindViewHolder(LatestPostHolder holder, final int position) {

        final ImageView imageView = holder.iv;
        Picasso.with(context).load(latestPostsImageUrl.get(position)).placeholder(R.drawable.stub).error(R.mipmap.ic_launcher).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences productPref = context.getSharedPreferences("productPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = productPref.edit();
                editor.putString("NAME",latestPostProductName.get(position));
                editor.putString("IMAGE",latestPostsImageUrl.get(position));
                editor.putString("DESC",latestPostsProductDesc.get(position));
                editor.putString("PRICE",latestPostPrices.get(position));
                editor.putString("SIZES",latestPostSizes.get(position));
                editor.putString("product_link",latestPostProductLinks.get(position));
                editor.commit();
                Intent intent = new Intent(context, FactoryproductDetails.class);
//                intent.putExtra("NAME",latestPostProductName.get(position));
//                intent.putExtra("IMAGE",latestPostsImageUrl.get(position));
//                intent.putExtra("DESC",latestPostsProductDesc.get(position));
//                intent.putExtra("PRICE",latestPostPrices.get(position));
//                intent.putExtra("SIZES",latestPostSizes.get(position));
//                intent.putExtra("product_link",latestPostProductLinks.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return latestPostPrices.size();
    }

    class LatestPostHolder extends RecyclerView.ViewHolder{

        ImageView iv;

        public LatestPostHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.ivLatestProductImage);
        }
    }
}
