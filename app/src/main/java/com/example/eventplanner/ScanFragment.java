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
import com.journeyapps.barcodescanner.CompoundBarcodeView;

/**
 * This is the ScanFragment class which is responsible for scanning QR codes.
 * It extends the Fragment class and uses the CompoundBarcodeView for scanning.
 */
public class ScanFragment extends Fragment {
    CompoundBarcodeView barcodeView;

    /**
     * This method is called to do initial creation of the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);

        //Makes sure app can access users camera
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //Creates dialog box to ask to user to give camera access
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        //Manages result of QRCode, shows Success if valid QRCode scan and False otherwise
        barcodeView.decodeContinuous(result -> {
            barcodeView.pause();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            if (result.getResult() != null) {
                builder.setTitle("Success!");
                builder.setMessage("Event: " + result.getResult());
            } else {
                builder.setTitle("Failure!");
                builder.setMessage("You have scanned an invalid QRCode");
            }
            builder.setPositiveButton("OK", (dialog, which) -> {
                dialog.dismiss();
                barcodeView.resume();
            }).show();
        });

        return view;
    }

    /**
     * Called when the Fragment is visible to the user.
     * Makes sure the barcodeView is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    /**
     * Called when the Fragment is no longer resumed.
     * Makes sure barcodeView is paused.
     */
    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}