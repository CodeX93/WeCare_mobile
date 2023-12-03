package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Signup extends AppCompatActivity {

    EditText name,email,password;
    RelativeLayout googlebtn,phnbtn;
    CheckBox checkboxAgree;
    Button signupbtn;
    TextView lgin;
    ImageView google,phone;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        googlebtn = findViewById(R.id.googlebtn);
        phnbtn = findViewById(R.id.phnbtn);
        checkboxAgree = findViewById(R.id.checkboxAgree);
        signupbtn = findViewById(R.id.signupbtn);
        lgin = findViewById(R.id.lgnin);
        google = findViewById(R.id.imageButton1);
        phone = findViewById(R.id.imageButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,PhoneAuthActivity.class);
                startActivity(intent);
            }
        });

        phnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this,PhoneAuthActivity.class);
                startActivity(intent);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkboxAgree.isChecked()) {
                    signUpWithEmail();
                } else {
                    displayToast("Please agree to the terms and conditions");
                }
            }
        });

        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("19953361777-2ec3ve5k6s185c0blvq3skdd3fs4most.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(Signup.this, googleSignInOptions);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);
            }
        });

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent, 100);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();




    }

    private void signUpWithEmail() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        // Check if email and password are valid
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(Signup.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Firebase account with email and password
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                // Save user data to the database
                                saveUserDataToDatabase(user.getUid(), name.getText().toString().trim(), userEmail);
                            }

                            Toast.makeText(Signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, PatiendDashboard.class));
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(Signup.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(String userId, String userName, String userEmail) {
        // Create a user object with name and email
        User user = new User(userId, userName, userEmail);

        // Save the user to the "users" node in the database
        databaseReference.child(userId).setValue(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                System.out.println("I am here");
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    startActivity(new Intent(Signup.this, PatiendDashboard.class));
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Authentication Failed :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                    displayToast("Google Sign-In Failed: " + e.getLocalizedMessage());
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(Signup.this, s, Toast.LENGTH_SHORT).show();
    }


}