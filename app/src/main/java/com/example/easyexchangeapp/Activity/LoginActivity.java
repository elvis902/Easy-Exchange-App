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

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
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

public class LoginActivity extends AppCompatActivity {

    EditText emailET, passwordET;
    Button mLoginBtn;
    TextView mLoginTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.emailEnt);
        passwordET = findViewById(R.id.passwordEnt);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginBtn = findViewById(R.id.login_btn);
        mLoginTextView = findViewById(R.id.not_have_account);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void launchMainActivity(String email, String name, String phone){
        Toast.makeText(this, "Please wait....Logging you in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
        manager.storeKeyValuePair(Constants.USER_ID,firebaseUser.getUid());
        manager.storeKeyValuePair(Constants.USER_EMAIL,email);
        manager.storeKeyValuePair(Constants.USER_NAME,name);
        manager.storeKeyValuePair(Constants.USER_PHONE,phone);
        startActivity(intent);
        finish();
    }

    private void searchUser(final String Uid){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RegisterUser currentUser = snapshot.child(Uid).getValue(RegisterUser.class);
                if(currentUser!=null){
                    launchMainActivity(currentUser.getUserEmail(), currentUser.getUserName(), currentUser.getUserPhoneNo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loginUser(){
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

            if(!email.equals("")&&
               !password.equals("")){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseUser=task.getResult().getUser();
                                searchUser(firebaseUser.getUid());
                            }else{
                                Toast.makeText(getApplicationContext(), "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null){
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            searchUser(firebaseUser.getUid());
        }
    }
}