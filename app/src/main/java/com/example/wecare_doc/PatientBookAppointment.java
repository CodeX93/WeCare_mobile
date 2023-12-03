package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PatientBookAppointment extends AppCompatActivity {

    TextView doctorName, doctorSpeciality, information, patients, experience, rating, selectDate, selectTime;
    Button bookBtn;

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

        Intent intent = getIntent();
        Doctor selectedDoctor = (Doctor) intent.getSerializableExtra("selectedDoctor");

        doctorName.setText(selectedDoctor.getDotorName().toString());
        doctorSpeciality.setText(selectedDoctor.getDoctorSpeciality().toString());
        information.setText(selectedDoctor.getInformation().toString());
        patients.setText(selectedDoctor.getPatients().toString());
        experience.setText(selectedDoctor.getExperience().toString());
        rating.setText(""+selectedDoctor.getRating());


    }
}