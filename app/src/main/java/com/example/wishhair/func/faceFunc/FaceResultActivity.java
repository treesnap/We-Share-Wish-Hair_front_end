package com.example.wishhair.func.faceFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wishhair.R;

public class FaceResultActivity extends AppCompatActivity {

    private TextView userName, faceShape, resultStyleName;
    private ImageView resultStyleImage;
    private String resultShape, hairStyleName, googleDriverLink;
    private Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_faceresult);

        Button btn_back = findViewById(R.id.faceResult_btn_back);
        btn_back.setOnClickListener(view -> finish());

        btn_finish = findViewById(R.id.faceResult_btn_finish);
        btn_finish.setOnClickListener(view -> finish());

        userName = findViewById(R.id.faceResult_userName);
        faceShape = findViewById(R.id.faceResult_faceShape);

        resultShape = getIntent().getStringExtra("face_shape");
        hairStyleName = getIntent().getStringExtra("hairstyle_name");
        googleDriverLink = getIntent().getStringExtra("google_drive_link");

//        result TODO: 사진 불러오기 안돼
        resultStyleImage = findViewById(R.id.faceResult_recommendStyle_pic);
        Glide.with((Context) this).load(googleDriverLink).into(resultStyleImage);

        resultStyleName = findViewById(R.id.faceResult_recommendStyle_styleName);
        resultStyleName.setText(hairStyleName);

        SharedPreferences sp = getSharedPreferences("userNickName", MODE_PRIVATE);

        userName.setText(sp.getString("userNickName", "fail"));
        String resultShapeText = resultShape + "얼굴";
        faceShape.setText(resultShapeText);
    }

}
