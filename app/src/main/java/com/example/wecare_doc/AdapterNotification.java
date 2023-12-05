package com.example.wecare_doc;

import android.content.Context;
import android.net.Uri;
import android.os.health.TimerStat;
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

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder>{



    List<Notification> notificationList;
    Context context;
    FirebaseFirestore db;
    public AdapterNotification(List<Notification> notificationList, Context context, FirebaseFirestore DB) {
        this.notificationList = notificationList;
        this.context = context;
        this.db=DB;
    }
    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false); // Replace with the correct item layout
        return new AdapterNotification.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {
        Notification notification=notificationList.get(position);
        fetchSenderName(notification.getSenderId(), holder.SenderName,holder.SenderProfileURL);
        holder.NotificationType.setText(notification.getType());
        holder.Timestamp.setText(notification.getTimestamp());
        holder.Title.setText(notification.getTitle());
        holder.Content.setText(notification.getContent());

    }
    private void fetchSenderName(String senderId, TextView senderNameTextView,ImageView imageView) {
        db.collection("users").document(senderId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String senderName = documentSnapshot.getString("name"); // Assuming 'name' is the field for sender's name
                String profileUrl = documentSnapshot.getString("profileImage"); // Assuming 'name' is the field for sender's name
                senderNameTextView.setText(senderName);
                if (profileUrl != null && !profileUrl.isEmpty()) {
                    Picasso.get().load(profileUrl).into(imageView);
                } else {
                    // Handle the case where there is no image URL
                    // Maybe set a default image
                    imageView.setImageResource(R.drawable.profile_picture); // Replace with your default image
                }

            } else {
                senderNameTextView.setText("Unknown Sender");
            }
        }).addOnFailureListener(e -> senderNameTextView.setText("Error"));
    }
    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView SenderName,NotificationType,Timestamp,Title,Content;
        ImageView SenderProfileURL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SenderName = itemView.findViewById(R.id.sender_name);
            NotificationType = itemView.findViewById(R.id.notification_type);
            Timestamp = itemView.findViewById(R.id.notification_timestamp);
            SenderProfileURL = itemView.findViewById(R.id.sender_image_profile);
            Title=itemView.findViewById(R.id.notification_title);
            Content=itemView.findViewById(R.id.notification_content);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
