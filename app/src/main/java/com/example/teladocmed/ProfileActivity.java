package com.example.teladocmed;

import static com.example.teladocmed.MainActivity.openDrawer;
import static com.example.teladocmed.MainActivity.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home;
    LinearLayout profile;
    LinearLayout logout;
    private Button editButton; // The "Edit Profile" button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        logout = findViewById(R.id.logout);
        profile = findViewById(R.id.profile);

        // Initialize Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the "Edit Profile" button
        editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the "Edit Profile" button is clicked, open the EditProfile activity
                Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Assuming 'user.getUid()' contains the unique identifier for the patient
            String patientId = user.getUid();

            // Reference to the 'patients' collection
            db.collection("patients")
                    .document(patientId) // Assuming 'patientId' is the document ID for the logged-in patient
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                // Data found, update TextViews with patient's information
                                String name = document.getString("name");
                                String email = document.getString("email");
                                String username = document.getString("username");
                                String phoneNumber = document.getString("phoneNumber");

                                // Update TextViews
                                TextView profileName = findViewById(R.id.profileName);
                                TextView profileEmail = findViewById(R.id.profileEmail);
                                TextView profileUsername = findViewById(R.id.profileUsername);
                                TextView profilePhoneNumber = findViewById(R.id.profilePhoneNumber);

                                profileName.setText(name);
                                profileEmail.setText(email);
                                profileUsername.setText(username);
                                profilePhoneNumber.setText(phoneNumber);
                            } else {
                                // Document doesn't exist
                            }
                        } else {
                            // Error occurred while fetching data
                        }
                    });
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, MainActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(ProfileActivity.this, LoginActivity.class);
            }
        });
    }
}
