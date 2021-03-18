package com.example.easyexchangeapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
  BottomNavigationView bottomNavigation;
    Button logOut;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigation = findViewById(R.id.main_navigationBar);
        bottomNavigation.setOnNavigationItemSelectedListener(this);


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
                fragment = new OrderFragment();
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
}