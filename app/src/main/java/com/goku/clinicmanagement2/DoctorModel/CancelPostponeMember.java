package com.goku.clinicmanagement2.DoctorModel;

public class CancelPostponeMember {
    private String doctorname, doctormobile, patientname, patientfees, accuratedate, previousschedule;

    public CancelPostponeMember(String doctorname, String doctormobile, String patientname, String patientfees, String accuratedate, String previousschedule) {
        this.doctorname = doctorname;
        this.doctormobile = doctormobile;
        this.patientname = patientname;
        this.patientfees = patientfees;
        this.accuratedate = accuratedate;
        this.previousschedule = previousschedule;
    }

    public CancelPostponeMember() {
    }

    public String getDoctrname() {
        return doctorname;
    }

    public void setDoctrname(String doctrname) {
        this.doctorname = doctrname;
    }

    public String getDoctormobile() {
        return doctormobile;
    }

    public void setDoctormobile(String doctormobile) {
        this.doctormobile = doctormobile;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPatientfees() {
        return patientfees;
    }

    public void setPatientfees(String patientfees) {
        this.patientfees = patientfees;
    }

    public String getAccuratedate() {
        return accuratedate;
    }

    public void setAccuratedate(String accuratedate) {
        this.accuratedate = accuratedate;
    }

    public String getPreviousschedule() {
        return previousschedule;
    }

    public void setPreviousschedule(String previousschedule) {
        this.previousschedule = previousschedule;
    }
}