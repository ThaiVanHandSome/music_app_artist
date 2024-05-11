package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.User;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateArtistProfileActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText artistNameTxt;
    private MaterialButton btnSave;
    private CircleImageView avatar;
    private RadioButton maleRadio, femaleRadio;
    private TextView emailTxt;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_artist_profile);

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        mapping();

        fillData();
    }

    private void fillData() {
        artistNameTxt.setText(user.getNickname());
        if(user.getGender() == 1) {
            maleRadio.setChecked(true);
        } else {
            femaleRadio.setChecked(true);
        }
        emailTxt.setText(user.getEmail());
        Glide.with(getApplicationContext()).load(user.getAvatar()).into(avatar);
    }

    private void mapping() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        artistNameTxt = (EditText) findViewById(R.id.artistNameTxt);
        btnSave = (MaterialButton) findViewById(R.id.btnSave);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        maleRadio = (RadioButton) findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton) findViewById(R.id.femaleRadio);
        emailTxt = (TextView) findViewById(R.id.emailTxt);
    }
}