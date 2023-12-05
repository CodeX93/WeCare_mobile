package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MyChat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        // Setup for Navigation Drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup for Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int id= item.getItemId();
                    if(id== R.id.navigation_home){

                            startActivity(new Intent(MyChat.this, MyPatient.class));
                        finish();
                        }

                if(id==  R.id.navigation_appointment){
//                    startActivity(new Intent(MyChat.this, MyAppointment.class));
                        finish();}
                if(id==   R.id.navigation_notifications) {
                    startActivity(new Intent(MyChat.this, MyNotifications.class));
                finish();
                }
                if(id== R.id.navigation_chats){
                        // Already in MyChat, no action needed

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

            if(id== R.id.nav_patients){
                    startActivity(new Intent(this, MyPatient.class));

                finish();}
        if(id== R.id.nav_appointments){
//            startActivity(new Intent(this, MyAppointment.class));
                finish();}
        if(id== R.id.nav_Chats){
                // Already in MyChat, no action needed
                finish();}
        if(id== R.id.nav_change_slots){
            startActivity(new Intent(this, MyAvailableSlots.class));
                finish();}
        if(id== R.id.nav_settings) {
//            startActivity(new Intent(this, Settings.class));
        finish();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
