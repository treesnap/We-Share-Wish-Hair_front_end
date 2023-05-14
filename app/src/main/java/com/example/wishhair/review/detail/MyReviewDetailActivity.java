package com.example.wishhair.review.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.wishhair.R;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.ReviewModifyActivity;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class MyReviewDetailActivity extends AppCompatActivity {

    private TextView tv_hairStyleName, tv_tags, tv_score, tv_likes, tv_date, tv_content;


    private int reviewId, likes;
    private boolean isWriter;
    private String hairStyleName, score, date, content;
    private ArrayList<String> hashTags, imageUrls;

    private ReviewItem myReviewDetailItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_detail_activity_my);

        Button btn_back = findViewById(R.id.toolbar_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        init
        getIntentData();
        initContents();

    }

    private void initContents() {
        //        image
        ViewPager2 sliderViewPager = findViewById(R.id.review_detail_my_viewPager);
        sliderViewPager.setOffscreenPageLimit(1);
                sliderViewPager.setAdapter(new ImageSliderAdapter(this, imageUrls));

        CircleIndicator3 circleIndicator = findViewById(R.id.review_detail_my_indicator);
        circleIndicator.setViewPager(sliderViewPager);

        tv_hairStyleName = findViewById(R.id.review_detail_my_hairStyleName);
        tv_hairStyleName.setText(hairStyleName);

        tv_tags = findViewById(R.id.review_detail_my_tags);
        StringBuilder stringTags = new StringBuilder();
        for (int i = 0; i < hashTags.size(); i++) {
            stringTags.append("#").append(hashTags.get(i)).append(" ");
        }
        tv_tags.setText(stringTags);

        tv_score = findViewById(R.id.review_detail_my_tv_score);
        tv_score.setText(score);

        tv_likes = findViewById(R.id.review_detail_my_tv_likes);
        tv_likes.setText(String.valueOf(likes));

        tv_date = findViewById(R.id.review_detail_my_tv_date);
        tv_date.setText(date);

        tv_content = findViewById(R.id.review_detail_my_tv_content);
        tv_content.setText(content);
    }

    private void getIntentData() {
        reviewId = getIntent().getIntExtra("reviewId", 0);
        hairStyleName = getIntent().getStringExtra("hairStyleName");
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        hashTags = getIntent().getStringArrayListExtra("tags");
        content = getIntent().getStringExtra("content");
        score = getIntent().getStringExtra("score");
        likes = getIntent().getIntExtra("likes", 0);
        date = getIntent().getStringExtra("date");
        isWriter = getIntent().getBooleanExtra("isWriter", false);

        myReviewDetailItem = new ReviewItem(reviewId, imageUrls, hairStyleName, hashTags, content, score, likes, date, isWriter);
    }

    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        MenuInflater inflater = menu.getMenuInflater();
        menu.setOnMenuItemClickListener(this::onMenuItemClick);
        inflater.inflate(R.menu.menu_review_detail, menu.getMenu());
        menu.show();
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_modify:
                Intent intent = new Intent(MyReviewDetailActivity.this, ReviewModifyActivity.class);
                intent.putExtra("reviewItem", myReviewDetailItem);
                startActivity(intent);
                finish();
                return true;
            case R.id.menu_detail_delete:
                Log.d("menu selectd", "delete");
                return true;
            default:
                return false;
        }
    }
}
