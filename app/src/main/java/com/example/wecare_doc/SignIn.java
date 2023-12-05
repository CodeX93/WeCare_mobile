package com.example.wecare_doc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SignIn extends AppCompatActivity {

    TextView signupbtn,forgetpw;

    Button signin;
    EditText name,email,password;
    RelativeLayout googlebtn,phnbtn;
    ImageView google,phone;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        googlebtn = findViewById(R.id.googlebtn);
        phnbtn = findViewById(R.id.phnbtn);
        google = findViewById(R.id.imageButton1);
        phone = findViewById(R.id.imageButton);
        forgetpw = findViewById(R.id.forgotpw);
        signin = findViewById(R.id.signinbtn);


        signupbtn = findViewById(R.id.signupbtn);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to the desired activity
            startActivity(new Intent(SignIn.this, PatiendDashboard.class));
            finish(); // Finish the current activity to prevent the user from coming back to the sign-in screen
        }

        forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordFragment();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this,Signup.class);
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this,PhoneAuthActivity.class);
                startActivity(intent);
            }
        });

        phnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this,PhoneAuthActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("19953361777-2ec3ve5k6s185c0blvq3skdd3fs4most.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(SignIn.this, googleSignInOptions);

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

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                // Check if email and password are not empty
                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    signInWithEmailAndPassword(userEmail, userPassword);
                } else {
                    // Display a message if email or password is empty
                    displayToast("Please enter email and password");
                }
            }
        });


    }

    private void signInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI and navigate to the desired activity
                            startActivity(new Intent(SignIn.this, PatiendDashboard.class));
                            displayToast("Email and password sign-in successful");
                        } else {
                            // If sign in fails, display a message to the user.
                            displayToast("Authentication Failed: " + task.getException().getMessage());
                        }
                    }
                });
    }



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
                                    startActivity(new Intent(SignIn.this, PatiendDashboard.class));
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

    private void showForgotPasswordFragment() {
        ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();
        forgotPasswordFragment.show(getSupportFragmentManager(), forgotPasswordFragment.getTag());
    }

    private void displayToast(String s) {
        Toast.makeText(SignIn.this, s, Toast.LENGTH_SHORT).show();
    }
}