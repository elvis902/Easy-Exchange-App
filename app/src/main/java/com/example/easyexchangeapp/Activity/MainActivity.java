package com.example.easyexchangeapp.Activity;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.easyexchangeapp.Fragments.ChatFragment;
import com.example.easyexchangeapp.Fragments.FavouriteFragment;
import com.example.easyexchangeapp.Fragments.HomeFragment;
import com.example.easyexchangeapp.Fragments.ProfileFragment;
import com.example.easyexchangeapp.NotificationManagerFiles.NotificationReceiver;
import com.example.easyexchangeapp.NotificationManagerFiles.NotificationServiceClass;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity{
    BottomNavigationView bottomNavigation;
//  Button logOut;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;

    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        bottomNavigation = findViewById(R.id.main_navigationBar);
        floatingActionButton = findViewById(R.id.fab);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigation,navController);

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddProduct.class)));

        Intent notificationServiceIntent=new Intent(this, NotificationServiceClass.class);
        startService(notificationServiceIntent);

    }


    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public  boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.edit_profile:
                Toast.makeText(this, "Edit Profile",Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.setting:
                Toast.makeText(this, "Settings",Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.logout:{
                mAuth.signOut();
                SharedPrefManager manager=new SharedPrefManager(getApplicationContext());
                manager.clearAll();
                signOutUser ();
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOutUser() {
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        loginActivity.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity( loginActivity );
        finish();
    }
}

