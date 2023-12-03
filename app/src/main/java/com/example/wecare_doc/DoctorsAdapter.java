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


import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private Context context;

    public DoctorsAdapter(List<Doctor> doctorList, Context context) {
        this.doctorList = doctorList;
        this.context = context;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_doctor_layout, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor contact = doctorList.get(position);
        holder.doctorName.setText(contact.getDotorName());
        holder.doctorSpeciality.setText(contact.getDoctorSpeciality());



//        holder.navigator.setOnClickListener(v -> {
//            String receiverUid = contact.getUserId();
//
//            Intent intent = new Intent(context, ChatDetailsActivity.class);
//            intent.putExtra("receiverUid", receiverUid);
//            intent.putExtra("receiverUsername", contact.getUsername());
//            intent.putExtra("receiverProfileUrl", User.currentUser.getMainProfileUrl());
//            context.startActivity(intent);
//
//        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView doctorName, doctorSpeciality;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);

        }
    }

}


