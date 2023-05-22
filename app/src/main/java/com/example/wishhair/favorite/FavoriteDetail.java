package com.example.wishhair.favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.MainActivity;
import com.example.wishhair.MyPage.adapters.MyPageRecyclerViewAdapter;
import com.example.wishhair.MyPage.items.HeartListItem;
import com.example.wishhair.R;
import com.example.wishhair.func.TagFunc.TagResultActivity;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.detail.ImageSliderAdapter;
import com.example.wishhair.review.detail.RecentReviewDetailActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class FavoriteDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteDetail() {}

    final static private String url_favorite = UrlConst.URL + "/api/hair_style/wish/";
    final static private String url_search_review = UrlConst.URL + "/api/review/hair_style/";

//    accessToken
    private SharedPreferences loginSP;
    static private String accessToken;

    private MainActivity mainActivity;
    private RequestQueue queue;
    private int styleId;
    private boolean isWishing;

    private ImageButton favoriteBtn;
    private Button backBtn;
    private TextView styleNameTv, hashtags;
    private OnBackPressedCallback callback;

    private ViewPager2 sliderViewPager;
    private CircleIndicator3 circleIndicator;

    private FavoriteDetailRecyclerViewAdapter favoriteDetailRecyclerViewAdapter;
    private ArrayList<FavoriteDetailRecyclerViewItem> favoriteDetailRecyclerViewItems;
    private RecyclerView reviewRecyclerView;

    public ArrayList<String> images = new ArrayList<>(
            Arrays.asList(
                    "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg",
                    "https://cdn.pixabay.com/photo/2020/11/04/15/29/coffee-beans-5712780_1280.jpg",
                    "https://cdn.pixabay.com/photo/2020/03/08/21/41/landscape-4913841_1280.jpg",
                    "https://cdn.pixabay.com/photo/2020/09/02/18/03/girl-5539094_1280.jpg",
                    "https://cdn.pixabay.com/photo/2014/03/03/16/15/mosque-279015_1280.jpg"
            ));

    public static FavoriteDetail newInstance(String param1, String param2) {
        FavoriteDetail fragment = new FavoriteDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (getActivity().getClass().getName().equals(MainActivity.class.getName())) {
            mainActivity = (MainActivity) getActivity();
            callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    mainActivity.onBackPressed();
                }
            };

        } else if (getActivity().getClass().getName().equals(TagResultActivity.class.getName())) {
            callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.tagResult_layout);
                    fm.beginTransaction().remove(fragment).commit();
                    getActivity().findViewById(R.id.tagResult_overlay).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.tagResult_btn_finish).setVisibility(View.VISIBLE);
                }
            };
        } else {
            callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.faceResult_layout);
                    fm.beginTransaction().remove(fragment).commit();
                    getActivity().findViewById(R.id.faceResult_overlay).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.faceResult_btn_finish).setVisibility(View.VISIBLE);
                }
            };
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favorite_detail, container, false);
        sliderViewPager = view.findViewById(R.id.favorite_detail_viewpager);
        circleIndicator = view.findViewById(R.id.favorite_detail_indicator);
        favoriteBtn = view.findViewById(R.id.favorite_detail_heart_button);
        styleNameTv = view.findViewById(R.id.favorite_detail_stylename);
        hashtags = view.findViewById(R.id.favorite_detail_hashtag);
        reviewRecyclerView = view.findViewById(R.id.favorite_detail_review_recyclerview);
        backBtn = view.findViewById(R.id.favorite_detail_back);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(requireContext());

        // data transfer (FavoriteFragment -> FavoriteDetailFragment)
        if (getArguments() != null) {
            styleNameTv.setText(getArguments().getString("hairStylename"));
            ArrayList<String> tags = getArguments().getStringArrayList("tags");
            String tag = "";
            boolean lineFlag = true;
            try {
                for (int i = 0; i < tags.size(); i++) {
                    tag = tag + "#" + tags.get(i) + " ";
//                    tag 30자 초과 시 줄바꿈
                    if (tag.length() > 29 && lineFlag) {
                        lineFlag = false;
                        tag += "\n";
                    }
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }

            hashtags.setText(tag);
            styleId = getArguments().getInt("hairStyleId");
            if (getArguments().getStringArrayList("ImageUrls") != null) {
                images = getArguments().getStringArrayList("ImageUrls");
            }
        }

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        // 찜 여부 확인
        FavoriteCheckRequest(accessToken);

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(getContext(), images));
        circleIndicator.setViewPager(sliderViewPager);

//        review recyclerview
        favoriteDetailRecyclerViewItems = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        reviewRecyclerView.setLayoutManager(layoutManager);

        favoriteDetailRecyclerViewAdapter = new FavoriteDetailRecyclerViewAdapter(getContext(),favoriteDetailRecyclerViewItems);
        favoriteDetailRecyclerViewAdapter.setOnItemClickListener((v, position) -> {
            FavoriteDetailRecyclerViewItem selectedItem = favoriteDetailRecyclerViewItems.get(position);
            recyclerviewRequest(accessToken, selectedItem.getItemReviewID());
        });

        FavoriteDetailRecyclerViewRequest(accessToken);
        reviewRecyclerView.setAdapter(favoriteDetailRecyclerViewAdapter);

//        Favorite Add or Delete
        favoriteBtn.setOnClickListener(view1 -> {
            if (!isWishing) {
                FavoritePOSTRequest(accessToken);
                favoriteBtn.setImageResource(R.drawable.heart_fill_5);
                isWishing =! isWishing;
            } else {
                FavoriteDELETERequest(accessToken);
                favoriteBtn.setImageResource(R.drawable.heart_empty);
                favoriteBtn.setBackgroundColor(Color.WHITE);
                isWishing =! isWishing;
            }
        });

