package com.pratik.covidvaccination;

public class AppointmentDetails {

    private String name;
    private String date;
    private String vaccineName;

    public AppointmentDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(AppointmentDetails.class)
    }

    public AppointmentDetails(String name, String date, String vaccineName) {
        this.name = name;
        this.date = date;
        this.vaccineName = vaccineName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
}
