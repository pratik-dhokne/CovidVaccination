package com.pratik.covidvaccination.admin;

public class AdminVacStatus {

    private String status;


    public AdminVacStatus() {
        this.status = "Not Vaccinated";
    }


    public AdminVacStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
