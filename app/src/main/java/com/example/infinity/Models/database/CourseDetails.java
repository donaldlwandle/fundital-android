package com.example.infinity.Models.database;

public class CourseDetails {
    private String institution ;
    private String institution_id ;
    private String rating ;
    private String seen ;
    private String logo ;
    private String title ;
    private String wallpaper ;
    private String field ;
    private String course_id ;
    private String about;
    private String prerequisites;
    private String language;

    public CourseDetails(String institution, String institution_id, String rating, String seen,
                         String logo, String title, String wallpaper, String field, String course_id,
                         String about, String prerequisites, String language) {
        this.institution = institution;
        this.institution_id = institution_id;
        this.rating = rating;
        this.seen = seen;
        this.logo = logo;
        this.title = title;
        this.wallpaper = wallpaper;
        this.field = field;
        this.course_id = course_id;
        this.about = about;
        this.prerequisites = prerequisites;
        this.language = language;
    }

    public CourseDetails() {

    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(String institution_id) {
        this.institution_id = institution_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "CourseDetails{" +
                "institution='" + institution + '\'' +
                ", institution_id='" + institution_id + '\'' +
                ", rating='" + rating + '\'' +
                ", seen='" + seen + '\'' +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", wallpaper='" + wallpaper + '\'' +
                ", field='" + field + '\'' +
                ", course_id='" + course_id + '\'' +
                ", about='" + about + '\'' +
                ", prerequisites='" + prerequisites + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