//       BackButton
        backBtn.setOnClickListener(view12 -> {
            if (getActivity().getClass().getName().equals(MainActivity.class.getName())) {
                mainActivity = (MainActivity) getActivity();
                mainActivity.onBackPressed();
            } else if (getActivity().getClass().getName().equals(TagResultActivity.class.getName())) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Fragment fragment = fm.findFragmentById(R.id.tagResult_layout);
                        fm.beginTransaction().remove(fragment).commit();
                        getActivity().findViewById(R.id.tagResult_overlay).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.tagResult_btn_finish).setVisibility(View.VISIBLE);
            } else {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Fragment fragment = fm.findFragmentById(R.id.tagResult_layout);
                        fm.beginTransaction().remove(fragment).commit();
                        getActivity().findViewById(R.id.faceResult_overlay).setVisibility(View.GONE);
                        getActivity().findViewById(R.id.faceResult_btn_finish).setVisibility(View.VISIBLE);
            }
        });

    }

    public void FavoriteCheckRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_favorite+styleId , null, response -> {
            try {
                isWishing = response.getBoolean("isWishing");

                if (isWishing) {
                    favoriteBtn.setImageResource(R.drawable.heart_fill_5);
                } else {
                    favoriteBtn.setImageResource(R.drawable.heart_empty);
                    favoriteBtn.setBackgroundColor(Color.WHITE);
                }

            } catch (JSONException e) {
//                    e.printStackTrace();
            }
        }, volleyError -> {
            int errorCode = volleyError.networkResponse != null ? volleyError.networkResponse.statusCode : -1;
            Log.e("error message", GetErrorMessage.getErrorMessage(volleyError));
            switch (errorCode) {
                case 400:
                    // Bad Request 에러 처리

                    break;
                case 401:
                    break;
                case 404:
                    // Not Found 에러 처리
                    break;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }

    public void FavoritePOSTRequest(String accessToken) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_favorite+styleId , null, response -> {
        }, volleyError -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }
    public void FavoriteDELETERequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url_favorite+styleId , null, response -> {
        }, volleyError -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }

//    favorite detail recyclerview request
    public void FavoriteDetailRecyclerViewRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_search_review+styleId , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray jsonArray = obj.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++) {
                        FavoriteDetailRecyclerViewItem item = new FavoriteDetailRecyclerViewItem();
                        JSONObject object = jsonArray.getJSONObject(i);
                        item.setItemReviewID(object.getInt("reviewId"));
                        item.setStyleReviewNickname(object.getString("userNickname"));
                        item.setStyleReviewHeartCount(object.getString("likes"));
                        item.setStyleReviewGrade(object.getString("score"));

                        JSONArray arrayList = object.getJSONArray("photos");
                        item.setStyleReviewPicture(arrayList.getJSONObject(0).getString("storeUrl"));

                        favoriteDetailRecyclerViewAdapter.addItem(item);
                    }
                    favoriteDetailRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                GetErrorMessage.getErrorMessage(volleyError);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }

    private void recyclerviewRequest(String accessToken, int reviewId) {
        final String URL_reviewList = UrlConst.URL + "/api/review/" + reviewId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_reviewList, null, response -> {
//                parse received data
            try {
                JSONObject resultObject = response.getJSONObject("reviewResponse");
                String hairStyleName = resultObject.getString("hairStyleName");
                String userNickName = resultObject.getString("userNickname");
                String score = resultObject.getString("score");
                String content = resultObject.getString("contents");
                String createDate = resultObject.getString("createdDate");
                int likes = resultObject.getInt("likes");

                JSONArray hashTagsArray = resultObject.getJSONArray("hashTags");
                ArrayList<String> tags = new ArrayList<>();
                for (int j = 0; j < hashTagsArray.length(); j++) {
                    JSONObject hasTagObject = hashTagsArray.getJSONObject(j);
                    tags.add(hasTagObject.getString("tag"));
                }

                JSONArray photosArray = resultObject.getJSONArray("photos");
                ArrayList<String> receivedUrls = new ArrayList<>();
                for (int j = 0; j < photosArray.length(); j++) {
                    JSONObject photoObject = photosArray.getJSONObject(j);
                    receivedUrls.add(photoObject.getString("storeUrl"));
                }

                ReviewItem receivedData = new ReviewItem(reviewId, receivedUrls, hairStyleName, userNickName, tags, content, score, likes, createDate, false);

                Intent intent = new Intent(getActivity(), RecentReviewDetailActivity.class);
                intent.putExtra("reviewId", receivedData.getReviewId());
                intent.putExtra("userNickname", receivedData.getUserNickName());
                intent.putExtra("hairStyleName", receivedData.getHairStyleName());
                intent.putStringArrayListExtra("tags", receivedData.getTags());
                intent.putExtra("score", receivedData.getScore());
                intent.putExtra("likes", receivedData.getLikes());
                intent.putExtra("date", receivedData.getCreatedDate());
                intent.putExtra("content", receivedData.getContent());
                intent.putStringArrayListExtra("imageUrls", receivedData.getImageUrls());
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("review search error", message);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }) { @Override
        public Map<String, String> getHeaders() {
            Map<String, String>  params = new HashMap();
            params.put("Authorization", "bearer" + accessToken);
            return params;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
