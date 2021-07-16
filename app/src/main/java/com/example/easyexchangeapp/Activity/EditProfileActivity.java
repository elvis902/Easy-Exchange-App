package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    //TODO: Update the mail and password using the auth credentials

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;
    private FirebaseUser curr_user;

    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_pass;
    private EditText edit_num;

    private Button update_info;

    private RegisterUser user_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        auth = FirebaseAuth.getInstance();
        curr_user = auth.getCurrentUser();
        userId = curr_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        edit_email = findViewById(R.id.edit2);
        edit_name = findViewById(R.id.edit1);
        edit_pass = findViewById(R.id.edit3);
        edit_num = findViewById(R.id.edit4);
        update_info = findViewById(R.id.button2);

        fetchUserDetails();
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

    }

    private void updateUserInfo() {
        String name = edit_name.getText().toString();
        String mail = edit_email.getText().toString();
        String num = edit_num.getText().toString();
        String pass = edit_pass.getText().toString();
        if(!name.isEmpty() &&
                !mail.isEmpty() &&
                !pass.isEmpty() &&
                (!num.isEmpty() && num.length()<11)){
            databaseReference.child("userName").setValue(name);
            databaseReference.child("userEmail").setValue(mail);
            databaseReference.child("userPhoneNo").setValue(num);
        }
    }


    private void fetchUserDetails() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    user_class = snapshot.getValue(RegisterUser.class);
                    edit_name.setText(user_class.getUserName());
                    edit_email.setText(user_class.getUserEmail());
                    edit_num.setText(user_class.getUserPhoneNo());
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }
}