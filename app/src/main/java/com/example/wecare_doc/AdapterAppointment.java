package com.example.wecare_doc;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAppointment extends RecyclerView.Adapter<AdapterAppointment.ViewHolder>{
String patientId;
    public AdapterAppointment(Context context, List<Appointment> appointmentList,FirebaseFirestore db) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.DB=db;
    }

    Context context;
    List<Appointment> appointmentList;
    FirebaseFirestore DB;
    @NonNull
    @Override
    public AdapterAppointment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentcard_row, parent, false); // Replace with the correct item layout
        return new AdapterAppointment.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAppointment.ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        // Ensure patientId is set from the appointment object
        String patientId = appointment.getPatientUid();
        if (patientId != null && !patientId.isEmpty()) {
            Log.d("PUIDD",patientId);
            fetchSenderName(patientId, holder.patientName, holder.profileImage, DB);
        } else {
            // Handle case where patientId is null or empty
            Log.e("AdapterAppointment", "Patient ID is null or empty for position: " + position);
            holder.patientName.setText("Unknown Patient");
        }

        holder.complain.setText(appointment.getComplain());
        holder.appointment_date.setText(appointment.getDate());
    }
    private void fetchSenderName(String senderId, TextView PatientNameTextView,ImageView imageView,FirebaseFirestore db) {

        db.collection("users").document(senderId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                String PatientName = documentSnapshot.getString("name"); // Assuming 'name' is the field for sender's name
                String profileUrl = documentSnapshot.getString("profileImage"); // Assuming 'name' is the field for sender's name

                PatientNameTextView.setText(PatientName);

                if (profileUrl != null && !profileUrl.isEmpty()) {
                    Picasso.get().load(profileUrl).into(imageView);
                } else {
                    // Handle the case where there is no image URL
                    // Maybe set a default image
                    imageView.setImageResource(R.drawable.profile_picture); // Replace with your default image
                }

            } else {
                PatientNameTextView.setText("Unknown Sender");
            }
        }).addOnFailureListener(e -> PatientNameTextView.setText("Error"));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patientName,complain,appointment_date;
        ImageView profileImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName=itemView.findViewById(R.id.patient_name);
            complain=itemView.findViewById(R.id.complain);
            appointment_date=itemView.findViewById(R.id.text_date);
            profileImage=itemView.findViewById(R.id.image_profile);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
