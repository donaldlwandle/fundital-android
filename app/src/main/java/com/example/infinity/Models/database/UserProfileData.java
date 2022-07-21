package com.example.infinity.Models.database;

public class UserProfileData {
    private UserData userData ;
    private UserPrivateData privateData ;

    public UserProfileData(UserData userData, UserPrivateData privateData) {
        this.userData = userData;
        this.privateData = privateData;
    }

    public UserProfileData() {

    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserPrivateData getPrivateData() {
        return privateData;
    }

    public void setPrivateData(UserPrivateData privateData) {
        this.privateData = privateData;
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "userData=" + userData +
                ", privateData=" + privateData +
                '}';
    }
}
