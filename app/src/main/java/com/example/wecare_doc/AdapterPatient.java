package com.example.wecare_doc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterPatient extends RecyclerView.Adapter<AdapterPatient.ViewHolder> {

Context context;
List<Patient> patientArrayList;

    public AdapterPatient(Context context, ArrayList<Patient> patientArrayList) {
        this.context = context;
        this.patientArrayList = patientArrayList;
    }

    @NonNull
    @Override
    public AdapterPatient.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false); // Replace with the correct item layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPatient.ViewHolder holder, int position) {
        Patient patient=patientArrayList.get(position);
        holder.patient_name.setText(patient.getName());
//        holder.complain.setText(patient.getComplain());
        holder.text_date.setText(patient.getAdmittedDate());
        holder.disease_type.setText(patient.getDisease());

        String imageUrl = patient.getProfileURI();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.image_profile);
        } else {
            // Handle the case where there is no image URL
            // Maybe set a default image
            holder.image_profile.setImageResource(R.drawable.profile_picture); // Replace with your default image
        }
    }

    @Override
    public int getItemCount() {
        return  patientArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView patient_name, text_date,disease_type,complain;
        ImageView image_profile;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this);
            patient_name = itemView.findViewById(R.id.patient_name);
            disease_type = itemView.findViewById(R.id.disease_type);
            text_date = itemView.findViewById(R.id.text_date);
//            complain=itemView.findViewById(R.id.Patient_Complain_Section);
            image_profile = itemView.findViewById(R.id.image_profile);


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Patient patient = patientArrayList.get(position);

                // Create an intent to start the PatientDetail activity
                Intent intent = new Intent(context, PatientDetail.class);

                // Pass the item data to the ItemScreen activity
                intent.putExtra("patient_name", patient.getName());
                intent.putExtra("disease_type", patient.getDisease());
                intent.putExtra("patient_email", patient.getEmail());
                intent.putExtra("appt_date", patient.getAdmittedDate());
                intent.putExtra("Patient_age", patient.getAge());
                intent.putExtra("patient_gender", patient.getGender());
                intent.putExtra("image_profile", patient.getProfileURI());
                intent.putExtra("Patient_location", patient.getCity());
                intent.putExtra("Patient_Complain", patient.getComplain());

//                intent.putExtra("UserName", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                intent.putExtra("UserContactNo",FirebaseAuth.getInstance().getCurrentUser());
                // Start the activity
                context.startActivity(intent);
            }

        }
    }
}
