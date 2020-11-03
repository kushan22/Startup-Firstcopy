package com.socialbuy.firstcopy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socialbuy.firstcopy.pojo.Address;

import java.util.ArrayList;

/**
 * Created by kushansingh on 05/11/17.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {

    private Context context;
    private ArrayList<Address> addresses;
    private RecyclerView rvAddress;
    private int mPosition = -1;
    public static final String PREF_ADDRESS = "address";
    public static final String PREF_NAME = "fullname";
    public static final String PREF_NUMBER = "phonenumber";
    public static final String PREF_ADDRESSLINE = "addressline";
    public static final String PREF_CITY  = "city";
    public static final String PREF_STATE = "state";
    public static final String PREF_PINCODE = "pincode";


    public AddressAdapter(Context context, ArrayList<Address> addresses, RecyclerView rvAddress) {

        this.context  = context;
        this.addresses = addresses;
        this.rvAddress = rvAddress;
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_address,parent,false);
        AddressHolder addressHolder = new AddressHolder(v);
        return addressHolder;
    }

    @Override
    public void onBindViewHolder(final AddressHolder holder, final int position) {
        final Address address = addresses.get(position);
        String addressLine = "";
        if (address.getAddressLine().length() > 20){
            addressLine = address.getAddressLine().substring(0,20);
            holder.tv1.setText(addressLine + "...");
        }else {
            addressLine  = address.getAddressLine();
            holder.tv1.setText(addressLine);
        }

        holder.tv2.setText(address.getCity());
        holder.tv3.setText(address.getState());
        holder.tv4.setText(address.getPincode());
        holder.tv5.setText(address.getPhoneNumber());
        holder.tv6.setText(address.getFullName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position;
                notifyDataSetChanged();
                SharedPreferences preferences = context.getSharedPreferences(PREF_ADDRESS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREF_NAME,address.getFullName());
                editor.putString(PREF_NUMBER,address.getPhoneNumber());
                editor.putString(PREF_ADDRESSLINE,address.getAddressLine());
                editor.putString(PREF_CITY,address.getCity());
                editor.putString(PREF_STATE,address.getState());
                editor.putString(PREF_PINCODE,address.getPincode());
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
        return addresses.size();
    }

    class AddressHolder extends RecyclerView.ViewHolder{

        private TextView tv1,tv2,tv3,tv4,tv5,tv6;
        private CheckBox checkBox;
        private LinearLayout linearLayout;

        public AddressHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tvShippingAddress);
            tv2 = itemView.findViewById(R.id.tvShippingCity);
            tv3 = itemView.findViewById(R.id.tvShippingState);
            tv4 = itemView.findViewById(R.id.tvShippingPincode);
            tv5 = itemView.findViewById(R.id.tvAddressPhoneNumber);
            tv6 = itemView.findViewById(R.id.tvAddressProductName);
            checkBox = itemView.findViewById(R.id.cbAddress);
            linearLayout = itemView.findViewById(R.id.llChooseAddress);
        }
    }
}
