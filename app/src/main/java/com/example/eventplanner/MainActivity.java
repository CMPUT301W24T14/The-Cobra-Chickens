package com.example.eventplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private ScanFragment scanFragment;
    private NotificationsFragment notificationsFragment;
    private ProfileFragment profileFragment;
    private BottomNavigationView bottomNavigationBar;

    /**
     * Sets up the bottom navigation bar and its listeners, then binds the respective fragments
     * to its button icons.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

                // select the fragment based on what was clicked in the bottomNavigationBar
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
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("FCM", "Token: " + token);
                        System.out.println(token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * When a navigation bar item is clicked, display the appropriate fragment.
     * @param fragment the fragment to be displayed
     */
    private void selectFragment(Fragment fragment) {

        // grab the fragment currently associated with MainActivity
        FragmentManager fragmentManager = getSupportFragmentManager();

        // replace the fragment container with the new fragment
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }
}
