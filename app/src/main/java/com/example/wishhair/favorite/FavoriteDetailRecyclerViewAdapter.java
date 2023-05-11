package com.example.wishhair.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishhair.R;

import java.util.ArrayList;

public class FavoriteDetailRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteDetailRecyclerViewAdapter.ViewHolder> {
    private ArrayList<FavoriteDetailRecyclerViewItem> items = new ArrayList<FavoriteDetailRecyclerViewItem>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewNickname, reviewHeartCount, reviewGrade;
        public ImageView reviewImage;
        public int reviewStyleId;

        ViewHolder(View view) {
            super(view);

            reviewImage = view.findViewById(R.id.detail_recyclerview_imageview);
            reviewNickname = view.findViewById(R.id.detail_recyclerview_nickname);
            reviewHeartCount = view.findViewById(R.id.detail_recyclerview_hearcount);
            reviewGrade = view.findViewById(R.id.detail_recyclerview_grade);

        }
    }

    @NonNull
    @Override
    public FavoriteDetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.favorite_detail_recyclerview_item, parent, false);

        return new FavoriteDetailRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteDetailRecyclerViewItem item = items.get(position);
        holder.reviewNickname.setText(item.getStyleReviewNickname());
        holder.reviewHeartCount.setText(item.getStyleReviewHeartCount());
        holder.reviewGrade.setText(item.getStyleReviewGrade());
//        holder.reviewImage.setImage

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FavoriteDetailRecyclerViewItem e) {
        items.add(e);

    }



}
