package com.example.wecare_doc;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends BottomSheetDialogFragment {

    private FirebaseAuth auth;

    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        EditText emailEditText = view.findViewById(R.id.email);

        Button resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(email)) {
                    // Call the method to send a password reset email
                    sendPasswordResetEmail(email);
                } else {
                    // Handle the case where email is empty
                    Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void sendPasswordResetEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email sent successfully
                            Toast.makeText(getContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                            dismiss(); // Close the bottom sheet after sending the email
                        } else {
                            // Handle errors
                            Toast.makeText(getContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Customize the behavior of the BottomSheetDialogFragment
        if (getDialog() != null) {
            getDialog().getWindow().setDimAmount(0.5f); // Adjust dim amount as needed
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
