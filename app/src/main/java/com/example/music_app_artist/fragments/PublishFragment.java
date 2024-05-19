package com.example.music_app_artist.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.music_app_artist.R;
import com.example.music_app_artist.activities.PublishAlbumActivity;
import com.example.music_app_artist.activities.PublishSongActivity;
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

public class PublishFragment extends Fragment {

    private TextView cntSongTxt, cntAlbumTxt;
    private LinearLayout btnPublishSong, btnPublishAlbum;
    private TabLayout publishTabLayout;
    private ViewPager2 publishViewPager;
    private LibraryViewPager2Adapter adapter;
    private User user;
    private APIService apiService;

    public PublishFragment() {
        // Required empty public constructor
    }

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        mapping(view);

        user = SharePrefManagerUser.getInstance(requireContext()).getUser();

        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new LibraryViewPager2Adapter(fragmentManager, getLifecycle());
        publishViewPager.setAdapter(adapter);

        publishTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                publishViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnPublishSong.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), PublishSongActivity.class);
            startActivity(intent);
        });

        btnPublishAlbum.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), PublishAlbumActivity.class);
            startActivity(intent);
        });

        getQuantityAlbums();
        getQuantitySongs();

        return view;
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

    private void mapping(View view) {
        cntSongTxt = view.findViewById(R.id.cntSongTxt);
        cntAlbumTxt = view.findViewById(R.id.cntAlbumTxt);
        btnPublishSong = view.findViewById(R.id.btnPublishSong);
        btnPublishAlbum = view.findViewById(R.id.btnPublishAlbum);
        publishTabLayout = view.findViewById(R.id.publishTabLayout);
        publishViewPager = view.findViewById(R.id.publish_view_pager);
    }
}