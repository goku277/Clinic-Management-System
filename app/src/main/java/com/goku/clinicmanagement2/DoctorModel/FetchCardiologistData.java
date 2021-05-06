package com.goku.clinicmanagement2.DoctorModel;

public class FetchCardiologistData {
    private String imageUrl;
    private String name;
    private String mobile;
    private String fees;
    private String schedule;
    String specialization;

    public FetchCardiologistData(String imageUrl, String name, String mobile, String fees, String schedule, String specialization) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.mobile= mobile;
        this.fees = fees;
        this.schedule = schedule;
        this.specialization= specialization;
    }



    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public FetchCardiologistData() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}