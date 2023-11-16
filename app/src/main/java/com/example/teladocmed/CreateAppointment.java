package com.example.teladocmed;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateAppointment extends AppCompatActivity {

    private EditText fullnameEditText, cellEditText, sysmptomsEditText, typeEditText;
    private Button addButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DatePicker datePicker;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fullnameEditText = findViewById(R.id.appointName);
        cellEditText = findViewById(R.id.appointCell);
        datePicker = findViewById(R.id.appointDate);
        sysmptomsEditText = findViewById(R.id.appointSymptoms);
        typeEditText = findViewById(R.id.appointType);
        addButton = findViewById(R.id.appointButton);

        // Initialize the date picker
        selectedDate = Calendar.getInstance();


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirestore();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        // Set the minimum date to the current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }


    private void uploadDataToFirestore() {
        String userId = auth.getCurrentUser().getUid();
        String name = fullnameEditText.getText().toString();
        String cell = cellEditText.getText().toString();
        String symptoms = sysmptomsEditText.getText().toString();
        String appointmentType = typeEditText.getText().toString();

        // Format the selected date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String formattedDate = dateFormat.format(selectedDate.getTime());

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("cell", cell);
        data.put("symptoms", symptoms);
        data.put("appointmentType", appointmentType);
        data.put("date", formattedDate); // Save the formatted date string

        db.collection("appointments").document(userId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Appointment Created", Toast.LENGTH_SHORT).show();

                        // Redirect to MedicalHistoryActivity
                        Intent intent = new Intent(CreateAppointment.this, AppointmentActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Close the current activity if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure if needed
                        Toast.makeText(getApplicationContext(), "Appointment Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
