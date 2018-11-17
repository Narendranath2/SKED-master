package com.roostergames.www.sked;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {
    LinearLayout linearLayout;
    Animation bottomtoup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        linearLayout = (LinearLayout)findViewById(R.id.logoLeniarlayout);
        bottomtoup = AnimationUtils.loadAnimation(this,R.anim.move_bottom_to_top);
        linearLayout.setAnimation(bottomtoup);
        final SharedPreferences sharedPreferences = getSharedPreferences("logininfo",Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getString("loginstatus","").contains("yes"))
                {

                    Intent i = new Intent(SplashScreen.this,HomeScreen.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this,LoginScreen.class);
                    startActivity(i);
                    finish();
                }

            }
        },2000);

    }
}
