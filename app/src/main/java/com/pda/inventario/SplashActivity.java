package com.pda.inventario;

import java.util.Timer;
import java.util.TimerTask;

import com.example.pda_inventario.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    private String login, senha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView pda = (ImageView) findViewById(R.id.splashmove);
        final Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, R.anim.up);
        pda.startAnimation(logoMoveAnimation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            if (getIntent() != null) {
                try {
                    login = getIntent().getExtras().getString("login", "NA");
                    senha = getIntent().getExtras().getString("senha", "NA");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SplashActivity.this, "Null", Toast.LENGTH_SHORT).show();
            }
        }


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();

                Intent intent = new Intent();
                intent.putExtra("login", login);
                intent.putExtra("senha", senha);
                intent.setClass(SplashActivity.this, AutorizacaoActivity.class);

                startActivity(intent);

            }
        }, 3000);
    }

}
