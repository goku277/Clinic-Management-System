package com.goku.clinicmanagement2.PatientModel;

public class PatientMember {

    private String name, age, mobile, imageUrl, gender;

    public PatientMember() {
    }

    public PatientMember(String name, String age, String mobile, String imageUrl, String gender) {
        this.name = name;
        this.age = age;
        this.mobile = mobile;
        this.imageUrl= imageUrl;
        this.gender= gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}