package com.example.music_app_artist.services;

import com.example.music_app_artist.models.AlbumsResponse;
import com.example.music_app_artist.models.ForgotPassword;
import com.example.music_app_artist.models.LoginRequest;
import com.example.music_app_artist.models.LoginResponse;
import com.example.music_app_artist.models.OtpResponse;
import com.example.music_app_artist.models.RegisterRequest;
import com.example.music_app_artist.models.RegisterResponse;
import com.example.music_app_artist.models.ResetPasswordRequest;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.SongResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @Multipart
    @POST("song/upload")
    Call<ResponseMessage> uploadSong(@Part MultipartBody.Part imageFile,
                                     @Part("name") String name,
                                     @Part MultipartBody.Part resourceFile);

    @GET("songs")
    Call<SongResponse> getAllSongs();

    @GET("artist/{idArtist}/songs/desc")
    Call<SongResponse> getAllSongsOfArtistDesc(@Path("idArtist") Long idArtist);

    @GET("artist/{artistId}/songs")
    Call<SongResponse> getAllSongsOfArtist(@Path("artistId") Long idArtist);

    @GET("artist/{id}/albums")
    Call<AlbumsResponse> getAlbumsByIdArtist(@Path("id") Long id);
}
