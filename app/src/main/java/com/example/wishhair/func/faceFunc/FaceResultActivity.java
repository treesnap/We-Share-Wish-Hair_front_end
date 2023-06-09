package com.example.wishhair.func.faceFunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.favorite.FavoriteDetail;
import com.example.wishhair.HairItemAdapter;
import com.example.wishhair.sign.UrlConst;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.home.HomeItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FaceResultActivity extends AppCompatActivity {

    private TextView userName, faceShape, faceShape_message;
    //        homeItem 과 형식이 같아 재사용
    private ArrayList<HomeItems> faceRecItems;
    private HairItemAdapter faceResultAdapter;
    RecyclerView recyclerView;
    private LinearLayout overlay;
    private String resultShape;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_faceresult);

        //        activity inactivation cover
        overlay = findViewById(R.id.faceResult_overlay);

        Button btn_back = findViewById(R.id.faceResult_btn_back);
        btn_back.setOnClickListener(view -> finish());

        btn_finish = findViewById(R.id.faceResult_btn_finish);
        btn_finish.setOnClickListener(view -> finish());

        faceRecItems = new ArrayList<>();

        userName = findViewById(R.id.faceResult_userName);
        faceShape = findViewById(R.id.faceResult_faceShape);
        faceShape_message = findViewById(R.id.faceResult_faceShape_message);

        resultShape = getIntent().getStringExtra("result");
        SharedPreferences sp = getSharedPreferences("userNickName", MODE_PRIVATE);

        userName.setText(sp.getString("userNickName", "fail"));
        faceShape.setText(resultShape);
        faceShape_message.setText(resultShape);

        recyclerView = findViewById(R.id.faceResult_recyclerView);

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();
        faceResultRequest(accessToken);
    }

    private void faceResultRequest(String accessToken) {
        String faceResultUrl = UrlConst.URL + "/api/hair_style/home";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, faceResultUrl, null, response -> {
            String recResponse = String.valueOf(response);
            try {
                JSONObject result = new JSONObject(recResponse);
                JSONArray resultArray = result.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    if (i > 3) {
                        break;
                    }
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

                    faceRecItems.add(item);
                }
                faceResultAdapter = new HairItemAdapter(faceRecItems, this);
                faceResultAdapter.setOnItemClickListener(((v1, position) -> {
                    // 프래그먼트 생성 시 액티비티 레이아웃 비활성화
                    overlay.setVisibility(View.VISIBLE);
                    btn_finish.setVisibility(View.GONE);

                    HomeItems selectedItem = faceRecItems.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("hairStylename", selectedItem.getHairStyleName());
                    bundle.putStringArrayList("tags", selectedItem.getTags());
                    bundle.putInt("hairStyleId", selectedItem.getHairStyleId());
                    bundle.putStringArrayList("ImageUrls", selectedItem.getHairImages());

                    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                    FavoriteDetail favoriteDetail = new FavoriteDetail();
                    favoriteDetail.setArguments(bundle);
                    transaction.replace(R.id.faceResult_layout, favoriteDetail);
                    transaction.commit();
                }));
                recyclerView.setAdapter(faceResultAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("validate error message", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}
