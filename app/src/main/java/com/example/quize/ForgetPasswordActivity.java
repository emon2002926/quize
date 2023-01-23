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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {


    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailEditText =findViewById(R.id.editText_email);
        resetPasswordButton = findViewById(R.id.resetpass);
        progressBar = findViewById(R.id.progressBar3);

        auth =FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }
    private void resetPassword(){

        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()){

            emailEditText.setError("Email is Required ");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please Provide a Valid Email");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    startActivity(new Intent(ForgetPasswordActivity.this,Login.class));
                    Toast.makeText(ForgetPasswordActivity.this,"Please Check your email  to reset your password ",Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else {
                    Toast.makeText(ForgetPasswordActivity.this,"Please try again ,Something  wrong happened ",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}