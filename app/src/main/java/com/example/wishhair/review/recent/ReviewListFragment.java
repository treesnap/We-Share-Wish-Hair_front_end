package com.example.wishhair.review.recent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.detail.RecentReviewDetailActivity;
import com.example.wishhair.review.write.WriteReviewActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ReviewListFragment extends Fragment {

    public ReviewListFragment() {}

    private ArrayList<ReviewItem> recentReviewItems;
    private RadioGroup filter;
    private RadioButton filter_whole, filter_man, filter_woman;
    private RecyclerView recentRecyclerView;
    private RecentAdapter recentAdapter;

    //    sort
    private static String sort_selected = null;
    private static final String[] sortItems = {"최신 순", "오래된 순", "좋아요 순"};

//    request
    private String accessToken;

    @Override
    public void onResume() {
        super.onResume();
        //       request List data
        reviewListRequest(accessToken);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_fragment_recent, container, false);

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(requireActivity());
        accessToken = customTokenHandler.getAccessToken();

//       write
        Button btn_write = v.findViewById(R.id.review_fragment_btn_write);
        btn_write.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), WriteReviewActivity.class);
            startActivity(intent);
        });

//        review list
        recentReviewItems = new ArrayList<>();
        recentAdapter = new RecentAdapter(recentReviewItems, getContext());
        recentRecyclerView = v.findViewById(R.id.review_recent_recyclerView);
        recentRecyclerView.setAdapter(recentAdapter);

//        layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recentRecyclerView.setLayoutManager(layoutManager);

        //        decorator
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        recentRecyclerView.addItemDecoration(dividerItemDecoration);

        //        swipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.review_recent_swipeRefLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            reviewListRequest(accessToken);
            final Handler handler = new Handler();
            handler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 500);
        });

        recentAdapter.setOnItemClickListener((v1, position) -> {
            Intent intent = new Intent(v1.getContext(), RecentReviewDetailActivity.class);
            ReviewItem selectedItem = recentReviewItems.get(position);
            intent.putExtra("reviewId", selectedItem.getReviewId());
            intent.putExtra("userNickname", selectedItem.getUserNickName());
            intent.putExtra("hairStyleName", selectedItem.getHairStyleName());
            intent.putStringArrayListExtra("tags", selectedItem.getTags());
            intent.putExtra("score", selectedItem.getScore());
            intent.putExtra("likes", selectedItem.getLikes());
            intent.putExtra("date", selectedItem.getCreatedDate());
            intent.putExtra("content", selectedItem.getContent());
            intent.putStringArrayListExtra("imageUrls", selectedItem.getImageUrls());
            ReviewListFragment.this.startActivity(intent);
        });

//        sort
        filter = v.findViewById(R.id.review_fragment_filter_radioGroup);
        filter_whole = v.findViewById(R.id.review_fragment_filter_whole);
        filter_man = v.findViewById(R.id.review_fragment_filter_man);
        filter_woman = v.findViewById(R.id.review_fragment_filter_woman);

        Spinner spinner_sort = v.findViewById(R.id.review_fragment_spinner_sort);
        sort_select(spinner_sort);

        return v;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void reviewListRequest(String accessToken) {
        final String URL_reviewList = UrlConst.URL + "/api/review";
        List<ReviewItem> requestItems = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_reviewList, null, response -> {
            Log.d("reviewListRequest", response.toString());
//                parse received data
            String stringResponse = String.valueOf(response);
            try {
                JSONObject jsonObject = new JSONObject(stringResponse);
                JSONArray resultArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    ReviewItem receivedData = new ReviewItem();
                    JSONObject resultObject = resultArray.getJSONObject(i);
//                        Log.d("resultObject", resultObject.toString());
                    int reviewId = resultObject.getInt("reviewId");
                    String userNickName = resultObject.getString("userNickname");
                    String score = resultObject.getString("score");
                    int likes = resultObject.getInt("likes");
                    String content = resultObject.getString("contents");
                    String createDate = resultObject.getString("createdDate");
                    String hairStyleName = resultObject.getString("hairStyleName");

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

                recentReviewItems.clear();
                recentReviewItems.addAll(requestItems);
                recentAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e("reviewList error", error.toString())) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sort_select(Spinner spinner_sort) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sortItems);
        spinner_sort.setAdapter(spinnerAdapter);

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                sort_selected = sortItems[position];
                if (sort_selected.equals(sortItems[0])) { //최신순 정렬
                    sort_latest();
                } else if (sort_selected.equals(sortItems[1])) { //오래된 순 정렬
                    sort_old();
                } else if (sort_selected.equals(sortItems[2])) { // 좋아요 순 정렬
//                    TODO : 좋아요 데이터가 없어서 테스트 못함
                    Comparator<ReviewItem> likeDesc = (item1, item2) -> (item2.getLikes() - item1.getLikes());
                    Collections.sort(recentReviewItems, likeDesc);
                }
                recentAdapter.notifyDataSetChanged();
            }

            private void sort_old() {
                Collections.sort(recentReviewItems, new Comparator<>() {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                    @Override
                    public int compare(ReviewItem item1, ReviewItem item2) {
                        try {
                            Date date1 = dateFormat.parse(item1.getCreatedDate());
                            Date date2 = dateFormat.parse(item2.getCreatedDate());
                            return Objects.requireNonNull(date1).compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
            }

            private void sort_latest() {
                Collections.sort(recentReviewItems, new Comparator<>() {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                    @Override
                    public int compare(ReviewItem item1, ReviewItem item2) {
                        try {
                            Date date1 = dateFormat.parse(item1.getCreatedDate());
                            Date date2 = dateFormat.parse(item2.getCreatedDate());
                            return Objects.requireNonNull(date2).compareTo(date1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }
}
