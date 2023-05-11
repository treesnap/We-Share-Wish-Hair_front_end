package com.example.wishhair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.home.HomeItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TagResultActivity extends AppCompatActivity {

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
        for (int i = 0; i < 5; i++) {
            HomeItems newItems = new HomeItems(imageSample, "물결펌", "876", false);
            items.add(newItems);
        }

        TagResultAdapter tagResultAdapter = new TagResultAdapter(items, this);

        RecyclerView recyclerView = findViewById(R.id.tagResult_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(tagResultAdapter);


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

        JSONObject jsonObject = new JSONObject();
        JSONArray imageTags = new JSONArray();
        try {
            jsonObject.put("hairLength", selectedHairLength);
            jsonObject.put("perm", selectedPerm);
            for (int i = 0; i < selectedImages.size(); i++) {
                imageTags.put(selectedImages.get(i));
            }
            jsonObject.put("imageTags", imageTags);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("json", jsonObject.toString());
        String URL = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                TODO : 결과 파싱해서 recyclerView 설정
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
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
