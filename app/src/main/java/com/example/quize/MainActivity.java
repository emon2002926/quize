package com.example.quize;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final List<QuestionList> questionslists = new ArrayList<>();



    private TextView quizTimer;
    private RelativeLayout option1Layout , option2Layout , option3Layout , option4Layout ;
    private TextView option1TV , option2TV , option3TV , option4TV ;
    private ImageView option1Icon , option2Icon , option3Icon , option4Icon ;

    private TextView questionTv;

    private TextView totalQuestionTV ;
    private TextView currentQuestion ;


    private  final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = database.getReference();


    CountDownTimer countDownTimer;

    ///Current Question position by Default 0
    private int currenQuestiontPosition = 0;

    //seleced option valu should be be
    private  int selectedOption = 0;
    ProgressBar progressBar;

    static int getAnswer;

    static int getAnswer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        quizTimer = findViewById (R.id.quizeTimer) ;


        option1Layout = findViewById (R.id.option1Layout ) ;
        option2Layout  = findViewById (R.id.option2Layout ) ;
        option3Layout  = findViewById (R.id.option3Layout ) ;
        option4Layout = findViewById (R.id.opton4Layout ) ;


        option1TV = findViewById(R.id.option1Tv);
        option2TV = findViewById(R.id.option2Tv);
        option3TV = findViewById(R.id.option3Tv);
        option4TV = findViewById(R.id.option4Tv);

        questionTv = findViewById(R.id.questionTv);
        option1Icon = findViewById(R.id.option1Icon);
        option2Icon = findViewById(R.id.option2Icon);
        option3Icon = findViewById(R.id.option3Icon);
        option4Icon = findViewById(R.id.option4Icon);

        totalQuestionTV = findViewById ( R.id.totalQuestionTv ) ;
        currentQuestion = findViewById ( R.id.currentQuestionTv ) ;



        final AppCompatButton nextBtn = findViewById ( R.id.nextQuction ) ;


        //Show Instration Dailog
        InstructionsDialog instructionsDialog = new InstructionsDialog(MainActivity.this);
        instructionsDialog.setCancelable(false);
        instructionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        instructionsDialog.show();

        progressBar =findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final int getQuizeTime = Integer.parseInt(snapshot.child("time").getValue(String.class));

                for (DataSnapshot question : snapshot.child("question2").getChildren()){

                    String GetQuestion = question.child("question").getValue(String.class);
                    String GetOption1  = question.child("option1").getValue(String.class);
                    String GetOption2  = question.child("option2").getValue(String.class);
                    String GetOption3  = question.child("option3").getValue(String.class);
                    String GetOption4  = question.child("option4").getValue(String.class);

                    getAnswer = Integer.parseInt(question.child("answer").getValue(String.class));

                 // creating Question list and object Diteals
                    QuestionList questionList = new QuestionList(GetQuestion,GetOption1,GetOption2,GetOption3,GetOption4,getAnswer);
                   //adding questionList Object into the list

                    questionslists.add(questionList);

                    Collections.shuffle(questionslists);



                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }

