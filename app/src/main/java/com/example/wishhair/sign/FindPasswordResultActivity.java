package com.example.wishhair.sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class FindPasswordResultActivity extends AppCompatActivity {

    private EditText ed_pw_input, ed_pw_config;
    private Drawable check_success, check_fail;
    private String pw_input, pw_config;
    private FrameLayout view_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_result);

//        back
        Button btn_back = findViewById(R.id.botBar_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        finish
        Button btn_next = findViewById(R.id.botBar_btn_next);
        btn_next.setVisibility(View.INVISIBLE);

        check_success = ContextCompat.getDrawable(this, R.drawable.sign_check_success);
        check_fail = ContextCompat.getDrawable(this, R.drawable.sign_check_fail);

        ed_pw_input = findViewById(R.id.find_password_input_et_password);
        ed_pw_input.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ed_pw_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputValidate();
                configValidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        ed_pw_config = findViewById(R.id.find_password_config_et_password);
        ed_pw_config.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ed_pw_config.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                configValidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputValidate() && configValidate()) {
                    btn_next.setVisibility(View.VISIBLE);
                } else {
                    btn_next.setVisibility(View.INVISIBLE);
                }
            }
        });

        String userEmail = getIntent().getStringExtra("inputEmail");
        view_done = findViewById(R.id.find_password_view_done);
        btn_next.setOnClickListener(view -> passwordRequest(userEmail, pw_config));

        Button btn_finish = findViewById(R.id.find_password_btn_done);
        btn_finish.setOnClickListener(view -> finish());
    }

    private boolean inputValidate() {
        pw_input = ed_pw_input.getText().toString();
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,20}$";

        boolean validateInput = Pattern.matches(passwordPattern, pw_input);
        if (validateInput) {
            ed_pw_input.setCompoundDrawablesWithIntrinsicBounds(null, null, check_success, null);
            return true;
        } else {
            ed_pw_input.setCompoundDrawablesWithIntrinsicBounds(null, null, check_fail, null);
            return false;
        }
    }

    private boolean configValidate() {
        pw_config = ed_pw_config.getText().toString();
        if (pw_config.equals(pw_input)) {
            ed_pw_config.setCompoundDrawablesWithIntrinsicBounds(null, null, check_success, null);
            return true;
        } else {
            ed_pw_config.setCompoundDrawablesWithIntrinsicBounds(null, null, check_fail, null);
            return false;
        }
    }

    private void passwordRequest(String email, String newPassword) {
        String refreshUrl = UrlConst.URL + "/api/user/refresh/password";
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("email", email);
            requestObject.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, refreshUrl, requestObject, response -> {
            ed_pw_config.setEnabled(false);
            ed_pw_input.setEnabled(false);
            view_done.setVisibility(View.VISIBLE);}, this::getErrorMessage);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void getErrorMessage(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String jsonError = new String(networkResponse.data);
            try {
                JSONObject jsonObject = new JSONObject(jsonError);
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("getErrorMessage", "fail to get error message");
        }
    }
}
