package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.LibraryViewPager2Adapter;
import com.google.android.material.tabs.TabLayout;

public class PublishActivity extends AppCompatActivity {

    private TextView cntSongTxt, cntAlbumTxt;
    private LinearLayout btnPublishSong, btnPublishAlbum;
    private TabLayout publishTabLayout;
    private ViewPager2 publishViewPager;
    private LibraryViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        mapping();

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