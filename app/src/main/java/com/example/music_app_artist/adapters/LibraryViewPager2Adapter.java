package com.example.music_app_artist.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_app_artist.fragments.AlbumFragment;
import com.example.music_app_artist.fragments.SongFragment;

public class LibraryViewPager2Adapter extends FragmentStateAdapter {
    private static final int ALBUM_TAB_POSITION = 0;
    private static final int SONG_TAB_POSITION = 1;
    public LibraryViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == ALBUM_TAB_POSITION) {
            return new AlbumFragment();
        } else {
            return new SongFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
