package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.SongAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongsResponse;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView cntSongTxt, albumNameTxt;
    private MaterialButton btnDeleteAlbum;
    private RecyclerView recyclerViewSong;
    private APIService apiService;
    private SongAdapter adapter;
    private List<Song> songs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        mapping();

        loadInitData();

        adapter = new SongAdapter(getApplicationContext(), songs, null);
        recyclerViewSong.setAdapter(adapter);
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewSong.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getSongs();

        btnDeleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeleteAlbum();
            }
        });
    }

    private void handleDeleteAlbum() {
        Intent intent = getIntent();
        Long idAlbum = intent.getLongExtra("idAlbum", -1);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.deleteAlbum(idAlbum).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage res = response.body();
                assert res != null;
                Toast.makeText(AlbumDetailActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    Intent intent1 = new Intent(AlbumDetailActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Toast.makeText(AlbumDetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSongs() {
        Intent intent = getIntent();
        Long idAlbum = intent.getLongExtra("idAlbum", -1);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongsByAlbumId(idAlbum).enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                SongsResponse res = response.body();
                assert res != null;
                if(res.getSuccess()) {
                    songs = res.getData();
                    adapter = new SongAdapter(getApplicationContext(), songs, null);
                    recyclerViewSong.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongsResponse> call, Throwable t) {

            }
        });
    }

    private void loadInitData() {
        Intent intent = getIntent();
        Long idAlbum = intent.getLongExtra("idAlbum", -1);
        String albumName = intent.getStringExtra("albumName");
        String albumImage = intent.getStringExtra("albumImage");
        int cntSong = intent.getIntExtra("cntSong", -1);
        albumNameTxt.setText(albumName);
        Glide.with(getApplicationContext()).load(albumImage).into(imageView);
        cntSongTxt.setText("Số lượng bài hát: " + cntSong + " bài");
    }

    private void mapping() {
        imageView = (ImageView) findViewById(R.id.albumImage);
        cntSongTxt = (TextView) findViewById(R.id.cntSongTxt);
        albumNameTxt = (TextView) findViewById(R.id.albumNameTxt);
        btnDeleteAlbum = (MaterialButton) findViewById(R.id.btnDeleteAlbum);
        recyclerViewSong = (RecyclerView) findViewById(R.id.recyclerViewSong);
    }
}