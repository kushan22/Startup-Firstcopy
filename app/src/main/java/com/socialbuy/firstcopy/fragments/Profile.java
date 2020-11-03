package com.socialbuy.firstcopy.fragments;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.socialbuy.firstcopy.Basehome;
import com.socialbuy.firstcopy.ProfileActivity;
import com.socialbuy.firstcopy.R;
import com.socialbuy.firstcopy.authentication.Register;
import com.socialbuy.firstcopy.authentication.Signin;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment  {


   // private boolean isFragmentLoaded = false;
    private Context context;

    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile,container,false);

//        setUserVisibleHint(getUserVisibleHint());

        Intent intent = new Intent(context, ProfileActivity.class);
        startActivity(intent);
        getActivity().finish();




        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            if (context != null) {
//
//                // isFragmentLoaded = true;
//            }
//        }
//    }
}
