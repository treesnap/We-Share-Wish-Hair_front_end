package com.example.wishhair.faceFunc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.home.HomeItems;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FaceResultActivity extends AppCompatActivity {

    private TextView userName, faceShape, faceShape_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_faceresult);

        Button btn_back = findViewById(R.id.faceResult_btn_back);
        btn_back.setOnClickListener(view -> finish());

        Button btn_finish = findViewById(R.id.faceResult_btn_finish);
        btn_finish.setOnClickListener(view -> finish());

        userName = findViewById(R.id.faceResult_userName);
        faceShape = findViewById(R.id.faceResult_faceShape);
        faceShape_message = findViewById(R.id.faceResult_faceShape_message);

//        TODO : 임시 코드
        userName.setText("현정");
        faceShape.setText("달걀형");
        faceShape_message.setText("달걀형");
//        homeItem 과 형식이 같아 재사용
        ArrayList<HomeItems> faceRecItems = new ArrayList<>();
//        dummyData
        String imageSample = "https://cdn.pixabay.com/photo/2019/12/26/10/44/horse-4720178_1280.jpg";
        for (int i = 0; i < 5; i++) {
            HomeItems newItems = new HomeItems(imageSample, "물결펌", "876", false);
            faceRecItems.add(newItems);
        }

        FaceResultAdapter faceResultAdapter = new FaceResultAdapter(faceRecItems, this);

        RecyclerView recyclerView = findViewById(R.id.faceResult_recyclerView);
        recyclerView.setAdapter(faceResultAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();
        faceResultRequest(accessToken);
    }

    private void faceResultRequest(String accessToken) {
        String URL = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                TODO : 결과 파싱해서 userName, faceShape, faceShape_message / recyclerView 설정
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