package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    //TODO: Add Changing Profile Image Utility

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userId;
    private FirebaseUser curr_user;
    private StorageReference storageRef;

    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_num;
    private String picUrl;
    private ImageView profilePic;

    private Button update_info;
    private Button update_pass;
    private ImageButton backButton;

    private RegisterUser user_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Objects.requireNonNull(getSupportActionBar()).hide();
        auth = FirebaseAuth.getInstance();
        curr_user = auth.getCurrentUser();
        userId = curr_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        storageRef = FirebaseStorage.getInstance().getReference("profile_pics");

        edit_email = findViewById(R.id.edit2);
        edit_name = findViewById(R.id.edit1);
        edit_num = findViewById(R.id.edit4);
        update_info = findViewById(R.id.button2);
        profilePic = findViewById(R.id.user_profilePic);
        update_pass = findViewById(R.id.update_password);
        backButton = findViewById(R.id.imageButton);

        fetchUserDetails();
        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateUserInfo()){
                    Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updatePassword() {
        auth.sendPasswordResetEmail(Objects.requireNonNull(curr_user.getEmail()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Please check your mail", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditProfileActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean updateUserInfo() {
        String name = edit_name.getText().toString();
        String mail = edit_email.getText().toString();
        String num = edit_num.getText().toString();
        if(!name.isEmpty() &&
                !mail.isEmpty() &&
                (!num.isEmpty() && num.length()<11)){
            databaseReference.child("userName").setValue(name);
            databaseReference.child("userEmail").setValue(mail);
            databaseReference.child("userPhoneNo").setValue(num);

            if(mail.contains("@")) {
                curr_user.updateEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Log.i("Update_Mail: ","Email Updated Successfully");
                    }
                });
            }else{
                Toast.makeText(this, "Please Enter valid email", Toast.LENGTH_SHORT).show();
            }
            return true;
        }else{
            return false;
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
                    picUrl = user_class.getProfile_image();
                    Picasso.get().
                            load(picUrl).
                            fit().
                            centerInside().
                            into(profilePic);
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(eventListener);
    }
}