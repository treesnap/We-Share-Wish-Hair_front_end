package com.example.wishhair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhair.home.HomeItems;

import java.util.ArrayList;

public class TagResultAdapter extends RecyclerView.Adapter<TagResultAdapter.ViewHolder> {
//    homeItem 과 형식이 같아 재사용
    private final ArrayList<HomeItems> faceResultItems;
    private final Context context;

    public TagResultAdapter(ArrayList<HomeItems> faceResultItems, Context context) {
        this.faceResultItems = faceResultItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_reco_style, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeItems item = faceResultItems.get(position);

        holder.bindHairImage(item.getHairImage());
        holder.hairStyle.setText(item.getHairStyle());
        holder.likes.setText(item.getLikes());

        boolean isLike = item.getIsLike();
        if (isLike) {
            holder.like.setImageResource(R.drawable.heart_fill);
        } else {
            holder.like.setImageResource(R.drawable.heart_empty);
        }
        holder.like.setOnClickListener(view -> {
            HomeItems clickItem = faceResultItems.get(position);
            boolean clickLike = clickItem.getIsLike();
            clickItem.setIsLike(!clickLike);

            if (clickItem.getIsLike()) {
                holder.like.setImageResource(R.drawable.heart_fill);
            } else {
                holder.like.setImageResource(R.drawable.heart_empty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faceResultItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView hairImage, like;
        private final TextView hairStyle, likes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.hairImage = itemView.findViewById(R.id.home_item_rec_hairImage);
            this.hairStyle = itemView.findViewById(R.id.home_item_rec_hairStyle);
            this.likes = itemView.findViewById(R.id.home_item_rec_heartCount);
            this.like = itemView.findViewById(R.id.home_item_rec_likeImageView);
        }

        public void bindHairImage(String imageURL) {
            Glide.with(context).load(imageURL).into(hairImage);
        }
    }

}
