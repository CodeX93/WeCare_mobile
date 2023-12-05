package com.example.wecare_doc;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientUploadMedicalHistory extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menuToggleBtn, calendar;

    private LinearLayout uploadLayout;
    private Spinner spinner;
    private EditText titleEditText, dateEditText;
    private Button doneButton;
    private FirebaseAuth firebaseAuth;
    private Uri selectedPdfUri;
    private CollectionReference medicalHistoryCollection;
    private StorageReference storageReference;

    private static final int PICK_PDF_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_upload_medical_history);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        menuToggleBtn = findViewById(R.id.menuToggleBtn);
        uploadLayout = findViewById(R.id.upload);
        spinner = findViewById(R.id.spinner);
        titleEditText = findViewById(R.id.title);
        dateEditText = findViewById(R.id.date);
        calendar = findViewById(R.id.calendar);
        doneButton = findViewById(R.id.done);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        medicalHistoryCollection = firestore.collection("medicalHistory");
        storageReference = FirebaseStorage.getInstance().getReference();

        // Set up calendar icon click listener
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();
            }
        });

        uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPdfFile();
            }
        });

        // Set up drawer toggle button click listener
        menuToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDrawer();
                setDrawerHeaderUserInfo();
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleNavigationItemSelected(item);
                return true;
            }
        });

        // Set up spinner with test types
        setUpSpinner();

        // Set up done button click listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPdfAndSaveData();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_home) {
                            // Switch to Home Fragment or Activity
                            // Example: replaceFragment(new HomeFragment());
                            Intent intent = new Intent(PatientUploadMedicalHistory.this, PatiendDashboard.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // Switch to Profile Fragment or Activity
                            // Example: replaceFragment(new ProfileFragment());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_calendar) {
                            // Switch to Calendar Fragment or Activity
                            // Example: replaceFragment(new CalendarFragment());
                            Intent intent = new Intent(PatientUploadMedicalHistory.this, PatientViewsAppointments.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_notification) {
                            // Switch to Notification Fragment or Activity
                            // Example: replaceFragment(new NotificationFragment());
                            Intent intent = new Intent(PatientUploadMedicalHistory.this, PatientNotificationScreen.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
    }

    private void showCalendar() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date to the dateEditText
                        dateEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void uploadPdfAndSaveData() {
        if (selectedPdfUri != null && !TextUtils.isEmpty(titleEditText.getText()) && !TextUtils.isEmpty(dateEditText.getText())) {
            // Upload PDF to Firebase Storage
            String pdfFileName = getFileName(selectedPdfUri);
            StorageReference pdfRef = storageReference.child("pdfs/" + pdfFileName);
            pdfRef.putFile(selectedPdfUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded PDF
                        pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save data to Firestore
                            String testType = spinner.getSelectedItem().toString();
                            String title = titleEditText.getText().toString();
                            String date = dateEditText.getText().toString();
                            String user = firebaseAuth.getCurrentUser().getUid();
                            String documentUrl = uri.toString();

                            // Create a map for the medical history entry
                            Map<String, Object> medicalHistoryData = new HashMap<>();
                            medicalHistoryData.put("testType", testType);
                            medicalHistoryData.put("title", title);
                            medicalHistoryData.put("date", date);
                            medicalHistoryData.put("user", user);
                            medicalHistoryData.put("documentUrl", documentUrl);

                            // Add the medical history entry to Firestore
                            medicalHistoryCollection.add(medicalHistoryData)
                                    .addOnSuccessListener(documentReference -> {
                                        // Display success message
                                        Toast.makeText(PatientUploadMedicalHistory.this, "PDF Uploaded and Data Saved Successfully", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                        Toast.makeText(PatientUploadMedicalHistory.this, "Failed to save data to Firestore", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(PatientUploadMedicalHistory.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    private void handleNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemLogout) {
            logout();
        } else if (itemId == R.id.menuItemAppointments) {
            startActivity(new Intent(PatientUploadMedicalHistory.this, PatientViewsAppointments.class));
        } else if (itemId == R.id.menuItemMedicalHistory) {
            startActivity(new Intent(PatientUploadMedicalHistory.this, PatientMedicalHistory.class));
        }
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setUpSpinner() {
        List<String> testTypes = new ArrayList<>();
        testTypes.add("Select Type");
        testTypes.add("Blood Test");
        testTypes.add("CBC");
        testTypes.add("Dengue");
        testTypes.add("Sugar");
        testTypes.add("Neuron Test");

        // Add your own test types here

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, testTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // Set up spinner item selected listener if needed
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedTestType = testTypes.get(position);
                // Handle spinner item selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case where nothing is selected
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        // Redirect to the login screen or any other desired screen
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    private void setDrawerHeaderUserInfo() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);

        // Adjust the layout IDs based on your drawer header layout
        TextView username = headerView.findViewById(R.id.username);
        TextView contactNumber = headerView.findViewById(R.id.contactNumber);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in, set username and contact number
            String displayName = user.getDisplayName();
            String email = user.getEmail();

            // Set username to either display name or email
            username.setText(displayName != null ? displayName : email);

            // Set contact number to email
            contactNumber.setText(email);
        }
    }

    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            // Store the selected PDF URI
            selectedPdfUri = data.getData();

            // Display the file name in a Toast
            String pdfFileName = getFileName(selectedPdfUri);
            Toast.makeText(this, "Selected PDF: " + pdfFileName, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
