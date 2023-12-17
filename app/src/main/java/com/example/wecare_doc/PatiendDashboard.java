package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatiendDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuToggleBtn;
    FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private AppointmentAdapter appAdapter;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patiend_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        menuToggleBtn = findViewById(R.id.menuToggleBtn);

        textview = findViewById(R.id.textViewName);



        recyclerView = findViewById(R.id.appointmentsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        appAdapter = new AppointmentAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(appAdapter);

        fetchAllAppointmentsFromFirestore();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in, update TextView with user's name
            String userName = currentUser.getDisplayName();
            textview.setText("Welcome, " + userName + "!");
        } else {
            textview.setText("Welcome, unknown!");
        }

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_home) {
                            // Switch to Home Fragment or Activity
                            // Example: replaceFragment(new HomeFragment());
                            Intent intent = new Intent(PatiendDashboard.this, PatiendDashboard.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // Switch to Profile Fragment or Activity
                            // Example: replaceFragment(new ProfileFragment());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_calendar) {
                            // Switch to Calendar Fragment or Activity
                            // Example: replaceFragment(new CalendarFragment());
                            Intent intent = new Intent(PatiendDashboard.this, PatientViewsAppointments.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_notification) {
                            // Switch to Notification Fragment or Activity
                            // Example: replaceFragment(new NotificationFragment());
                            Intent intent = new Intent(PatiendDashboard.this, PatientNotificationScreen.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
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
        }else if(itemId == R.id.menuItemSwitch){
            startActivity(new Intent(PatiendDashboard.this, MyPatient.class));
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

    private void fetchAllAppointmentsFromFirestore() {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "appointments" collection
        CollectionReference appointmentsRef = db.collection("appointments");

        appointmentsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Appointment> fetchedAppointments = new ArrayList<>();
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                // Convert each document to an Appointment object
                Appointment appointment = new Appointment();
                appointment.setDate(document.get("AppointmentDate").toString());
                appointment.setTime(document.get("Time").toString());
                appointment.setPatientUid(document.get("patientUid").toString());
                appointment.setDoctorUid(document.get("DoctorId").toString());
                appointment.setDoctorName(document.get("DoctorName").toString());
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                String currentUid = currentUser.getUid();
                if (appointment != null && appointment.getPatientUid().equals("sample")) {
                    fetchedAppointments.add(appointment);
                }
            }

            // Update the adapter with the list of today's appointments
            appAdapter.setAppointmentList(fetchedAppointments);
            appAdapter.notifyDataSetChanged();


        }).addOnFailureListener(e -> {
            String errorMessage = "Failed to fetch appointments from Firestore. Please try again later.";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

}


