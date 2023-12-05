package com.example.wecare_doc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientSideMenu extends AppCompatActivity {

    TextView username , contactname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_side_menu);

        username = findViewById(R.id.username);
        contactname = findViewById(R.id.contactNumber);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();

            // Set username or email (whichever is available) to the TextViews
            if (displayName != null && !displayName.isEmpty()) {
                username.setText(displayName);
            } else if (email != null && !email.isEmpty()) {
                username.setText(email);
            }

            // Set contact number or email to the TextView
            if (email != null && !email.isEmpty()) {
                contactname.setText(email);
            }
        } else {
            Toast.makeText(PatientSideMenu.this,"Pleas login",Toast.LENGTH_LONG).show();
        }
    }

}