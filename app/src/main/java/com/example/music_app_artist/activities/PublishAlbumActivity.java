package com.example.music_app_artist.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.SongChooseAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.MultipartUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class PublishAlbumActivity extends AppCompatActivity {

    private ImageView albumImage;
    private EditText albumNameTxt;
    private MaterialButton btnUploadAlbum;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private RecyclerView listSong;
    private Uri mUri;
    private SongChooseAdapter adapter;
    private List<Song> songs = new ArrayList<>();
    private APIService apiService;
    private User user;
    private final List<Long> selectedSongs = new ArrayList<>();

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
        setContentView(R.layout.activity_publish_album);

        mapping();

        albumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        loadSongs();

        btnUploadAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openOverlay();
                    uploadAlbum();
                } catch (IOException | URISyntaxException e) {
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

    private void uploadAlbum() throws IOException, URISyntaxException {
        // idArtist
        // albumName
        // generate idAlbum
        // update Song idAlbum

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        MultipartBody.Part imagePart = MultipartUtil.createMultipartFromUri(this, mUri, "image", "image_file.png");
        List<Long> selectedSongs = adapter.getSelectedSongs();
        String s = "";
        for (int i = 0; i < selectedSongs.size(); i++) {
            s += String.valueOf(selectedSongs.get(i));
            if (i < selectedSongs.size() - 1) {
                s += ",";
            }
        }
        System.out.println(adapter.getSelectedSongs());
        apiService.uploadAlbum(imagePart, (long )user.getId(), String.valueOf(albumNameTxt.getText()), s).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                hideOverlay();
                ResponseMessage res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    Intent intent = new Intent(PublishAlbumActivity.this, PublishActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(PublishAlbumActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideOverlay();
                Toast.makeText(PublishAlbumActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSongs() {
        adapter = new SongChooseAdapter(getApplicationContext(), songs, null);
        listSong.setAdapter(adapter);
        listSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listSong.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAllSongs();
    }

    private void getAllSongs() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();
        apiService.getAllSongsOfArtist((long) user.getId()).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                SongResponse res = response.body();
                assert res != null;
                if(res.getSuccess()) {
                    songs = res.getData();
                    adapter = new SongChooseAdapter(getApplicationContext(), songs, new SongChooseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Song song) {

                        }
                    });
                    listSong.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
            }
        });
    }

    private void mapping() {
        albumImage = (ImageView) findViewById(R.id.albumImage);
        albumNameTxt = (EditText) findViewById(R.id.albumNameTxt);
        btnUploadAlbum = (MaterialButton) findViewById(R.id.btnUploadAlbum);
        listSong = (RecyclerView) findViewById(R.id.listSong);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }
}