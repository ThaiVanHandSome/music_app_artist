package com.example.music_app_artist.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.SongAdapter;
import com.example.music_app_artist.databinding.FragmentSongBinding;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongsResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongFragment extends Fragment {

    FragmentSongBinding binding;
    RecyclerView recyclerView;
    SongAdapter adapter;
    List<Song> songs = new ArrayList<>();
    LinearLayout linearLayoutAddToLibrary;
    private APIService apiService;

    User user = SharePrefManagerUser.getInstance(this.getContext()).getUser();
    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharePrefManagerUser.getInstance(this.getContext()).getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSongBinding.inflate(inflater, container, false);
        recyclerView = binding.listSong;
        adapter = new SongAdapter(getContext(), songs, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAlbumsByIdUser();
        return binding.getRoot();
    }

    private void getAlbumsByIdUser() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongsOfArtistDesc((long) user.getId()).enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                SongsResponse res = response.body();
                assert res != null;
                if (res.getSuccess()) {
                    songs = res.getData();
                    adapter = new SongAdapter(getContext(), songs, null);
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