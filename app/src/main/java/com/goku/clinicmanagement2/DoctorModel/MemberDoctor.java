package com.goku.clinicmanagement2.DoctorModel;

public class MemberDoctor {
    private String accurateDetails;
    private String UrlImage;
    private String referenceId;

    public MemberDoctor(String accurateDetails, String UrlImage, String referenceId) {
        this.accurateDetails = accurateDetails;
        this.UrlImage= UrlImage;
        this.referenceId= referenceId;
    }

    public MemberDoctor() {
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getAccurateDetails() {
        return accurateDetails;
    }

    public void setAccurateDetails(String accurateDetails) {
        this.accurateDetails = accurateDetails;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        this.UrlImage = urlImage;
    }
}