package com.example.mymovielist;

import java.io.Serializable;

public class List implements Serializable {
    String key;
    String title, platform, date, rating, content, image;

    // 생성자 1
    public List(String title, String platform, String date, String rating, String content, String image){
        this.key = "";
        this.title = title;
        this.platform = platform;
        this.date = date;
        this.rating = rating;
        this.content = content;
        this.image = image;
    }
    // 생성자 2
    public List(String key, String title, String platform, String date, String rating, String content, String image) {
        this.key = key;
        this.title = title;
        this.platform = platform;
        this.date = date;
        this.rating = rating;
        this.content = content;
        this.image = image;
    }
    public String getKey() { return key; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    public String getDate() { return date; }
    public String getRating() { return rating; }
    public String getContent() { return content; }
    public String getImage() { return image; }

    public void setKey(String key) { this.key = key; }
    public void setTitle(String title) { this.title = title; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setDate(String date) { this.date = date; }
    public void setRating(String rating) { this.rating = rating; }
    public void setContent(String content) { this.content = content; }
    public void setImage(String image) { this.image = image; }
}
