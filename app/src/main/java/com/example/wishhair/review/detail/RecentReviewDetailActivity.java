package com.example.wishhair.review.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class RecentReviewDetailActivity extends AppCompatActivity {

    private RequestQueue queue;

    private int reviewId;
    private boolean isLike;

    private ImageView btn_like;

    //    content
    private TextView userNickname, hairStyleName, tags, score, likes, date, content;
    private int likeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_detail_activity_recent);
//        title
        Button btn_back = findViewById(R.id.toolbar_btn_back);
        btn_back.setOnClickListener(view -> finish());
        TextView title = findViewById(R.id.toolbar_textView_title);
        title.setText("");

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();

        queue = Volley.newRequestQueue(this);
        reviewId = getIntent().getIntExtra("reviewId", 0);

        btn_like = findViewById(R.id.review_detail_like);
        isLikeRequest(accessToken);
        btn_like.setOnClickListener(view -> {
            if (isLike) {
                likeCancelRequest(accessToken);
            } else {
                likeRequest(accessToken);
            }
            isLike = !isLike;
        });

//        images
        ViewPager2 sliderViewPager = findViewById(R.id.review_detail_viewPager);
        sliderViewPager.setOffscreenPageLimit(1);

        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, imageUrls));

        CircleIndicator3 circleIndicator = findViewById(R.id.review_detail_indicator);
        circleIndicator.setViewPager(sliderViewPager);

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

        likeCount = getIntent().getIntExtra("likes", 0);
        likes = findViewById(R.id.review_detail_tv_likes);
        likes.setText(String.valueOf(likeCount));

        date = findViewById(R.id.review_detail_tv_date);
        date.setText(parseDate(getIntent().getStringExtra("date")));

        content = findViewById(R.id.review_detail_tv_content);
        content.setText(getIntent().getStringExtra("content"));
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

    private void isLikeRequest(String accessToken) {
        String likeUrl = UrlConst.URL + "/api/review/like/" + reviewId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, likeUrl,null, response -> {
            try {
                isLike = response.getBoolean("isLiking");
                btn_like.setImageResource(isLike ? R.drawable.heart_fill : R.drawable.heart_empty);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, this::printErrorMessage) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        queue.add(request);
    }

    private void likeRequest(String accessToken) {
        String likeUrl = UrlConst.URL + "/api/review/like/" + reviewId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, likeUrl, null, response -> {
            btn_like.setImageResource(R.drawable.heart_fill);
            likeCount++;
            likes.setText(String.valueOf(likeCount));
        }, this::printErrorMessage) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    private void likeCancelRequest(String accessToken) {
        String cancelUrl = UrlConst.URL + "/api/review/like/cancel/" + reviewId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, cancelUrl, null, response -> {
            btn_like.setImageResource(R.drawable.heart_empty);
            likeCount--;
            likes.setText(String.valueOf(likeCount));
        }, this::printErrorMessage) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    private void printErrorMessage(VolleyError error) {
        String message = GetErrorMessage.getErrorMessage(error);
        Log.e("validate error message", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
