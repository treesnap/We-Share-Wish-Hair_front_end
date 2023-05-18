package com.example.wishhair.func.TagFunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
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

        StringBuilder query = new StringBuilder();
        query.append("tags=").append(selectedHairLength).append("&");
        query.append("tags=").append(selectedPerm).append("&");
        for (int i = 0; i < selectedImages.size(); i++) {
            query.append("tags=").append(selectedImages.get(i)).append("&");
        }
        query.delete(query.length() - 1, query.length());

        String tagResultUrl = UrlConst.URL + "/api/hair_style/recommend?" + query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tagResultUrl, null, response -> {
//                TODO : 결과 파싱해서 recyclerView 설정
            Log.d("tagResponse", response.toString());
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
