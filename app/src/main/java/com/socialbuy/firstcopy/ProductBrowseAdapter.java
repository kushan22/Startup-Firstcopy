package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.*;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kushansingh on 23/09/17.
 */

public class ProductBrowseAdapter extends RecyclerView.Adapter<ProductBrowseAdapter.ProductBrowseViewHolder> {

    private Context context;
    // private ArrayList<String> profile_links,image_links,descriptions;
    private ArrayList<Products> productses;

    // private com.android.volley.toolbox.ImageLoader imageLoader;
    //com.android.volley.toolbox.ImageLoader imageLoader;





    public ProductBrowseAdapter(Context context, ArrayList<Products> productses){

        this.context = context;
//        this.profile_links = profile_links;
//        this.image_links = image_links;
//        this.descriptions = descriptions;
        this.productses = productses;

       // imageLoader1 = new com.socialbuy.firstcopy.ImageLoader(context);

    }

    @Override
    public ProductBrowseAdapter.ProductBrowseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_products,parent,false);
        ProductBrowseAdapter.ProductBrowseViewHolder ah = new ProductBrowseAdapter.ProductBrowseViewHolder(v);

        return ah;
    }

    @Override
    public void onBindViewHolder(ProductBrowseAdapter.ProductBrowseViewHolder holder, final int position) {

        final Products product = productses.get(position);


        if (Long.parseLong(product.getNumberofdays()) <= 30){
            holder.ib.setVisibility(View.VISIBLE);
        }else {
            holder.ib.setVisibility(View.INVISIBLE);
        }


//        holder.tv2.setText(product.getDateofcreation());
//
//        holder.tv3.setText(product.getProductDesc());

        //  Log.i("IMAGE_LINK",image_links.get(position));

        ImageView iv = holder.imageView;
     //   imageLoader1.DisplayImage(product.getImageUrl(),iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProductDetails.class);
                intent.putExtra("USERNAME",product.getUserName());
                intent.putExtra("DATEOFCREATION",product.getDateofcreation());
                intent.putExtra("IMAGEURL",product.getImageUrl());
                intent.putExtra("DESC",product.getProductDesc());
                intent.putExtra("PROFILE_LINK",product.getProfileLink());
                intent.putExtra("SELLER_BIO",product.getSellerBio());
                intent.putExtra("PHONE",product.getSellerPhoneNumber());
                intent.putExtra("ACTIVITY","2");
                intent.putExtra("NUMBEROFDAYS",product.getNumberofdays());
                context.startActivity(intent);
            }
        });

        Picasso.with(context).load(product.getImageUrl()).
                config(Bitmap.Config.RGB_565).
                placeholder(R.drawable.stub).
                error(R.mipmap.ic_launcher).into(iv);




//        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
//        imageLoader.get(product.getImageUrl(), com.android.volley.toolbox.ImageLoader.getImageListener(holder.imageView,R.drawable.stub,R.mipmap.ic_launcher_round));
//        iv.setImageUrl(product.getImageUrl(),imageLoader);



//        holder.tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(product.getProfileLink()));
//                context.startActivity(intent);
//            }
//        });

    }



    @Override
    public int getItemCount() {
        return productses.size();
    }

    class ProductBrowseViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private ImageButton ib;

        public ProductBrowseViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivProductImage);
            ib = itemView.findViewById(R.id.ibNewProduct);
//            tv1 = itemView.findViewById(R.id.tvUsername);
//            tv2 = itemView.findViewById(R.id.tvDate);
//            tv3 = itemView.findViewById(R.id.tvDescription);
//            civ = itemView.findViewById(R.id.circleImageView);
        }
    }
}
