package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.User;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imageView;
    private TextView artistNameTxt;
    private MaterialButton btnModify;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mapping();

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();
        Glide.with(getApplicationContext()).load(user.getAvatar()).into(imageView);
        artistNameTxt.setText(user.getNickname());

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateArtistProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    private void mapping() {
        imageView = (CircleImageView) findViewById(R.id.imageView);
        artistNameTxt = (TextView) findViewById(R.id.artistNameTxt);
        btnModify = (MaterialButton) findViewById(R.id.btnModify);
    }
}