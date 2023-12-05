package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PatientViewsAppointments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_views_appointments);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_home) {
                            // Switch to Home Fragment or Activity
                            // Example: replaceFragment(new HomeFragment());
                            Intent intent = new Intent(PatientViewsAppointments.this, PatiendDashboard.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // Switch to Profile Fragment or Activity
                            // Example: replaceFragment(new ProfileFragment());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_calendar) {
                            // Switch to Calendar Fragment or Activity
                            // Example: replaceFragment(new CalendarFragment());
                            Intent intent = new Intent(PatientViewsAppointments.this, PatientViewsAppointments.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_notification) {
                            // Switch to Notification Fragment or Activity
                            // Example: replaceFragment(new NotificationFragment());
                            Intent intent = new Intent(PatientViewsAppointments.this, PatientNotificationScreen.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
    }
}