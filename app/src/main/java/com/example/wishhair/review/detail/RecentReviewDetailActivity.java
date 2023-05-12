package com.example.wishhair.review.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class RecentReviewDetailActivity extends AppCompatActivity {

    private int reviewId;
    private boolean isLike;

    private Button btn_back;
    private ImageView btn_like;

    //    content
    private TextView userNickname, hairStyleName, tags, score, likes, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_detail_activity_recent);

        btn_like = findViewById(R.id.review_detail_like);
        btn_like.setOnClickListener(view -> {
            isLike = !isLike;
            setLikeStatus();
        });

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();

        isLikeRequest(accessToken);

        btn_back = findViewById(R.id.toolbar_btn_back);
        btn_back.setOnClickListener(view -> finish());
        TextView title = findViewById(R.id.toolbar_textView_title);
        title.setText("");

        ViewPager2 sliderViewPager = findViewById(R.id.review_detail_viewPager);
        CircleIndicator3 circleIndicator = findViewById(R.id.review_detail_indicator);

        sliderViewPager.setOffscreenPageLimit(1);

        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, imageUrls));

        circleIndicator.setViewPager(sliderViewPager);

        reviewId = getIntent().getIntExtra("reviewId", 0);

//        content
        userNickname = findViewById(R.id.review_detail_userNickname);
        userNickname.setText(getIntent().getStringExtra("userNickname"));

        hairStyleName = findViewById(R.id.review_detail_hairStyleName);
        hairStyleName.setText(getIntent().getStringExtra("hairStyleName"));

        tags = findViewById(R.id.review_detail_tags);
        ArrayList<String> hashTags = getIntent().getStringArrayListExtra("tags");
        StringBuilder stringTags = new StringBuilder();
        for (int i = 0; i < hashTags.size(); i++) {
            stringTags.append("#").append(hashTags.get(i)).append(" ");
        }
        tags.setText(stringTags);

        score = findViewById(R.id.review_detail_tv_score);
        score.setText(getIntent().getStringExtra("score"));

        likes = findViewById(R.id.review_detail_tv_likes);
        likes.setText(String.valueOf(getIntent().getIntExtra("likes", 0)));

        date = findViewById(R.id.review_detail_tv_date);
        date.setText(getIntent().getStringExtra("date"));

        content = findViewById(R.id.review_detail_tv_content);
        content.setText(getIntent().getStringExtra("content"));
        }

    private void isLikeRequest(String accessToken) {
        String likeUrl = UrlConst.URL + "/api/review/like/" + reviewId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, likeUrl,null, response -> {
            try {
                isLike = response.getBoolean("isLiking");
                setLikeStatus();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("recentIsLikeError", error.toString())) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void setLikeStatus() {
        btn_like.setImageResource(isLike ? R.drawable.heart_fill : R.drawable.heart_empty);
    }

}
