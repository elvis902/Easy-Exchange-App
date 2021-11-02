package gdscnits.easyexchange.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import gdscnits.easyexchange.app.Constants.Constants;
import gdscnits.easyexchange.app.NotificationManagerFiles.NotificationServiceClass;
import com.example.easyexchangeapp.R;
import gdscnits.easyexchange.app.SharedPrefManager.SharedPrefManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity{
    public static String currentUserID;
    BottomNavigationView bottomNavigation;
//  Button logOut;
    SharedPrefManager manager;
    FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    Intent notificationServiceIntent;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=new SharedPrefManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        currentUserID=manager.getValue(Constants.USER_ID);
        bottomNavigation = findViewById(R.id.main_navigationBar);
        floatingActionButton = findViewById(R.id.fab);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigation,navController);

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddProduct.class)));

        notificationServiceIntent=new Intent(getBaseContext(), NotificationServiceClass.class);
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
                startActivity(new Intent(this,EditProfileActivity.class));
                return true;

            case  R.id.logout:{
                manager.clearAll();
                stopService(notificationServiceIntent);
                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser()==null)
                            Process.killProcess(Process.myPid());
                    }
                });
                mAuth.signOut();
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void signOutUser() {
//        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
//        loginActivity.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(loginActivity);
//    }
}
