package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kushansingh on 14/11/17.
 */

public class FilterAdapter extends BaseAdapter {
    private Context context;
    private String[] filters;
    public Set<String> filtersSet = new HashSet<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int mPosition = -1;



    public FilterAdapter(Context context, String[] filters) {
        this.context = context;
        this.filters = filters;


    }

    public FilterAdapter(){

    }



    @Override
    public int getCount() {
        return filters.length;
    }

    @Override
    public Object getItem(int position) {
        return filters[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_filter,parent,false);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        preferences = context.getSharedPreferences("FILTERPREF",Context.MODE_PRIVATE);


        final TextView tvFilter = view.findViewById(R.id.tvFilter);
        final ImageButton ibFilter = view.findViewById(R.id.ibFilter);

        LinearLayout linearLayout = view.findViewById(R.id.llFilterList);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mPosition = position;
              notifyDataSetChanged();
            }
        });

        tvFilter.setTypeface(typeface);

        tvFilter.setText(filters[position]);

        if (mPosition == position){
            ibFilter.setVisibility(View.VISIBLE);
           // filtersSet.add(tvFilter.getText().toString());
            editor = preferences.edit();
            editor.putString("MYFILTERS",tvFilter.getText().toString());
            editor.commit();
        }else {
            ibFilter.setVisibility(View.INVISIBLE);
        }














        return view;
    }
}

