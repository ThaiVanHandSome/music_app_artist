package com.example.music_app_artist.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app_artist.R;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.MultipartUtil;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishSongActivity extends AppCompatActivity {

    private ImageView songImage;
    private EditText songNameTxt;
    private MaterialButton btnChooseAudio, btnUploadSong;
    private TextView audioNameTxt;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private Uri mUri;
    private Uri audioUri;
    private ActivityResultLauncher<String> mGetContent;
    private static final int REQUEST_CODE_PICK_MP3 = 1;
    private APIService apiService;
    private String imagePath, audioPath;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data == null) {
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    songImage.setImageBitmap(bitmap);
                    System.out.println(mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private ActivityResultLauncher<Intent> mAudioActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data == null) {
                    return;
                }
                Uri uri = data.getData();
                audioUri = uri;
                String fileName = getFileNameFromUri(audioUri);
                audioNameTxt.setText(fileName);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_song);

        mapping();

        songImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnChooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMp3File();
            }
        });

        btnUploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOverlay();
                try {
                    uploadSong();
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void hideOverlay() {
        overlay.setVisibility(View.INVISIBLE);
        overlay.setFocusable(false);
        overlay.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void openOverlay() {
        overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
        overlay.setVisibility(View.VISIBLE);
        overlay.setFocusable(true);
        overlay.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void uploadSong() throws IOException, URISyntaxException {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        MultipartBody.Part imagePart = MultipartUtil.createMultipartFromUri(this, mUri, "imageFile", "image_file.png");
        MultipartBody.Part resourcePart = MultipartUtil.createMultipartFromUri(this, audioUri, "resourceFile", "audio_file.mp3");


        apiService.uploadSong(imagePart, songNameTxt.getText().toString(), resourcePart).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                hideOverlay();
                ResponseMessage res = response.body();
                Toast.makeText(PublishSongActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideOverlay();
                Log.d("error", t.toString());
                Toast.makeText(PublishSongActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }

    public void pickMp3File() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        mAudioActivityResultLauncher.launch(Intent.createChooser(intent, "Select Audio!"));
    }

    private void mapping() {
        songImage = (ImageView) findViewById(R.id.songImage);
        songNameTxt = (EditText) findViewById(R.id.songNameTxt);
        btnChooseAudio = (MaterialButton) findViewById(R.id.btnChooseAudio);
        btnUploadSong = (MaterialButton) findViewById(R.id.btnUploadSong);
        audioNameTxt = (TextView) findViewById(R.id.audioNameTxt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }
}