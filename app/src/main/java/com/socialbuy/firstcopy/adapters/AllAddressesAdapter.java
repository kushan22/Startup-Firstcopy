package com.socialbuy.firstcopy.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialbuy.firstcopy.AllAddresses;
import com.socialbuy.firstcopy.R;

import java.util.ArrayList;

/**
 * Created by kushansingh on 08/01/18.
 */

public class AllAddressesAdapter extends RecyclerView.Adapter<AllAddressesAdapter.AdapterHolder> {

    private Context context;
    private ArrayList<String> nameList,addressList,phoneNumberList,pincodeList;
    private int mPosition = -1;
    public static final String PREF_ADDRESS = "allAddresses";
    public static final String PREF_NAME = "fullname";
    public static final String PREF_NUMBER = "phonenumber";
    public static final String PREF_ADDRESSLINE = "addressline";
    public static final String PREF_PINCODE = "pincode";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    Typeface typefaceReg,typeFaceBold;
    public AllAddressesAdapter(Context context, ArrayList<String> nameList, ArrayList<String> addressList, ArrayList<String> phoneNumberList, ArrayList<String> pincodeList) {

        this.nameList = nameList;
        this.addressList = addressList;
        this.phoneNumberList = phoneNumberList;
        this.pincodeList = pincodeList;
        this.context = context;
    }

    @Override
    public AdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_alladdresses,parent,false);
        AdapterHolder adapterHolder = new AdapterHolder(view);

        return adapterHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHolder holder, final int position) {

        holder.tvName.setText(nameList.get(position));
        holder.tvAddress.setText(addressList.get(position));
        holder.tvPhoneNumber.setText(phoneNumberList.get(position));

        typefaceReg = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Regular.ttf");
        typeFaceBold = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Bold.ttf");

        holder.tvName.setTypeface(typeFaceBold);
        holder.tvAddress.setTypeface(typefaceReg);
        holder.tvPhoneNumber.setTypeface(typefaceReg);





//        if (position == 0){
//            holder.checkBox.setVisibility(View.VISIBLE);
//            holder.checkBox.setChecked(true);
//        }


        holder.llChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                notifyDataSetChanged();
                preferences = context.getSharedPreferences(PREF_ADDRESS,Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString(PREF_NAME,nameList.get(position));
                editor.putString(PREF_ADDRESSLINE,addressList.get(position));
                editor.putString(PREF_NUMBER,phoneNumberList.get(position));
                editor.putString(PREF_PINCODE,pincodeList.get(position));
                editor.commit();
            }
        });

        if (mPosition == position){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(true);

        }else {
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    class AdapterHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox;
        private TextView tvName,tvAddress,tvPhoneNumber;
        private LinearLayout llChooseAddress;
        public AdapterHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.cbAddress);
            tvName = itemView.findViewById(R.id.tvAddressBuyerName);
            tvAddress = itemView.findViewById(R.id.tvShippingAddress);
            tvPhoneNumber = itemView.findViewById(R.id.tvAddressPhoneNumber);
            llChooseAddress = itemView.findViewById(R.id.llChooseAddress);
        }
    }
}
