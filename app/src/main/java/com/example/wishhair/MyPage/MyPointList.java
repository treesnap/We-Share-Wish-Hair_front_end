package com.example.wishhair.MyPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MainActivity;
import com.example.wishhair.MyPage.adapters.PointAdapter;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import org.threeten.bp.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPointList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPointList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MainActivity mainActivity;
    private OnBackPressedCallback callback;

    private PointAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private SharedPreferences loginSP;
    final static private String url = UrlConst.URL + "/api/user/my_page";
    final static private String point_url = UrlConst.URL + "/api/point";
    final static private String pointhistory_url = UrlConst.URL + "/api/point?size=10&page=0";

    static private String accessToken;
    private TextView myPointView;
    private Button toPointRefund;
    private int myPoint;
    public MyPointList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPointList.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPointList newInstance(String param1, String param2) {
        MyPointList fragment = new MyPointList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = getView().findViewById(R.id.point_toolbar);
        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");
//        backButton
        Button backBtn = view.findViewById(R.id.point_back_btn);
        backBtn.setOnClickListener(view1 -> {
            mainActivity.ChangeFragment(2);
        });

//        data transfer(pointlist -> refund)
        toPointRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle pointTransfer = new Bundle();
                pointTransfer.putInt("point", myPoint);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                RefundFragment refundFragment = new RefundFragment();
                refundFragment.setArguments(pointTransfer);
                transaction.replace(R.id.MainLayout, refundFragment);
                transaction.commit();
            }
        });

        recyclerView = view.findViewById(R.id.pointlist_recyclerview);
        adapter = new PointAdapter();
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.point_recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        PointListRequest(accessToken);
        PointHistoryRequest(accessToken);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_point_list_fragment, container, false);
        myPointView = view.findViewById(R.id.point_pointview);
        toPointRefund = view.findViewById(R.id.point_refund);
        AndroidThreeTen.init(getContext());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return view;
    }

    public void PointListRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                myPoint = response.optInt("point");
                myPointView.setText("현재 포인트\n"+myPoint+"P");

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
    public void PointHistoryRequest(String accessToken) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, pointhistory_url , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    JSONArray jsonArray = obj.getJSONArray("result");

                    for (int i = 0; i<jsonArray.length(); i++) {
                        PointHistory item = new PointHistory();
                        JSONObject object = jsonArray.getJSONObject(i);
                        item.setPointType(object.getString("pointType"));
                        item.setDealAmount(object.getInt("dealAmount"));
                        item.setPoint(object.getInt("point"));
                        item.setDealDate(object.getString("dealDate"));

                        adapter.addItem(item);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
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
}
