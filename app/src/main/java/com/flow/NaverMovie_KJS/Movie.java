package com.flow.NaverMovie_KJS;

public class Movie {
    String title;
    String link;
    String image;
    String subtitle;
    String pubDate;
    String director;
    String actor;
    float userRating;

    public Movie(String title, String link, String image, String pubDate, String director, String actor, String userRating){
        this.title = title;
        this.link = link;
        this.image = image;
        this.director = director;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.userRating = Float.parseFloat(userRating);
    }
}
