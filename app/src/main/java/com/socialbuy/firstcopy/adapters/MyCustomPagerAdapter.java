package com.socialbuy.firstcopy.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialbuy.firstcopy.FactoryproductDetails;
import com.socialbuy.firstcopy.R;

import java.util.ArrayList;

/**
 * Created by kushansingh on 02/01/18.
 */

public class MyCustomPagerAdapter extends PagerAdapter {


    private Context context;
    private ArrayList<String> firstNameList,lastNameList;
    private ArrayList<Integer> timeInMinutes;

    public MyCustomPagerAdapter(Context context, ArrayList<String> firstNameList, ArrayList<String> lastNameList, ArrayList<Integer> timeInMinutes) {
        this.context = context;
        this.firstNameList = firstNameList;
        this.lastNameList = lastNameList;
        this.timeInMinutes = timeInMinutes;
    }

    @Override
    public int getCount() {
        return firstNameList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
       return view.equals(object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_buyer,container,false);

//        TextView textView = v.findViewById(R.id.tvBuyerDetail);
//        textView.setText(buyerDetails.get(position));
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        assert v != null;
        TextView textView = v.findViewById(R.id.tvBuyerDetail);
        textView.setTypeface(typeface);
        textView.setText(firstNameList.get(position) + " " + lastNameList.get(position) + " purchased this " + timeInMinutes.get(position) + " minutes ago");

        container.addView(v,0);


        return v;
    }



    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
