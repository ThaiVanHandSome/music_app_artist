package com.example.music_app_artist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.music_app_artist.R;
import com.example.music_app_artist.models.SongComment;
import com.example.music_app_artist.utils.Util;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongCommentAdapter extends RecyclerView.Adapter<SongCommentAdapter.SongCommentViewHolder> {

    private final Context context;

    private List<SongComment> songComments;

    private final OnItemClickListener onItemClickListener;

    public SongCommentAdapter(Context context, List<SongComment> songComments, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.songComments = songComments;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SongCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_comment, parent, false);
        return new SongCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongCommentViewHolder holder, int position) {
        SongComment songComment = songComments.get(position);
        if(songComment == null) return;
        holder.nameTxt.setText(songComment.getUser().getFirstName() + " " + songComment.getUser().getLastName());
        Glide.with(context).load(songComment.getUser().getAvatar()).into(holder.avatarImageView);
        holder.contentTxt.setText(songComment.getContent());
        holder.likeCntTxt.setText(String.valueOf(songComment.getLikes()));
        holder.timeTxt.setText(Util.covertToDate(songComment.getDayCommented()));

    }

    @Override
    public int getItemCount() {
        return songComments == null ? 0 : songComments.size();
    }

    public class SongCommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarImageView;
        TextView nameTxt, contentTxt, likeCntTxt, timeTxt;

        public SongCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            likeCntTxt = itemView.findViewById(R.id.likeCntTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
        }

    }

    public interface OnItemClickListener {
    }
}
