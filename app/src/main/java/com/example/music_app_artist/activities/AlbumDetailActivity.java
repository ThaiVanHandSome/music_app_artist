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
import com.example.music_app_artist.models.Album;
import com.example.music_app_artist.models.AlbumResponse;
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
    private MaterialButton btnDeleteAlbum, btnUpdateAlbum;
    private RecyclerView recyclerViewSong;
    private APIService apiService;
    private SongAdapter adapter;
    private List<Song> songs;
    private Album album = new Album();


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

        btnUpdateAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumDetailActivity.this, UpdateAlbumActivity.class);
                intent.putExtra("idAlbum", album.getIdAlbum());
                intent.putExtra("albumUrl", album.getImage());
                intent.putExtra("albumName", album.getName());
                startActivity(intent);
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
                    Intent intent1 = new Intent(AlbumDetailActivity.this, MainActivity.class);
                    intent1.putExtra("id", R.id.menu_item_home);
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
        getAlbum(idAlbum);
    }

    private void getAlbum(Long idAlbum) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAlbumById(idAlbum).enqueue(new Callback<AlbumResponse>() {
            @Override
            public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                AlbumResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    album = res.getData();
                    albumNameTxt.setText(album.getName());
                    Glide.with(getApplicationContext()).load(album.getImage()).into(imageView);
                    cntSongTxt.setText("Số lượng bài hát: " + album.getCntSong() + " bài");
                }
            }

            @Override
            public void onFailure(Call<AlbumResponse> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        imageView = (ImageView) findViewById(R.id.albumImage);
        cntSongTxt = (TextView) findViewById(R.id.cntSongTxt);
        albumNameTxt = (TextView) findViewById(R.id.albumNameTxt);
        btnDeleteAlbum = (MaterialButton) findViewById(R.id.btnDeleteAlbum);
        recyclerViewSong = (RecyclerView) findViewById(R.id.recyclerViewSong);
        btnUpdateAlbum = (MaterialButton) findViewById(R.id.btnUpdateAlbum);
    }
}