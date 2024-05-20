package com.example.music_app_artist.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.music_app_artist.R;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_app_artist.activities.AlbumDetailActivity;
import com.example.music_app_artist.activities.HomeActivity;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.TokenResponse;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.utils.Const;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseNotification extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "MY_CHANNEL";
    private APIService apiService;

    private TokenResponse tokenResponse;

    public void sendNotificationToUser(Song song, String receiverToken, String token) {
        new Thread(() -> {
            try {
                Log.e("sendNotificationToUserToken", token);
                Log.e("sendNotificationToUser", "Sending notification");
                Log.e("sendNotificationToUser", receiverToken);
                String messageBody = song.getArtistName() + " vừa phát hành ca khúc " + song.getName();
                Log.e("sendNotificationToUserMessage", messageBody);

                URL url = new URL("https://fcm.googleapis.com/v1/projects/music-app-967da/messages:send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " +  token);
                conn.setRequestProperty("Content-Type", "application/json; UTF-8");

                String payload = "{\n" +
                        "  \"message\": {\n" +
                        "    \"token\": \"" + receiverToken + "\",\n" +
                        "    \"notification\": {\n" +
                        "      \"body\": \"" + messageBody + "\",\n" +
                        "      \"title\": \"Bài hát mới\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Log.d("FCM123", "Response: " + response.toString());
                    }
                } else {
                    Log.e("FCM123", "Failed to send notification, response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getToken(Song song, String receiverToken) {
        Log.e("token12345","Code chay vao day");
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getToken().enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    tokenResponse = response.body();
                     String token = tokenResponse.getData();
                     Log.e("token1234",token);
                     sendNotificationToUser(song, receiverToken, token);
                } else {
                    Log.e("getToken", "Failed to get token");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.e("getToken", "Error: " + t.getMessage());
            }
        });
    }
}
