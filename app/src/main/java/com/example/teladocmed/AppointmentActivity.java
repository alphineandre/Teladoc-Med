package com.example.teladocmed;

import static com.example.teladocmed.MainActivity.openDrawer;
import static com.example.teladocmed.MainActivity.redirectActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AppointmentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout home, profile, about, logout;

    private Button newAppoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        menu = findViewById(R.id.menu);
        drawerLayout = findViewById(R.id.drawerLayout);
        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);


        newAppoint = findViewById(R.id.newAppoint);

        newAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentActivity.this, CreateAppointment.class);
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
                redirectActivity(AppointmentActivity.this, MainActivity.class);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AppointmentActivity.this, ProfileActivity.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AppointmentActivity.this, LoginActivity.class);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Assuming 'user.getUid()' contains the unique identifier for the patient
            String patientId = user.getUid();

            // Reference to the 'patients' collection
            db.collection("appointments")
                    .document(patientId) // Assuming 'patientId' is the document ID for the logged-in patient
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                // Data found, update TextViews with patient's information
                                String name = document.getString("name");
                                String cell = document.getString("cell");
                                String date = document.getString("date");
                                String symptoms = document.getString("symptoms");
                                String appointmentType = document.getString("appointmentType");

                                // Update TextViews
                                TextView appointFullname = findViewById(R.id.aName);
                                TextView appointCell = findViewById(R.id.aCell);
                                TextView appointDate = findViewById(R.id.aDate);
                                TextView appointSymptoms = findViewById(R.id.aSymptoms);
                                TextView appointType = findViewById(R.id.aType);

                                appointFullname.setText(name);
                                appointCell.setText(cell);
                                appointDate.setText(date);
                                appointSymptoms.setText(symptoms);
                                appointType.setText(appointmentType);
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

