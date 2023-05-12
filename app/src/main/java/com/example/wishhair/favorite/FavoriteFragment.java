package com.example.wishhair.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MainActivity;
import com.example.wishhair.MyPage.PointHistory;
import com.example.wishhair.MyPage.items.HeartlistItem;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnBackPressedCallback callback;
    MainActivity mainActivity;
    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    Button btn;
    int targetStyleId;

    private SharedPreferences loginSP;
    final static private String url = UrlConst.URL + "/api/hair_style/wish";
    final static private String url2 = UrlConst.URL + "/api/hair_style/wish/4";

    static private String accessToken;

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorite_fragment, container, false);
        recyclerView = v.findViewById(R.id.favorite_recyclerview);
        btn = v.findViewById(R.id.favorite_modify);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRequest(accessToken);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FavoriteAdapter(getContext());

        adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, int id, String stylename, String[] tags) {
                Toast.makeText(getContext(),"position:" +position + " hairstyleid:"+id, Toast.LENGTH_SHORT).show();
                targetStyleId = id;
                Bundle bundle = new Bundle();
                bundle.putString("hairStylename", stylename);
                bundle.putStringArray("tags", tags);
                bundle.putInt("hairStyleId", id);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FavoriteDetail favoriteDetail = new FavoriteDetail();
                favoriteDetail.setArguments(bundle);
                transaction.replace(R.id.MainLayout, favoriteDetail);
                transaction.commit();
            }
        });

        FavoriteListRequest(accessToken);
        recyclerView.setAdapter(adapter);
    }

    public void FavoriteListRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray jsonArray = obj.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++) {
                        FavoriteItem item = new FavoriteItem();
                        JSONObject object = jsonArray.getJSONObject(i);
                        item.setFavoriteStyleName(object.getString("name"));
                        item.setFavoriteStyleId(object.getInt("hairStyleId"));
                        JSONArray ImageUrls = object.getJSONArray("photos");
                        JSONObject ImageUrl = ImageUrls.getJSONObject(0);
                        item.setFavoritePicture(ImageUrl.getString("storeUrl"));

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

    public void testRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2 , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("error message", response.getString("message"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
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