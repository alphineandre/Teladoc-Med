package com.example.teladocmed;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditText editName;
    private EditText editUsername;
    private EditText editPhoneNumber;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get references to the EditText fields and the Save button
        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData(db);
            }
        });
    }

    private void updateUserData(final FirebaseFirestore db) {
        // Get the current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String userId = auth.getCurrentUser().getUid();

        // Reference to the user's document in Firestore
        final DocumentReference userRef = db.collection("patients").document(userId);

        // Create a data map with updated user details
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("name", editName.getText().toString());
        updatedData.put("username", editUsername.getText().toString());
        updatedData.put("phoneNumber", editPhoneNumber.getText().toString());

        // Update the user data in Firestore
        userRef.update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User data updated successfully
                        // You can display a success message or navigate to another screen

                        // Create an Intent to go back to the ProfileActivity
                        Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                        startActivity(intent);

                        // Finish the current EditProfile activity to prevent going back to it
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the error (e.g., display an error message)
                    }
                });
    }
}