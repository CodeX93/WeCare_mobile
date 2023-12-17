package com.example.wecare_doc;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


public class ChangePrescription extends AppCompatActivity {

    FirebaseFirestore db;
    String Duration;
    EditText MedName,DosageIntake;
    Spinner DurationChooser;
    Button Update_Btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_prescription_record);
        db = FirebaseFirestore.getInstance();

        MedName = findViewById(R.id.MedicineName);
        DurationChooser = findViewById(R.id.Duration_Chooser);
        DosageIntake = findViewById(R.id.DosageIntake);
        Update_Btn = findViewById(R.id.updatePrescription);

        Intent intent = getIntent();
        String medName = intent.getStringExtra("MedName");
        String Dosage = intent.getStringExtra("Dosage");

        String PatientEmail = intent.getStringExtra("PatEmail");
        String Date = intent.getStringExtra("ApptDate");

        MedName.setText(medName);

        DosageIntake.setText(Dosage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.medicience_duration, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DurationChooser.setAdapter(adapter);

        DurationChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Duration = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Update_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!medName.isEmpty()) {
                    Log.d("PatientE",PatientEmail);
                    Log.d("PatientM",MedName.getText().toString());
                    Log.d("PatientD",DosageIntake.getText().toString());
                    Log.d("PatientDu",Duration);
                    updatePrescription(PatientEmail, MedName.getText().toString(), DosageIntake.getText().toString(), Duration, Date.toString());

                }
            }

            private void updatePrescription(String patientEmail, String medName, String dosage, String duration, String date) {
                db.collection("Prescription")
                        .whereEqualTo("PatientEmail", patientEmail) // Make sure the field name matches your Firestore field
                        .whereEqualTo("Date", date)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    // Prepare the nested Medicine map
                                    Map<String, Object> medicineUpdate = new HashMap<>();
                                    medicineUpdate.put("Medicine.Name", medName); // Use dot notation for nested fields
                                    medicineUpdate.put("Medicine.Dosage", dosage);
                                    medicineUpdate.put("Medicine.Duration", duration);

                                    // Update the document
                                    db.collection("Prescription").document(documentSnapshot.getId())
                                            .update(medicineUpdate)
                                            .addOnSuccessListener(aVoid -> Toast.makeText(ChangePrescription.this, "Prescription Updated Successfully", Toast.LENGTH_LONG).show())
                                            .addOnFailureListener(e -> Toast.makeText(ChangePrescription.this, "Error updating prescription", Toast.LENGTH_LONG).show());

                                startActivity(new Intent(ChangePrescription.this, MyPatient.class));
                                }
                            } else {
                                Toast.makeText(ChangePrescription.this, "No matching document found", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(ChangePrescription.this, "Error finding prescription", Toast.LENGTH_LONG).show());
            }

        });
    }
}
