package com.example.wecare_doc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wecare_doc.PatiendDashboard;
import com.example.wecare_doc.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText phoneNumberEditText, codeEditText, usernameEditText;
    private Button sendCodeButton, verifyCodeButton;

    private FirebaseAuth mAuth;
    private String verificationId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mAuth = FirebaseAuth.getInstance();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        codeEditText = findViewById(R.id.codeEditText);
        usernameEditText = findViewById(R.id.nameEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        verifyCodeButton = findViewById(R.id.verifyCodeButton);

        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                sendVerificationCode(phoneNumber, username);
                Toast.makeText(PhoneAuthActivity.this, "Code sent", Toast.LENGTH_LONG).show();
            }
        });

        verifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = codeEditText.getText().toString().trim();
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber, String username) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential, username);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(PhoneAuthActivity.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        PhoneAuthActivity.this.verificationId = verificationId;
                    }
                }
        );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        String username = usernameEditText.getText().toString().trim();
        signInWithPhoneAuthCredential(credential, username);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String username) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();

                        // Save the user information to the database
                        saveUserDataToDatabase(user.getUid(), username, user.getPhoneNumber());

                        startActivity(new Intent(PhoneAuthActivity.this, PatiendDashboard.class));
                        Toast.makeText(PhoneAuthActivity.this, "Phone Authentication Successful", Toast.LENGTH_SHORT).show();
                        // Add your logic to proceed after successful authentication
                    } else {
                        Toast.makeText(PhoneAuthActivity.this, "Phone Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDataToDatabase(String userId, String username, String phoneNumber) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Create a user object with username and phone number
        User user = new User(userId, username, phoneNumber);

        // Save the user to the "users" node in the database
        databaseReference.child(userId).setValue(user);
    }
}
