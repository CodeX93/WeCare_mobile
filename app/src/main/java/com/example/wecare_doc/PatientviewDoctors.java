package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientviewDoctors extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorsAdapter docAdapter;

    private EditText searchField;
    private ImageView searchIcon, crossIcon;

    List<Doctor> docList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientview_doctors);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_home) {
                            // Switch to Home Fragment or Activity
                            // Example: replaceFragment(new HomeFragment());
                            Intent intent = new Intent(PatientviewDoctors.this, PatiendDashboard.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // Switch to Profile Fragment or Activity
                            // Example: replaceFragment(new ProfileFragment());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_calendar) {
                            // Switch to Calendar Fragment or Activity
                            // Example: replaceFragment(new CalendarFragment());
                            Intent intent = new Intent(PatientviewDoctors.this, PatientViewsAppointments.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_notification) {
                            // Switch to Notification Fragment or Activity
                            // Example: replaceFragment(new NotificationFragment());
                            Intent intent = new Intent(PatientviewDoctors.this, PatientNotificationScreen.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });

        docList = new ArrayList<>();

        searchField = findViewById(R.id.searchField);
        searchIcon = findViewById(R.id.searchIcon);
        crossIcon = findViewById(R.id.crossIcon);

        recyclerView = findViewById(R.id.doctorsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        docAdapter = new DoctorsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(docAdapter);

        fetchAllItemsFromFirestore();

        searchIcon.setOnClickListener(view -> {
            String searchQuery = searchField.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                performSearch(searchQuery);
            } else {
                // If the search query is empty, show a message or handle it accordingly.
                Toast.makeText(this, "Please enter a doctor's name to search", Toast.LENGTH_SHORT).show();
            }
        });

        crossIcon.setOnClickListener(view -> {
            if (!docList.isEmpty()) {
                docAdapter.setDoctorList(docList);
                docAdapter.notifyDataSetChanged();
            } else {
                // If the search query is empty, show a message or handle it accordingly.
                Toast.makeText(this, "No doctors available", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void performSearch(String searchQuery) {
        List<Doctor> searchResults = new ArrayList<>();

        for (Doctor doctor : docList) {
            // Case-insensitive search
            if (doctor.getDotorName().toLowerCase().contains(searchQuery.toLowerCase())) {
                searchResults.add(doctor);
            }
        }

        if (!searchResults.isEmpty()) {
            // Display the search results
            docAdapter.setDoctorList(searchResults);
            docAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Search results updated", Toast.LENGTH_SHORT).show();
        } else {
            // If no matching results found, you can handle it accordingly (e.g., show a message).
            Toast.makeText(this, "No matching doctors found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAllItemsFromFirestore() {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "doctors" collection
        CollectionReference itemsRef = db.collection("doctors");

        itemsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {

            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                Doctor ad = new Doctor();
                ad.setUid (document.getId());
                ad.setDotorName(document.get("name").toString());
                ad.setDoctorSpeciality(document.get("speciality").toString());
                ad.setRating( Integer.parseInt(document.get("rating").toString()));
                ad.setExperience(document.get("experience").toString());
                ad.setInformation(document.get("information").toString());
                ad.setPatients(document.get("patients").toString());
                ad.setFcmToken(document.get("fcmToken").toString());

                docList.add(ad);
            }

            Toast.makeText(this, "Fetched docs", Toast.LENGTH_SHORT).show();
            // Update the adapter with the fetched itemList
            docAdapter.setDoctorList(docList);
            docAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            String errorMessage = "Failed to fetch data from Firestore. Please try again later.";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }


}