package com.example.wecare_doc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_home) {
                            // Switch to Home Fragment or Activity
                            // Example: replaceFragment(new HomeFragment());
                            Intent intent = new Intent(PatientBookAppointment.this, PatiendDashboard.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // Switch to Profile Fragment or Activity
                            // Example: replaceFragment(new ProfileFragment());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_calendar) {
                            // Switch to Calendar Fragment or Activity
                            // Example: replaceFragment(new CalendarFragment());
                            Intent intent = new Intent(PatientBookAppointment.this, PatientViewsAppointments.class);
                            startActivity(intent);
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_notification) {
                            // Switch to Notification Fragment or Activity
                            // Example: replaceFragment(new NotificationFragment());
                            Intent intent = new Intent(PatientBookAppointment.this, PatientNotificationScreen.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });

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

            String complaint = showComplaintDialog();
            // Add the new appointment to Firebase Firestore
            addAppointmentToFirestore(newAppointment, complaint);
        });

    }

    private void addAppointmentToFirestore(Appointment appointment, String complain) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "appointments" collection
        CollectionReference appointmentsRef = db.collection("appointments");

        // Create a Map to represent the data
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("AppointmentDate", appointment.getDate());
        appointmentData.put("AppointmentDay", getDayOfWeek());
        appointmentData.put("AppointmentType", "Physical");
        appointmentData.put("AppointmentFee", "3000");
        appointmentData.put("Complain", complain);
        appointmentData.put("DoctorId",appointment.getDoctorUid() );
        //appointmentData.put("PatientId",appointment.getPatientUid() );
        appointmentData.put("patientUid","sample");
        appointmentData.put("Status","Confirmed");
        appointmentData.put("Timestamp", System.currentTimeMillis());
        appointmentData.put("id", generateRandomId());
        appointmentData.put("DoctorName", appointment.getDoctorName());
        appointmentData.put("Time", appointment.getTime());



        // Add a new document with a generated ID and the manually created map
        appointmentsRef.add(appointmentData)
                .addOnSuccessListener(documentReference -> {
                    // Handle success, e.g., show a success message
                    Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();

                    try {
                        sendNotification();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent=new Intent(this, PatientAppointmentConfirmation.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Handle failure, e.g., show an error message
                    Toast.makeText(this, "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    private String showComplaintDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Complaint");

        final String[] userComplaint = {"no complain"};

        // Set up the layout for the dialog
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userComplaint[0] = input.getText().toString();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();

        return userComplaint[0];

    }

    private String getDayOfWeek() {
        // Get the current day of the week
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Map the numeric day of the week to its corresponding name
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return daysOfWeek[dayOfWeek - 1];
    }

    private String generateRandomId() {
        // Generate a random UUID
        return UUID.randomUUID().toString();
    }


//    private void addAppointmentToFirestore(Appointment appointment) {
//        // Initialize Firestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Reference to the "appointments" collection
//        CollectionReference appointmentsRef = db.collection("appointments");
//
//        // Add a new document with a generated ID
//        appointmentsRef.add(appointment)
//                .addOnSuccessListener(documentReference -> {
//                    // Handle success, e.g., show a success message
//                    Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(this, PatientAppointmentConfirmation.class);
//                    startActivity(intent);
//                })
//                .addOnFailureListener(e -> {
//                    // Handle failure, e.g., show an error message
//                    Toast.makeText(this, "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show();
//                });
//    }

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

    public static void sendNotification() throws JSONException {

        JSONObject object = new JSONObject();

        JSONObject notfObject = new JSONObject();
        notfObject.put("title", "Appointment");
        notfObject.put("body", "An appointment has been booked");

        JSONObject dataObject = new JSONObject();
        dataObject.put("userId", "sample");

        object.put("notification", notfObject);
        object.put("data", dataObject);
        object.put("to", "eoC50vQjTLa9yabxRItgFE:APA91bHnAAmjK6yogZv9la6xkbzHlqkFpr7jff8wh8eIpXvFA-oLM31OlnLk97S9y_Dh_hskbYorj9eUNGwC0E42kk9fEyDCnZQeFjeW5WjuMRaipOX8_dNEYCx7snmEbcd3_UaTdVjX");

        callApi(object);


    }

    private static void callApi(JSONObject object){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";

        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAABKVQI3E:APA91bEnilLLDPJY-Uc985pk3E-CQbYpXKQwPGXYQ4jLJA5O9XsWRWFimy-44HR9VcA8oobJC-kaXRfnOfhXO6-_gDs5y8zxKF0jntmDfFGrj94g5Pw3Fy0TYklYIfISMKMqnT13c3j1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i("notification sent", object.toString());
            }
        });



    }


}