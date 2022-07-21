package com.example.infinity.Models.database;

public class Reviews {

    private String username;
    private String profile_pic ;
    private String review;
    private String rating ;
    private String date_created ;

    public Reviews(String username, String profile_pic, String review, String rating, String date_created) {
        this.username = username;
        this.profile_pic = profile_pic;
        this.review = review;
        this.rating = rating;
        this.date_created = date_created;
    }

    public Reviews() {

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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "username='" + username + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", review='" + review + '\'' +
                ", rating='" + rating + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
