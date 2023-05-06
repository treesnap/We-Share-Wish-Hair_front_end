package com.example.wishhair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.lang.reflect.Method;

public class FaceResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_result);


    }

    private void faceResultRequest(String accessToken) {
        String URL = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}