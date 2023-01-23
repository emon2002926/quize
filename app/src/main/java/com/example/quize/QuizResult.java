package com.example.quize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuizResult extends AppCompatActivity {

    private List<QuestionList> questionLists = new ArrayList<>();

    Handler handler = new Handler();

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    FirebaseAuth firebaseAuth;
    ImageView imageViewWinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        final TextView scoreTv = findViewById(R.id.scoreTv);
        final TextView totalScoreTv = findViewById(R.id.totalScoreTv);
        final TextView correctTv = findViewById(R.id.correctTv);
        final TextView incorrect = findViewById(R.id.incorrectTv);
//        final AppCompatButton shareBtn = findViewById(R.id.shareBtn);
        final AppCompatButton reTakeBtn = findViewById(R.id.reTakeQuizBtn);
        final TextView textViewCongras = findViewById(R.id.congratulation);
        final TextView textViewgerting = findViewById(R.id.textViewgereting);

        imageViewWinner = findViewById(R.id.winner);



        // gating question from MainActivity
        questionLists = (List<QuestionList>) getIntent().getSerializableExtra("qutions");
        totalScoreTv.setText("/"+"10");


        scoreTv.setText(getCorrectAnswer()+"");
        correctTv.setText(getCorrectAnswer()+"");

//        incorrect.setText(String.valueOf(questionLists.size() - getCorrectAnswer()));

        incorrect.setText(String.valueOf(10 - getCorrectAnswer()));



//
//        shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //Open share Intent
//                Intent sendIntend = new Intent();
//                sendIntend.setAction(Intent.ACTION_SEND);
//                sendIntend.putExtra(Intent.EXTRA_TEXT,"My Score ="+scoreTv.getText());
//
//                Intent shareIntent = Intent.createChooser(sendIntend,"Share Via");
//
//                startActivity(shareIntent);
//            }
//        });



        reTakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(QuizResult.this,QuizFinish.class));


            }
        });




        user= FirebaseAuth.getInstance().getCurrentUser();
        reference =FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();




        //setting database Path for winner
        DatabaseReference referenceForpassed;
        referenceForpassed =FirebaseDatabase.getInstance().getReference("sucess");

        //setting database Path for loser
        DatabaseReference referenceForFailed;
        referenceForFailed = FirebaseDatabase.getInstance().getReference("loser");


        final String score =String.valueOf(getCorrectAnswer());

        Log.d("score",score);

        reference.child(userId).child("score").setValue(score);

        int myNum ;
        myNum = Integer.parseInt(score);


        if (myNum>5){

            //saving sucess data

            imageViewWinner.setImageResource(R.drawable.winner);
            textViewgerting.setText("Congratulations!!\n" +
                    "You have won the\n" +
                    " FIFA WORLD CUP TROPHY");

            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User userProfile = snapshot.getValue(User.class);
                    if (userProfile !=null){

                        String DrName = userProfile.DrName;
                        String email = userProfile.email;
                        String DrId = userProfile.DrID;




                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                referenceForpassed.child(DrId).child("DRName").setValue(DrName);
                                referenceForpassed.child(DrId).child("DrEmail").setValue(email);
                                referenceForpassed.child(DrId).child("score").setValue(score);
                            }
                        }, 100);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{

            imageViewWinner.setImageResource(R.drawable.loser);

            textViewgerting.setText("SORRY you have scored less than 6 \n" +
                    "BETTER LUCK for the next WORLD CUP!!!");

            ///saving failed data
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User userProfile = snapshot.getValue(User.class);
                    if (userProfile !=null){

                        String DrName = userProfile.DrName;
                        String email = userProfile.email;
                        String DrId = userProfile.DrID;



                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                referenceForFailed.child(DrId).child("DRName").setValue(DrName);
                                referenceForFailed.child(DrId).child("DrEmail").setValue(email);
                                referenceForFailed.child(DrId).child("score").setValue(score);

                            }
                        }, 100);




                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }



    }
    private int getCorrectAnswer(){
        int correctAnswer = 0;

        for(int i =0; i < questionLists.size(); i++){

            int getUserSelectedOption = questionLists.get(i).getUserSelecedAnswer();//Get User Selected Option
            int getQuestionAnswer = questionLists.get(i).getAnswer();

        // Check UserSelected Answer is correct Answer
        if (getQuestionAnswer == getUserSelectedOption){
            correctAnswer++;
        }
        }
        return correctAnswer;
    }

    @Override
    protected void onStart() {
        super.onStart();

        questionLists = (List<QuestionList>) getIntent().getSerializableExtra("qutions");
        final String s =String.valueOf(getCorrectAnswer());



    }
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        finish();
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }, 100);

    }


}