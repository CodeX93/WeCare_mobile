package com.example.wecare_doc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointmentList, Context context) {
        this.appointmentList = appointmentList;
        this.context = context;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.textViewDoctorName.setText(appointment.getDoctorName());
        holder.textViewAppointmentTime.setText(appointment.getTime());



//        holder.row.setOnClickListener(v -> {
//
//            Intent intent = new Intent(context, PatientBookAppointment.class);
//            // Pass the entire Doctor object with the Intent
//            intent.putExtra("selectedDoctor", contact);
//            context.startActivity(intent);
//
//        });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDoctorName, textViewAppointmentTime;
        LinearLayout row;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDoctorName = itemView.findViewById(R.id.textViewDoctorName);
            textViewAppointmentTime = itemView.findViewById(R.id.textViewAppointmentTime);

        }
    }

}