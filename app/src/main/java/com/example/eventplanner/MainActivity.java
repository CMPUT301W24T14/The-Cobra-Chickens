package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private ScanFragment scanFragment;
    private NotificationsFragment notificationsFragment;
    private ProfileFragment profileFragment;
    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the navigation bar
        bottomNavigationBar = findViewById(R.id.bottom_nav_bar);

        // begin on the home screen
        selectFragment(new HomeFragmentUpdated());

        // set listener for the bottom navigation bar for each menu item/button
        bottomNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    selectFragment(new HomeFragmentUpdated());
                } else if (itemId == R.id.scan) {
                    selectFragment(new ScanFragment());
                } else if (itemId == R.id.notifications) {
                    selectFragment(new NotificationsFragment());
                } else if (itemId == R.id.profile) {
                    selectFragment(new ProfileFragment());
                }

                return true;
            }
        });
    }

    /**
     * When a navigation bar item is clicked, display the appropriate fragment.
     * @param fragment the fragment to be displayed
     */
    private void selectFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }
}
