package com.example.wishhair.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.MainActivity;
import com.example.wishhair.favorite.FavoriteDetail;
import com.example.wishhair.HairItemAdapter;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.detail.RecentReviewDetailActivity;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.func.faceFunc.FaceFuncActivity;
import com.example.wishhair.R;
import com.example.wishhair.func.TagFunc.TagFuncActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private RequestQueue queue;

    TextView hello, receivedText, settingMessage1, settingMessage2, settingMessage3;
    private Button btn_faceFunc;
    private boolean hasFaceShape;
    private String userNickName, faceShapeTag, accessToken;

//    monthly review
    private final ArrayList<HomeItems> monthlyReviewItems = new ArrayList<>();
    private HomeMonthlyReviewAdapter monthlyAdapter;
    private ViewPager2 monthlyReviewPager;
    private CircleIndicator3 monthlyIndicator;
//    recommend
    private final ArrayList<HomeItems> recommendItems = new ArrayList<>();
    private HairItemAdapter homeRecommendAdapter;
    private RecyclerView recommendRecyclerView;
    private MainActivity mainActivity;

    public HomeFragment() {}
        //TODO : bundle, LoginActivity에서 intent로 넘기는거 필요없는지 다시 체크(homeInfo에서 받으면 필요없으니까)해서 수정
    public static HomeFragment newInstance(String userNickName, boolean hasFaceShape, String faceShapeTag) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nickname", userNickName);
        bundle.putBoolean("hasFaceShape", hasFaceShape);
        bundle.putString("faceShapeTag", faceShapeTag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        infoRequest(accessToken);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

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
        accessToken = customTokenHandler.getAccessToken();

        queue = Volley.newRequestQueue(requireActivity());

//        title
        initTitle(v);
        infoRequest(accessToken);

//        faceFunc
        btn_faceFunc.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), TagFuncActivity.class);
            startActivity(intent);
        });

//        monthlyReview
        monthlyReviewPager = v.findViewById(R.id.home_ViewPager_review_monthly);
        monthlyIndicator = v.findViewById(R.id.home_circleIndicator);
        monthlyAdapter = new HomeMonthlyReviewAdapter(monthlyReviewItems);
        monthlyAdapter.setOnItemClickListener(((v1, position) -> {
            HomeItems selectedItem = monthlyReviewItems.get(position);
            reviewRequest(accessToken, selectedItem.getReviewId());
        }));

        monthlyReviewRequest(accessToken);

        monthlyReviewPager.setOffscreenPageLimit(1);

