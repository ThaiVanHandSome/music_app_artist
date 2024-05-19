package com.example.music_app_artist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.models.Song;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class SongChooseAdapter extends RecyclerView.Adapter<SongChooseAdapter.SongChooseViewHolder> {
    private final Context context;
    private final List<Song> songs;

    private final SongChooseAdapter.OnItemClickListener listener;

    private final List<Long> selectedSongs = new ArrayList<>();

    public List<Long> getSelectedSongs() {
        return selectedSongs;
    }

    public SongChooseAdapter(Context context, List<Song> songs, SongChooseAdapter.OnItemClickListener listener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_choose, parent, false);
        return new SongChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongChooseViewHolder holder, int position) {
        Song song = songs.get(position);
        Glide.with(context)
                .load(song.getImage())
                .into(holder.songImage);
        holder.songTitle.setText(song.getName());
        holder.checkboxBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(selectedSongs.contains(song.getIdSong())) {
                    selectedSongs.remove(song.getIdSong());
                } else {
                    selectedSongs.add(song.getIdSong());
                }
                System.out.println(selectedSongs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public static class SongChooseViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle;
        MaterialCheckBox checkboxBtn;
        public SongChooseViewHolder(View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.imv_song_image);
            songTitle = itemView.findViewById(R.id.tv_song_title);
            checkboxBtn = itemView.findViewById(R.id.checkboxBtn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Song song);
    }
}
