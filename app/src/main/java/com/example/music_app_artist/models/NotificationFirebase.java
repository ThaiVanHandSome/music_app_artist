package com.example.music_app_artist.models;

public class NotificationFirebase {
    private int songId;
    private String songName;
    private String userName;

    private String cover;

    public NotificationFirebase(int songId, String songName, String userName, String cover) {
        this.songId = songId;
        this.songName = songName;
        this.userName = userName;
        this.cover = cover;
    }

    public NotificationFirebase() {
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
