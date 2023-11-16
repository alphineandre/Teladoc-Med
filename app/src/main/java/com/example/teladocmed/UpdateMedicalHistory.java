package com.example.teladocmed;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UpdateMedicalHistory extends AppCompatActivity {

    private EditText nameEditText, allergiesEditText, ageEditText, lastVisitEditText, familyDisordersEditText;
    private Button saveButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medical_history);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.uploadName);
        allergiesEditText = findViewById(R.id.uploadAllegies);
        ageEditText = findViewById(R.id.uploadAge);
        lastVisitEditText = findViewById(R.id.uploadLastVisit);
        familyDisordersEditText = findViewById(R.id.uploadFRD);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });
    }

    private void uploadDataToFirestore() {
        String userId = auth.getCurrentUser().getUid();
        String name = nameEditText.getText().toString();
        String allergies = allergiesEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String lastVisit = lastVisitEditText.getText().toString();
        String familyDisorders = familyDisordersEditText.getText().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("allergies", allergies);
        data.put("age", age);
        data.put("lastVisit", lastVisit);
        data.put("familyDisorders", familyDisorders);

        db.collection("medical_history").document(userId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Medical History Added", Toast.LENGTH_SHORT).show();

                        // Redirect to MedicalHistoryActivity
                        Intent intent = new Intent(UpdateMedicalHistory.this, MedicalHistoryActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Close the current activity if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure if needed
                        Toast.makeText(getApplicationContext(), "Medical History not saved", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}