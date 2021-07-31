package com.example.easyexchangeapp.Fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyexchangeapp.Activity.EditProfileActivity;
import com.example.easyexchangeapp.Activity.MyAds;
import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.EventListener;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.security.AccessController.getContext;

public class ProfileFragment extends Fragment {

    private TextView userName, userPhone, userMail;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private RegisterUser user;
    private ImageView toMyAds,toEditProfile;
    private FloatingActionButton editPic;
    private StorageReference storageRef;
    private StorageTask storageTask;

    private Uri imageFilePath;
    private ImageView profilePic;
    private String picUrl;

    private AlertDialog.Builder builder;
    private ProgressBar uploadProgress;

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
        toEditProfile=view.findViewById(R.id.profile_to_edit);
        editPic = view.findViewById(R.id.edit_pic);
        profilePic = view.findViewById(R.id.user_profilePic);
        uploadProgress = view.findViewById(R.id.progress_picUpload);
        uploadProgress.setVisibility(View.INVISIBLE);
        storageRef = FirebaseStorage.getInstance().getReference("profile_pics");
        //Getting user data
        fetchUserDetails();

        toMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyAds();
            }
        });

        toEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEdit();
            }
        });

        builder = new AlertDialog.Builder(getContext());
        editPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemImage();
            }
        });

        return view;

    }

    private void selectItemImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1
                && resultCode == -1
                && data != null
                && data.getData() != null) {
            imageFilePath = data.getData();
            profilePic.setVisibility(View.VISIBLE);
            builder.setMessage("Are you sure ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            uploadProgress.setVisibility(View.VISIBLE);
                            uploadImage(imageFilePath);
                            uploadProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), "Updating Profile Pic...", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Alert!");
            alert.show();
        }

    }

    private void uploadImage(Uri imageFilePath) {
            if(imageFilePath!=null){
                final String userUid = auth.getUid();
                final StorageReference storageReference = storageRef.child(userUid + ".jpg");
                storageReference.delete();
                storageTask = storageReference.putFile(imageFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child("profile_image").setValue(uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Upload failed!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    private void fetchUserDetails() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(RegisterUser.class);
                userName.setText(user.getUserName());
                userPhone.setText(user.getUserPhoneNo());
                userMail.setText(user.getUserEmail());
                picUrl = user.getProfile_image();
                Picasso.get().
                        load(picUrl).
                        fit().
                        centerInside().
                        into(profilePic);
                System.out.println(user.getUserEmail() + " -- " + user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addValueEventListener(listener);
    }


    public void goToMyAds(){
        Intent intent = new Intent(getContext(), MyAds.class);
        intent.putExtra("user_mail",userMail.getText());
        startActivity(intent);
    }
    public void goToEdit(){
        Intent intent=new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }
}