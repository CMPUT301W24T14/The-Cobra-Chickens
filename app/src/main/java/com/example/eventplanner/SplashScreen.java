package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
/**
 * SplashScreen Activity serves as the initial screen when the app is launched.
 * It handles user authentication and initialization of user data.
 */
public class SplashScreen extends AppCompatActivity {

    private Button buttonContinue;
    private TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        // initialize connection to the database
        db = FirebaseFirestore.getInstance();

        buttonContinue = findViewById(R.id.btn_continue);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // initialize user object
                                    initializeUser();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(SplashScreen.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    /**
     * Initializes a user in the database and passes a User object to MainActivity
     * @return The initialized User object to be passed to MainActivity
     */
    private void initializeUser() {

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            HashMap<String, Object> userMap = new HashMap<>();

                            userMap.put("Name", mAuth.getCurrentUser().getUid());

                            userMap.put("Homepage", "");
                            userMap.put("Contact", "");
                            userMap.put("ProfilePic", "");
                            userMap.put("Location", false);
                            userMap.put("myEvents", new ArrayList<>());
                            userMap.put("Organizing", new ArrayList<>());
                            userMap.put("checkedInto", new ArrayList<>());
                            userMap.put("reusableCodes", new ArrayList<>());

                            db.collection("users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        else {
                            Log.e("Firestore", task.getException().toString());
                        }
                    }
                });

    }
}