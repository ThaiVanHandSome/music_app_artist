package com.example.music_app_artist.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.music_app_artist.R;
import com.example.music_app_artist.fragments.HomeFragment;
import com.example.music_app_artist.fragments.ProfileFragment;
import com.example.music_app_artist.fragments.PublishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, HomeFragment.newInstance())
                .commit();

        navigationView = findViewById(R.id.main_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_item_home) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame_layout, HomeFragment.newInstance())
                            .commit();
                    return true;
                } else if (itemId == R.id.menu_item_profile) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame_layout, ProfileFragment.newInstance())
                            .commit();
                    return true;
                } else if (itemId == R.id.menu_item_publish) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_frame_layout, PublishFragment.newInstance())
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}