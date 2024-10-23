package com.pratik.covidvaccination.admin;

public class AdminAddSlotData {

    private String id;
    private String name;
    private String address;
    private String district;
    private String taluka;
    private int totalVaccines;
    private int pinCode;
    private String vaccineName;
    private String state;

    public AdminAddSlotData() {
        // Default constructor required for calls to DataSnapshot.getValue(Hospital.class)
    }

    public AdminAddSlotData(String id, String name, String address, String district, String taluka, int totalVaccines, int pinCode,String vaccineName,String state) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.district = district;
        this.taluka = taluka;
        this.totalVaccines = totalVaccines;
        this.pinCode=pinCode;
        this.vaccineName=vaccineName;
        this.state=state;
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

    public String getDistrict() {
        return district;
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

    public String getState(){
        return state;
    }

}
