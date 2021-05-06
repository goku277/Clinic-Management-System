package com.goku.clinicmanagement2.DoctorModel;

public class BookingMember {
    private String Doctor_Details, Patient_Details;

    public BookingMember(String Doctor_Details, String Patient_Details) {
        this.Doctor_Details = Doctor_Details;
        this.Patient_Details = Patient_Details;
    }

    public BookingMember() {
    }

    public String getDoctor_Details() {
        return Doctor_Details;
    }

    public void setDoctor_Details(String doctor_Details) {
        Doctor_Details = doctor_Details;
    }

    public String getPatient_Details() {
        return Patient_Details;
    }

    public void setPatient_Details(String patient_Details) {
        Patient_Details = patient_Details;
    }
}