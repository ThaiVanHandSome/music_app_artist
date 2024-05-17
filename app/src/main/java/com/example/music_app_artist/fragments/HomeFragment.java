package com.example.music_app_artist.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.music_app_artist.R;
import com.example.music_app_artist.activities.SongDetailActivity;
import com.example.music_app_artist.adapters.SongAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongsResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView cntLikeTxt, cntViewTxt, cntCommentTxt;
    private List<Song> songs;
    private APIService apiService;
    private SongAdapter adapter;
    private RecyclerView recyclerView;
    private User user;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        user = SharePrefManagerUser.getInstance(getContext()).getUser();

        handleGetAllSongs();

        cntCommentTxt = view.findViewById(R.id.cntCommentTxt);
        cntLikeTxt = view.findViewById(R.id.cntLikeTxt);
        cntViewTxt = view.findViewById(R.id.cntViewTxt);
        recyclerView = view.findViewById(R.id.listSong);
        adapter = new SongAdapter(getContext(), songs, null);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));

        loadLikes();
        loadViews();
        loadComments();

        return view;
    }

    private void loadComments() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCountOfComments((long) user.getId()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                assert res != null;
                cntCommentTxt.setText(String.valueOf((int) Double.parseDouble(res.getData().toString())));
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void loadViews() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCountOfViews((long) user.getId()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                assert res != null;
                cntViewTxt.setText(String.valueOf((int) Double.parseDouble(res.getData().toString())));
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void loadLikes() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getCountOfLikes((long) user.getId()).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                assert res != null;
                cntLikeTxt.setText(String.valueOf((int) Double.parseDouble(res.getData().toString())));
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void handleGetAllSongs() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllSongsOfArtistDesc((long) user.getId()).enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Call<SongsResponse> call, Response<SongsResponse> response) {
                SongsResponse res = response.body();
                if (res != null && res.getSuccess()) {
                    songs = res.getData();
                    adapter = new SongAdapter(getContext(), songs, null);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SongsResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
