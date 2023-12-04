package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatiendDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuToggleBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patiend_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        menuToggleBtn = findViewById(R.id.menuToggleBtn);

        // Set up the drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle clicks on the menu toggle button
        menuToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawer();
                setDrawerHeaderUserInfo();
            }

        });

        // Set up the navigation view
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemLogout) {
            logout();
        } else if (itemId == R.id.menuItemAppointments) {
            startActivity(new Intent(PatiendDashboard.this, PatientViewsAppointments.class));
        }else if(itemId == R.id.menuItemMedicalHistory){
            startActivity(new Intent(PatiendDashboard.this, PatientMedicalHistory.class));
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
        // Redirect to the login screen or any other desired screen
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void setDrawerHeaderUserInfo() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        TextView username = headerView.findViewById(R.id.username);
        TextView contactNumber = headerView.findViewById(R.id.contactNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in, set username and contact number
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            // Set username to either display name or email
            username.setText(displayName != null ? displayName : email);

            // Set contact number to email
            contactNumber.setText(email);
        }
    }
}


