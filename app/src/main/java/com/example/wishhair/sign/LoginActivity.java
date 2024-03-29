package com.example.wishhair.sign;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.MainActivity;
import com.example.wishhair.R;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    final static private String URL = UrlConst.URL + "/api/auth/login";

    private EditText login_id, login_pw;
    private boolean hasFaceShape;
    private String userNickName, faceShapeTag;

    private SharedPreferences loginSP;
    //https://wonpaper.tistory.com/232

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity_login);
        login_id = findViewById(R.id.ed_login_id);
        login_pw = findViewById(R.id.ed_login_pw);

//        login
        Button login_loginBtn = findViewById(R.id.btn_login);
        login_loginBtn.setOnClickListener(view -> login_request());

//        register
        Button login_registerBtn = findViewById(R.id.btn_register);
        login_registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, TermsActivity.class);
            LoginActivity.this.startActivity(intent);
        });

//        find passwd
        Button btn_findPassword = findViewById(R.id.btn_findPassword);
        btn_findPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, EmailCertActivity.class);
            intent.putExtra("pageRequest", "findPassword");
            startActivity(intent);
        });
    }

    public void login_request() {
        String id = login_id.getText().toString();
        String pw = login_pw.getText().toString();

        JSONObject userJsonObject = new JSONObject();
        try {
            userJsonObject.put("email", id);
            userJsonObject.put("pw", pw);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loginSP = getSharedPreferences("UserInfo", MODE_PRIVATE);
        //임시 로그인 패스 코드
            /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
        //서버 연동 코드
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, userJsonObject, response -> {
            Log.d("token", response.toString());
            try {
                String accessToken = response.getString("accessToken");
                String refreshToken = response.getString("refreshToken");

                SharedPreferences.Editor editor = loginSP.edit();
                editor.putString("accessToken", accessToken);
                editor.putString("refreshToken", refreshToken);
                editor.apply();

                JSONObject userInfo = response.getJSONObject("userInfo");
                userNickName = userInfo.getString("nickname");
                hasFaceShape = userInfo.getBoolean("hasFaceShape");
                faceShapeTag = userInfo.getString("faceShapeTag");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            startActivity(intent);
            finish();
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("validate error message", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(jsonObjectRequest);

    }

}
