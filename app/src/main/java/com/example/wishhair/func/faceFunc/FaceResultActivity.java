package com.example.wishhair.func.faceFunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

    private String resultShape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_faceresult);

        Button btn_back = findViewById(R.id.faceResult_btn_back);
        btn_back.setOnClickListener(view -> finish());

        Button btn_finish = findViewById(R.id.faceResult_btn_finish);
        btn_finish.setOnClickListener(view -> finish());

        faceRecItems = new ArrayList<>();

        userName = findViewById(R.id.faceResult_userName);
        faceShape = findViewById(R.id.faceResult_faceShape);
        faceShape_message = findViewById(R.id.faceResult_faceShape_message);

//        TODO : 임시 코드
        resultShape = getIntent().getStringExtra("result");
        SharedPreferences sp = getSharedPreferences("userNickName", MODE_PRIVATE);

        userName.setText(sp.getString("userNickName", "fail"));
        faceShape.setText(resultShape);
        faceShape_message.setText(resultShape);

//        dummyData
        String imageSample = "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg";
//        for (int i = 0; i < 4; i++) {
//            HomeItems newItems = new HomeItems(imageSample, "물결펌", "876", false);
//            faceRecItems.add(newItems);
//        }

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
                    HomeItems selectedItem = faceRecItems.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("hairStylename", selectedItem.getHairStyleName());
                    bundle.putStringArrayList("tags", selectedItem.getTags());
                    bundle.putInt("hairStyleId", selectedItem.getHairStyleId());
                    bundle.putStringArrayList("ImageUrls", selectedItem.getHairImages());

                    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                    FavoriteDetail favoriteDetail = new FavoriteDetail();
                    favoriteDetail.setArguments(bundle);
                    transaction.replace(R.id.MainLayout, favoriteDetail);
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
