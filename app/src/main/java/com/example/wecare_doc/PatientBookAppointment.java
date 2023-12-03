package com.example.wecare_doc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PatientBookAppointment extends AppCompatActivity {

    TextView doctorName, doctorSpeciality, information, patients, experience, rating, selectDate, selectTime;
    Button bookBtn;

    ImageView backIcon;

    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment);

        doctorName=findViewById(R.id.doctorName);
        doctorSpeciality=findViewById(R.id.doctorSpeciality);
        information=findViewById(R.id.information);
        patients=findViewById(R.id.patients);
        experience=findViewById(R.id.experience);
        rating=findViewById(R.id.rating);
        selectDate=findViewById(R.id.selectDate);
        selectTime=findViewById(R.id.selectTime);
        bookBtn=findViewById(R.id.bookBtn);
        backIcon=findViewById(R.id.backIcon);

        Intent intent = getIntent();
        Doctor selectedDoctor = (Doctor) intent.getSerializableExtra("selectedDoctor");

        doctorName.setText(selectedDoctor.getDotorName().toString());
        doctorSpeciality.setText(selectedDoctor.getDoctorSpeciality().toString());
        information.setText(selectedDoctor.getInformation().toString());
        patients.setText(selectedDoctor.getPatients().toString());
        experience.setText(selectedDoctor.getExperience().toString());
        rating.setText(""+selectedDoctor.getRating());

        // Set click listeners for the date and time TextViews
        selectDate.setOnClickListener(v -> showDatePickerDialog());
        selectTime.setOnClickListener(v -> showTimePickerDialog());

        backIcon.setOnClickListener(v -> {
           Intent intent1=new Intent(this, PatientviewDoctors.class);
           startActivity(intent1);
        });

        bookBtn.setOnClickListener(v -> {
            // Get the selected date and time from TextViews
            String selectedDate = selectDate.getText().toString();
            String selectedTime = selectTime.getText().toString();

            // You need to retrieve the doctorUid and patientUid from wherever you store them
            String doctorUid = selectedDoctor.getUid();
            String patientUid = "currentPatientUid"; // Replace with the actual patientUid
            String doctorName = selectedDoctor.getDotorName();

            // Create a new Appointment object
            Appointment newAppointment = new Appointment(selectedDate, selectedTime, doctorUid, patientUid, doctorName);

            // Add the new appointment to Firebase Firestore
            addAppointmentToFirestore(newAppointment);
        });

    }

    private void addAppointmentToFirestore(Appointment appointment) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "appointments" collection
        CollectionReference appointmentsRef = db.collection("appointments");

        // Add a new document with a generated ID
        appointmentsRef.add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // Handle success, e.g., show a success message
                    Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this, PatientAppointmentConfirmation.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., show an error message
                    Toast.makeText(this, "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(year, monthOfYear, dayOfMonth);
                    updateDateTextView();
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minute);
                    updateTimeTextView();
                },
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false // Set to true if you want 24-hour format
        );
        timePickerDialog.show();
    }

    private void updateDateTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void updateTimeTextView() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        selectTime.setText(sdf.format(selectedTime.getTime()));
    }

}