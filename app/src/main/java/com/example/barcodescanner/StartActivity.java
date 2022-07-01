package com.example.barcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText email;
    private EditText password;
    private Button login;
    //private DatabaseReference reference;

    TextView register;
    TextView forgot_psw;
    public int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        forgot_psw = findViewById(R.id.forgot_psw);
        register = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        login = findViewById(R.id.login_btn);

        // On clicking register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                //finish();
            }
        });

        //On clicking forgot password
        forgot_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, ForgetPassword.class));
            }
        });

        // On clicking login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(StartActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(txt_email, txt_password);
                }
            }
        });


    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
            if(task.isSuccessful()){

                String uid = task.getResult().getUser().getUid();
                Log.d("Here", "loginUser: "+uid);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("Users").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("here", "onDataChange: "+"success" );
                        userType = dataSnapshot.getValue(Integer.class);
                        Toast.makeText(StartActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                        if(userType==0) {
                            Log.d("here", "onDataChange: "+"success 0" );
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                        }
                        if(userType==1){
                            Log.d("here", "onDataChange: "+"success 1" );
                            startActivity(new Intent(StartActivity.this, AdminPage.class));
                        }
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(StartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(StartActivity.this, "Please Enter Valid Credentials!", Toast.LENGTH_LONG).show();
            }
        });

    }




   @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();


        if(mAuth.getCurrentUser() != null){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference().child("Users").child(uid).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(Integer.class);
                    if(userType==0) {
                        startActivity(new Intent(StartActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                    if(userType==1){
                        startActivity(new Intent(StartActivity.this, AdminPage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}