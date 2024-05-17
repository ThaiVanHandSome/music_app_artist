package com.example.music_app_artist.models;

public class Category {
    private Long idSongCategory;
    private String name;

    public Long getIdSongCategory() {
        return idSongCategory;
    }

    public void setIdSongCategory(Long idSongCategory) {
        this.idSongCategory = idSongCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
