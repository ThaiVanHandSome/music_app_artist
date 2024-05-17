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

import com.example.music_app_artist.R;
import com.example.music_app_artist.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private final List<Category> categories;
    private final OnItemClickListener listener;
    private int checkedPosition = 1;
    private int checkedIdCategory = 1;

    public int getCheckedIdCategory() {
        return checkedIdCategory;
    }

    public CategoryAdapter(Context context, List<Category> categories, OnItemClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(category);
                }
            }
        });
        holder.radioBtn.setOnClickListener(new View.OnClickListener() {
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
        holder.radioBtn.setChecked(checkedPosition == position);
        if(checkedPosition == position) {
            checkedIdCategory = Math.toIntExact(category.getIdSongCategory());
        }
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        RadioButton radioBtn;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            radioBtn = itemView.findViewById(R.id.radio_btn);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}
