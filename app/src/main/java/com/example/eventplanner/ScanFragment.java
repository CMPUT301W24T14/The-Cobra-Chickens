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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class ScanFragment extends Fragment {
    CompoundBarcodeView barcodeView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);

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
                        db.collection("users").document(userId).update("checkedInto", FieldValue.arrayUnion(eventId));
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

//package com.example.eventplanner;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.journeyapps.barcodescanner.CompoundBarcodeView;
//
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * This is the ScanFragment class which is responsible for scanning QR codes.
// * It extends the Fragment class and uses the CompoundBarcodeView for scanning.
// */
//public class ScanFragment extends Fragment {
//    CompoundBarcodeView barcodeView;
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    /**
//     * This method is called to do initial creation of the fragment.
//     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
//     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
//     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
//     * @return Return the View for the fragment's UI, or null.
//     */
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_scan, container, false);
//        barcodeView = view.findViewById(R.id.barcode_scanner);
//
//        //Makes sure app can access users camera
//        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            //Creates dialog box to ask to user to give camera access
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
//        }
//
//        //Manages result of QRCode, shows Success if valid QRCode scan and False otherwise
//        barcodeView.decodeContinuous(result -> {
//            barcodeView.pause();
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            if (result.getResult() != null) {
//
//                String[] parts = String.valueOf(result.getResult()).split(":");
//                String eventId = parts[0];
//                Task<DocumentSnapshot> event = db.collection("events").document(eventId).get();
//                Task<DocumentSnapshot> users = db.collection("users").document(eventId).get();
//                // I want to get the id of the device I am currently using check that it is in the users id collection and add
//                // that user to id to the signedUpUsers field in event
//
//            } else {
//                builder.setTitle("Failure!");
//                builder.setMessage("result is null!");
//            }
//            builder.setPositiveButton("OK", (dialog, which) -> {
//                dialog.dismiss();
//                barcodeView.resume();
//            }).show();
//        });
//
//        return view;
//    }
//
//    /**
//     * Called when the Fragment is visible to the user.
//     * Makes sure the barcodeView is resumed.
//     */
//    @Override
//    public void onResume() {
//        super.onResume();
//        barcodeView.resume();
//    }
//
//    /**
//     * Called when the Fragment is no longer resumed.
//     * Makes sure barcodeView is paused.
//     */
//    @Override
//    public void onPause() {
//        super.onPause();
//        barcodeView.pause();
//    }
//}