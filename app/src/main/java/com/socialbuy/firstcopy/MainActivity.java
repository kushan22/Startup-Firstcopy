package com.socialbuy.firstcopy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUESTCODE = 200;
    private ImageView iv;
    private RotateAnimation anim;
    private Animation animation1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.ivSplash);
//        Button crashButton = new Button(this);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });
//        addContentView(crashButton,
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));

        // if (checkPermission()){

        animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide);
        iv.startAnimation(animation1);
//            anim = new RotateAnimation(0f, 350f, 15f, 15f);
//            anim.setInterpolator(new LinearInterpolator());
//           anim.setRepeatCount(Animation.INFINITE);
//            anim.setDuration(700);700

        //iv.startAnimation(anim);

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(3000);
                    iv.setAnimation(null);
                    Intent intent = new Intent(MainActivity.this, Versioncheck.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();


        //       }else {
        //       requestPermission();
        //       }


    }




//

}
