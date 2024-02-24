package com.example.eventplanner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * Represents the fragment that scans and generates(?) event QR codes.
 */
//Followed this tutorial to figure out QRCode Scanning: https://youtu.be/jtT60yFPelI?si=1gDHYYOa1lBAuNSh
public class ScanFragment extends Fragment {

    Button scan_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the layout for the scan fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        scan_btn = view.findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(v -> {
            scanCode();
        });
        return view;

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to turn on flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            Log.d("Scan Fragment", result.getContents());
        }
    });
}
