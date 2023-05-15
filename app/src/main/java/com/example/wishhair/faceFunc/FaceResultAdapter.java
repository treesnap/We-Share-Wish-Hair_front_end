package com.example.wishhair.faceFunc;

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
import com.example.wishhair.home.HomeItems;

import java.util.ArrayList;

public class FaceResultAdapter extends RecyclerView.Adapter<FaceResultAdapter.ViewHolder> {
//    homeItem 과 형식이 같아 재사용
    private final ArrayList<HomeItems> faceResultItems;
    private final Context context;

    public FaceResultAdapter(ArrayList<HomeItems> faceResultItems, Context context) {
        this.faceResultItems = faceResultItems;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private FaceResultAdapter.OnItemClickListener listener = null;
    public void setOnItemClickListener(FaceResultAdapter.OnItemClickListener listener) {
        this.listener = listener;
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
        return faceResultItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView hairImage;
        private final TextView hairStyle;
        private final Button btn_view;

        public ViewHolder(@NonNull View itemView) {
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
