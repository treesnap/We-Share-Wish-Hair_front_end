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
    private final ArrayList<HeartListItem> heartListItems;
    private final Context context;

    public MyPageRecyclerViewAdapter(Context context, ArrayList<HeartListItem> items) {
        this.context = context;
        this.heartListItems = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private MyPageRecyclerViewAdapter.OnItemClickListener listener = null;
    public void setOnItemClickListener(MyPageRecyclerViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView heartListPicture;
        TextView heartListGrade, heartListHeartCount;
        TextView heartListStyleName;

        ViewHolder(View view) {
            super(view);

            this.heartListPicture = view.findViewById(R.id.heartlist_my_imageView_picture);
            this.heartListGrade = view.findViewById(R.id.heartlist_my_tv_grade);
            this.heartListHeartCount = view.findViewById(R.id.heartlist_my_tv_heartCount);
            this.heartListStyleName = view.findViewById(R.id.heartlist_stylename);
        }
        public void bindContentImage(String imageUrl) {
            Glide.with(context).load(imageUrl).into(heartListPicture);
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.heartlist_item, parent, false);

        MyPageRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(view1 -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(view1, position);
            }
        });

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HeartListItem item = heartListItems.get(position);
        holder.heartListStyleName.setText(item.getHeartListStyleName());
        holder.heartListHeartCount.setText(String.valueOf(item.getHeartListHeartCount()));
        holder.heartListGrade.setText(item.getHeartListGrade());

        if (item.getHeartListPicture() != null) {
            holder.bindContentImage(item.getHeartListPicture());
        }
    }

    @Override
    public int getItemCount() {
        return heartListItems.size();
    }
}
