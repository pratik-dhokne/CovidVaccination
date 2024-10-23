package com.pratik.covidvaccination;

public class ReadWriteUserDetails {
    private String uid;
    private String fullname;
    private String dob;
    private String gender;
    private String contact;

    public String aadhaarNumber;

    // Default constructor required for calls to DataSnapshot.getValue(ReadWriteUserDetails.class)
    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String uid, String fullname, String dob, String gender, String contact, String aadhaarNumber) {
        this.uid = uid;
        this.fullname = fullname;
        this.dob = dob;
        this.gender = gender;
        this.contact = contact;
        this.aadhaarNumber = aadhaarNumber;
    }

    // Getters
    public String getUid() {
        return uid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }
}
