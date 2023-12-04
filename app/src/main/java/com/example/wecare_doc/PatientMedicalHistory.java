package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientMedicalHistory extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuToggleBtn;
    private FirebaseAuth firebaseAuth;
    private RecyclerView medicalHistoryRecyclerView;
    private MedicalHistoryAdapter medicalHistoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_history);

        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawerLayout);
        menuToggleBtn = findViewById(R.id.menuToggleBtn);
        medicalHistoryRecyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView
        medicalHistoryAdapter = new MedicalHistoryAdapter(PatientMedicalHistory.this);
        medicalHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicalHistoryRecyclerView.setAdapter(medicalHistoryAdapter);

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

        LinearLayout upload = findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientMedicalHistory.this, PatientUploadMedicalHistory.class);
                startActivity(intent);
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

        // Load medical history data
        loadMedicalHistoryData();
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
            startActivity(new Intent(PatientMedicalHistory.this, PatientViewsAppointments.class));
        } else if (itemId == R.id.menuItemMedicalHistory) {
            // You are already in the Medical History activity, no need to navigate
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

        // Adjust the layout IDs based on your drawer header layout
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

    private void loadMedicalHistoryData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the medicalHistory collection
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("medicalHistory")
                    .whereEqualTo("user", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<MedicalHistory> medicalHistoryList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MedicalHistory medicalHistory = document.toObject(MedicalHistory.class);
                                medicalHistoryList.add(medicalHistory);
                            }

                            // Check if no records are found
                            if (medicalHistoryList.isEmpty()) {
                                // Handle the case where no records are found
                                // For example, show a message to the user
                                // You can update the UI accordingly
                                // For now, let's just log a message
                                Log.d("MedicalHistory", "No records found");
                            }

                            medicalHistoryAdapter.setMedicalHistoryList(medicalHistoryList);
                        } else {
                            // Handle failure
                            Log.e("MedicalHistory", "Error getting documents: ", task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Log the exception for additional debugging
                        Log.e("MedicalHistory", "Failed to load medical history data", e);
                    });
        }
    }

}
