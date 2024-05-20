package com.example.music_app_artist.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.fragments.HomeFragment;
import com.example.music_app_artist.fragments.ProfileFragment;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.UpdateUserResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.MultipartUtil;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateArtistProfileActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText artistNameTxt;
    private MaterialButton btnSave;
    private CircleImageView avatar;
    private RadioButton maleRadio, femaleRadio;
    private TextView emailTxt;
    User user;
    private Uri mUri;
    private APIService apiService;
    private ProgressBar progressBar;
    private FrameLayout overlay;
    FragmentManager fragmentManager;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data == null) {
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    avatar.setImageBitmap(bitmap);
                    System.out.println(mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_artist_profile);

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();

        mapping();

        fillData();

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openOverlay();
                    modifyUser();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void hideOverlay() {
        overlay.setVisibility(View.INVISIBLE);
        overlay.setFocusable(false);
        overlay.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void openOverlay() {
        overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
        overlay.setVisibility(View.VISIBLE);
        overlay.setFocusable(true);
        overlay.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void modifyUser() throws IOException {
        MultipartBody.Part imageFile = null;
        if(mUri != null) {
            imageFile = MultipartUtil.createMultipartFromUri(this, mUri, "imageFile", "image_file.png");
        }
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();
        int gender = 1;
        if(femaleRadio.isChecked()) {
            gender = 0;
        }
        apiService.updateArtist((long) user.getId(), imageFile, artistNameTxt.getText().toString(), gender).enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                hideOverlay();
                UpdateUserResponse res = response.body();
                assert res != null;
                Toast.makeText(UpdateArtistProfileActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.isSuccess()) {
                    User user1 = res.getData();
                    Log.d("user", user1.getAvatar());
                    Log.d("user", user1.getNickname());
                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("keynickname", user1.getNickname());
                    editor.putString("keyimage", user1.getAvatar());
                    editor.putInt("keygender", user1.getGender());
                    editor.apply();
                    Intent intent = new Intent(UpdateArtistProfileActivity.this, MainActivity.class);
                    intent.putExtra("id", R.id.menu_item_profile);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                hideOverlay();
                Toast.makeText(UpdateArtistProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
    }
}