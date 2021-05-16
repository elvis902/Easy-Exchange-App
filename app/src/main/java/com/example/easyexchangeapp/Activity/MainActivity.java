package com.example.easyexchangeapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.easyexchangeapp.Fragments.ChatFragment;
import com.example.easyexchangeapp.Fragments.FavouriteFragment;
import com.example.easyexchangeapp.Fragments.HomeFragment;
import com.example.easyexchangeapp.Fragments.ProfileFragment;
import com.example.easyexchangeapp.R;
import com.example.easyexchangeapp.SharedPrefManager.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
  BottomNavigationView bottomNavigation;
//  Button logOut;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bottomNavigation = findViewById(R.id.main_navigationBar);
        floatingActionButton = findViewById(R.id.fab);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddProduct.class)));

        loadFragment(new HomeFragment());


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_favourites:
                fragment = new FavouriteFragment();
                break;
            case R.id.nav_orders:
                fragment = new ChatFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }


    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragments_container, fragment)
                    .commit();
            return true;
        }
        return false;
// =======

//         logOut = findViewById(R.id.logOut);
//         logOut.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 mAuth.signOut();
//                 startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                 finish();
//             }
//         });
// >>>>>>> master
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
            if(item.getItemId()== android.R.id.home) {

                finish();
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

