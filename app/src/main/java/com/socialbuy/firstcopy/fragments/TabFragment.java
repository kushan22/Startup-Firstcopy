package com.socialbuy.firstcopy.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socialbuy.firstcopy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    private TabLayout tabGender;
    private ViewPager viewPager;
    private static final int NUMBEROFITEMS = 2;
    private static final String[] ITEMS = {"Men","Women"};

    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab,container,false);


        tabGender = v.findViewById(R.id.tabLayout1);
        viewPager = v.findViewById(R.id.viewPager1);

        viewPager.setAdapter(new TabFragmentAdapter(getChildFragmentManager()));

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                tabGender.setupWithViewPager(viewPager);
            }
        });


        return v;
    }


    class TabFragmentAdapter extends FragmentPagerAdapter{

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Men men = new Men();
                    return men;
                case 1:
                    Women women = new Women();
                    return women;

            }
             return null;

        }

        @Override
        public int getCount() {
            return NUMBEROFITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ITEMS[position];
        }
    }

}
