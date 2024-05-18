package com.example.music_app_artist.utils;

public class Const {
    private static String ACCESS_TOKEN = "";

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }
}
