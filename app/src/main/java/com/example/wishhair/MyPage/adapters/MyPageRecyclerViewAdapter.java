package com.example.wishhair.MyPage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhair.MyPage.items.HeartListItem;
import com.example.wishhair.R;

import java.util.ArrayList;

public class MyPageRecyclerViewAdapter extends RecyclerView.Adapter<MyPageRecyclerViewAdapter.ViewHolder>{
    private ArrayList<HeartListItem> heartlistItems = new ArrayList<HeartListItem>();
    Context context;
    public MyPageRecyclerViewAdapter(Context context) {
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView HeartListPicture;
        private TextView HeartListGrade, HeartListHeartCount, HeartListUserName;

        ViewHolder(View view) {
            super(view);

            HeartListPicture = view.findViewById(R.id.heartlist_my_imageView_picture);
            HeartListGrade = view.findViewById(R.id.heartlist_my_tv_grade);
            HeartListHeartCount = view.findViewById(R.id.heartlist_my_tv_heartCount);
            HeartListUserName = view.findViewById(R.id.heartlist_username);
        }
        public void bindContentImage(String imageUrl) {
            Glide.with(context).load(imageUrl).into(HeartListPicture);
        };
    }

    @NonNull
    @Override
    public MyPageRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.heartlist_item, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HeartListItem item = heartlistItems.get(position);
        holder.HeartListUserName.setText(item.getHeartListReviewerNickname()+"님");
        holder.HeartListGrade.setText(item.getHeartListGrade());
        holder.HeartListHeartCount.setText(item.getHeartListHeartCount());
        if (item.getHeartListPicture() != null) {
            holder.bindContentImage(item.getHeartListPicture());
        }
    }

    @Override
    public int getItemCount() {
        return heartlistItems.size();
    }
    public void addItem(HeartListItem e) {
        heartlistItems.add(e);
    }
    public void setItems(ArrayList<HeartListItem> items) {
        this.heartlistItems = items;
    }
}

