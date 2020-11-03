package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kushansingh on 29/10/17.
 */

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeHolder> {

    private Context context;
    private ArrayList<String> sizes,stocks;
    private int mPosition = -1;
    private RecyclerView rvSize;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;



    public SizeAdapter(Context context, ArrayList<String> sizes, RecyclerView rvSize) {
        this.context = context;
        this.sizes = sizes;
        this.rvSize = rvSize;
        this.stocks = stocks;
    }


    @Override
    public SizeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_size,parent,false);
        SizeHolder sizeHolder = new SizeHolder(v);
        return sizeHolder;
    }

    @Override
    public void onBindViewHolder(SizeHolder holder, final int position) {

        holder.tvSize.setText(sizes.get(position));



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                notifyDataSetChanged();
            }
        });

        if (mPosition == position){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#567845"));
            holder.tvSize.setTextColor(Color.parseColor("#ffffff"));
            preferences = context.getSharedPreferences("SIZEPREF",Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString("SIZE",holder.tvSize.getText().toString());
            editor.commit();

        } else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvSize.setTextColor(Color.parseColor("#000000"));
        }



    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }






    class SizeHolder extends RecyclerView.ViewHolder{

        private TextView tvSize;
        public LinearLayout linearLayout;

        public SizeHolder(View itemView) {
            super(itemView);
            tvSize = itemView.findViewById(R.id.tvSize);
            linearLayout = itemView.findViewById(R.id.size_item);

        }



    }
}
