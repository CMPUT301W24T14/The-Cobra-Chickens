package com.example.eventplanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ScanFragment extends Fragment {
    private CompoundBarcodeView barcodeView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);

        // In your onCreateView method, before calling decodeContinuous
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        barcodeView.decodeContinuous(result -> {
            barcodeView.pause();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (result.getResult() != null) {
                String[] parts = String.valueOf(result.getResult()).split(":");
                String checkInCodeFromQR = parts[0];
                String userId = auth.getCurrentUser().getUid();

                final String[] eventId = new String[1];

                if (Objects.equals(parts[1], "check")) {
                    db.collection("events").whereEqualTo("checkInCode", checkInCodeFromQR)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventId[0] = document.getId();
                                    }
                                    Log.d("EVENT ID", eventId[0]);
                                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists() && eventId[0] != null) {
                                            db.collection("events").document(eventId[0]).update("signedUpUsers", FieldValue.arrayUnion(userId));
                                            db.collection("users").document(userId).update("myEvents", FieldValue.arrayUnion(eventId[0]));

                                            db.collection("events").document(eventId[0]).update("checkedInUsers", FieldValue.arrayUnion(userId));
                                            db.collection("users").document(userId).update("checkedInto", FieldValue.arrayUnion(eventId[0]));

                                            sharedViewModel.setEventUpdated(true);

                                            builder.setTitle("Success!");
                                            builder.setMessage("You have successfully checked in to the event!");
                                        } else {
                                            builder.setTitle("Failure!");
                                            if (eventId[0] == null) {
                                                builder.setMessage("Event Id is empty");
                                            } else {
                                                builder.setMessage("User does not exist!");
                                            }
                                        }
                                        builder.setPositiveButton("OK", (dialog, which) -> {
                                            dialog.dismiss();
                                            barcodeView.resume();
                                        }).show();
                                    });
                                }
                            });

                }
                else if (Objects.equals(parts[1], "promo")) {
                    db.collection("events").whereEqualTo("promoCode", checkInCodeFromQR)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventId[0] = document.getId();
                                    }
                                    Log.d("EVENT ID", eventId[0]);
                                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                        Dialog dialog = new Dialog(requireContext());
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                // This code will be called as soon as the dialog is no longer showing on screen
                                                barcodeView.resume();
                                            }
                                        });

                                        if (documentSnapshot.exists() && eventId[0] != null) {

                                            dialog.setContentView(R.layout.activity_event_details);

                                            ImageView poster = dialog.findViewById(R.id.poster);
                                            poster.setVisibility(View.GONE);
                                            db.collection("events").document(eventId[0]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        // Get the field from the document
                                                        String eventName = documentSnapshot.getString("eventName");
                                                        String eventTime = documentSnapshot.getString("eventTime");
                                                        String eventDate = documentSnapshot.getString("eventDate");
                                                        String eventLocation = documentSnapshot.getString("eventLocation");
                                                        String eventDescription = documentSnapshot.getString("eventDescription");

                                                        // Find the TextView and set the text

                                                        TextView eventNameTextView = dialog.findViewById(R.id.event_name);
                                                        TextView eventTimeTextView = dialog.findViewById(R.id.event_time);
                                                        TextView eventDateTextView = dialog.findViewById(R.id.event_date);
                                                        TextView eventLocationTextView = dialog.findViewById(R.id.event_location);
                                                        TextView eventDescriptionTextView = dialog.findViewById(R.id.event_description);

                                                        eventNameTextView.setText(eventName);
                                                        eventDateTextView.setText(eventDate);
                                                        eventTimeTextView.setText(eventTime);
                                                        eventDescriptionTextView.setText(eventDescription);
                                                        eventLocationTextView.setText(eventLocation);
                                                    }
                                                }
                                            });

                                            Button signUpButton = dialog.findViewById(R.id.signupBtn);

                                            signUpButton.setOnClickListener(v -> {
                                                Toast.makeText(requireActivity(), "Your event is in My Events", Toast.LENGTH_SHORT).show();
                                                db.collection("events").document(eventId[0]).update("signedUpUsers", FieldValue.arrayUnion(userId));
                                                db.collection("users").document(userId).update("myEvents", FieldValue.arrayUnion(eventId[0]));
                                                dialog.dismiss();
                                            });


                                            Window window = dialog.getWindow();
                                            if (window != null) {
                                                // Set the dimensions of the dialog
                                                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                                                layoutParams.copyFrom(window.getAttributes());

                                                // Set the width and height of the dialog to match the screen size
                                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                                ((Activity) requireContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                                layoutParams.width = displayMetrics.widthPixels;
                                                layoutParams.height = (int) (displayMetrics.heightPixels * .60);

                                                window.setAttributes(layoutParams);
                                            }
                                            dialog.show();
                                        } else {
                                            builder.setTitle("Failure!");
                                            if (eventId[0] == null) {
                                                builder.setMessage("Event Id is empty");
                                            } else {
                                                builder.setMessage("User does not exist!");
                                            }
                                        }

                                    });
                                }
                            });
                }
                else if (Objects.equals(parts[1], "admin")) {
                    Intent intent = new Intent(getContext(), AdminActivity.class);
                    startActivity(intent);
                }
            } else {
                builder.setTitle("Failure!");
                builder.setMessage("result is null!");
                builder.setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    barcodeView.resume();
                }).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}