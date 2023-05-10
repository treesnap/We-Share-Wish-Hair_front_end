package com.example.wishhair;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.sign.UrlConst;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class OauthCallBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_call_back);

        Button btn_oauthTest = findViewById(R.id.btn_oauthTest);
        btn_oauthTest.setOnClickListener(view -> testRequest());
    }


    private void testRequest() {
        String testURL = UrlConst.URL + "/api/oauth/access";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, testURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String clientId = response.getString("clientId");
                    String redirectUri = response.getString("redirectUri");
                    String scopes = response.getString("scope");

                    Log.d("clientId", clientId );
                    Log.d("redirectUri", redirectUri );
                    String scope1 = scopes.substring(2, 9);
                    String scope2 = scopes.substring(12, 17);
                    Log.d("scope", scope1 + " " + scope2);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("oauth fail", error.toString());
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        Uri uri = intent.getData();
//        String code = uri.getQueryParameter("code");

        Log.d("code", "code");
    }

    ActivityResultLauncher<Intent> testResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("suc result", result.toString());
                }
            }
    );

}