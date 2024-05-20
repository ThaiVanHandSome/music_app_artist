package com.example.music_app_artist.utils;

public class Const {
    private static String ACCESS_TOKEN = "";

    private static String TOKEN="";

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static void setTOKEN(String TOKEN) {
        Const.TOKEN = TOKEN;
    }
}
