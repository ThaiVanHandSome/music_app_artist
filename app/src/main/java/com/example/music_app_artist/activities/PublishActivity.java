package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.LibraryViewPager2Adapter;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishActivity extends AppCompatActivity {

    private TextView cntSongTxt, cntAlbumTxt;
    private LinearLayout btnPublishSong, btnPublishAlbum;
    private TabLayout publishTabLayout;
    private ViewPager2 publishViewPager;
    private LibraryViewPager2Adapter adapter;
    private User user;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        mapping();

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new LibraryViewPager2Adapter(fragmentManager, getLifecycle());
        publishViewPager.setAdapter(adapter);
        publishTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                publishViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnPublishSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishActivity.this, PublishSongActivity.class);
                startActivity(intent);
            }
        });

        btnPublishAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublishActivity.this, PublishAlbumActivity.class);
                startActivity(intent);
            }
        });

        getQuantityAlbums();
        getQuantitySongs();

    }

    private void getQuantitySongs() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCountSongsOfArtist((long) user.getId()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    cntSongTxt.setText(String.valueOf((int) Double.parseDouble(res.getData().toString())));
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void getQuantityAlbums() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCountAlbumsOfArtist((long) user.getId()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    cntAlbumTxt.setText(String.valueOf((int) Double.parseDouble(res.getData().toString())));
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        cntSongTxt = (TextView) findViewById(R.id.cntSongTxt);
        cntAlbumTxt = (TextView) findViewById(R.id.cntAlbumTxt);
        btnPublishSong = (LinearLayout) findViewById(R.id.btnPublishSong);
        btnPublishAlbum = (LinearLayout) findViewById(R.id.btnPublishAlbum);
        publishTabLayout = (TabLayout) findViewById(R.id.publishTabLayout);
        publishViewPager = (ViewPager2) findViewById(R.id.publish_view_pager);
    }
}