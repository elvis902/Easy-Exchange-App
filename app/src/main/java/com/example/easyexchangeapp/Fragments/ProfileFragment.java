package com.example.easyexchangeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Activity.MyAds;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class ProfileFragment extends Fragment {

    private TextView userName, userPhone, userMail;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private RegisterUser user;
    private ImageView toMyAds;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
        userName = view.findViewById(R.id.profile_name);
        userMail = view.findViewById(R.id.profile_email_id);
        userPhone = view.findViewById(R.id.profile_phone_no);
        toMyAds = view.findViewById(R.id.profile_to_myAds);
        //Getting user data
        fetchUserDetails();

        toMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyAds();
            }
        });

        return view;

    }


    private void fetchUserDetails() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(RegisterUser.class);
                userName.setText(user.getUserName());
                userPhone.setText(user.getUserPhoneNo());
                userMail.setText(user.getUserEmail());
                System.out.println(user.getUserEmail() + " -- " + user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addListenerForSingleValueEvent(listener);
    }

    public void goToMyAds(){
        Intent intent = new Intent(getContext(), MyAds.class);
        intent.putExtra("user_mail",userMail.getText());
        startActivity(intent);
    }
}