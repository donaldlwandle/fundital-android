package com.example.infinity.Models.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Discussions  {

    private String discussion;
    private String user_id ;
    private List<Votes> votes;
    private String username ;
    private String profile_pic ;
    private String date_created ;
    private String instructor ;
    private String discussion_id ;


    public Discussions(String discussion, String user_id, List<Votes> votes, String username,
                       String profile_pic, String date_created, String instructor, String discussion_id) {
        this.discussion = discussion;
        this.user_id = user_id;
        this.votes = votes;
        this.username = username;
        this.profile_pic = profile_pic;
        this.date_created = date_created;
        this.instructor = instructor;
        this.discussion_id = discussion_id;
    }


    public Discussions() {

    }



    public String getDiscussion() {
        return discussion;
    }

    public void setDiscussion(String discussion) {
        this.discussion = discussion;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Votes> getVotes() {
        return votes;
    }

    public void setVotes(List<Votes> votes) {
        this.votes = votes;
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

    public String getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(String discussion_id) {
        this.discussion_id = discussion_id;
    }

    @Override
    public String toString() {
        return "Discussions{" +
                "discussion='" + discussion + '\'' +
                ", user_id='" + user_id + '\'' +
                ", votes=" + votes +
                ", username='" + username + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", date_created='" + date_created + '\'' +
                ", instructor='" + instructor + '\'' +
                ", discussion_id='" + discussion_id + '\'' +
                '}';
    }


}