//        recommend
        TextView recUserName = v.findViewById(R.id.home_recommend_userName);
        recUserName.setText(userNickName);

        recommendRecyclerView = v.findViewById(R.id.home_recommend_recyclerView);
        recommendRequest(accessToken);

        return v;
    }

    private void initTitle(View v) {
        btn_faceFunc = v.findViewById(R.id.home_btn_faceFunc);

        hello = v.findViewById(R.id.home_tv_hello);
        receivedText = v.findViewById(R.id.home_title_receivedText);
        settingMessage1 = v.findViewById(R.id.home_tv_settingMessage1);
        settingMessage2 = v.findViewById(R.id.home_tv_settingMessage2);
        settingMessage3 = v.findViewById(R.id.home_tv_settingMessage3);

        /*Bundle homeBundle = getArguments();
        if (homeBundle != null) {
            userNickName = homeBundle.getString("nickname");
            hasFaceShape = homeBundle.getBoolean("hasFaceShape");
            faceShapeTag = homeBundle.getString("faceShapeTag");
        }
        if (hasFaceShape) {
            hello.setVisibility(View.GONE);
            receivedText.setText(faceShapeTag);
            settingMessage1.setText("에 어울리는");
            settingMessage2.setText("헤어스타일은?");
            settingMessage3.setVisibility(View.GONE);
            btn_faceFunc.setVisibility(View.GONE);
        } else {
            receivedText.setText(userNickName);
            btn_tagFunc.setVisibility(View.GONE);
            btn_faceFuncAgain.setVisibility(View.GONE);
        }*/
    }
    @SuppressLint("NotifyDataSetChanged")
    private void monthlyReviewRequest(String accessToken) {
        monthlyReviewItems.clear();
        final String monthlyURL = UrlConst.URL + "/api/review/month";
        JsonObjectRequest monthlyRequest = new JsonObjectRequest(Request.Method.GET, monthlyURL, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int reviewId = object.getInt("reviewId");
                    String receivedUserNickname = object.getString("userNickname");
                    String receivedContents = object.getString("contents");

                    HomeItems newItem = new HomeItems(reviewId, receivedUserNickname, receivedContents);
                    monthlyReviewItems.add(newItem);
                }
                monthlyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            monthlyReviewPager.setAdapter(monthlyAdapter);
            monthlyIndicator.setViewPager(monthlyReviewPager);
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("validate error message", message);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        queue.add(monthlyRequest);
    }

    private void infoRequest(String accessToken) {
        String infoUrl = UrlConst.URL + "/api/user/home_info";
        JsonObjectRequest infoRequest = new JsonObjectRequest(Request.Method.GET, infoUrl, null, response -> {
            try {
                userNickName = response.getString("nickname");
                hasFaceShape = response.getBoolean("hasFaceShape");
                faceShapeTag = response.getString("faceShapeTag");

                SharedPreferences sp = requireActivity().getSharedPreferences("userNickName", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userNickName", userNickName);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (hasFaceShape) {
                hello.setVisibility(View.GONE);
                receivedText.setText(faceShapeTag);
                settingMessage1.setText("에 어울리는");
                settingMessage2.setText("헤어스타일은?");
                settingMessage3.setVisibility(View.GONE);
                btn_faceFunc.setVisibility(View.GONE);
            } else {
                receivedText.setText(userNickName);
            }
        }, error -> {}) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(infoRequest);
    }

    private void reviewRequest(String accessToken, int reviewId) {
        final String URL_reviewList = UrlConst.URL + "/api/review/" + reviewId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_reviewList, null, response -> {
//                parse received data
            try {
                JSONObject resultObject = response.getJSONObject("reviewResponse");
                Log.d("result", resultObject.toString());
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

    private void recommendRequest(String accessToken) {
        String recUrl = UrlConst.URL + "/api/hair_style/home";
        JsonObjectRequest recRequest = new JsonObjectRequest(Request.Method.GET, recUrl, null, response -> {
//            Log.d("recResponse", response.toString());
            String recResponse = String.valueOf(response);
            try {
                JSONObject result = new JSONObject(recResponse);
                JSONArray resultArray = result.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {

                    JSONObject itemObject = resultArray.getJSONObject(i);

                    int hairStyleId = itemObject.getInt("hairStyleId");
                    String hairStyleName = itemObject.getString("name");

                    JSONArray photosArray = itemObject.getJSONArray("photos");
                    ArrayList<String> photoUrls = new ArrayList<>();
                    for (int j = 0; j < photosArray.length(); j++) {
                        JSONObject photoObject = photosArray.getJSONObject(j);
                        photoUrls.add(photoObject.getString("storeUrl"));
                    }

                    JSONArray hashTagsArray = itemObject.getJSONArray("hashTags");
                    ArrayList<String> tags = new ArrayList<>();
                    for (int j = 0; j < hashTagsArray.length(); j++) {
                        JSONObject hasTagObject = hashTagsArray.getJSONObject(j);
                        tags.add(hasTagObject.getString("tag"));
                    }

                    HomeItems item = new HomeItems(hairStyleId, photoUrls, hairStyleName, tags);

                    recommendItems.add(item);
                }
                homeRecommendAdapter = new HairItemAdapter(recommendItems, getContext());
                homeRecommendAdapter.setOnItemClickListener(((v1, position) -> {
                    mainActivity.setBackPressFlag(true);
                    HomeItems selectedItem = recommendItems.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("hairStylename", selectedItem.getHairStyleName());
                    bundle.putStringArrayList("tags", selectedItem.getTags());
                    bundle.putInt("hairStyleId", selectedItem.getHairStyleId());
                    bundle.putStringArrayList("ImageUrls", selectedItem.getHairImages());

                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    FavoriteDetail favoriteDetail = new FavoriteDetail();
                    favoriteDetail.setArguments(bundle);
                    transaction.replace(R.id.MainLayout, favoriteDetail);
                    transaction.commit();
                }));
                recommendRecyclerView.setAdapter(homeRecommendAdapter);
                recommendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("validate error message", message);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(recRequest);
    }
}
