package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

public class MyAppointment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    FirebaseFirestore db;
    private AdapterAppointment AppointmentAdapter;
    private ArrayList<Appointment> appointmentList;
    private String selectedStatus = "Today"; // Default status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
db=FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.appointmentList_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<>();
        AppointmentAdapter = new AdapterAppointment(this, appointmentList,db);
        recyclerView.setAdapter(AppointmentAdapter);



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
            startActivity(new Intent(MyAppointment.this,MyAppointment.class));
            finish();
        } else if (id == R.id.navigation_appointment) {

        } else if (id == R.id.navigation_notifications) {
            startActivity(new Intent(MyAppointment.this, MyNotifications.class));
            finish();
        } else if (id == R.id.navigation_chats) {
            startActivity(new Intent(MyAppointment.this, MyChat.class));
        }
        return true;
    });
}



    private void loadPatientsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get today's date in the required format
        String todayDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

        Query query;
        if (selectedStatus.equals("today")) {
            // If selectedStatus is "today", query appointments with today's date
            query = db.collection("appointments").whereEqualTo("AppointmentDate", todayDate);

        } else {
            // If selectedStatus is not "today", query all appointments
            query = db.collection("appointments");
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                appointmentList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Appointment appointment = document.toObject(Appointment.class);
                    if (appointment != null) {
                        appointmentList.add(appointment);
                    } else {
                        Log.d("MyAppointment", "Document to Appointment conversion returned null");
                    }
                }
                AppointmentAdapter.notifyDataSetChanged();
            } else {
                Exception e = task.getException();
                if (e != null) {
                    Log.d("MyAppointment", "Error fetching documents: ", e);
                } else {
                    Log.d("MyAppointment", "Task failed without exception.");
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
