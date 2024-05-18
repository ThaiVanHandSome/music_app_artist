package com.example.music_app_artist.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.MultipartUtil;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAlbumActivity extends AppCompatActivity {

    private CircleImageView albumImage;
    private EditText albumNameTxt;
    private MaterialButton btnSave;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private Uri mUri;
    private APIService apiService;

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
                    albumImage.setImageBitmap(bitmap);
                    System.out.println(mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_album);

        mapping();
        
        fillData();

        albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOverlay();
                try {
                    updateAlbum();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void updateAlbum() throws IOException {
        Intent intent = getIntent();
        Long idAlbum = intent.getLongExtra("idAlbum", -1);
        MultipartBody.Part imagePart = null;
        if(mUri != null) {
            imagePart = MultipartUtil.createMultipartFromUri(this, mUri, "imageFile", "image_file.png");
        }

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.updateAlbum(idAlbum, imagePart, albumNameTxt.getText().toString()).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage res = response.body();
                assert res != null;
                Toast.makeText(UpdateAlbumActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    Intent intent1 = new Intent(UpdateAlbumActivity.this, AlbumDetailActivity.class);
                    intent1.putExtra("idAlbum", idAlbum);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(UpdateAlbumActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }

    private void mapping() {
        albumImage = (CircleImageView) findViewById(R.id.albumImage);
        albumNameTxt = (EditText) findViewById(R.id.albumNameTxt);
        btnSave = (MaterialButton) findViewById(R.id.btnSave);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void fillData() {
        Intent intent = getIntent();
        String albumUrl = intent.getStringExtra("albumUrl");
        String albumName = intent.getStringExtra("albumName");
        albumNameTxt.setText(albumName);
        Glide.with(getApplicationContext()).load(albumUrl).into(albumImage);
    }
}