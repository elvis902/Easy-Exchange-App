package com.example.easyexchangeapp.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.easyexchangeapp.Fragments.FavouriteFragment;
import com.example.easyexchangeapp.Fragments.HomeFragment;
import com.example.easyexchangeapp.Fragments.ChatFragment;
import com.example.easyexchangeapp.Fragments.ProfileFragment;
import com.example.easyexchangeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;



import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
  BottomNavigationView bottomNavigation;
    Button logOut;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigation = findViewById(R.id.main_navigationBar);
        floatingActionButton = findViewById(R.id.fab);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddProduct.class));
            }
        });

        loadFragment(new HomeFragment());
        //price = getResources().getStringArray(R.array.price);
        //description = getResources().getStringArray(R.array.description);
        //address = getResources().getStringArray(R.array.address);

        //ItemAdapter itemAdapter = new ItemAdapter(this, price, description, address, images);

//        recyclerView = findViewById(R.id.home_item_recycle_view);
//        recyclerView.setAdapter(itemAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

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
//         mAuth = FirebaseAuth.getInstance();
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

    @Override
    public  boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.edit_profile:
                Toast.makeText(this, "Edit Profile",Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.setting:
                Toast.makeText(this, "Settings",Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.logout:
                Toast.makeText(this, "Logged Out",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

