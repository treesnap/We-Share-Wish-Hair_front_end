package com.example.wishhair.review.recent;

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
import com.example.wishhair.review.ReviewItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private final ArrayList<ReviewItem> reviewItems;
    private final Context context;

    public RecentAdapter(ArrayList<ReviewItem> reviewItems, Context context) {
        this.reviewItems = reviewItems;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.review_recycler_item_recent, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewItem item = reviewItems.get(position);

        if (item.getImageUrls().size() > 0) {
            holder.bindContentImage(item.getImageUrls().get(0));
        }

        holder.hairStyleName.setText(item.getHairStyleName());
        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < item.getTags().size(); i++) {
            tags.append("#").append(item.getTags().get(i)).append(" ");
        }
        holder.tags.setText(tags);
        holder.nickname.setText(item.getUserNickName());
        holder.grade.setText(item.getScore());
        holder.date.setText(parseDate(item.getCreatedDate()));
        if (item.getIsHeart()){
            holder.isHeart.setImageResource(R.drawable.heart_fill);
        } else {
            holder.isHeart.setImageResource(R.drawable.heart_empty);
        }
        holder.likes.setText(String.valueOf(item.getLikes()));
        holder.viewContent.setOnClickListener(view -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                if (mListener != null) {
                    mListener.onItemClick(view, position1);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView contentImage, isHeart;
        TextView nickname, hairStyleName, tags,grade, date, likes;
        Button viewContent;

        ViewHolder(View itemView) {
            super(itemView);
            this.contentImage = itemView.findViewById(R.id.review_recent_contentImage);
            this.nickname = itemView.findViewById(R.id.review_recent_tv_nickname);
            this.hairStyleName = itemView.findViewById(R.id.review_recent_tv_hairStyleName);
            this.tags = itemView.findViewById(R.id.review_recent_tv_tags);
            this.grade = itemView.findViewById(R.id.review_recent_tv_grade);
            this.isHeart = itemView.findViewById(R.id.review_recent_imageView_isHeart);
            this.likes = itemView.findViewById(R.id.review_recent_tv_likes);
            this.date = itemView.findViewById(R.id.review_recent_tv_date);
            this.viewContent = itemView.findViewById(R.id.review_recent_btn_viewContent);
        }

        public void bindContentImage(String imageUrl) {
            Glide.with(context).load(imageUrl).into(contentImage);
        }

    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }

    private String parseDate(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "failParseDate";
    }
}
