package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    TextView patientName, diseaseType, age, location, gender, complain;
    ImageView profileImage;
    private RecyclerView recyclerView;
    private AdapterPrescription PrescriptionAdapter;
    private ArrayList<Prescription> prescriptionlist;
    Button EditPrescriptionBtn;
    Prescription tempPrescription;
    String Medname,Dosage,Duration,Email,Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        // Initialize views
        initViews();

        // Get data from intent
        setDataFromIntent(getIntent());

        // Fetch prescriptions from Firestore


        // Setup navigation
        setupNavigation();
    }

    private void initViews() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        recyclerView = findViewById(R.id.MedicationPlan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prescriptionlist=new ArrayList<>();
        PrescriptionAdapter = new AdapterPrescription(this, prescriptionlist);
        recyclerView.setAdapter(PrescriptionAdapter);



        patientName = findViewById(R.id.patient_name);
        diseaseType = findViewById(R.id.disease_type);
        age = findViewById(R.id.Patient_age);
        location = findViewById(R.id.Patient_location);
        gender = findViewById(R.id.patient_gender);
        profileImage = findViewById(R.id.image_profile);
        complain = findViewById(R.id.Patient_Complain_Section);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        EditPrescriptionBtn=findViewById(R.id.Edit_Prescription_Btn);

        EditPrescriptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PatientDetail.this,ChangePrescription.class);
//                Log.d("PresData",tempPrescription.toString());
                intent.putExtra("MedName",Medname);
                intent.putExtra("Dosage",Dosage);
                intent.putExtra("Duration",Duration);
                intent.putExtra("PatEmail",Email);
                intent.putExtra("ApptDate",Date);
                startActivity(intent);

            }
        });



    }

    private void setDataFromIntent(Intent intent) {
        String PatientName = intent.getStringExtra("patient_name");
        String DiseaseType = intent.getStringExtra("disease_type");
        String PatientAge = intent.getStringExtra("Patient_age");
        String PatientGender = intent.getStringExtra("patient_gender");
        String PatientProfileURI = intent.getStringExtra("image_profile");
        String PatientCity = intent.getStringExtra("Patient_location");
        String PatientComplain = intent.getStringExtra("Patient_Complain");
        String PatientEmail=intent.getStringExtra("patient_email");
        String AppointedDate=intent.getStringExtra("appt_date");
        Date=AppointedDate;


        patientName.setText(PatientName);
        gender.setText(PatientGender);
        diseaseType.setText(DiseaseType);
        age.setText(PatientAge);
        if (PatientProfileURI != null && !PatientProfileURI.isEmpty()) {
            profileImage.setImageURI(Uri.parse(PatientProfileURI));
        } else {
            profileImage.setImageResource(R.drawable.profile_picture);
        }
        complain.setText(PatientComplain);
        location.setText(PatientCity);
        fetchPrescriptions(AppointedDate,PatientEmail);

    }

    private void fetchPrescriptions(String date,String patientEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Filter the query by the patient name
        db.collection("Prescription")
                .whereEqualTo("Date", date)
                .whereEqualTo("PatientEmail", patientEmail)// Filter by patient name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prescriptionlist.clear(); // Clear the list before adding new data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, String> medicine = (Map<String, String>) document.getData().get("Medicine");
                            if (medicine != null) {
                                String name = medicine.get("Name");
                                String dosage = medicine.get("Dosage");
                                String duration = medicine.get("Duration");


                                Medname=name;
                                Dosage=dosage;
                                Duration=duration;
                                Email=patientEmail;

                                Map<String, String> medicineDetails = new HashMap<>();
                                medicineDetails.put("Name", name);
                                medicineDetails.put("Dosage", dosage);
                                medicineDetails.put("Duration", duration);



                                Prescription prescription = new Prescription(medicineDetails, date, patientEmail, patientEmail);
                                tempPrescription=prescription;
                                prescriptionlist.add(prescription);
                            } else {
                                Log.d("PrescriptionError", "Medicine details are null for document: " + document.getId());
                            }
                        }
                        PrescriptionAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("PatientDetail", "Error getting documents: ", task.getException());
                    }
                });
    }




    private void setupNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigation(id);
            return true;
        });
    }

    private void handleNavigation(int id) {
        if (id == R.id.navigation_home) {
            startActivity(new Intent(this, MyPatient.class));
            finish();
        } else if (id == R.id.navigation_appointment) {
            // Handle appointment navigation
            finish();
        } else if (id == R.id.navigation_notifications) {
            startActivity(new Intent(this, MyNotifications.class));
            finish();
        } else if (id == R.id.navigation_chats) {
            startActivity(new Intent(this, MyChat.class));
        } else if (id == R.id.nav_change_slots) {
            startActivity(new Intent(this, MyAvailableSlots.class));
            finish();
        } else if (id == R.id.nav_settings) {
            // Handle settings navigation
            finish();
        }
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
        handleNavigation(item.getItemId());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
