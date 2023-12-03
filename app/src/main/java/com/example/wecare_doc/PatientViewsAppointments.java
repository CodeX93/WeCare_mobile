package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientViewsAppointments extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AppointmentAdapter appAdapter;
    List<Appointment> appList;

    ImageView addAppointment;

    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_views_appointments);

        appList = new ArrayList<>();

        addAppointment=findViewById(R.id.addAppointment);

        calendarView=findViewById(R.id.calendarView);

        recyclerView = findViewById(R.id.appointmentsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        appAdapter = new AppointmentAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(appAdapter);

        fetchAllAppointmentsFromFirestore();

        addAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, PatientviewDoctors.class);
            startActivity(intent);
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Convert selected date to the desired format ("dd/MM/yyyy")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            String selectedDateString = sdf.format(selectedDate.getTime());

            // Filter appointments based on the selected date
            List<Appointment> filteredAppointments = filterAppointmentsByDate(selectedDateString);

            // Update the adapter with the filtered appointments list
            appAdapter.setAppointmentList(filteredAppointments);
            appAdapter.notifyDataSetChanged();
        });

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
                Appointment appointment = document.toObject(Appointment.class);

                if (appointment != null && appointment.getPatientUid().equals("currentPatientUid")) {
                    fetchedAppointments.add(appointment);
                }
            }

            appList=fetchedAppointments;
            // Filter appointments for today and update the adapter
            filterTodayAppointments(fetchedAppointments);
        }).addOnFailureListener(e -> {
            String errorMessage = "Failed to fetch appointments from Firestore. Please try again later.";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void filterTodayAppointments(List<Appointment> appointments) {
        List<Appointment> todayAppointments = new ArrayList<>();

        // Get the current date in the format "dd/MM/yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Iterate through appointments and filter those scheduled for today
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(currentDate)) {
                todayAppointments.add(appointment);
            }
        }

        // Update the adapter with the list of today's appointments
        appAdapter.setAppointmentList(todayAppointments);
        appAdapter.notifyDataSetChanged();
    }

    private List<Appointment> filterAppointmentsByDate(String selectedDate) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appList) {
            if (appointment.getDate().equals(selectedDate)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }
}