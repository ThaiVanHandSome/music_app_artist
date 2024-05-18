package com.example.music_app_artist.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.example.music_app_artist.R;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.music_app_artist.activities.HomeActivity;
import com.example.music_app_artist.models.Song;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseNotification extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "MY_CHANNEL";

    public static void sendNotificationToUser(Song song, String receiverToken) {
        new Thread(() -> {
            try {
                Log.e("sendNotificationToUser","Chạy vào gửi thông báo");
                Log.e("sendNotificationToUser", receiverToken);
                String messageBody = song.getArtistName() + " vừa phát hành ca khúc " + song.getName();
                Log.e("sendNotificationToUser", messageBody);
                String token = "ya29.c.c0AY_VpZhy_Lezs_JHit1SHfxf-ndSEVfHdvXj8CiH-RlDE9tYiBbA7l3GxwH4uFv4q_ZS3qPHA48oSX080U3CEg8xmGaoaL5SbGw3donvBHLxItwMyY4JesyVH9sBdQV4yDghikswPtm7NrL4y39DVushK7rbsN3w12oh-B6IIyqkXA6ee-vG9pG6V6E3LHseBWYegrH4UoShvKjiP4ZzfEez1Du_S1VXISIW07efRw_06yipcqrcdFmyZeIPXasMF-OvxLVLPGY3L_h_InT2dbdrN7ivviTmc6Ls-ZC-PMkSSmds6fgqnzjWBmqZ44x4JnE_h3HohpNt-OvJPHU7J_Az9ld5Im4aMhLMgvWSjeG0A-kmVhISpVU-H385A7Mw6Vi-lpjq1femQOhjeqQJs2llS4kbvbc4pqgS5VQ2wWjbsOufix1SSivFurR0ndarfv57gujWXboc8qb10njm6swQ-io305g42Ooqmsgc46edYxwZVZ597JifegxJ_3e4OOp12zdoUFVlq7q0f1usSuSxr4ub41oxRffXSpVVq1OkU4sVRigYMgYs6IIyyZ435ZxOlol63gVxOf8FU6y1Fv3OW-XB1xmYk4O4hJpfJFlxr2VfyovQJ9hB2t93pbY2QyBf7dr1m8vBBVaf_rujJirsspdpoRIhdu1wfwX1S4129Osbh4I0Q5y1eMRdkeo2JxISrbuJmnwUf4QxQgUsw4m0j7r2Z8OUOSXXmJ6OMMhkWMi2o__-p3MY80jt8RvWflh-cfFxj1_4pUr6vXVxd_l0IfWkqIiQ4uF6mfJwXbOs6uFmcnaUqmh7QvpnnSW4wB0ybQFwx6bWQ544B0Zzkk3j60RBZQW41pjrRylV-gUYl7myp-f6pslF2M_R6SnXdVcqlFpBzvQ-ec06ozRUv8BatlB95nQcqXt83h3UcRqoI0FXZt8kabhI5avMpyzIRlZSrXZtv1S1vcX-VVFc0rfiZVV_QvYSFSk87ut0BhbWlJcejpRpyYu";
                URL url = new URL("https://fcm.googleapis.com/v1/projects/music-app-967da/messages:send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + token);
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
                    byte[] input = payload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
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
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here.
        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music App Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}
