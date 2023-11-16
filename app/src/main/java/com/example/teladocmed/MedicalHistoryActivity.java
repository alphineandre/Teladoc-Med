package com.example.teladocmed;

import static com.example.teladocmed.MainActivity.openDrawer;
import static com.example.teladocmed.MainActivity.redirectActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MedicalHistoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, profile, about, logout;
    private Button updateMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        logout = findViewById(R.id.logout);
        profile = findViewById(R.id.profile);

        // Initialize Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the "Edit Profile" button
        updateMed = findViewById(R.id.updateMed);

        updateMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the "Edit Profile" button is clicked, open the EditProfile activity
                Intent intent = new Intent(MedicalHistoryActivity.this, UpdateMedicalHistory.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MedicalHistoryActivity.this, MainActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MedicalHistoryActivity.this, ProfileActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(MedicalHistoryActivity.this, LoginActivity.class);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Assuming 'user.getUid()' contains the unique identifier for the patient
            String patientId = user.getUid();

            // Reference to the 'patients' collection
            db.collection("medical_history")
                    .document(patientId) // Assuming 'patientId' is the document ID for the logged-in patient
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                // Data found, update TextViews with patient's information
                                String name = document.getString("name");
                                String age = document.getString("age");
                                String allergies = document.getString("allergies");
                                String familyDisorders = document.getString("familyDisorders");
                                String lastVisit = document.getString("lastVisit");

                                // Update TextViews
                                TextView medFullname = findViewById(R.id.medFullname);
                                TextView medAge = findViewById(R.id.medAge);
                                TextView medAllergies = findViewById(R.id.medAllergies);
                                TextView medFamilyHistory = findViewById(R.id.medFamilyHistory);
                                TextView medLastVisit = findViewById(R.id.medLastVisit);

                                medFullname.setText(name);
                                medAge.setText(age);
                                medAllergies.setText(allergies);
                                medFamilyHistory.setText(familyDisorders);
                                medLastVisit.setText(lastVisit);
                            } else {
                                // Document doesn't exist
                            }
                        } else {
                            // Error occurred while fetching data
                        }
                    });
        }
    }
}