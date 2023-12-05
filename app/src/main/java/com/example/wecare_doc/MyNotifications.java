package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyNotifications extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private AdapterNotification NotificationAdapter;
    private ArrayList<Notification> notificationArrayList;
    FirebaseFirestore DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        recyclerView = findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationArrayList=new ArrayList<>();
        DB=FirebaseFirestore.getInstance();
        NotificationAdapter= new AdapterNotification(notificationArrayList, this,DB);
        recyclerView.setAdapter(NotificationAdapter);




        fetchNotifications();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                if(id== R.id.navigation_home){
                  startActivity(new Intent(MyNotifications.this, MyPatient.class));

                    finish();
                }

                if(id==  R.id.navigation_appointment){
//                    startActivity(new Intent(MyChat.this, MyAppointment.class));
                    finish();}
                if(id==   R.id.navigation_notifications) {

                }
                if(id== R.id.navigation_chats){
                    startActivity(new Intent(MyNotifications.this, MyChat.class));

                }
                return true;
            }
        });
    }

    private void fetchNotifications() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Notification")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Notification notification = document.toObject(Notification.class);
                            notificationArrayList.add(notification);
                        }
                        Log.d("SUC", notificationArrayList.toString());
                        NotificationAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("YourActivity", "Error getting notifications: ", task.getException());
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

        if(id== R.id.nav_patients){
            startActivity(new Intent(this, MyPatient.class));

            finish();}
        if(id== R.id.nav_appointments){
//            startActivity(new Intent(this, MyAppointment.class));
            finish();}
        if(id== R.id.nav_Chats){
            // Already in MyChat, no action needed
            finish();}
        if(id== R.id.nav_change_slots){
            startActivity(new Intent(this, MyAvailableSlots.class));
            finish();}
        if(id== R.id.nav_settings) {
//            startActivity(new Intent(this, Settings.class));
            finish();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}