package com.example.music_app_artist.decorations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.music_app_artist.R;

public class GradientHelper {
    public static void applyGradient(Context context, View view, String imageUrl, int defaultColor) {
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.background_gradient);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                int mutedColor = palette.getMutedColor(context.getResources().getColor(defaultColor));
                                gradientDrawable.setColors(new int[]{context.getResources().getColor(R.color.neutral0), mutedColor});
                                view.setBackground(gradientDrawable);
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
