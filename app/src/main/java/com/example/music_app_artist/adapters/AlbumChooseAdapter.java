package com.example.music_app_artist.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.models.Album;

import java.util.List;

public class AlbumChooseAdapter extends RecyclerView.Adapter<AlbumChooseAdapter.AlbumViewHolder>  {
    private final Context context;
    private final List<Album> albums;
    private int checkedPosition = -1;
    private int checkedIdAlbum = -1;

    public int getCheckedIdAlbum() {
        return this.checkedIdAlbum;
    }

    private final AlbumChooseAdapter.OnItemClickListener listener;

    public AlbumChooseAdapter(Context context, List<Album> albums, AlbumChooseAdapter.OnItemClickListener listener) {
        this.context = context;
        this.albums = albums;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumChooseAdapter.AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_album, parent, false);
        return new AlbumChooseAdapter.AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumChooseAdapter.AlbumViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Album album = albums.get(position);
        Glide.with(context)
                .load(album.getImage())
                .into(holder.albumImage);
        holder.albumName.setText(album.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(album);
                }
            }
        });
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkedPosition != position) {
                    if (checkedPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(checkedPosition);
                    }
                    checkedPosition = position;
                    notifyItemChanged(checkedPosition);
                }
            }
        });
        holder.radioButton.setChecked(checkedPosition == position);
        if(checkedPosition == position) {
            checkedIdAlbum = Math.toIntExact(album.getIdAlbum());
        }
    }

    @Override
    public int getItemCount() {
        return albums == null ? 0 : albums.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName;
        RadioButton radioButton;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.imv_album_image);
            albumName = itemView.findViewById(R.id.tv_album_title);
            radioButton = itemView.findViewById(R.id.radioBtn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Album albums);
    }
}
