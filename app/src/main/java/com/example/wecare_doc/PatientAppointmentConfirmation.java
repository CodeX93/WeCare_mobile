package com.example.wecare_doc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PatientAppointmentConfirmation extends AppCompatActivity {

    Button viewAppointments,scheduleAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_confirmation);

        viewAppointments.setOnClickListener(v -> {
            Intent intent=new Intent(this, PatientViewsAppointments.class);
            startActivity(intent);
        });

        scheduleAnother.setOnClickListener(v -> {
            Intent intent=new Intent(this, PatientviewDoctors.class);
            startActivity(intent);
        });



    }
}