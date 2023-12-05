package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyAvailableSlots extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    Button buttonDate, oldbuttonTime, newbuttonTime;
    private RecyclerView recyclerView;
    private AdapterAvailableSlot adapterAvailableSlot;
    private ArrayList<TimeSlots> timeSlotsArrayList;
    FirebaseFirestore DB;
    String queryDate, DoctorId;
    Spinner newDuration;

    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] dur = new String[]{"10", "15", "20", "25", "30", "45", "60"};
        setContentView(R.layout.activity_my_available_slots);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        newDuration = findViewById(R.id.new_slot_duration);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        recyclerView = findViewById(R.id.availableslots_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        timeSlotsArrayList = new ArrayList<>();
        DB = FirebaseFirestore.getInstance();
        adapterAvailableSlot = new AdapterAvailableSlot(timeSlotsArrayList, this, DB);
        recyclerView.setAdapter(adapterAvailableSlot);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonDate = findViewById(R.id.DateChooser_button);
        oldbuttonTime = findViewById(R.id.button_time);
        newbuttonTime = findViewById(R.id.button_newtime);
        DoctorId = DB.collection("doctor").document().getId();

        buttonDate.setOnClickListener(v -> showDatePickerDialog());

        oldbuttonTime.setOnClickListener(view -> showTimePickerDialog());

        newbuttonTime.setOnClickListener(view -> showTimePickerDialog());

        // Additional logic to add a new time slot
        newbuttonTime.setOnClickListener(view -> {
            String selectedTime = newbuttonTime.getText().toString();
            String duration = "01:00"; // example duration
            addNewTimeSlot(DoctorId, queryDate, selectedTime, duration);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.duration_time, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newDuration.setAdapter(adapter);
        newDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MyAvailableSlots.this,
                (view, hourOfDay, minute1) -> {
                    String selectedTime = hourOfDay + ":" + minute1;
                    newbuttonTime.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    buttonDate.setText(selectedDate);
                    queryDate = selectedDate;
                    DoctorId = "current_doctor_id";  // Replace with actual logic to get the doctor's ID
                    fetchTimeSlots(DoctorId, queryDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    // Fetch booked time slots
    private void fetchTimeSlots(String doctorId, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AvailableSlots")
                .whereEqualTo("DoctorId", doctorId)
                .whereEqualTo("Date", date)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        timeSlotsArrayList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TimeSlots timeSlot = document.toObject(TimeSlots.class);
                            timeSlotsArrayList.add(timeSlot);
                        }
                        adapterAvailableSlot.notifyDataSetChanged();
                    } else {
                        Log.d("MyAvailableSlots", "Error getting documents: ", task.getException());
                    }
                });
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
        int id = item.getItemId();

        if (id == R.id.nav_patients) {
            Intent intent = new Intent(this, MyPatient.class);
            startActivity(intent);
        } else if (id == R.id.nav_appointments) {

//            Intent intent = new Intent(this, app.class);
//            startActivity(intent);

        } else if (id == R.id.nav_Chats)

        {
            Intent intent = new Intent(this, MyChat.class);
            startActivity(intent);

        } else if (id == R.id.nav_change_slots) {
            Intent intent = new Intent(this, MyAvailableSlots.class);
//            intent.putExtra("DoctorName",)
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_settings) {
//            Intent intent = new Intent(this, MyS.class);
//            startActivity(intent);
        }

        // Close the navigation drawer
        DrawerLayout drawer = findViewById(R.id.my_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void addNewTimeSlot(String doctorId, String date, String startTime, String duration) {
        // Convert startTime and duration to a format that can be compared
        Calendar startCal = convertToCalendar(startTime);
        Calendar endCal = (Calendar) startCal.clone();
        addDurationToCalendar(endCal, duration);
        String endTime = null;

        // Check if the slot is available
        boolean isSlotAvailable = true;
        for (TimeSlots bookedSlot : timeSlotsArrayList) {
            Calendar bookedStart = convertToCalendar(bookedSlot.getStartTime());
            Calendar bookedEnd = convertToCalendar(bookedSlot.getEndTime());

            if (isTimeSlotOverlap(startCal, endCal, bookedStart, bookedEnd)) {
                isSlotAvailable = false;
                endTime=bookedEnd.toString();
                break;
            }
        }

        if (isSlotAvailable) {
            // Slot is available, add it to Firestore
            TimeSlots newSlot = new TimeSlots(doctorId, date, startTime, calendarToString(endCal), "",calculateEndTime(startTime,duration));
            DB.collection("AvailableSlots").add(newSlot)
                    .addOnSuccessListener(documentReference -> Log.d("AddSlot", "Slot added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w("AddSlot", "Error adding slot", e));
        } else {
            Log.d("AddSlot", "Time slot is not available");
        }
    }

    private Calendar convertToCalendar(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar;
    }
    public static String calculateEndTime(String startTime, String duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(startTime));

            String[] durationParts = duration.split(":");
            int hoursToAdd = Integer.parseInt(durationParts[0]);
            int minutesToAdd = Integer.parseInt(durationParts[1]);

            calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
            calendar.add(Calendar.MINUTE, minutesToAdd);

            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addDurationToCalendar(Calendar calendar, String duration) {
        String[] parts = duration.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.add(Calendar.MINUTE, minutes);
    }

    private boolean isTimeSlotOverlap(Calendar start1, Calendar end1, Calendar start2, Calendar end2) {
        return start1.before(end2) && start2.before(end1);
    }

    private String calendarToString(Calendar calendar) {
        return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }


}
