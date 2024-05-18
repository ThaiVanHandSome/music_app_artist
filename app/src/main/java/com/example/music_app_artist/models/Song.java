package com.example.music_app_artist.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class Song {
    @SerializedName("idSong")
    private Long idSong;
    @SerializedName("name")
    private String name;
    @SerializedName("views")
    private int views;
    @SerializedName("dayCreated")
    private List<Integer> dayCreated;
    @SerializedName("resource")
    private String resource;
    @SerializedName("image")
    private String image;
    @SerializedName("artistId")
    private Long artistId;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("cntComments")
    private int cntComments;
    @SerializedName("cntLikes")
    private int cntLikes;

    public Song() {
    }

    public Song(Long idSong, String name, int views, List<Integer> dayCreated, String resource, String image, Long artistId, String artistName, int cntComments, int cntLikes) {
        this.idSong = idSong;
        this.name = name;
        this.views = views;
        this.dayCreated = dayCreated;
        this.resource = resource;
        this.image = image;
        this.artistId = artistId;
        this.artistName = artistName;
        this.cntComments = cntComments;
        this.cntLikes = cntLikes;
    }

    public Long getIdSong() {
        return idSong;
    }

    public void setIdSong(Long idSong) {
        this.idSong = idSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDateTime getDayCreated() {
        LocalDateTime dateTime = LocalDateTime.of(
                dayCreated.get(0),
                dayCreated.get(1),
                dayCreated.get(2),
                dayCreated.get(3),
                dayCreated.get(4),
                dayCreated.get(5));
        return dateTime;
    }

    public void setDayCreated(List<Integer> dayCreated) {
        this.dayCreated = dayCreated;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "Song{" +
                "idSong=" + idSong +
                ", name='" + name + '\'' +
                ", views=" + views +
                ", dayCreated=" + dayCreated +
                ", resource='" + resource + '\'' +
                ", image='" + image + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                '}';
    }

    public int getCntComments() {
        return cntComments;
    }

    public void setCntComments(int cntComments) {
        this.cntComments = cntComments;
    }

    public int getCntLikes() {
        return cntLikes;
    }

    public void setCntLikes(int cntLikes) {
        this.cntLikes = cntLikes;
    }
}
