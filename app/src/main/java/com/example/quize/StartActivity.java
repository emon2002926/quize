package com.example.quize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;

public class StartActivity extends AppCompatActivity  {

//    implements ConnectionReceiver.ReceiverListener
//    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final AppCompatButton quizeStartBtn = findViewById(R.id.startQuizeBtn);


        quizeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                MusicManager.player.stop();

                MediaPlayer mp = MediaPlayer.create(StartActivity.this,R.raw.whistle);
                mp.start();

                startActivity(new Intent(StartActivity.this,MainActivity.class));
//                checkConnection();
            }
        });

//        quitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

    }

//    private void checkConnection() {
//
//        // initialize intent filter
//        IntentFilter intentFilter = new IntentFilter();
//
//        // add action
//        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");
//
//        // register receiver
//        registerReceiver(new ConnectionReceiver(), intentFilter);
//
//        // Initialize listener
//        ConnectionReceiver.Listener = this;
//
//        // Initialize connectivity manager
//        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Initialize network info
//        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
//
//        // get connection status
//        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
//
//        // display snack bar
//        showSnackBar(isConnected);
//    }
//
//    private void showSnackBar(boolean isConnected) {
//
//        // initialize color and message
//
//        // check condition
//        if (isConnected) {
//
//            // when internet is connected
//            // set message
//
//            startActivity(new Intent(StartActivity.this,MainActivity.class));
//
//        } else {
//
//            // when internet
//            // is disconnected
//            // set message
//
//            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
//
//    @Override
//    public void onNetworkChange(boolean isConnected) {
//
//
//        showSnackBar(isConnected);
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // call method
//        checkConnection();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // call method
//
//    }
    }