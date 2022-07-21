package com.example.infinity.Models.database;

public class UserPrivateData {
    private String full_name ;
    private String email ;
    private String date_of_birth ;
    private String country ;
    private String gender ;
    private String location ;


    public UserPrivateData(String full_name, String email, String date_of_birth, String country, String gender, String location) {
        this.full_name = full_name;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.country = country;
        this.gender = gender;
        this.location = location;
    }

    public UserPrivateData() {

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserPrivateData{" +
                "full_name='" + full_name + '\'' +
                ", email='" + email + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
