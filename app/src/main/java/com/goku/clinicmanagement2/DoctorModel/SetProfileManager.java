package com.goku.clinicmanagement2.DoctorModel;

public class SetProfileManager {
    private String Fees, Specialization, Mobile, Name, Schedule, ImageUrl;

    public SetProfileManager() {
    }

    public SetProfileManager(String fees, String specialization, String mobile, String name, String schedule, String imageUrl) {
        Fees = fees;
        Specialization = specialization;
        Mobile = mobile;
        Name = name;
        Schedule = schedule;
        ImageUrl = imageUrl;
    }

    public String getFees() {
        return Fees;
    }

    public void setFees(String fees) {
        Fees = fees;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
