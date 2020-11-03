package com.socialbuy.firstcopy;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.instamojo.android.Instamojo;

/**
 * Created by kushansingh on 30/10/17.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
       // Instamojo.initialize(this);
    }
}
