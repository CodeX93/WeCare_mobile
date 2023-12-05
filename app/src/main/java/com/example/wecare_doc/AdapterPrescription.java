package com.example.wecare_doc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterPrescription extends RecyclerView.Adapter<AdapterPrescription.ViewHolder> {
    private Context context;
    private List<Prescription> prescriptionList;

    public AdapterPrescription(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public AdapterPrescription.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medication_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPrescription.ViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);

        // Extracting the medicine details from the map
        String medicineName = prescription.getMedicineDetails().getOrDefault("Name", "Not specified");
        String dosage = prescription.getMedicineDetails().getOrDefault("Dosage", "Not specified");
        String duration = prescription.getMedicineDetails().getOrDefault("Duration", "Not specified");

        holder.MedicineName.setText(medicineName);
        holder.Dosage.setText(dosage);
        holder.Duration.setText(duration);
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView MedicineName, Dosage, Duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MedicineName = itemView.findViewById(R.id.MedicienceName);
            Dosage = itemView.findViewById(R.id.IntakeDosages);
            Duration = itemView.findViewById(R.id.MedicienceDuration);
        }

        @Override
        public void onClick(View view) {
            // Implement onClick action if necessary
        }
    }
}
