package com.pratik.covidvaccination;

public class sample {

    private String id;
    private String name;
    private String address;
    private String mobile;
    private String taluka;
    private int totalVaccines;
    private int pinCode;
    private String vaccineName;

    public sample() {
        // Default constructor required for calls to DataSnapshot.getValue(Hospital.class)
    }

    public sample(String id, String name, String address, String mobile, String taluka, int totalVaccines, int pinCode,String vaccineName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.taluka = taluka;
        this.totalVaccines = totalVaccines;
        this.pinCode=pinCode;
        this.vaccineName=vaccineName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getTaluka() {
        return taluka;
    }

    public int getTotalVaccines() {
        return totalVaccines;
    }

    public String getVaccineName(){
        return vaccineName;
    }

    public int getPinCode(){
        return pinCode;
    }

    public void setVaccineName() {
        this.vaccineName = vaccineName;
    }




}
