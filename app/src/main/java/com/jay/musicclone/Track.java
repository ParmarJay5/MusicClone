package com.jay.musicclone;

public class Track {
    private String title;
    private String artist;
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Track(String title, String artist, String image) {
        this.title = title;
        this.artist = artist;
        this.image = image;


    }
}
