package com.example.wishhair.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhair.MyPage.items.PointItem;
import com.example.wishhair.R;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private ArrayList<FavoriteItem> FavoriteItems = new ArrayList<FavoriteItem>();

    Context context;
    public interface OnItemClickListener {
        void onItemClicked(int position, int id, String stylename, ArrayList<String> tags, ArrayList<String> arrayList);
    }
    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FavoriteStyleName;
        public ImageView FavoriteStyleImage;
        public int FavoriteStyleId;
        public ArrayList<String> FavoriteHashtags;
        public ArrayList<String> FavoriteStyleImageUrls;

        public TextView getFavoriteStyleName() {
            return FavoriteStyleName;
        }

        public int getFavoriteStyleId() {
            return FavoriteStyleId;
        }

        public void setFavoriteStyleId(int favoriteStyleId) {
            FavoriteStyleId = favoriteStyleId;
        }

        public ArrayList<String> getFavoriteHashtags() {
            return FavoriteHashtags;
        }

        public void setFavoriteHashtags(ArrayList<String> favoriteHashtags) {
            FavoriteHashtags = favoriteHashtags;
        }

        public ArrayList<String> getFavoriteStyleImageUrls() {
            return FavoriteStyleImageUrls;
        }

        public void setFavoriteStyleImageUrls(ArrayList<String> favoriteStyleImageUrls) {
            FavoriteStyleImageUrls = favoriteStyleImageUrls;
        }

        ViewHolder(View view) {
            super(view);

            FavoriteStyleImage = view.findViewById(R.id.favorite_style_image);
            FavoriteStyleName = view.findViewById(R.id.favorite_style_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = -1;
                    String stylename = "";
                    ArrayList<String> tags = new ArrayList<>();
                    ArrayList<String> arrayList = new ArrayList<>();
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        id = getFavoriteStyleId();
                        stylename = getFavoriteStyleName().getText().toString();
                        tags = getFavoriteHashtags();
                        arrayList = getFavoriteStyleImageUrls();
                    }
                    itemClickListener.onItemClicked(pos, id, stylename, tags, arrayList);
                }
            });
        }
        public void bindContentImage(String imageUrl) {
            Glide.with(context).load(imageUrl).into(FavoriteStyleImage);
        };
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
        holder.FavoriteStyleImageUrls = item.getFavoritePictureUrls();
        holder.FavoriteHashtags = item.getFavoriteHashtags();
        if (item.getFavoritePictureUrls() != null) {
            holder.bindContentImage(item.getFavoritePictureUrls().get(0));
        }
    }

    @Override
    public int getItemCount() {
        return FavoriteItems.size();
    }

    public void addItem(FavoriteItem e) {
        FavoriteItems.add(e);
    }
}
