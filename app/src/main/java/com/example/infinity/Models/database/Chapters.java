package com.example.infinity.Models.database;

public class Chapters {
    private String chapter_id ;
    private String institution_id ;
    private String course_id ;
    private String chapter_title ;
    private String downloaded_media ;

    public Chapters(String chapter_id, String institution_id, String course_id, String chapter_title, String downloaded_media) {
        this.chapter_id = chapter_id;
        this.institution_id = institution_id;
        this.course_id = course_id;
        this.chapter_title = chapter_title;
        this.downloaded_media = downloaded_media;
    }

    public Chapters() {

    }



    public String getInstitution_id() {
        return institution_id;
    }

    public void setInstitution_id(String institution_id) {
        this.institution_id = institution_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getDownloaded_media() {
        return downloaded_media;
    }

    public void setDownloaded_media(String downloaded_media) {
        this.downloaded_media = downloaded_media;
    }

    @Override
    public String toString() {
        return "Chapters{" +
                "chapter_id='" + chapter_id + '\'' +
                ", institution_id='" + institution_id + '\'' +
                ", course_id='" + course_id + '\'' +
                ", chapter_title='" + chapter_title + '\'' +
                ", downloaded_media='" + downloaded_media + '\'' +
                '}';
    }
}
