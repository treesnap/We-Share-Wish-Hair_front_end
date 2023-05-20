package com.example.wishhair.MyPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.MainActivity;
import com.example.wishhair.MyPage.adapters.MyPageRecyclerViewAdapter;
import com.example.wishhair.MyPage.items.HeartListItem;
import com.example.wishhair.R;
import com.example.wishhair.review.ReviewItem;
import com.example.wishhair.review.detail.RecentReviewDetailActivity;
import com.example.wishhair.sign.LoginActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerDecoration recyclerDecoration;

    private SharedPreferences loginSP;
    private String accessToken;

    private RequestQueue queue;
    final static private String url_logout = UrlConst.URL + "/api/logout";
    final static private String url_myPage = UrlConst.URL + "/api/user/my_page";
    final static private String url_wishlist = UrlConst.URL + "/api/hair_style/wish";
    final static private String url_withdraw = UrlConst.URL + "/api/user";
    final static private String url_info = UrlConst.URL + "/api/user/info";

    static String testName = null;
    static String UserEmail, UserName;
    private TextView tv, point_preview;;
    private ImageView userPicture;

    private OnBackPressedCallback callback;
    private RecyclerView HeartListRecyclerView;
    private MyPageRecyclerViewAdapter adapter;
    private ArrayList<HeartListItem> heartListItems;

    public MyPageFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainActivity.onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.mypage_toolbar);

        ImageButton toConfig = view.findViewById(R.id.mypage_to_config);
        ImageButton toMyPoint = view.findViewById(R.id.mypage_to_point);
        ImageButton withdrawBtn = view.findViewById(R.id.mypage_withdraw);


        toConfig.setOnClickListener(view1 -> {
            Bundle UserInfoTransfer = new Bundle();
            UserInfoTransfer.putString("userEmail", UserEmail);
            UserInfoTransfer.putString("userName", UserName);

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            ConfigFragment configFragment = new ConfigFragment();
            configFragment.setArguments(UserInfoTransfer);
            transaction.replace(R.id.MainLayout, configFragment);
            transaction.commit();
        });
        toMyPoint.setOnClickListener(view12 -> mainActivity.ChangeFragment(7));
        withdrawBtn.setOnClickListener(view13 -> {
            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(view13.getContext(), R.style.WithdrawAlertDialogTheme);
            View v = LayoutInflater.from(getContext()).inflate(R.layout.mypage_withdraw_dialog, view13.findViewById(R.id.dialog_withdraw_layout));
            // alert의 title과 Messege 세팅

            myAlertBuilder.setView(v);
            AlertDialog alertDialog = myAlertBuilder.create();

            // 버튼 리스너 설정
            v.findViewById(R.id.dialog_withdraw_OKbtn).setOnClickListener(view131 -> {
                WithdrawRequest(accessToken);
                Toast.makeText(view131.getContext().getApplicationContext(),"회원 탈퇴 완료",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });
            v.findViewById(R.id.dialog_withdraw_Canclebtn).setOnClickListener(view1312 -> alertDialog.dismiss());

            // 다이얼로그 형태 지우기
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
            alertDialog.show();
        });

//      Profile Picture Click Event
//        userpicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                activityResultLauncher.launch(intent);
//            }
//        });

//        LOGOUT
        ImageButton btn_logout  = view.findViewById(R.id.mypage_button_logout);
        btn_logout.setOnClickListener(view14 -> logout(accessToken));

        transferRequest(accessToken);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment, container, false);

        queue = Volley.newRequestQueue(requireContext());

        tv = view.findViewById(R.id.mypage_nickname);
        point_preview = view.findViewById(R.id.mypage_point_preview);

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        tv.setText(testName+" 님");

//        heartList
        heartListItems = new ArrayList<>();
        myPageRecyclerviewRequest(accessToken);
        HeartListRecyclerView = view.findViewById(R.id.HeartlistRecyclerView);
        HeartListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new MyPageRecyclerViewAdapter(getContext(), heartListItems);
        adapter.setOnItemClickListener((v, position) -> {
            HeartListItem selectedItem = heartListItems.get(position);
            reviewRequest(accessToken, selectedItem.getHeartListReviewID());
        });

        return view;
    }

    private void logout(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url_logout, null, response -> {
            logout_delete_token(loginSP);
            Intent intent = new Intent(mainActivity, LoginActivity.class);
            startActivity(intent);
            mainActivity.finish();

        }, volleyError -> {
            logout_delete_token(loginSP);
            Intent intent = new Intent(mainActivity, LoginActivity.class);
            startActivity(intent);
            mainActivity.finish();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void logout_delete_token (SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("accessToken");
        editor.remove("refreshToken");
        editor.apply();
    }


    //wishlist recyclerview request
    @SuppressLint("NotifyDataSetChanged")
    public void myPageRecyclerviewRequest(String accessToken) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_myPage, null, response -> {
            try {
                tv.setText(response.getString("nickname")+" 님");
                point_preview.setText(response.getString("point")+"P");
                JSONObject obj = new JSONObject(response.toString());
                JSONArray jsonArray = obj.getJSONArray("reviews");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HeartListItem item = new HeartListItem();
                    JSONObject object = jsonArray.getJSONObject(i);
                    item.setHeartListStyleName(object.getString("hairStyleName"));
                    item.setHeartListReviewerNickname(object.getString("userNickname"));
                    item.setHeartListGrade(object.getString("score"));
                    item.setHeartListReviewID(object.getInt("reviewId"));
                    item.setHeartListHeartCount(object.getString("likes"));

                    JSONArray imageUrls = object.getJSONArray("photos");
                    item.setHeartListPicture(imageUrls.getJSONObject(0).getString("storeUrl"));

                    heartListItems.add(item);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HeartListRecyclerView.setAdapter(adapter);
        }, volleyError ->  {
            String message = GetErrorMessage.getErrorMessage(volleyError);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    // 회원 탈퇴 Request
    public void WithdrawRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url_withdraw , null, response -> {
            Log.i("WithDraw Request", "success");
            Intent intent = new Intent(mainActivity, LoginActivity.class);
            startActivity(intent);
            mainActivity.finish();
            }, volleyError -> {
            String message = GetErrorMessage.getErrorMessage(volleyError);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            Log.e("withDraw error", message);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }
    public void transferRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_info , null, response -> {
            try {
                UserEmail = response.getString("email");
                UserName = response.getString("name");
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }, volleyError -> {
            String message = GetErrorMessage.getErrorMessage(volleyError);
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }


//    class로 분리 시도
    private void reviewRequest(String accessToken, int reviewId) {
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

    public ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        userPicture.setImageURI(uri);
                    }
                }
            }
    );
}
