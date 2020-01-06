package com.mmuhamadamirzaidi.elkencarapptest.Modules;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mmuhamadamirzaidi.elkencarapptest.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView SplashLogo;
    private int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View overlay = findViewById(R.id.splashlogo);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        SplashLogo = (ImageView) findViewById(R.id.splash_logo_startup);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent splash = new Intent(SplashActivity.this, ListCarActivity.class);
                startActivity(splash);
                finish();
            }
        }, SPLASH_TIME_OUT);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.splashanimation);
        SplashLogo.startAnimation(myanim);
    }
}
