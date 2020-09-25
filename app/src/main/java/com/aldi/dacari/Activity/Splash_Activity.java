package com.aldi.dacari.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.aldi.dacari.R;

public class Splash_Activity extends AppCompatActivity {

    private ImageView imageLogo;

    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        imageLogo = (ImageView) findViewById(R.id.logo);

        Animation animTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        imageLogo.startAnimation(animTop);

        Thread th = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent i = new Intent(Splash_Activity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        th.start();
    }

    public void StartCheckAnimatiom (){
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(800);
    }
}
