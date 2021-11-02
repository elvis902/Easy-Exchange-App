package gdscnits.easyexchange.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import gdscnits.easyexchange.app.Constants.Constants;
import gdscnits.easyexchange.app.DialogFile.AppDialog;
import gdscnits.easyexchange.app.Models.RegisterUser;
import com.example.easyexchangeapp.R;
import gdscnits.easyexchange.app.SharedPrefManager.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    ImageView logo;
    TextView titleTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAGS_CHANGED,WindowManager.LayoutParams.FLAGS_CHANGED);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        logo=findViewById(R.id.logo);
        titleTx=findViewById(R.id.title_tv);


        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(logo, scaleX, scaleY);
        animator.setDuration(1000);
        animator.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(titleTx, View.ALPHA, 1f);
        animator2.setDuration(500);
        animator2.setStartDelay(100);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator, animator2);
        set.setDuration(4000);

        set.setDuration(4000);
        set.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isConnected()){
                    firebaseUser = mAuth.getCurrentUser();
                    if(firebaseUser !=null){
                        searchUser(firebaseUser.getUid());
                    }else{
                        Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    AppDialog dialog=new AppDialog();
                    dialog.show(getSupportFragmentManager(),"error dialog");
                }

            }
        }, 2000);

    }

    private void launchMainActivity(String email, String name, String phone){
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
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

    private boolean isConnected(){
        try {
            ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=null;
            if(networkInfo==null){
                networkInfo=manager.getActiveNetworkInfo();
            }
            return networkInfo!=null&&networkInfo.isConnected();
        }catch (NullPointerException e){
            return false;
        }
    }
}