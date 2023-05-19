package com.example.wishhair.func.TagFunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.favorite.FavoriteDetail;
import com.example.wishhair.hairItemAdapter;
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

public class TagResultActivity extends AppCompatActivity {
    private final ArrayList<HomeItems> tagResultItems = new ArrayList<>();
    private hairItemAdapter tagResultAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_result);

//        back
        Button btn_back = findViewById(R.id.tagResult_btn_back);
        btn_back.setOnClickListener(view -> finish());

        ArrayList<HomeItems> items = new ArrayList<>();
        //        dummyData
        String imageSample = "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg";
//        for (int i = 0; i < 5; i++) {
//            HomeItems newItems = new HomeItems(imageSample, "물결펌", "876", false);
//            items.add(newItems);
//        }

        hairItemAdapter tagResultAdapter = new hairItemAdapter(items, this);
        tagResultAdapter.setOnItemClickListener((v1, position) -> {
            HomeItems selectedItem = items.get(position);
        });

        recyclerView = findViewById(R.id.tagResult_recyclerView);

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();
        tagResultRequest(accessToken);

//        finish
        Button btn_finish = findViewById(R.id.tagResult_btn_finish);
        btn_finish.setOnClickListener(view -> finish());
    }

    private void tagResultRequest(String accessToken) {
        String selectedHairLength = getIntent().getStringExtra("selectedHairLength");
        String selectedPerm = getIntent().getStringExtra("selectedPerm");
        ArrayList<String> selectedImages = getIntent().getStringArrayListExtra("selectedImages");

        StringBuilder query = new StringBuilder();
        query.append("tags=").append(selectedHairLength).append("&");
        query.append("tags=").append(selectedPerm).append("&");
        for (int i = 0; i < selectedImages.size(); i++) {
            query.append("tags=").append(selectedImages.get(i)).append("&");
        }
        query.delete(query.length() - 1, query.length());

        String tagResultUrl = UrlConst.URL + "/api/hair_style/recommend?" + query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tagResultUrl, null, response -> {
            Log.d("recResponse", response.toString());
            String recResponse = String.valueOf(response);
            try {
                JSONObject result = new JSONObject(recResponse);
                JSONArray resultArray = result.getJSONArray("result");
                Log.d("size", String.valueOf(resultArray.length()));
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

                    tagResultItems.add(item);
                }
                tagResultAdapter = new hairItemAdapter(tagResultItems, this);
                tagResultAdapter.setOnItemClickListener(((v1, position) -> {
//                    TODO : 헤어 상세 이동 버그 발생
                    HomeItems selectedItem = tagResultItems.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putString("hairStylename", selectedItem.getHairStyleName());
                    bundle.putStringArrayList("tags", selectedItem.getTags());
                    bundle.putInt("hairStyleId", selectedItem.getHairStyleId());
                    bundle.putStringArrayList("ImageUrls", selectedItem.getHairImages());

                    FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
                    FavoriteDetail favoriteDetail = new FavoriteDetail();
                    favoriteDetail.setArguments(bundle);
                    transaction.replace(R.id.tagResult_layout, favoriteDetail);
//                    transaction.replace(R.id.MainLayout, favoriteDetail);
                    transaction.commit();
                }));
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(tagResultAdapter);
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
