package com.example.infinity.Models.database.courseinfo;

public class CourseInstructors {

    private String name;
    private String profile_pic ;

    public CourseInstructors(String name, String profile_pic) {
        this.name = name;
        this.profile_pic = profile_pic;
    }

    public CourseInstructors() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "CourseInstructors{" +
                "name='" + name + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }
}
