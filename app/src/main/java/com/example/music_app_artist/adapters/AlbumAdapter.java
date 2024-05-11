package com.example.music_app_artist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.models.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private final Context context;
    private final List<Album> albums;

    private final OnItemClickListener listener;

    public AlbumAdapter(Context context, List<Album> albums, OnItemClickListener listener) {
        this.context = context;
        this.albums = albums;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        Glide.with(context)
                .load(album.getImage())
                .into(holder.albumImage);
        holder.albumName.setText(album.getName());
        holder.albumSongCount.setText(context.getString(R.string.label_songs, album.getCntSong()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(album);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }

        public static class AlbumViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName;
        TextView albumSongCount;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.imv_user_album);
            albumName = itemView.findViewById(R.id.tv_user_album);
            albumSongCount = itemView.findViewById(R.id.tv_song_count);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Album albums);
    }
}
