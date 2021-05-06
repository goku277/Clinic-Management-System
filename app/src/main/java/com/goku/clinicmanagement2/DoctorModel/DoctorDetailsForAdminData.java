package com.goku.clinicmanagement2.DoctorModel;

public class DoctorDetailsForAdminData {
    private String name, fee, imageUrl, mobile, schedule;

    public DoctorDetailsForAdminData(String name, String fee, String imageUrl, String mobile, String schedule) {
        this.name = name;
        this.fee = fee;
        this.imageUrl = imageUrl;
        this.mobile = mobile;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
