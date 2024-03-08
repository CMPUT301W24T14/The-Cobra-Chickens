package com.example.eventplanner;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class ScanFragment extends Fragment {
    CompoundBarcodeView barcodeView;
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
                String eventId = parts[0];
                String userId = auth.getCurrentUser().getUid();

                db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("events").document(eventId).update("checkedInUsers", FieldValue.arrayUnion(userId));
                        db.collection("events").document(eventId).update("signedUpUsers", FieldValue.arrayUnion(userId));
                        db.collection("users").document(userId).update("myEvents", FieldValue.arrayUnion(eventId));
                        db.collection("users").document(userId).update("checkedInto", FieldValue.arrayUnion(eventId));

                        sharedViewModel.setEventUpdated(true);

                        builder.setTitle("Success!");
                        builder.setMessage("You have successfully signed up for the event!");
                    } else {
                        builder.setTitle("Failure!");
                        builder.setMessage("User does not exist!");
                    }
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        barcodeView.resume();
                    }).show();
                });
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