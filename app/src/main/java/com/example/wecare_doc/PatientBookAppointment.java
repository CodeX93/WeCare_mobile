package com.example.wecare_doc;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PatientBookAppointment extends AppCompatActivity {

    EditText doctorName, doctorSpeciality, information, patients, experience, rating, selectDate, selectTime;
    Button bookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_book_appointment);
    }
}