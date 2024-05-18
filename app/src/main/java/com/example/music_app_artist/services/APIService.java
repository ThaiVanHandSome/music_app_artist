package com.example.music_app_artist.services;

import com.example.music_app_artist.models.AlbumsResponse;
import com.example.music_app_artist.models.FollowerResponse;
import com.example.music_app_artist.models.CategoriesResponse;
import com.example.music_app_artist.models.ForgotPassword;
import com.example.music_app_artist.models.LoginRequest;
import com.example.music_app_artist.models.LoginResponse;
import com.example.music_app_artist.models.OtpResponse;
import com.example.music_app_artist.models.RegisterRequest;
import com.example.music_app_artist.models.RegisterResponse;
import com.example.music_app_artist.models.ResetPasswordRequest;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.SongCommentResponse;
import com.example.music_app_artist.models.SongResponse;
import com.example.music_app_artist.models.SongsResponse;
import com.example.music_app_artist.models.UpdateUserResponse;
import com.example.music_app_artist.models.UploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<UploadResponse> uploadSong(@Part MultipartBody.Part imageFile,
                                     @Part("idArtist") Long idArtist,
                                     @Part("name") String name,
                                     @Part("idSongCategory") Long idSongCategory,
                                     @Part("idAlbum") Long idAlbum,
                                     @Part MultipartBody.Part resourceFile);

    @GET("songs")
    Call<SongsResponse> getAllSongs();

    @GET("artist/{idArtist}/songs/desc")
    Call<SongsResponse> getAllSongsOfArtistDesc(@Path("idArtist") Long idArtist);


    @GET("artist/{id}/albums")
    Call<AlbumsResponse> getAlbumsByIdArtist(@Path("id") Long id);

    @Multipart
    @PATCH("artist/update")
    Call<UpdateUserResponse> updateArtist(@Part("idArtist") Long idArtist, @Part MultipartBody.Part imageFile, @Part("nickname") String nickname, @Part("gender") int gender);

    @GET("categories")
    Call<CategoriesResponse> getAllCategories();

    @Multipart
    @POST("album/upload")
    Call<ResponseMessage> uploadAlbum(@Part MultipartBody.Part image, @Part("idArtist") Long idArtist, @Part("albumName") String albumName, @Part("listSong") String listSong);

    @GET("artist/{idArtist}/songs/count")
    Call<DefaultResponse> getCountSongsOfArtist(@Path("idArtist") Long idArtist);

    @GET("albums/artist/{idArtist}/count")
    Call<DefaultResponse> getCountAlbumsOfArtist(@Path("idArtist") Long idArtist);

    @GET("artist/{idArtist}/followers")
    Call<FollowerResponse> getAllFollowers(@Path("idArtist") Long idArtist);

    @GET("artist/{idArtist}/views/count")
    Call<DefaultResponse> getCountOfViews(@Path("idArtist") Long idArtist);

    @GET("artist/{idArtist}/likes/count")
    Call<DefaultResponse> getCountOfLikes(@Path("idArtist") Long idArtist);

    @GET("artist/{idArtist}/comments/count")
    Call<DefaultResponse> getCountOfComments(@Path("idArtist") Long idArtist);

    @GET("song/{idSong}")
    Call<SongResponse> getSongById(@Path("idSong") Long idSong);

    @GET("song/{id_song}/comments")
    Call<SongCommentResponse> getAllCommentsOfSong(@Path("id_song") Long idSong);

    @DELETE("song/{idSong}")
    Call<ResponseMessage> deleteSong(@Path("idSong") Long idSong);

    @GET("album/{id}/songs")
    Call<SongsResponse> getAllSongsByAlbumId(@Path("id") Long idAlbum);

    @DELETE("album/{id}")
    Call<ResponseMessage> deleteAlbum(@Path("id") Long idAlbum);
}
