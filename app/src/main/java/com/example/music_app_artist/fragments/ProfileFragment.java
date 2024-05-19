package com.example.music_app_artist.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.activities.UpdateArtistProfileActivity;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.User;
import com.google.android.material.button.MaterialButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imageView;
    private TextView artistNameTxt;
    private MaterialButton btnModify;
    private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Initialize UI elements
        imageView = view.findViewById(R.id.imageView);
        artistNameTxt = view.findViewById(R.id.artistNameTxt);
        btnModify = view.findViewById(R.id.btnModify);

        // Load user data
        user = SharePrefManagerUser.getInstance(getContext()).getUser();
        Glide.with(getContext()).load(user.getAvatar()).into(imageView);
        artistNameTxt.setText(user.getNickname());

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateArtistProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
