package com.example.wishhair.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.faceFunc.FaceFuncActivity;
import com.example.wishhair.R;
import com.example.wishhair.TagFuncActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private final ArrayList<HomeItems> monthlyReviewItems = new ArrayList<>();
    Button btn_tagFunc, btn_faceFunc, btn_faceFuncAgain;
    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

//      accessToken
        CustomTokenHandler customTokenHandler = new CustomTokenHandler(requireActivity());
        String accessToken = customTokenHandler.getAccessToken();

//        title
        initTitle(v);

//        faceFunc
        btn_faceFunc.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), FaceFuncActivity.class);
            startActivity(intent);
        });

//        TagFunc
        btn_tagFunc.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), TagFuncActivity.class);
            startActivity(intent);
        });

//        faceFuncAgain
        btn_faceFuncAgain.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), FaceFuncActivity.class);
            startActivity(intent);
        });

//        monthlyReview
        monthlyReviewRequest(accessToken);

        ViewPager2 monthlyReviewPager = v.findViewById(R.id.home_ViewPager_review_monthly);
        CircleIndicator3 monthlyIndicator = v.findViewById(R.id.home_circleIndicator);

        monthlyReviewPager.setOffscreenPageLimit(1);
        monthlyReviewPager.setAdapter(new HomeMonthlyReviewAdapter(monthlyReviewItems));

        monthlyIndicator.setViewPager(monthlyReviewPager);

//        recommend
        ArrayList<HomeItems> recommendItems = new ArrayList<>();
        //===============================dummy data===============================
        String imageSample = "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg";
        for (int i = 0; i < 5; i++) {
            HomeItems newRecItems = new HomeItems(imageSample, "hairStyle", "876", false);
            recommendItems.add(newRecItems);
        }

        RecyclerView recommendRecyclerView = v.findViewById(R.id.home_recommend_recyclerView);
        HomeRecommendAdapter homeRecommendAdapter = new HomeRecommendAdapter(recommendItems, getContext());

        recommendRecyclerView.setAdapter(homeRecommendAdapter);
        recommendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        return v;
    }

    private void initTitle(View v) {
        btn_tagFunc = v.findViewById(R.id.home_btn_tagFunc);
        btn_faceFunc = v.findViewById(R.id.home_btn_faceFunc);
        btn_faceFuncAgain = v.findViewById(R.id.home_btn_faceFuncAgain);

        TextView hello, receivedText, settingMessage1, settingMessage2, settingMessage3;

        hello = v.findViewById(R.id.home_tv_hello);
        receivedText = v.findViewById(R.id.home_title_receivedText);
        settingMessage1 = v.findViewById(R.id.home_tv_settingMessage1);
        settingMessage2 = v.findViewById(R.id.home_tv_settingMessage2);
        settingMessage3 = v.findViewById(R.id.home_tv_settingMessage3);

//        TODO : 여부 받아오기
        if (true) {
            hello.setVisibility(View.GONE);
//            TODO : 받아온 얼굴형 사용
            receivedText.setText("달걀형");
            settingMessage1.setText("에 어울리는");
            settingMessage2.setText("헤어스타일은?");
            settingMessage3.setVisibility(View.GONE);
            btn_faceFunc.setVisibility(View.GONE);
        } else {
            receivedText.setText("현정");
            btn_tagFunc.setVisibility(View.GONE);
            btn_faceFuncAgain.setVisibility(View.GONE);
        }
    }

    private void monthlyReviewRequest(String accessToken) {
        final String URL = UrlConst.URL + "api/review/month";
        JsonArrayRequest monthlyRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        int reviewId = object.getInt("reviewId");
                        String userNickname = object.getString("userNickname");
                        String contents = object.getString("contents");

                        HomeItems newItem = new HomeItems(reviewId, userNickname, contents);
                        monthlyReviewItems.add(newItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(monthlyRequest);
    }
}
