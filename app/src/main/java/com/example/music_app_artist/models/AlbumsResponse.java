package com.example.music_app_artist.models;

import java.util.List;

public class AlbumsResponse {
    private boolean success;
    private boolean error;
    private String message;
    private List<Album> data;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Album> getData() {
        return data;
    }

    public void setData(List<Album> albums) {
        this.data = albums;
    }
}
