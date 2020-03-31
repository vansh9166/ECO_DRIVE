package com.example.ecodrive5;

public class UserDatabase {
    public String fname;
    public String lname;
    public String address;
    public String dob;
    public String mobileno;

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String licenceno;
    public String source;
    public String destination;
    public String vehicle;
    public String fuel;
    public String time;
    public int carbonfootprint;

    public void setCarbonfootprint(int carbonfootprint) {
        this.carbonfootprint = carbonfootprint;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setFname(String fname) { this.fname = fname; }

    public void setLname(String lname) { this.lname = lname; }

    public void setAddress(String address) { this.address = address; }

    public void setDob(String dob) { this.dob = dob; }

    public void setLicenceno(String licenceno) { this.licenceno = licenceno; }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
