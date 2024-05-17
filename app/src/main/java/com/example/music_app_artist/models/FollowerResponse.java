package com.example.music_app_artist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowerResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("error")
    private Boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Long> data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Boolean getError() {
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

    public List<Long> getData() {
        return data;
    }

    public void setIds(List<Long> data) {
        this.data = data;
    }
}
