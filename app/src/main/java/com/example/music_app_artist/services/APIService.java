package com.example.music_app_artist.services;

import com.example.music_app_artist.models.RegisterRequest;
import com.example.music_app_artist.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
