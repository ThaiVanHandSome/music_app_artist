package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.SongCommentAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongComment;
import com.example.music_app_artist.models.SongCommentResponse;
import com.example.music_app_artist.models.SongResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongDetailActivity extends AppCompatActivity {

    private TextView tvArtistName, tvSongTitle, cntLikeTxt, cntViewTxt, cntCommentTxt;
    private CircleImageView imSongAvt;
    private MaterialButton btnDeleteSong;
    private RecyclerView recyclerViewCmt;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private APIService apiService;
    private User user;
    private Song song = new Song();
    private SongCommentAdapter songCommentAdapter;
    private List<SongComment> songComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        mapping();

        loadInitData();

        songCommentAdapter = new SongCommentAdapter(getApplicationContext(), songComments, null);
        recyclerViewCmt.setAdapter(songCommentAdapter);
        recyclerViewCmt.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
        recyclerViewCmt.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAllComments();

        btnDeleteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOverlay();
                deleteSong();
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

    private void deleteSong() {
        Intent intent = getIntent();
        Long idSong = intent.getLongExtra("idSong", -1);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.deleteSong(idSong).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                hideOverlay();
                ResponseMessage res = response.body();
                assert res != null;
                Toast.makeText(SongDetailActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    Intent intent1 = new Intent(SongDetailActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                hideOverlay();
                Toast.makeText(SongDetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllComments() {
        Intent intent = getIntent();
        Long idSong = intent.getLongExtra("idSong", -1);
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllCommentsOfSong(idSong).enqueue(new Callback<SongCommentResponse>() {
            @Override
            public void onResponse(Call<SongCommentResponse> call, Response<SongCommentResponse> response) {
                SongCommentResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    songComments = res.getData();
                    songCommentAdapter = new SongCommentAdapter(getApplicationContext(), songComments, null);
                    recyclerViewCmt.setAdapter(songCommentAdapter);
                    songCommentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongCommentResponse> call, Throwable t) {

            }
        });
    }

    private void loadInitData() {
        Intent intent = getIntent();
        Long idSong = intent.getLongExtra("idSong", -1);
        getSong(idSong);
    }

    private void getSong(Long idSong) {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getSongById(idSong).enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                SongResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    song = res.getData();
                    tvArtistName.setText(song.getArtistName());
                    tvSongTitle.setText(song.getName());
                    cntLikeTxt.setText(String.valueOf(song.getCntLikes()));
                    cntCommentTxt.setText(String.valueOf(song.getCntComments()));
                    cntViewTxt.setText(String.valueOf(song.getViews()));
                    Glide.with(getApplicationContext()).load(song.getImage()).into(imSongAvt);
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {
                Toast.makeText(SongDetailActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapping() {
        tvArtistName = (TextView) findViewById(R.id.tvArtistName);
        tvSongTitle = (TextView) findViewById(R.id.tvSongTitle);
        imSongAvt = (CircleImageView) findViewById(R.id.imSongAvt);
        btnDeleteSong = (MaterialButton) findViewById(R.id.btnDeleteSong);
        cntLikeTxt = (TextView) findViewById(R.id.cntLikeTxt);
        cntViewTxt = (TextView) findViewById(R.id.cntViewTxt);
        cntCommentTxt = (TextView) findViewById(R.id.cntCommentTxt);
        recyclerViewCmt = (RecyclerView) findViewById(R.id.recyclerViewCmt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }
}