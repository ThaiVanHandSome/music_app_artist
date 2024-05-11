package com.example.music_app_artist.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.AlbumAdapter;
import com.example.music_app_artist.databinding.FragmentAlbumBinding;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.Album;
import com.example.music_app_artist.models.AlbumsResponse;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumFragment extends Fragment {
    FragmentAlbumBinding binding;
    RecyclerView recyclerView;
    AlbumAdapter adapter;
    List<Album> albums = new ArrayList<>();
    private APIService apiService;

    User user;
    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        user = SharePrefManagerUser.getInstance(this.getContext()).getUser();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(inflater, container, false);

        recyclerView = binding.listAlbum;
        adapter = new AlbumAdapter(getContext(), albums, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAlbumsByIdUser();
        return binding.getRoot();
    }

    private void getAlbumsByIdUser() {
        APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAlbumsByIdArtist((long) user.getId()).enqueue(new Callback<AlbumsResponse>() {
            @Override
            public void onResponse(Call<AlbumsResponse> call, Response<AlbumsResponse> response) {
                AlbumsResponse res = response.body();
                assert res != null;
                if (res.isSuccess()) {
                    albums = res.getData();
                    Log.d("albums", "ok");
                    adapter = new AlbumAdapter(getContext(), albums, new AlbumAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Album albums) {

                        }
                    });
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AlbumsResponse> call, Throwable t) {
            }
        });
    }
}