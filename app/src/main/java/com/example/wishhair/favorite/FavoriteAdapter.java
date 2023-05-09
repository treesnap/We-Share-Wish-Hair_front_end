package com.example.wishhair.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishhair.MyPage.items.PointItem;
import com.example.wishhair.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private ArrayList<FavoriteItem> FavoriteItems = new ArrayList<FavoriteItem>();

    public interface OnItemClickListener {
        void onItemClicked(int position, int id, String stylename, String[] tags);
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FavoriteStyleName, FavoriteHeartCount;
        public ImageView FavoriteStyleImage;
        public int FavoriteStyleId;
        public String[] FavoriteHashtags;
        // 이미지도 넣어놔야될라나

        public TextView getFavoriteStyleName() {
            return FavoriteStyleName;
        }

        public int getFavoriteStyleId() {
            return FavoriteStyleId;
        }

        public void setFavoriteStyleId(int favoriteStyleId) {
            FavoriteStyleId = favoriteStyleId;
        }

        public String[] getFavoriteHashtags() {
            return FavoriteHashtags;
        }

        ViewHolder(View view) {
            super(view);

            FavoriteStyleImage = view.findViewById(R.id.favorite_style_image);
            FavoriteStyleName = view.findViewById(R.id.favorite_style_name);
            FavoriteHeartCount = view.findViewById(R.id.favorite_heart_count);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = -1;
                    String stylename = "";
                    String[] tags = {};
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        id = getFavoriteStyleId();
                        stylename = getFavoriteStyleName().getText().toString();
                        tags = getFavoriteHashtags();
                    }
                    itemClickListener.onItemClicked(pos, id, stylename, tags);
                }
            });
        }
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.favorite_item, parent, false);

        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = FavoriteItems.get(position);
        holder.FavoriteStyleName.setText(item.getFavoriteStyleName());
        holder.FavoriteStyleId = item.getFavoriteStyleId();
//        holder.FavoriteHashtags = item.getFavoriteHashtags();
//        holder.FavoriteHeartCount.setText(item.getFavoriteHeartcount());

    }

    @Override
    public int getItemCount() {
        return FavoriteItems.size();
    }

    public void addItem(FavoriteItem e) {
        FavoriteItems.add(e);

    }



}
