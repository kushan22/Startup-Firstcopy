package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socialbuy.firstcopy.pojo.FactoryProducts;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by kushansingh on 28/10/17.
 */

public class FactoryAdapter extends RecyclerView.Adapter<FactoryAdapter.FactoryHolder> {

    ArrayList<FactoryProducts> factoryProductses;
    Context context;

    public FactoryAdapter(Context context, ArrayList<FactoryProducts> factoryProductses) {
        this.factoryProductses = factoryProductses;
        this.context = context;
    }

    @Override
    public FactoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_factory,parent,false);

        FactoryHolder factoryHolder = new FactoryHolder(v);

        return factoryHolder;
    }

    @Override
    public void onBindViewHolder(FactoryHolder holder, int position) {

       final FactoryProducts factoryProduct = factoryProductses.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        holder.tvFactoryName.setTypeface(typeface);

        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Bold.ttf");
        holder.tvFactoryPrice.setTypeface(typeface1);

       ImageView iv = holder.ivFactoryImage;
       Picasso.with(context).load(factoryProduct.getFactoryProductImageLink()).
                config(Bitmap.Config.RGB_565).
                placeholder(R.drawable.stub).
                error(R.mipmap.ic_launcher).into(iv);
       final String productName = factoryProduct.getFactoryProductName();
       if (productName.length() > 15){
           String trimmedProductName = productName.substring(0,15);
           holder.tvFactoryName.setText(trimmedProductName + "...");
       }else{
           holder.tvFactoryName.setText(productName);
       }
           // holder.tvFactoryName.setText(factoryProduct.getFactoryProductName());
           holder.tvFactoryPrice.setText(context.getResources().getString(R.string.Rs) + factoryProduct.getFactoryProductPrice());

//        Log.i("FACTORY_SIZES",factoryProduct.getFactoryProductSizes());

       iv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             //  Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
               SharedPreferences productPref = context.getSharedPreferences("productPref",Context.MODE_PRIVATE);
               SharedPreferences.Editor productEditor = productPref.edit();
               productEditor.putString("NAME",factoryProduct.getFactoryProductName());
               productEditor.putString("IMAGE",factoryProduct.getFactoryProductImageLink());
               productEditor.putString("DESC",factoryProduct.getFactoryProductDesc());
               productEditor.putString("PRICE",factoryProduct.getFactoryProductPrice());
               productEditor.putString("SIZES",factoryProduct.getFactoryProductSizes());
               productEditor.putString("product_link",factoryProduct.getFactoryProductLink());
               productEditor.commit();
               Intent sendIntent = new Intent(context,FactoryproductDetails.class);
               //sendIntent.putExtra("NAME",factoryProduct.getFactoryProductName());
              // Log.i("PRODUCT_NAME",factoryProduct.getFactoryProductName());
//               sendIntent.putExtra("IMAGE",factoryProduct.getFactoryProductImageLink());
//               sendIntent.putExtra("DESC",factoryProduct.getFactoryProductDesc());
//               sendIntent.putExtra("PRICE",factoryProduct.getFactoryProductPrice());
//               sendIntent.putExtra("SIZES",factoryProduct.getFactoryProductSizes());
//               sendIntent.putExtra("product_link",factoryProduct.getFactoryProductLink());
               context.startActivity(sendIntent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return factoryProductses.size();
    }

    class FactoryHolder extends RecyclerView.ViewHolder{

        private TextView tvFactoryName,tvFactoryPrice;
        private ImageView ivFactoryImage;

        public FactoryHolder(View itemView) {
            super(itemView);

            tvFactoryName = itemView.findViewById(R.id.tvFactoryName);
            tvFactoryPrice = itemView.findViewById(R.id.tvFactoryPrice);
            ivFactoryImage = itemView.findViewById(R.id.ivFactoryImage);
        }
    }
}
