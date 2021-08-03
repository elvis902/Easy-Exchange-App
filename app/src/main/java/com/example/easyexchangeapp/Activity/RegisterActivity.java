package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText nameET, emailET, phone_noET, passwordET, confirmPasswordET;
    TextView login;
    Button singUp;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.loginbgr);
        getSupportActionBar().hide();

        nameET = findViewById(R.id.editTextPersonName);
        emailET = findViewById(R.id.editTextEmail);
        phone_noET = findViewById(R.id.editTextPhoneNumber);
        passwordET = findViewById(R.id.editTextPassword);
        confirmPasswordET = findViewById(R.id.editTextConfirmPassword);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        singUp = findViewById(R.id.buttonSignUp);
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        login = findViewById(R.id.loginTextView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    void makeUser(RegisterUser user, String uId){
        databaseReference.child(uId).setValue(user);
    }

    void register(){

        String email = emailET.getText().toString();
        String name = nameET.getText().toString();
        String phone = phone_noET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        if(!email.equals("") &&
           !name.equals("") &&
           !phone.equals("") &&
           !password.equals("") &&
           !confirmPassword.equals("")){
            if(email.contains("@")) {
                if (confirmPassword.equals(password) && password.length()>=8) {
                        RegisterUser registerUser = new RegisterUser(name, email, phone);
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            FirebaseUser user = task.getResult().getUser();
                                            System.out.println(user);
                                            makeUser(registerUser, user.getUid());
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(),"User already registered with this Email!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"An unexpected error occurred",Toast.LENGTH_SHORT).show();
                                    }
                                });
                }else{
                    passwordET.setError("Please enter a valid password of length greater than 8 and and confirm your password");
                }
            }else{
                emailET.setError("PLease enter a valid emailId");
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please fill all the fields",Toast.LENGTH_LONG).show();
        }

    }
}