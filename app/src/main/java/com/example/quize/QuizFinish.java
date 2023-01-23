package com.example.quize;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class QuizFinish extends AppCompatActivity {

    private Button quitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);


        quitBtn = findViewById(R.id.quitApp);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FirebaseAuth.getInstance().signOut();
                finish();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }, 100);




            }
        });






    }
}