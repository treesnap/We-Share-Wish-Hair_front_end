package com.example.wishhair.favorite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhair.MyPage.adapters.MyPageRecyclerViewAdapter;
import com.example.wishhair.R;

import java.util.ArrayList;

public class FavoriteDetailRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteDetailRecyclerViewAdapter.ViewHolder> {
    private ArrayList<FavoriteDetailRecyclerViewItem> items = new ArrayList<FavoriteDetailRecyclerViewItem>();
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private FavoriteDetailRecyclerViewAdapter.OnItemClickListener detail_review_listener;
    public void setOnItemClickListener(FavoriteDetailRecyclerViewAdapter.OnItemClickListener listener) {
        this.detail_review_listener = listener;
    }
    public FavoriteDetailRecyclerViewAdapter(Context context, ArrayList<FavoriteDetailRecyclerViewItem> favoriteDetailRecyclerViewItems) {
        this.context = context;
        this.items = favoriteDetailRecyclerViewItems;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewNickname, reviewHeartCount, reviewGrade;
        private ImageView reviewImage;
        private int reviewStyleId;

        ViewHolder(View view) {
            super(view);

            reviewImage = view.findViewById(R.id.detail_recyclerview_imageview);
            reviewNickname = view.findViewById(R.id.detail_recyclerview_nickname);
            reviewHeartCount = view.findViewById(R.id.detail_recyclerview_heartcount);
            reviewGrade = view.findViewById(R.id.detail_recyclerview_grade);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        detail_review_listener.onItemClick(view, pos);
                    }
                }
            });
        }

        public ImageView getReviewImage() {
            return reviewImage;
        }

        public void setReviewImage(ImageView reviewImage) {
            this.reviewImage = reviewImage;
        }

        public void bindContentImage(String imageUrl) {
            Glide.with(context).load(imageUrl).into(reviewImage);
        };
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

        if (item.getStyleReviewPicture() != null) {
            holder.bindContentImage(item.getStyleReviewPicture());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FavoriteDetailRecyclerViewItem e) {
        items.add(e);
    }
}
