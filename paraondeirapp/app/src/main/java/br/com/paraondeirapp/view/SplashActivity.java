package br.com.paraondeirapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.paraondeirapp.R;

public class SplashActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView ivSplash = (ImageView) findViewById(R.id.iv_splash);
        ivSplash.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        new Handler().postDelayed(this, 3000);
    }

    @Override
    public void run() {
        startActivity(new Intent(this, ListaActivity.class));
        finish();
    }
}
