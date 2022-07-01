package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText email_et;
    private Button reset_psw;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email_et = findViewById(R.id.email_et);
        reset_psw = findViewById(R.id.reset_btn);

        auth = FirebaseAuth.getInstance();

        reset_psw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                resetPassword();
            }
        });


    }

    private void resetPassword() {
        String email = email_et.getText().toString().trim();

        if(email.isEmpty()){
            email_et.setError("Email is required");
            email_et.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("Please provide valid email");
            email_et.requestFocus();
            return;
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword.this,
                            "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgetPassword.this,
                            "Try again! Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}