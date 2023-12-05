package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyPatient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private AdapterPatient PatientAdapter;
    private ArrayList<Patient> patientList;
    private String selectedStatus = "All"; // Default status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        recyclerView = findViewById(R.id.patientlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientList = new ArrayList<>();
        PatientAdapter = new AdapterPatient(this, patientList);
        recyclerView.setAdapter(PatientAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedStatus=null;
                selectedStatus = tab.getText().toString().toLowerCase();
                loadPatientsFromFirestore();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected if needed
            }
        });

        // Load patients from Firestore initially
        loadPatientsFromFirestore();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                // Handle home navigation
            } else if (id == R.id.navigation_appointment) {
                // Handle appointment navigation
                finish();
            } else if (id == R.id.navigation_notifications) {
                startActivity(new Intent(MyPatient.this, MyNotifications.class));
                finish();
            } else if (id == R.id.navigation_chats) {
                startActivity(new Intent(MyPatient.this, MyChat.class));
            }
            return true;
        });
    }

    private void loadPatientsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("mypatients");
        if (!selectedStatus.equals("all")) {
            query = query.whereEqualTo("status", selectedStatus);
        }
        query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        patientList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Patient patient = document.toObject(Patient.class);
                            if (patient != null) {
                                patientList.add(patient);
                            } else {
                                Log.d("MyPatient", "Document to Patient conversion returned null");
                            }
                        }
                        PatientAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            Log.d("MyPatient", "Error fetching documents: ", e);
                        } else {
                            Log.d("MyPatient", "Task failed without exception.");
                        }
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
        if (id == R.id.nav_patients) {
            startActivity(new Intent(this, MyPatient.class));
            finish();
        } else if (id == R.id.nav_appointments) {
            // Handle appointments navigation
            finish();
        } else if (id == R.id.nav_Chats) {
            startActivity(new Intent(this, MyChat.class));
            finish();
        } else if (id == R.id.nav_change_slots) {
            startActivity(new Intent(this, MyAvailableSlots.class));
            finish();
        } else if (id == R.id.nav_settings) {
            // Handle settings navigation
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
