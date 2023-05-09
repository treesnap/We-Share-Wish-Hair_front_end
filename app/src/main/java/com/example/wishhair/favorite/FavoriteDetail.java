package com.example.wishhair.favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MyPage.items.HeartlistItem;
import com.example.wishhair.R;
import com.example.wishhair.review.detail.ImageSliderAdapter;
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

    public FavoriteDetail() {
        // Required empty public constructor
    }
    private SharedPreferences loginSP;
    final static private String url_favorite = UrlConst.URL + "/api/hair_style/wish/";
    final static private String url2 = UrlConst.URL + "/api";

    static private String accessToken;

    int styleId;
    ImageButton favoriteBtn;
    TextView stylenameTv;
    TextView hashtags;

    private ViewPager2 sliderViewPager;
    private CircleIndicator3 circleIndicator;
    FavoriteDetailRecyclerViewAdapter favoriteDetailRecyclerViewAdapter;
    RecyclerView reviewRecyclerView;
    public ArrayList<String> images = new ArrayList<String>(
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
        stylenameTv = view.findViewById(R.id.favorite_detail_stylename);
        hashtags = view.findViewById(R.id.favorite_detail_hashtag);
        reviewRecyclerView = view.findViewById(R.id.favorite_detail_review_recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(getContext(), images));
        circleIndicator.setViewPager(sliderViewPager);

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        favoriteDetailRecyclerViewAdapter = new FavoriteDetailRecyclerViewAdapter();
        reviewRecyclerView.setAdapter(favoriteDetailRecyclerViewAdapter);

        // data transfer (FavoriteFragment -> FavoriteDetailFragment)
        if (getArguments() != null) {
            stylenameTv.setText(getArguments().getString("hairStylename"));
            String[] tags = getArguments().getStringArray("tags");
            String tag = "";
            try {
                for (int i = 0; i < tags.length; i++) {
                    tag = tag + "#" + tags[i] + " ";
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }

            hashtags.setText(tag);
            styleId = getArguments().getInt("hairStyleId");
        }
    }

    public void FavoriteDetailRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_favorite , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
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

    public void FavoriteAddRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_favorite , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
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

    //favorite detail recyclerview request
    public void FavoriteDetailRecyclerViewRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2 , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray jsonArray = obj.getJSONArray("reviews");
                    for (int i=0;i<jsonArray.length();i++) {
                        FavoriteDetailRecyclerViewItem item = new FavoriteDetailRecyclerViewItem();
                        JSONObject object = jsonArray.getJSONObject(i);
                        item.setStyleReviewNickname(object.getString(""));
                        item.setStyleReviewHeartCount(object.getString(""));
                        item.setStyleReviewGrade(object.getString(""));
                        item.setReviewStyleID(object.getInt(""));
//                        item.setStyleReviewPicture(object.getString());

                        favoriteDetailRecyclerViewAdapter.addItem(item);
                        favoriteDetailRecyclerViewAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
//                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
}