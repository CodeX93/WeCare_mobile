package com.example.wecare_doc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterAvailableSlot extends RecyclerView.Adapter<AdapterAvailableSlot.ViewHolder>{
    List<TimeSlots> timeSlots;
    Context context;

    public AdapterAvailableSlot(List<TimeSlots> timeSlots, Context context, FirebaseFirestore db) {
        this.timeSlots = timeSlots;
        this.context = context;
        this.db = db;
    }

    FirebaseFirestore db;
    @NonNull
    @Override
    public AdapterAvailableSlot.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointmentslotrow, parent, false); // Replace with the correct item layout
        return new AdapterAvailableSlot.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAvailableSlot.ViewHolder holder, int position) {
        TimeSlots slots=timeSlots.get(position);
        holder.TimeSlot.setText(slots.getStartTime());

    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TimeSlot;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TimeSlot = itemView.findViewById(R.id.appointmentTime);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
