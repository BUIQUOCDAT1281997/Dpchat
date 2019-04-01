package com.example.dpchat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ConstraintLayout mainContraint;
    private BottomNavigationView bottomNavigationView;

    private FragmentHome fragmentHome;
    private FragmentContact fragmentContact;
    private FragmentRequest fragmentRequest;
    private FragmentUser fragmentUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        //Progress
        //progressDialog = new ProgressDialog(this);


        //bottomNavigation
        bottomNavigationView = findViewById(R.id.navigationView);
        mainContraint = findViewById(R.id.main_nav);

        //create Fragments
        fragmentHome = new FragmentHome();
        fragmentContact = new FragmentContact();
        fragmentRequest = new FragmentRequest();
        fragmentUser = new FragmentUser();

        //set ToolBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DpChat");

        setFragment(fragmentHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home: {
                        setFragment(fragmentHome);
                        return true;
                    }
                    case R.id.nav_contact: {
                        setFragment(fragmentContact);
                        return true;
                    }
                    case R.id.nav_request: {
                        setFragment(fragmentRequest);
                        return true;
                    }
                    case R.id.nav_me: {
                        setFragment(fragmentUser);
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_nav, fragment);
        fragmentTransaction.commit();
    }

    private void sendToStartLayout() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (currentUser == null) {
            sendToStartLayout();
        }
    }


    //định nghĩa menu chính cho main_layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //viet lai phuong thuc khi chon item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.toolBar_logOut) {
            FirebaseAuth.getInstance().signOut();
            sendToStartLayout();
        }
        if (item.getItemId() == R.id.main_menu_me){
            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(settingIntent);
        }
        if (item.getItemId() == R.id.main_allUsers){
            Intent settingIntent = new Intent(MainActivity.this, AllUserActivity.class);
            startActivity(settingIntent);
        }
        return true;
    }
}
