package com.example.wishhair.MyPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MainActivity;
import com.example.wishhair.MyPage.adapters.MyPageRecyclerViewAdapter;
import com.example.wishhair.MyPage.items.HeartlistItem;
import com.example.wishhair.R;
import com.example.wishhair.sign.LoginActivity;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView HeartlistRecyclerView;
    private MyPageRecyclerViewAdapter adapter;
    private RecyclerDecoration recyclerDecoration;

    private SharedPreferences loginSP;
    private String accessToken;
    final static private String url = UrlConst.URL + "/api/logout";
    final static private String url_mypage = UrlConst.URL + "/api/user/my_page";
    final static private String url_info = UrlConst.URL + "/api/user/info";
    final static private String url_withdraw = UrlConst.URL + "/api/user";

    static String testName = null;
    static String mypoint, UserEmail, UserName;
    ArrayList<HeartlistItem> list;
    private TextView tv, point_preview;;
    private ImageView userpicture;

    private OnBackPressedCallback callback;

    public MyPageFragment() {
        // Required empty public constructor
    }

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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.mypage_toolbar);

        ImageButton toConfig = view.findViewById(R.id.mypage_to_config);
        ImageButton toMyPoint = view.findViewById(R.id.mypage_to_point);
        ImageButton withdrawBtn = view.findViewById(R.id.mypage_withdraw);
        transferRequest(accessToken);
        toConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle UserInfoTransfer = new Bundle();
                UserInfoTransfer.putString("userEmail", UserEmail);
                UserInfoTransfer.putString("userName", UserName);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ConfigFragment configFragment = new ConfigFragment();
                configFragment.setArguments(UserInfoTransfer);
                transaction.replace(R.id.MainLayout, configFragment);
                transaction.commit();
            }
        });
        toMyPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.ChangeFragment(7);
            }
        });
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(view.getContext(), R.style.WithdrawAlertDialogTheme);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.mypage_withdraw_dialog, view.findViewById(R.id.dialog_withdraw_layout));
                // alert의 title과 Messege 세팅

                myAlertBuilder.setView(v);
                AlertDialog alertDialog = myAlertBuilder.create();

                // 버튼 리스너 설정
                v.findViewById(R.id.dialog_withdraw_OKbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WithdrawRequest(accessToken);
                        Toast.makeText(view.getContext().getApplicationContext(),"회원 탈퇴 완료",Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                v.findViewById(R.id.dialog_withdraw_Canclebtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                // 다이얼로그 형태 지우기
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                alertDialog.show();
            }
        });

/*      HomeFragment로 이동하는 버튼 <불필요 시 삭제>
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.MainLayout, homeFragment);
                fragmentTransaction.commit();
            }
        });
        toolbar.inflateMenu(R.menu.메뉴.xml);   버튼 추가 시 사용할 것 */

//        LOGOUT
        ImageButton btn_logout  = view.findViewById(R.id.mypage_button_logout);

        Log.d("acc", loginSP.getString("accessToken", "fail acc"));
        Log.d("ref", loginSP.getString("refreshToken", "fail ref"));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(accessToken);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_fragment, container, false);
        tv = view.findViewById(R.id.mypage_nickname);
        point_preview = view.findViewById(R.id.mypage_point_preview);
        userpicture = view.findViewById(R.id.mypage_user_picture);

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        HeartlistRecyclerView = view.findViewById(R.id.HeartlistRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        HeartlistRecyclerView.setLayoutManager(layoutManager);

        adapter = new MyPageRecyclerViewAdapter(getContext());

        HeartlistRecyclerView.setAdapter(adapter);

        tv.setText(testName+" 님");
        point_preview.setText(mypoint+"P");

//        myPageRequest(accessToken);
        myPageRecyclerviewRequest(accessToken);
        return view;
    }

    private void logout(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                logout_delete_token(loginSP);
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                logout_delete_token(loginSP);
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
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
        queue.add(jsonObjectRequest);
    }

    private void logout_delete_token (SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("accessToken");
        editor.remove("refreshToken");
        editor.apply();
    }

//    public void myPageRequest(String accessToken) {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_mypage, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    testName = response.getString()
//                    tv.setText(testName+" 님");
//                    point_preview.setText(mypoint+"P");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> params = new HashMap();
//                params.put("Authorization", "bearer" + accessToken);
//
//                return params;
//            }
//        };
//
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        queue.add(jsonObjectRequest);
//    }
    public void transferRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_info , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    UserEmail = response.getString("email");
                    UserName = response.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
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

    //wishlist recyclerview request
    public void myPageRecyclerviewRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_mypage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tv.setText(response.getString("nickname")+" 님");
                    point_preview.setText(response.getString("point")+"P");
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray jsonArray = obj.getJSONArray("reviews");
                    for (int i=0;i<jsonArray.length();i++) {
                        HeartlistItem item = new HeartlistItem();
                        JSONObject object = jsonArray.getJSONObject(i);
                        item.setHeartlistStyleName(object.getString("hairStyleName"));
                        item.setHeartlistReviewerNickname(object.getString("userNickname"));
                        item.setHeartlistGrade(object.getString("score"));
                        item.setHeartlistReviewID(object.getInt("reviewId"));
                        item.setHeartlistHeartcount(String.valueOf(object.getInt("likes")));

                        JSONArray imageUrls = object.getJSONArray("photos");
                        item.setHeartlistPicture(imageUrls.getJSONObject(0).getString("storeUrl"));

                        adapter.addItem(item);
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    // 회원 탈퇴 Request
    public void WithdrawRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url_withdraw , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i("WithDraw Request", "success");
                Intent intent = new Intent(mainActivity, LoginActivity.class);
                startActivity(intent);
                mainActivity.finish();
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
