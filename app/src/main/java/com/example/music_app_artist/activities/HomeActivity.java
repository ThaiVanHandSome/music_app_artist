package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.SongAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongsResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private List<Song> songs;
    private APIService apiService;
    private SongAdapter adapter;
    RecyclerView recyclerView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        handleGetAllSongs();

        recyclerView = (RecyclerView) findViewById(R.id.listSong);
        adapter = new SongAdapter(getApplicationContext(), songs, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
    }

    private void handleGetAllSongs() {

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongsOfArtistDesc((long) user.getId()).enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                SongsResponse res = response.body();
                assert res != null;
                if(res.getSuccess()) {
                    songs = res.getData();
                    adapter = new SongAdapter(getApplicationContext(), songs, new SongAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Song song) {
                            Intent intent = new Intent(HomeActivity.this, SongDetailActivity.class);
                            intent.putExtra("idSong", song.getIdSong());
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<SongsResponse> call, Throwable t) {

            }
        });
    }
}