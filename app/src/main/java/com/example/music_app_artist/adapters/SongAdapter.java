package com.example.music_app_artist.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.activities.SongDetailActivity;
import com.example.music_app_artist.models.Song;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final Context context;
    private final List<Song> songs;

    private final SongAdapter.OnItemClickListener listener;

    public SongAdapter(Context context, List<Song> songs, SongAdapter.OnItemClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, null);
        return new SongViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        Glide.with(context)
                .load(song.getImage())
                .into(holder.songImage);
        holder.songTitle.setText(song.getName());
        holder.songViews.setText(String.valueOf(song.getViews()) + " lượt nghe");
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle;
        TextView songViews;
        MaterialButton songActionButton;
        public SongViewHolder(View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.imv_song_image);
            songTitle = itemView.findViewById(R.id.tv_song_title);
            songViews = itemView.findViewById(R.id.tv_song_views);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song = songs.get(getAdapterPosition());
                    Intent intent = new Intent(context, SongDetailActivity.class);
                    intent.putExtra("idSong", song.getIdSong());
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Song song);
    }
}
