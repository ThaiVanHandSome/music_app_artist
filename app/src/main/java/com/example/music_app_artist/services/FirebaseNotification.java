package com.example.music_app_artist.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseNotification extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "MY_CHANNEL";

    public static void sendNotificationToUser(String messageBody, String receiverToken) {
        new Thread(() -> {
            try {
                String token = "ya29.c.c0AY_VpZgGrW7aUj2SoDo9caRSHpcO8_x91PvogsQNZsbk3ko-qAYKz14j6axvCg_DylHGa7vmTjUCK8vMKi8IeegFdtWoOel4JpVT3YyQce0CXKn4flOy-qsFK-SlGrXFltEEiqdzdOjEbCvTWqzUGGugOuAcgySe0qlAMw8JhBDB2CH-6GrkXEr0yIbyE1cNje0Tby0_IVpFQQE8pd9b86BpQJ8VxTm7oFZ81xRsRTGsedQvoES_NsAVIqdREzr_sCwDQREeJ16f9HcFy6at9mNrB8TP-z2fdtWHBTDS0IzTHOCW4pl4SQq4qYq_mVV1_MzjzswmbRZbDCCQrOxFHljkxZdrcl49cwzRTk3OZSI8SPX6y1HwIUfVN385PqOFkgXXnfusmRmp-O7a7SRSx03iWicJnV9VXorXW-o0Rfvdmiz1kzqlm4Ffopltb9IQoc572QJXwbQvXJjUmqhb-86ruyolSJV_iQBO9wdMwlJRuOleajVlJ3IIbzZpwYkUcsuFe588UjMX5bBZwfneOlFc3yO4Vr91fv_m7cstmcoJBQldllY_844egsBrXkBij5jhoRSm3aBqJ9pnfuIwbt7XkRqywnnQhYsVJ8cf2XZqv9UOXJbllUlhavsUztIvdYma9VyQsMrJ1aRIrM5w_USuRBeOeaypV9m_zjh3VaWc_gpJyQevUb4ayZ6Zsp1lUu9M0uFIx0wbrkW4rOyZgoFoOZ6f31sWqgvXcpQ1JBmBI2s4_p6kSg-S0UywXtUdfUap-Q2lSYnO0YUVXg17uJIFOv9QSSfX7WBr07zZMpBUq_Qdbd1BVIl46bpljd-pd2qhpJOdMiWJmqF2Scg7_Boqcv43oVakQqny0w4blMhoFpvFrkszcthjS8Ikkrp85dISM0gV0l2e2Q4-FBVIoc69xWMx-nd1ZJa7W-I1iBlh2qVXQBfySl80wSRjtgJpykVBs0BXfBRZiRXud7rk7bQ2nfB3gc6jOZ4aykSuj4U57Ovi16Bzvbg";
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
}
