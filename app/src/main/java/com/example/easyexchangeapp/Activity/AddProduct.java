package com.example.easyexchangeapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.easyexchangeapp.Constants.Constants;
import com.example.easyexchangeapp.Models.Product;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

public class AddProduct extends AppCompatActivity {

    private EditText productName;
    private EditText productDescription;
    private EditText productPrice;
    private EditText productAddress;
    private ImageView selectedImage;
    private ImageView backButton;
    private FloatingActionButton selectImage;
    private Button saveProductButton;

    private String currentUserName,currentUserEmail,currentUserId;

    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
        currentUserId=manager.getValue(Constants.USER_ID);
        currentUserEmail=manager.getValue(Constants.USER_EMAIL);
        currentUserName=manager.getValue(Constants.USER_NAME);

        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        productPrice=findViewById(R.id.productPrice);
        selectedImage=findViewById(R.id.productImage);
        productAddress = findViewById(R.id.productAddress);
        selectImage=findViewById(R.id.edit_pic);
        saveProductButton=findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference(Constants.STORAGE_LOCATION);
        databaseReference= FirebaseDatabase.getInstance().getReference(Constants.STORAGE_LOCATION);
        firebaseAuth=FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading...");

        //progressBar.setVisibility(View.INVISIBLE);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemImage();
            }
        });

        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageTask!=null&&storageTask.isInProgress()){
                    Toast.makeText(AddProduct.this,"Please wait upload in progress",Toast.LENGTH_SHORT).show();
                }else{
                    pd.show();
                    saveImage();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getFileExtension(Uri fileUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    private void selectItemImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    public void saveImage(){
        String prodName=productName.getText().toString();
        String prodDescription=productDescription.getText().toString();
        String prodPrice= productPrice.getText().toString();
        String prodAddress=productAddress.getText().toString();

        if(filePath!=null&&
            !prodName.equals("")&&
            !prodPrice.equals("")&&
            !prodDescription.equals("") &&
        !prodAddress.equals("")){
            //progressBar.setVisibility(View.VISIBLE);
            String finalProdPrice = "Rs " + prodPrice;
            final String uploadId=databaseReference.push().getKey();
            final StorageReference fileReference=storageReference.child(uploadId+"."+getFileExtension(filePath));
            storageTask=fileReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressBar.setVisibility(View.VISIBLE);
                            //progressBar.setProgress(0);
                            pd.dismiss();
                            Toast.makeText(AddProduct.this,"Upload successful!",Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUri=uri;
                                    String imageUrl=downloadUri.toString();
                                    String sellerName=currentUserName;
                                    String sellerEmail=currentUserEmail;
                                    String soldStatus="false";
                                    String prodAddress = productAddress.getText().toString();
                                    String itemKey=uploadId;
                                    Product product=new Product(prodName,prodDescription,finalProdPrice,imageUrl,sellerName,soldStatus,itemKey,sellerEmail,prodAddress,currentUserId);
                                    databaseReference.child(uploadId).setValue(product);
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddProduct.this,"Upload Failed!",Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            System.out.println("UPLOAD: "+progress);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                           //progressBar.setVisibility(View.INVISIBLE);
                            pd.dismiss();
                        }
                    });
        }else if(prodName.equals("")){
            Toast.makeText(AddProduct.this,"Please add product name!",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else if(prodPrice.equals("")){
            Toast.makeText(AddProduct.this,"Please add product price!",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else if(prodDescription.equals("")){
            Toast.makeText(AddProduct.this,"Please add product description!",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }else if(prodAddress.equals("")){

            Toast.makeText(AddProduct.this,"Please add product location!",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
        else{
            Toast.makeText(AddProduct.this,"File not selected!",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }

    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            selectedImage.setVisibility(View.VISIBLE);
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                selectedImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}