package com.example.infinity.Models.database;

public class Courses {

    private String institution ;
    private String institution_id ;
    private String rating ;
    private String seen ;
    private String title ;
    private String wallpaper ;
    private String field ;
    private String logo ;
    private String intro_vid ;
    private String about;
    private String prerequisites;
    private String language;
    private String course_id ;

    public Courses(String institution, String institution_id, String rating, String seen,
                   String title, String wallpaper, String field, String logo, String intro_vid,
                   String about, String prerequisites, String language, String course_id) {
        this.institution = institution;
        this.institution_id = institution_id;
        this.rating = rating;
        this.seen = seen;
        this.title = title;
        this.wallpaper = wallpaper;
        this.field = field;
        this.logo = logo;
        this.intro_vid = intro_vid;
        this.about = about;
        this.prerequisites = prerequisites;
        this.language = language;
        this.course_id = course_id;
    }

    public Courses() {

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIntro_vid() {
        return intro_vid;
    }

    public void setIntro_vid(String intro_vid) {
        this.intro_vid = intro_vid;
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

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "institution='" + institution + '\'' +
                ", institution_id='" + institution_id + '\'' +
                ", rating='" + rating + '\'' +
                ", seen='" + seen + '\'' +
                ", title='" + title + '\'' +
                ", wallpaper='" + wallpaper + '\'' +
                ", field='" + field + '\'' +
                ", logo='" + logo + '\'' +
                ", intro_vid='" + intro_vid + '\'' +
                ", about='" + about + '\'' +
                ", prerequisites='" + prerequisites + '\'' +
                ", language='" + language + '\'' +
                ", course_id='" + course_id + '\'' +
                '}';
    }
}
