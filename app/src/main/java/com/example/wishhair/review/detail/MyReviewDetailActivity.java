package com.example.wishhair.review.detail;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.ReviewModifyActivity;
import com.example.wishhair.sign.UrlConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class MyReviewDetailActivity extends AppCompatActivity {

    private String accessToken;
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

        //        accessToken
        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        accessToken = customTokenHandler.getAccessToken();


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

    private void getIntentData() {
        reviewId = getIntent().getIntExtra("reviewId", 0);
        hairStyleName = getIntent().getStringExtra("hairStyleName");
        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        hashTags = getIntent().getStringArrayListExtra("tags");
        content = getIntent().getStringExtra("content");
        score = getIntent().getStringExtra("score");
        likes = getIntent().getIntExtra("likes", 0);
        date = parseDate(getIntent().getStringExtra("date"));
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("예", (dialogInterface, i) -> {
                            reviewDeleteRequest(accessToken);
                            Handler handler = new Handler();
                            handler.postDelayed(this::finish, 500);
                        })
                        .setNegativeButton("아니요", (dialogInterface, i) -> {}).show();
                return true;
            default:
                return false;
        }
    }

    private void reviewDeleteRequest(String accessToken) {
        String deleteUrl = UrlConst.URL + "/api/review/" + reviewId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                response -> Toast.makeText(this, "리뷰가 삭제되었습니다.", Toast.LENGTH_LONG).show(),
                error -> {
                    String message = GetErrorMessage.getErrorMessage(error);
                    Log.e("validate error message", message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }){ @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
