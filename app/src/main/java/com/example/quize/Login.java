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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {



    private TextView register,restPass;
    private EditText editTextEmail ,editTextPassword;
    private Button singIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registerNow);
        register.setOnClickListener(this);

        singIn = findViewById(R.id.loginBtn);
        singIn.setOnClickListener(this);

        editTextEmail = findViewById(R.id.phone);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);
        mAuth =FirebaseAuth.getInstance();

        restPass = findViewById(R.id.reset);
        restPass.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerNow:
                startActivity(new Intent(this,Register.class));
                break;
            case  R.id.loginBtn:

                UserLogin();
                break;

            case R.id.reset:
                startActivity(new Intent(this,ForgetPasswordActivity.class));

        }

    }

    private void UserLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError("Require Email");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Provide a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Require Password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() <6){

            editTextPassword.setError("Password length should be in 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                        startActivity(new Intent(Login.this,StartActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();


                }
                else {
                    Toast.makeText(Login.this,"Failed To LogIn please Check your Email and Doctor ID",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }




}