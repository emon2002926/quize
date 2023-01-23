package com.example.quize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private  Button  registerUser;
    private  EditText editTextfullName,editTextAge,editTextEmail,editTextRegion,editTextZone,editTextMarket;
    private ProgressBar progressBar;
    private TextView banner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();


        banner = findViewById(R.id.loginNow);
        banner.setOnClickListener(this);



        registerUser = (Button) findViewById(R.id.registerBtn);
        registerUser.setOnClickListener(this);


        editTextfullName = findViewById(R.id.fullName);
        editTextEmail = findViewById(R.id.email);
        editTextAge  = findViewById(R.id.phone);

        editTextZone = findViewById(R.id.editTextZone1);
        editTextMarket = findViewById(R.id.editTextMarket1);
        editTextRegion = findViewById(R.id.editTextRegion);

        progressBar = findViewById(R.id.progressBar2);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginNow:
                startActivity(new Intent(this,Login.class));
                break;
            case R.id.registerBtn:
                registerUser();
                break;

        }

    }


    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String DrName = editTextfullName.getText().toString().trim();
        String DrID = editTextAge.getText().toString().trim();

        String zone = editTextZone.getText().toString().trim();
        String market = editTextMarket.getText().toString().trim();
        String region = editTextRegion.getText().toString().trim();

        if (DrName.isEmpty()){

            editTextfullName.setError("Full name is Required");
            editTextfullName.requestFocus();
            return;
        }
        if (email.isEmpty()){

            editTextEmail.setError("Email  is Required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Provide a Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (DrID.isEmpty()){

            editTextAge.setError("DrID is Required");
            editTextAge.requestFocus();
            return;
        }

        if (zone.isEmpty()){

            editTextZone.setError("Zone is Required");
            editTextZone.requestFocus();
            return;
        }
        if (region.isEmpty()){

            editTextRegion.setError("Region is Required");
            editTextRegion.requestFocus();
            return;
        }


        if (market.isEmpty()){
            editTextMarket.setError(" Market is Required");
            editTextMarket.requestFocus();
            return;
        }


//        if (password.isEmpty()){
//
//            editTextPassword.setError("Password  is Required");
//            editTextPassword.requestFocus();
//            return;
//        }
//        if (password.length() <6){
//
//            editTextPassword.setError("Password lenth should be in 6 characters");
//            editTextPassword.requestFocus();
//            return;
//        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,DrID)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(DrName,DrID,email,zone,market,region);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(Register.this,"Thanks for creating a new account",Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
//                                                startActivity(new Intent(Register.this,Login.class));
//                                                finish();


                                                mAuth.signInWithEmailAndPassword(email,DrID).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()){

                                                            startActivity(new Intent(Register.this,StartActivity.class));
                                                            progressBar.setVisibility(View.GONE);
                                                            finish();


                                                        }

                                                    }
                                                });


                                            }
                                            else {
                                                Toast.makeText(Register.this,"Register Failed",Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }}});
                        }else {
                            Toast.makeText(Register.this,"Dr. ID / Email Already Registered ",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}