//                totalQuestionTV.setText("/"+questionslists.size());
                totalQuestionTV.setText("/"+"10");

            //select First Question
                selectQuestion(currenQuestiontPosition);


                startQuizeTimer(getQuizeTime);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Toast.makeText(MainActivity.this,"Failed to Get data",Toast.LENGTH_SHORT).show();

            }
        });





        option1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //asaing  as the first option
                selectedOption = 1;

                selectedOption(option1Layout,option1Icon);
            }
        });

        option2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //asaing  as the scaound option
                selectedOption = 2;
                selectedOption(option2Layout,option2Icon);
            }
        });
        option3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //asaing  as the third option
                selectedOption = 3;
                selectedOption(option3Layout,option3Icon);
            }
        });
        option4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //asaing  as the 4th option
                selectedOption = 4;
                selectedOption(option4Layout,option4Icon);

            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedOption !=0){

                        questionslists.get(currenQuestiontPosition).setUserSelecedAnswer(selectedOption);

                        int i = selectedOption;


                // Showing  User Answer Is Correct Or Not
                    String Scr = String.valueOf(getAnswer2);
                    Log.d("score",Scr);

                    if (getAnswer2 == 1){

                        selectedRightOption(option1Layout,option1Icon);


                    }
                    if (getAnswer2 == 2){

                        selectedRightOption(option2Layout,option2Icon);

                    }
                    if (getAnswer2 == 3){

                        selectedRightOption(option3Layout,option3Icon);
                    }
                    if (getAnswer2 == 4){

                        selectedRightOption(option4Layout,option4Icon);
                    }


//              for right or worong logic for ui
                    if (i==1  ){

                        if (i==getAnswer2){

                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.correct);
                            mp.start();
//                         selectedRightOption(option1Layout,option1Icon);
                        }
                        else {


                            selectedWrongOption(option1Layout,option1Icon);
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.worong);
                            mp.start();
                        }


                    }
                    if (i==2){

                        if (i==getAnswer2){

                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.correct);
                            mp.start();
//                            selectedRightOption(option2Layout,option2Icon);
                        }
                        else {
                            selectedWrongOption(option2Layout,option2Icon);
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.worong);
                            mp.start();
                        }
                    }

                    if (i==3){

                        if (i==getAnswer2){

                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.correct);
                            mp.start();

//                            selectedRightOption(option3Layout,option3Icon);

                        }else {

                            selectedWrongOption(option3Layout,option3Icon);
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.worong);
                            mp.start();

                        }
                    }

                    if (i==4){
                        if (i==getAnswer2){

                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.correct);
                            mp.start();

//                            selectedRightOption(option4Layout,option4Icon);

                        }else {

                            selectedWrongOption(option4Layout,option4Icon);
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this,R.raw.worong);
                            mp.start();

                        }
                    }

                    selectedOption = 0;
                    currenQuestiontPosition++; //gating New Question

            // set  limit for Question

                    if (currenQuestiontPosition ==10){
                        countDownTimer.cancel();
                        finishQuiz();
                    }

                    //Check list has more Question

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            nextBtn.setEnabled(true);
                            if (currenQuestiontPosition < questionslists.size()){

                                selectQuestion(currenQuestiontPosition);

                            }else {
                                countDownTimer.cancel();
                                finishQuiz();
                            }

                        }
                    }, 1200);


                    }else {
                        Toast.makeText(MainActivity.this,"please Select A Option",Toast.LENGTH_SHORT).show();
                    }

            }
        });

    }

    private void finishQuiz(){

        Intent intent = new Intent(MainActivity.this,QuizResult.class);

        //Creating Bundle To pass QuestionList

        Bundle bundle = new Bundle();
        bundle.putSerializable("qutions",(Serializable) questionslists);

        intent.putExtras(bundle);
        startActivity(intent);

        ////Distroy Current Activity
        finish();
    }


// ToDo method to start QuizeTimer
    private void startQuizeTimer(int maxTimerSceounds){
        countDownTimer = new CountDownTimer(maxTimerSceounds*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                long getHour = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                long getMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long getSceond = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                String genarateTime = String.format(Locale.getDefault(),"%02d:%02d:%02d",getHour,
                        getMinutes - TimeUnit.HOURS.toMinutes(getHour),
                        getSceond - TimeUnit.MINUTES.toSeconds(getMinutes));

                quizTimer.setText(genarateTime);

            }

            @Override
            public void onFinish() {

                //Finish  quiz when time is Over
                finishQuiz();

            }


        };
        countDownTimer.start();
    }

    private void selectQuestion(int questionListPositon){

        restOption();


//        Collections.shuffle(questionslists);


        //gating Question Ditals and set to Textview
        questionTv.setText(questionslists.get(questionListPositon).getQuestion());
        option1TV.setText(questionslists.get(questionListPositon).getOption1());
        option2TV.setText(questionslists.get(questionListPositon).getOption2());
        option3TV.setText(questionslists.get(questionListPositon).getOption3());
        option4TV.setText(questionslists.get(questionListPositon).getOption4());

        //sating current question By Number

        int i = questionListPositon;

        int questionPositon2 = i+1;

        String myString = Integer.toString(questionPositon2);


        currentQuestion.setText("Question "+questionPositon2);

        getAnswer2 =questionslists.get(questionListPositon).getAnswer();




    }
    private  void restOption () {

//        Collections.shuffle(questionslists);

        option1Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option2Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option3Layout.setBackgroundResource(R.drawable.round_back_white50_10);
        option4Layout.setBackgroundResource(R.drawable.round_back_white50_10);

        option1Icon.setImageResource(R.drawable.round_back_white50_100);
        option2Icon.setImageResource(R.drawable.round_back_white50_100);
        option3Icon.setImageResource(R.drawable.round_back_white50_100);
        option4Icon.setImageResource(R.drawable.round_back_white50_100);

    }

    private void selectedOption(RelativeLayout selectedOptionLayout ,ImageView selectedOptionIcon) {

        restOption();
        selectedOptionIcon.setImageResource(R.drawable.chack);
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_selected_option);

    }

    private void selectedWrongOption(RelativeLayout selectedOptionLayout,ImageView selectedOptionIcon){
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_red50_10);
        selectedOptionIcon.setImageResource(R.drawable.cross);

    }

    private void selectedRightOption(RelativeLayout selectedOptionLayout,ImageView selectedOptionIcon){
        selectedOptionLayout.setBackgroundResource(R.drawable.round_back_green50_10);
        selectedOptionIcon.setImageResource(R.drawable.chack);

    }

}