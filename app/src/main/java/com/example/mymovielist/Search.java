package com.example.mymovielist;

import java.io.Serializable;

public class Search implements Serializable {
    String title, link, image, subtitle, pubDate, director, actor, userRating;

    public Search(String title, String link, String image, String subtitle,
                  String pubDate, String director, String actor, String userRating) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.subtitle = subtitle;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = userRating;
    }

    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getImage() { return image; }
    public String getSubtitle() { return subtitle; }
    public String getPubDate() { return pubDate; }
    public String getDirector() { return director; }
    public String getActor() { return actor; }
    public String getUserRating() { return userRating; }
}
