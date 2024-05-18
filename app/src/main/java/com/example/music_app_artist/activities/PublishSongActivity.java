package com.example.music_app_artist.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music_app_artist.R;
import com.example.music_app_artist.adapters.AlbumChooseAdapter;
import com.example.music_app_artist.adapters.CategoryAdapter;
import com.example.music_app_artist.decorations.BottomOffsetDecoration;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.Album;
import com.example.music_app_artist.models.AlbumsResponse;
import com.example.music_app_artist.models.CategoriesResponse;
import com.example.music_app_artist.models.Category;
import com.example.music_app_artist.adapters.SongAdapter;
import com.example.music_app_artist.internals.SharePrefManagerUser;
import com.example.music_app_artist.models.DefaultResponse;
import com.example.music_app_artist.models.FollowerResponse;
import com.example.music_app_artist.models.NotificationFirebase;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.models.Song;
import com.example.music_app_artist.models.SongResponse;
import com.example.music_app_artist.models.UploadResponse;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.models.User;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.services.FirebaseNotification;
import com.example.music_app_artist.utils.MultipartUtil;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishSongActivity extends AppCompatActivity {

    private ImageView songImage;
    private EditText songNameTxt;
    private MaterialButton btnChooseAudio, btnUploadSong;
    private TextView audioNameTxt;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private RecyclerView listAlbum, listCategory;
    private AlbumChooseAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private Uri mUri;
    private Uri audioUri;
    private ActivityResultLauncher<String> mGetContent;
    private static final int REQUEST_CODE_PICK_MP3 = 1;
    private APIService apiService;
    private String imagePath, audioPath;
    private List<Album> albums = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private User user;

    private List<Long> ids;

    private Song song;


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
                    songImage.setImageBitmap(bitmap);
                    System.out.println(mUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private ActivityResultLauncher<Intent> mAudioActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data == null) {
                    return;
                }
                Uri uri = data.getData();
                audioUri = uri;
                String fileName = getFileNameFromUri(audioUri);
                audioNameTxt.setText(fileName);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_song);

        mapping();

        user = SharePrefManagerUser.getInstance(this.getApplicationContext()).getUser();

        songImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnChooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMp3File();
            }
        });

        btnUploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openOverlay();
                try {
                    uploadSong();
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        loadAlbums();

        loadCategories();
    }

    private void loadCategories() {
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categories, null);
        listCategory.setAdapter(categoryAdapter);
        listCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listCategory.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAllCategories();
    }

    private void getAllCategories() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllCategories().enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                CategoriesResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    categories = res.getData();
                    categoryAdapter = new CategoryAdapter(getApplicationContext(), categories, new CategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Category category) {

                        }
                    });
                    listCategory.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                }
                Toast.makeText(PublishSongActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(PublishSongActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAlbums() {
        adapter = new AlbumChooseAdapter(getApplicationContext(), albums, null);
        listAlbum.setAdapter(adapter);
        listAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listAlbum.addItemDecoration(new BottomOffsetDecoration(getResources().getDimensionPixelSize(R.dimen.bottom_offset)));
        getAllAlbums();
    }

    private void getAllAlbums() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        apiService.getAlbumsByIdArtist((long) user.getId()).enqueue(new Callback<AlbumsResponse>() {
            @Override
            public void onResponse(Call<AlbumsResponse> call, Response<AlbumsResponse> response) {
                AlbumsResponse res = response.body();
                assert res != null;
                if(res.isSuccess()) {
                    albums = res.getData();
                    adapter = new AlbumChooseAdapter(getApplicationContext(), albums, new AlbumChooseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Album albums) {

                        }
                    });
                    listAlbum.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AlbumsResponse> call, Throwable t) {
                Toast.makeText(PublishSongActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
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

    private void uploadSong() throws IOException, URISyntaxException {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);

        MultipartBody.Part imagePart = MultipartUtil.createMultipartFromUri(this, mUri, "imageFile", "image_file.png");
        MultipartBody.Part resourcePart = MultipartUtil.createMultipartFromUri(this, audioUri, "resourceFile", "audio_file.mp3");

        user = SharePrefManagerUser.getInstance(getApplicationContext()).getUser();
        apiService.uploadSong(imagePart, (long) user.getId(), songNameTxt.getText().toString(), (long) categoryAdapter.getCheckedIdCategory(), (long) adapter.getCheckedIdAlbum(), resourcePart).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                hideOverlay();
                UploadResponse res = response.body();
                Toast.makeText(PublishSongActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                if(res.getSuccess()) {
                    Song song = res.getData();
                    sendNotification(song);
                    Intent intent = new Intent(PublishSongActivity.this, PublishActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                hideOverlay();
                Log.d("error", t.toString());
                Toast.makeText(PublishSongActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if (uri.getScheme().equals("file")) {
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture!"));
    }

    public void pickMp3File() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        mAudioActivityResultLauncher.launch(Intent.createChooser(intent, "Select Audio!"));
    }

    private void mapping() {
        songImage = (ImageView) findViewById(R.id.songImage);
        songNameTxt = (EditText) findViewById(R.id.songNameTxt);
        btnChooseAudio = (MaterialButton) findViewById(R.id.btnChooseAudio);
        btnUploadSong = (MaterialButton) findViewById(R.id.btnUploadSong);
        audioNameTxt = (TextView) findViewById(R.id.audioNameTxt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        listAlbum = (RecyclerView) findViewById(R.id.listAlbum);
        listCategory = (RecyclerView) findViewById(R.id.listCategory);
    }

    private  void sendNotification(Song song){
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.getAllFollowers((long) user.getId()).enqueue(new Callback<FollowerResponse>() {
            @Override
            public void onResponse(Call<FollowerResponse> call, Response<FollowerResponse> response) {
                FollowerResponse res = response.body();
                assert res != null;
                if(res.getSuccess()) {
                    ids = res.getData();
                    Log.e("NotificationFirebase", ids.toString());
                    if (ids != null && !ids.isEmpty()) {
                        Log.e("NotificationFirebase", ids.toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://music-app-967da-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        Log.e("NotificationFirebase", database.toString());
                        FirebaseNotification notification = new FirebaseNotification();
                        for (int i = 0; i < ids.size(); i++) {
                            int id = Math.toIntExact(ids.get(i));
                            Log.e("NotificationFirebase", String.valueOf(id));
                            DatabaseReference tokenRef = database.getReference("tokenPhone").child(String.valueOf(id));

                            tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String token = (String) snapshot.getValue();
                                    Log.e("NotificationFirebase", token);
                                    notification.sendNotificationToUser( song, token);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            final DatabaseReference countRef = database.getReference("index").child(String.valueOf(id));
                            countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Long notiCount = (Long) snapshot.getValue();
                                    if (notiCount == null) {
                                        notiCount = 0L;
                                    }
                                    String notiCountString = String.valueOf(notiCount);
                                    DatabaseReference notiRef = database.getReference("user");
                                    NotificationFirebase noti = new NotificationFirebase(Math.toIntExact(song.getIdSong()), song.getName(), song.getArtistName(), song.getImage());
                                    notiRef.child(String.valueOf(id)).child("notification").child(notiCountString).setValue(noti);
                                    notiCount++;
                                    countRef.setValue(notiCount);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }

                    }
                }

            }

            @Override
            public void onFailure(Call<FollowerResponse> call, Throwable t) {

            }
        });

    }
}