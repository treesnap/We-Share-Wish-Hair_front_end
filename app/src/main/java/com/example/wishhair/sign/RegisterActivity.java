package com.example.wishhair.sign;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wishhair.GetErrorMessage;
import com.example.wishhair.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    final static private String URL = UrlConst.URL + "/api/user";

    private EditText ed_pw, ed_name, ed_nickname;
    private RadioButton radioButton_man, radioButton_woman;
    private String select_sex;
    private Drawable check_success, check_fail;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_activity_register);

        Button btn_back = findViewById(R.id.botBar_btn_back);
        btn_back.setOnClickListener(view -> finish());

        check_success = ContextCompat.getDrawable(this, R.drawable.sign_check_success);
        check_fail = ContextCompat.getDrawable(this, R.drawable.sign_check_fail);

        ed_name = findViewById(R.id.sign_register_et_name);
        String nameRegex = "^[가-힣a-zA-Z]+$";
        validate(ed_name, nameRegex);

        ed_nickname = findViewById(R.id.sign_register_et_nickname);
        String nicknameRegex = "^[가-힣a-zA-Z0-9]{2,8}$";
        validate(ed_nickname, nicknameRegex);

        ed_pw = findViewById(R.id.sign_register_et_password);
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,20}$";
        validate(ed_pw, passwordRegex);

        RadioGroup radioGroup_sex = findViewById(R.id.radioGroupSEX);
        radioButton_man = findViewById(R.id.radio_sex_man);
        radioButton_woman = findViewById(R.id.radio_sex_woman);

        radioGroup_sex.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_sex_man -> select_sex = "MAN";
                case R.id.radio_sex_woman -> select_sex = "WOMAN";
            }
        });


        Button btn_join = findViewById(R.id.botBar_btn_next);
        btn_join.setOnClickListener(view -> registerRequest(select_sex));
    }

    private void validate(EditText editText, String regex) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = editText.getText().toString();
                boolean isValid = input.matches(regex);

                if (isValid) {
                    editText.setCompoundDrawablesWithIntrinsicBounds(null, null, check_success, null);
                } else {
                    editText.setCompoundDrawablesWithIntrinsicBounds(null, null, check_fail, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }



    private void registerRequest(String select_sex) {
        String id = getIntent().getStringExtra("inputEmail");
        String pw = ed_pw.getText().toString();
        String name = ed_name.getText().toString();
        String nickname = ed_nickname.getText().toString();

        JSONObject userJsonObject = new JSONObject();
        try {
            userJsonObject.put("email", id);
            userJsonObject.put("pw", pw);
            userJsonObject.put("name", name);
            userJsonObject.put("nickname", nickname);
            userJsonObject.put("sex", select_sex);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, userJsonObject, response -> {
            Log.d("register success", response.toString());
            Toast.makeText(getApplicationContext(), "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, error -> {
            String message = GetErrorMessage.getErrorMessage(error);
            Log.e("validate error message", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(jsonObjectRequest);
    }
}
