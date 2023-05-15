package com.example.wishhair.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhair.R;
import com.example.wishhair.faceFunc.FaceResultAdapter;

import java.util.ArrayList;

public class HomeRecommendAdapter extends RecyclerView.Adapter<HomeRecommendAdapter.RecViewHolder>{
    private final ArrayList<HomeItems> items;
    private final Context context;

    public HomeRecommendAdapter(ArrayList<HomeItems> items, Context context) {
        this.items = items;
        this.context = context;
    }
    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private HomeRecommendAdapter.OnItemClickListener listener = null;
    public void setOnItemClickListener(HomeRecommendAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_reco_style, parent, false);
        return new RecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecViewHolder holder, int position) {
        HomeItems item = items.get(position);

        holder.bindHairImage(item.getHairImage());
        holder.hairStyle.setText(item.getHairStyle());
        holder.btn_view.setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                if (listener != null) {
                    listener.onItemClick(view, position1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecViewHolder extends RecyclerView.ViewHolder {
        private final ImageView hairImage;
        private final TextView hairStyle;
        private final Button btn_view;
        public RecViewHolder(@NonNull View itemView) {
            super(itemView);

            this.hairImage = itemView.findViewById(R.id.home_item_rec_hairImage);
            this.hairStyle = itemView.findViewById(R.id.home_item_rec_hairStyle);
            this.btn_view = itemView.findViewById(R.id.home_item_rec_btn_view);
        }
        public void bindHairImage(String imageURL) {
            Glide.with(context).load(imageURL).into(hairImage);
        }
    }

}
