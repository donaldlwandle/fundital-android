package com.example.infinity.Models.database;

public class Votes {

    private String user_id ;

    public Votes(String user_id) {
        this.user_id = user_id;
    }

    public Votes() {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Votes{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
