package com.example.wishhair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import com.example.wishhair.faceFunc.FaceFuncActivity;
import com.example.wishhair.faceFunc.FaceResultActivity;

public class TagFuncActivity extends AppCompatActivity {

    private FuncLoading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_tag);
//        back
        Button btn_back = findViewById(R.id.tagFunc_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        loading
        loading = new FuncLoading(this);
        TextView loadingMessage = loading.findViewById(R.id.loading_message);
        loadingMessage.setText("태그에 적합한\n헤어스타일을 찾고있어요");
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        submit
        Button btn_submit = findViewById(R.id.tagFunc_btn_submit);
        btn_submit.setOnClickListener(view -> {
            loading.show();
            loadingTime();
        });

    }

    private void loadingTime() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(TagFuncActivity.this, TagResultActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }


}
