package com.example.teladocmed;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Appointment extends RecyclerView.Adapter {
    private String name;
    private String cell;
    private String date;
    private String symptoms;
    private String appointmentType;

    public Appointment(List<Appointment> appointmentList) {
        // Empty constructor for Firestore
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Appointment(String name, String cell, String date, String symptoms, String appointmentType) {
        this.name = name;
        this.cell = cell;
        this.date = date;
        this.symptoms = symptoms;
        this.appointmentType = appointmentType;
    }

    public String getName() {
        return name;
    }

    public String getCell() {
        return cell;
    }

    public String getDate() {
        return date;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getAppointmentType() {
        return appointmentType;
    }
}
