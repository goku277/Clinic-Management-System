package com.goku.clinicmanagement2.DoctorModel;

public class PatientDetailsForAdminData {
    private String name, imageurl, gender, age, moile;

    public PatientDetailsForAdminData(String name, String imageurl, String gender, String age, String moile) {
        this.name = name;
        this.imageurl = imageurl;
        this.gender = gender;
        this.age = age;
        this.moile = moile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMoile() {
        return moile;
    }

    public void setMoile(String moile) {
        this.moile = moile;
    }
}