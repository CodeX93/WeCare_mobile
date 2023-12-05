package com.example.wecare_doc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> {

    private List<MedicalHistory> medicalHistoryList;
    private Context context;

    // Constructor to receive the data list and context
    public MedicalHistoryAdapter(Context context) {
        this.context = context;
        this.medicalHistoryList = new ArrayList<>(); // Initialize the list
    }

    // ViewHolder class to hold the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, fileSizeTextView;
        ImageView fileIconImageView, downloadIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from the layout
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            fileSizeTextView = itemView.findViewById(R.id.fileSizeTextView);
            fileIconImageView = itemView.findViewById(R.id.fileIconImageView);
            downloadIconImageView = itemView.findViewById(R.id.downloadIconImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind data to the views
        MedicalHistory medicalHistory = medicalHistoryList.get(position);

        // Set file name
        holder.fileNameTextView.setText(medicalHistory.getTitle());

        // Set file size (you may need to convert the file size to a human-readable format)
        holder.fileSizeTextView.setText("128kb"); // Replace with actual file size logic

        // Set file icon (you may need to determine the icon based on the file type)
        holder.fileIconImageView.setImageResource(R.drawable.baseline_file_copy_24); // Replace with actual file icon

        // Set download icon
        holder.downloadIconImageView.setImageResource(R.drawable.baseline_file_download_24);

        // Set click listener for the download icon if needed
        holder.downloadIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the download URL from the MedicalHistory object
                String downloadUrl = medicalHistoryList.get(position).getDocumentUrl();

                // Check if the download URL is not null or empty
                if (downloadUrl != null && !downloadUrl.isEmpty()) {
                    // Create a reference to the Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl);

                    // Create a local file to save the downloaded file
                    File localFile;
                    try {
                        // Create a temporary file with a random name
                        localFile = File.createTempFile("medical_history", "pdf");
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle the error, return or show a toast message
                        Toast.makeText(context, "Failed to create local file", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Download the file to the local file
                    storageRef.getFile(localFile)
                            .addOnSuccessListener(taskSnapshot -> {
                                // File downloaded successfully
                                String filePath = localFile.getAbsolutePath();
                                Toast.makeText(context, "File downloaded successfully. Path: " + filePath, Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(exception -> {
                                // Handle failed download
                                // Show a toast message or log the error
                                Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });


    }




    @Override
    public int getItemCount() {
        return medicalHistoryList != null ? medicalHistoryList.size() : 0;
    }

    // Method to update the data in the adapter
    public void setMedicalHistoryList(List<MedicalHistory> medicalHistoryList) {
        this.medicalHistoryList = medicalHistoryList;
        notifyDataSetChanged();
    }
}
