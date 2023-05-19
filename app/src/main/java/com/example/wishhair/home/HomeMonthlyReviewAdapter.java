package com.example.wishhair.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishhair.R;

import java.util.ArrayList;

public class HomeMonthlyReviewAdapter extends RecyclerView.Adapter<HomeMonthlyReviewAdapter.MonthlyViewHolder> {
    private final ArrayList<HomeItems> items;

    public HomeMonthlyReviewAdapter(ArrayList<HomeItems> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private HomeMonthlyReviewAdapter.OnItemClickListener listener = null;
    public void setOnItemClickListener(HomeMonthlyReviewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonthlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_review_monthly, parent, false);

        HomeMonthlyReviewAdapter.MonthlyViewHolder viewHolder = new MonthlyViewHolder(view);

        view.setOnClickListener(view1 -> {
            int position = viewHolder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(view1, position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyViewHolder holder, int position) {
        HomeItems item = items.get(position);

        holder.userName.setText(item.getUsername());
        holder.context_review.setText(item.getContext_review());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MonthlyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, context_review;
        public MonthlyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.userName = itemView.findViewById(R.id.home_item_review_userName);
            this.context_review = itemView.findViewById(R.id.home_item_review_monthly_content);
        }
    }
}
