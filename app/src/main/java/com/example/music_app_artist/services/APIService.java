package com.example.music_app_artist.services;

import com.example.music_app_artist.models.ForgotPassword;
import com.example.music_app_artist.models.LoginRequest;
import com.example.music_app_artist.models.LoginResponse;
import com.example.music_app_artist.models.OtpResponse;
import com.example.music_app_artist.models.RegisterRequest;
import com.example.music_app_artist.models.RegisterResponse;
import com.example.music_app_artist.models.ResetPasswordRequest;
import com.example.music_app_artist.models.ResponseMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("auth/authenticate")
    Call<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @GET("auth/register/confirm")
    Call<OtpResponse> verifyOtp(@Query("token") String token, @Query("type") String type);

    @POST("auth/send-email")
    Call<ResponseMessage> sendOtp(@Body ForgotPassword forgotPassword);

    @PATCH("user/forgot-password")
    Call<ResponseMessage> changePassword(@Body ResetPasswordRequest resetPasswordRequest);
}
