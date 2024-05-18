package com.example.music_app_artist.models;

public class AlbumResponse {
    private String message;
    private boolean success;
    private boolean error;
    private Album data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Album getData() {
        return data;
    }

    public void setData(Album album) {
        this.data = album;
    }
}
