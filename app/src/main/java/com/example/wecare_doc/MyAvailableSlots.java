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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyAvailableSlots extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    Button buttonDate, oldbuttonTime, newbuttonTime,updateTimeSlotBtn;
    private RecyclerView recyclerView;
    private String oldTimeSelected,newTimeSelected,newDurationPeriod;
    FirebaseFirestore db;
    private AdapterAvailableSlot adapterAvailableSlot;
    private ArrayList<TimeSlots> timeSlotsArrayList;
    private int hour, minute;
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
        db = FirebaseFirestore.getInstance();
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
        updateTimeSlotBtn=findViewById(R.id.UpadateAppointSlot_Btn);
        DoctorId = DB.collection("doctor").document().getId();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.duration_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newDuration.setAdapter(adapter);


        newDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newDurationPeriod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });


        updateTimeSlotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldTime=oldTimeSelected;
                String newTime=newTimeSelected;
                String duration=newDurationPeriod;
                String endTime=calculateFinalTime(newTime,duration);
                String date=queryDate;
                String DoctorId="idisidsadsadsadqwe";
                Log.d("TimeSlotData",""+newTime+" "+duration+" "+endTime+" "+date+" "+DoctorId);
                TimeSlots timeSlots=new TimeSlots(date,newTime,duration,"false",DoctorId,endTime);
                db.collection("AvailableSlots").add(timeSlots)
                        .addOnSuccessListener(documentReference -> Toast.makeText(MyAvailableSlots.this,"SLots updated",Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e->Log.d("failing","Slots not updated"));
            }
        });




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
                    DoctorId = "idisidsadsadsadqwe";  // Replace with actual logic to get the doctor's ID
                    fetchTimeSlots(DoctorId, queryDate);
                }, year, month, day);
        datePickerDialog.show();





    oldbuttonTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OldshowTimePickerDialog();
        }
    });

    newbuttonTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NewshowTimePickerDialog();
        }
    });


    }


    private void OldshowTimePickerDialog() {
        // Use the current time as the default values for the picker

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    hour = selectedHour;
                    minute = selectedMinute;
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    oldTimeSelected=formattedTime;
                }, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    private void NewshowTimePickerDialog() {
        // Use the current time as the default values for the picker

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    hour = selectedHour;
                    minute = selectedMinute;
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                    newTimeSelected=formattedTime;
                }, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }





    // Fetch booked time slots
    private void fetchTimeSlots(String doctorId, String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AvailableSlots")
                .whereEqualTo("doctorId", doctorId)
                .whereEqualTo("Date", date)
                .whereEqualTo("booked", "true")
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

    public static String calculateFinalTime(String startTime, String duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date start = dateFormat.parse(startTime);

            // Check if duration is just in minutes
            int durationHours = 0;
            int durationMinutes;
            if (duration.contains(":")) {
                String[] durationParts = duration.split(":");
                if (durationParts.length < 2) {
                    throw new IllegalArgumentException("Invalid duration format");
                }
                durationHours = Integer.parseInt(durationParts[0]);
                durationMinutes = Integer.parseInt(durationParts[1]);
            } else {
                // Assuming duration is just minutes
                durationMinutes = Integer.parseInt(duration);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.HOUR_OF_DAY, durationHours);
            calendar.add(Calendar.MINUTE, durationMinutes);

            return dateFormat.format(calendar.getTime());
        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
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


    private String calendarToString(Calendar calendar) {
        return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }


}
