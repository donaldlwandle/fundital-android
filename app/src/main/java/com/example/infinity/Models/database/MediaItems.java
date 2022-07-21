package com.example.infinity.Models.database;

public class MediaItems {
    private String media_type;
    private String subscription_type ;
    private String media_url ;
    private String media_description ;

    public MediaItems(String media_type, String subscription_type, String media_url, String media_description) {
        this.media_type = media_type;
        this.subscription_type = subscription_type;
        this.media_url = media_url;
        this.media_description = media_description;
    }

    public MediaItems() {

    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_description() {
        return media_description;
    }

    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }

    @Override
    public String toString() {
        return "MediaItems{" +
                "media_type='" + media_type + '\'' +
                ", subscription_type='" + subscription_type + '\'' +
                ", media_url='" + media_url + '\'' +
                ", media_description='" + media_description + '\'' +
                '}';
    }
}
