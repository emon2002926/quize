package com.example.quize;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    Handler handler = new Handler();



    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference reference;
    static String score;
    static int Scr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        MusicManager.SoundPlayer(this, R.raw.themesong);
        MusicManager.player.start();

        mAuth = FirebaseAuth.getInstance();
//
        FirebaseUser user = mAuth.getCurrentUser();

        reference =FirebaseDatabase.getInstance().getReference("Users");

        Toast.makeText(Splash.this,"Please tap to proceed",Toast.LENGTH_LONG).show();




        if (user !=null){

            userId = mAuth.getUid();
            reference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.hasChild("score")){

                        score = snapshot.child("score").getValue(String.class);
                        Scr = Integer.parseInt(snapshot.child("score").getValue(String.class));

                        Log.d("score",score);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


//        ImageView imageView = findViewById(R.id.nextScreen);


        LinearLayout relativeLayout = findViewById(R.id.realtiveLayoutt);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) {

                            Intent intent = new Intent(Splash.this, Register.class);
                            startActivity(intent);
//                            finish();

                        } else {




                    if (Scr>1){

                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(Splash.this, Register.class);
                        startActivity(intent);
                        finish();

                    }else {


                        Intent mainIntent = new Intent(Splash.this, StartActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

                    }



                        }

                    }
                },100);


            }
        });



    }
}