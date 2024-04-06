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

import java.util.ArrayList;
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
                final String[] eventName = new String[1];
                final String[] eventDescription = new String[1];
                final String[] eventMaxAttendees = new String[1];
                final String[] eventDate = new String[1];
                final String[] eventTime = new String[1];
                final String[] eventLocation = new String[1];
                final String[] eventPoster = new String[1];
                final String[] checkInCode = new String[1];
                final String[] promoCode = new String[1];
                final ArrayList<String>[] eventAnnouncements = new ArrayList[]{new ArrayList<>()};
                final ArrayList<String>[] checkedInUsers = new ArrayList[]{new ArrayList<>()};
                final ArrayList<String>[] signedUpUsers = new ArrayList[]{new ArrayList<>()};

                if (Objects.equals(parts[1], "check")) {
                    db.collection("events").whereEqualTo("checkInCode", checkInCodeFromQR)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        eventId[0] = document.getId();
                                    }
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
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        eventId[0] = doc.getId();
                                        eventName[0] = doc.getString("eventName");
                                        eventDescription[0] = doc.getString("eventDescription");
                                        eventMaxAttendees[0] = doc.getString("eventMaxAttendees");
                                        eventDate[0] = doc.getString("eventDate");
                                        eventTime[0] = doc.getString("eventTime");
                                        eventLocation[0] = doc.getString("eventLocation");
                                        eventPoster[0] = doc.getString("eventPoster");
                                        checkInCode[0] = doc.getString("checkInCode");
                                        promoCode[0] = doc.getString("promoCode");
                                        eventAnnouncements[0] = (ArrayList<String>) doc.get("eventAnnouncements");
                                        checkedInUsers[0] = (ArrayList<String>) doc.get("checkedInUsers");
                                        signedUpUsers[0] = (ArrayList<String>) doc.get("signedUpUsers");
                                    }
                                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                                        Dialog dialog = new Dialog(requireContext());

                                        if (documentSnapshot.exists() && eventId[0] != null) {
                                            //Change activity to my events details
                                            Intent intent = new Intent(requireContext(), EventDetailsActivity.class);

                                            Event newEvent = new Event(eventId[0], eventName[0], eventDescription[0], eventMaxAttendees[0], eventDate[0], eventTime[0], eventLocation[0], eventPoster[0], checkInCode[0], promoCode[0], eventAnnouncements[0], checkedInUsers[0], signedUpUsers[0]);
                                            intent.putExtra("event", newEvent);
                                            startActivity(intent);
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