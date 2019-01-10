package com.moviestracker;

public class Movie {
    String title;
    String seen_date;
    String imgURL;

    public Movie(String title, String date, String imgURL) {
        this.title = title;
        this.seen_date = date;
        this.imgURL = imgURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeen_date() {
        return seen_date;
    }

    public void setSeen_date(String name) {
        this.seen_date = name;
    }
}
