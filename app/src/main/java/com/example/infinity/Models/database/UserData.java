package com.example.infinity.Models.database;

public class UserData {

    private String user_id;
    private String username ;
    private String profile_photo ;
    private String profession ;

    public UserData(String user_id, String username, String profile_photo, String profession) {
        this.user_id = user_id;
        this.username = username;
        this.profile_photo = profile_photo;
        this.profession = profession;
    }

    public UserData() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}
