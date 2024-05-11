package com.example.music_app_artist.models;

import com.google.gson.annotations.SerializedName;

public class Album {

    @SerializedName("idAlbum")
    private Long idAlbum;
    @SerializedName("image")
    private String image;

    @SerializedName("name")
    private String name;

    @SerializedName("cntSong")
    private int cntSong;

    public Long getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Long idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCntSong() {
        return cntSong;
    }

    public void setCntSong(int cntSong) {
        this.cntSong = cntSong;
    }
}
