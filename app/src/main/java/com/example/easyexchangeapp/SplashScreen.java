package com.example.easyexchangeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easyexchangeapp.Activity.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView logo;
    TextView titleTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED,WindowManager.LayoutParams.FLAGS_CHANGED);
        logo=findViewById(R.id.logo);
        titleTx=findViewById(R.id.title_tv);


        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(logo, scaleX, scaleY);
        animator.setDuration(1000);
        animator.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(titleTx, View.ALPHA, 1f);
        animator2.setDuration(500);
        animator2.setStartDelay(100);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator2);
        set.setDuration(4000);

        set.setDuration(4000);
        set.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);

    }
}