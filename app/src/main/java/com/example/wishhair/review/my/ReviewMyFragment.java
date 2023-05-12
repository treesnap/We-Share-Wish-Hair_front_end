package com.example.wishhair.review.my;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.review.detail.MyReviewDetailActivity;
import com.example.wishhair.review.detail.RecentReviewDetailActivity;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewMyFragment extends Fragment {

    public ReviewMyFragment() {}


    private ArrayList<ReviewItem> myReviewItems;
    private String accessToken;
    private MyReviewAdapter myReviewAdapter;

    @Override
    public void onResume() {
        super.onResume();
        myReviewRequest(accessToken);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_fragment_my, container, false);

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(requireActivity());
        accessToken = customTokenHandler.getAccessToken();

        RecyclerView myRecyclerView = v.findViewById(R.id.review_my_recyclerView);
        myReviewItems = new ArrayList<>();

        myReviewAdapter = new MyReviewAdapter(myReviewItems, requireContext());
        myRecyclerView.setAdapter(myReviewAdapter);

//        layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        myRecyclerView.setLayoutManager(layoutManager);

//        decorator
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        myRecyclerView.addItemDecoration(dividerItemDecoration);

        myReviewAdapter.setOnItemClickListener((v1, position) -> {
            Intent intent = new Intent(v1.getContext(), MyReviewDetailActivity.class);
            ReviewItem selectedItem = myReviewItems.get(position);
            intent.putExtra("reviewId", selectedItem.getReviewId());
            intent.putExtra("hairStyleName", selectedItem.getHairStyleName());
            intent.putStringArrayListExtra("tags", selectedItem.getTags());
            intent.putExtra("score", selectedItem.getScore());
            intent.putExtra("likes", selectedItem.getLikes());
            intent.putExtra("date", selectedItem.getCreatedDate());
            intent.putExtra("content", selectedItem.getContent());
            intent.putStringArrayListExtra("imageUrls", selectedItem.getImageUrls());
            intent.putExtra("isWriter", selectedItem.isWriter());
            startActivity(intent);
        });

        return v;
    }

    private void myReviewRequest(String accessToken) {
        String myReviewUrl = UrlConst.URL + "/api/review/my";
        List<ReviewItem> requestItems = new ArrayList<>();
        @SuppressLint("NotifyDataSetChanged") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, myReviewUrl, null, response -> {
            Log.d("myReviewRequest", response.toString());
            String stringResponse = String.valueOf(response);
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                JSONArray resultArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    ReviewItem receivedData = new ReviewItem();
                    JSONObject resultObject = resultArray.getJSONObject(i);

                    int reviewId = resultObject.getInt("reviewId");
                    String hairStyleName = resultObject.getString("hairStyleName");
                    String userNickName = resultObject.getString("userNickname");
                    String score = resultObject.getString("score");
                    String content = resultObject.getString("contents");
                    String createDate = resultObject.getString("createdDate");
                    int likes = resultObject.getInt("likes");
                    boolean isWriter = resultObject.getBoolean("writer");

                    JSONArray hashTagsArray = resultObject.getJSONArray("hashTags");
                    ArrayList<String> tags = new ArrayList<>();
                    for (int j = 0; j < hashTagsArray.length(); j++) {
                        JSONObject hasTagObject = hashTagsArray.getJSONObject(j);
                        tags.add(hasTagObject.getString("tag"));
                    }
                    receivedData.setTags(tags);

                    receivedData.setReviewId(reviewId);
                    receivedData.setUserNickName(userNickName);
                    receivedData.setScore(score);
                    receivedData.setLikes(likes);
                    receivedData.setCreatedDate(createDate);
                    receivedData.setHairStyleName(hairStyleName);
                    receivedData.setContent(content);
                    receivedData.setIsWriter(isWriter);

                    JSONArray photosArray = resultObject.getJSONArray("photos");
                    ArrayList<String> receivedUrls = new ArrayList<>();
                    for (int j = 0; j < photosArray.length(); j++) {
                        JSONObject photoObject = photosArray.getJSONObject(j);
                        String imageUrl = photoObject.getString("storeUrl");

                        receivedUrls.add(imageUrl);
                    }
                    receivedData.setImageUrls(receivedUrls);

                    requestItems.add(receivedData);
                }

                JSONObject pagingObject = jsonObject.getJSONObject("paging");
                String contentSize = pagingObject.getString("contentSize");
                String page = pagingObject.getString("page");
                String hasNext = pagingObject.getString("hasNext");
                Log.d("paging", contentSize + " " + page + " " + hasNext);

                myReviewItems.clear();
                myReviewItems.addAll(requestItems);
                myReviewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e("myReviewRequestError", error.toString())) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
