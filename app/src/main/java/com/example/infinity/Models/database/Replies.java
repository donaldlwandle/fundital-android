package com.example.infinity.Models.database;

public class Replies {

    private String username;
    private String profile_pic ;
    private String reply;
    private String date_created ;
    private String instructor;

    public Replies(String username, String profile_pic, String reply, String date_created, String instructor) {
        this.username = username;
        this.profile_pic = profile_pic;
        this.reply = reply;
        this.date_created = date_created;
        this.instructor = instructor;
    }

    public Replies() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Replies{" +
                "username='" + username + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", reply='" + reply + '\'' +
                ", date_created='" + date_created + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }
}
