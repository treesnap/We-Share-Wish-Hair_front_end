package com.example.wishhair.func.TagFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wishhair.func.FuncLoading;
import com.example.wishhair.R;

import java.util.ArrayList;

public class TagFuncActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private FuncLoading loading;
    private String selectedHairLength, selectedPerm;


    private CheckBox imageTag_1, imageTag_2, imageTag_3, imageTag_4, imageTag_5, imageTag_6, imageTag_7, imageTag_8, imageTag_9, imageTag_10, imageTag_11, imageTag_12;
    private final ArrayList<String> selectedImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_tag);
//        back
        Button btn_back = findViewById(R.id.tagFunc_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        hair Length RadioGroup
        RadioButton hairLength_1, hairLength_2, hairLength_3;
        hairLength_1 = findViewById(R.id.tagFunc_hairLength_1);
        hairLength_2 = findViewById(R.id.tagFunc_hairLength_2);
        hairLength_3 = findViewById(R.id.tagFunc_hairLength_3);

        selectedHairLength = hairLength_1.getText().toString();
        RadioGroup hairLengthGroup = findViewById(R.id.tagFunc_hairLength);
        hairLengthGroup.setOnCheckedChangeListener((radioGroup, checkId) -> {
            switch (checkId) {
                case R.id.tagFunc_hairLength_1 -> selectedHairLength = hairLength_1.getText().toString();
                case R.id.tagFunc_hairLength_2 -> selectedHairLength = hairLength_2.getText().toString();
                case R.id.tagFunc_hairLength_3 -> selectedHairLength = hairLength_3.getText().toString();
            }
        });

//        perm RadioGroup
        RadioButton perm_1, perm_2;
        perm_1 = findViewById(R.id.tagFunc_perm_1);
        perm_2 = findViewById(R.id.tagFunc_perm_2);

        selectedPerm = perm_1.getText().toString();
        RadioGroup permGroup = findViewById(R.id.tagFunc_perm);
        permGroup.setOnCheckedChangeListener(((radioGroup, checkId) -> {
            switch (checkId) {
                case R.id.tagFunc_perm_1 -> selectedPerm = perm_1.getText().toString();
                case R.id.tagFunc_perm_2 -> selectedPerm = perm_2.getText().toString();
            }
        }));

//        image Tags
        initImageTags();

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.tagFunc_imageTag_1:
                if (isChecked) {
                    selectedImages.add(imageTag_1.getText().toString());
                } else {
                    selectedImages.remove(imageTag_1.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_2:
                if (isChecked) {
                    selectedImages.add(imageTag_2.getText().toString());
                } else {
                    selectedImages.remove(imageTag_2.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_3:
                if (isChecked) {
                    selectedImages.add(imageTag_3.getText().toString());
                } else {
                    selectedImages.remove(imageTag_3.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_4:
                if (isChecked) {
                    selectedImages.add(imageTag_4.getText().toString());
                } else {
                    selectedImages.remove(imageTag_4.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_5:
                if (isChecked) {
                    selectedImages.add(imageTag_5.getText().toString());
                } else {
                    selectedImages.remove(imageTag_5.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_6:
                if (isChecked) {
                    selectedImages.add(imageTag_6.getText().toString());
                } else {
                    selectedImages.remove(imageTag_6.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_7:
                if (isChecked) {
                    selectedImages.add(imageTag_7.getText().toString());
                } else {
                    selectedImages.remove(imageTag_7.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_8:
                if (isChecked) {
                    selectedImages.add(imageTag_8.getText().toString());
                } else {
                    selectedImages.remove(imageTag_8.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_9:
                if (isChecked) {
                    selectedImages.add(imageTag_9.getText().toString());
                } else {
                    selectedImages.remove(imageTag_9.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_10:
                if (isChecked) {
                    selectedImages.add(imageTag_10.getText().toString());
                } else {
                    selectedImages.remove(imageTag_10.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_11:
                if (isChecked) {
                    selectedImages.add(imageTag_11.getText().toString());
                } else {
                    selectedImages.remove(imageTag_11.getText().toString());
                }
                break;
            case R.id.tagFunc_imageTag_12:
                if (isChecked) {
                    selectedImages.add(imageTag_12.getText().toString());
                } else {
                    selectedImages.remove(imageTag_12.getText().toString());
                }
                break;
            default:
                break;

        }
    }

    private void initImageTags() {
        imageTag_1 = findViewById(R.id.tagFunc_imageTag_1);
        imageTag_2 = findViewById(R.id.tagFunc_imageTag_2);
        imageTag_3 = findViewById(R.id.tagFunc_imageTag_3);
        imageTag_4 = findViewById(R.id.tagFunc_imageTag_4);
        imageTag_5 = findViewById(R.id.tagFunc_imageTag_5);
        imageTag_6 = findViewById(R.id.tagFunc_imageTag_6);
        imageTag_7 = findViewById(R.id.tagFunc_imageTag_7);
        imageTag_8 = findViewById(R.id.tagFunc_imageTag_8);
        imageTag_9 = findViewById(R.id.tagFunc_imageTag_9);
        imageTag_10 = findViewById(R.id.tagFunc_imageTag_10);
        imageTag_11 = findViewById(R.id.tagFunc_imageTag_11);
        imageTag_12 = findViewById(R.id.tagFunc_imageTag_12);

        imageTag_1.setOnCheckedChangeListener(this);
        imageTag_2.setOnCheckedChangeListener(this);
        imageTag_3.setOnCheckedChangeListener(this);
        imageTag_4.setOnCheckedChangeListener(this);
        imageTag_5.setOnCheckedChangeListener(this);
        imageTag_6.setOnCheckedChangeListener(this);
        imageTag_7.setOnCheckedChangeListener(this);
        imageTag_8.setOnCheckedChangeListener(this);
        imageTag_9.setOnCheckedChangeListener(this);
        imageTag_10.setOnCheckedChangeListener(this);
        imageTag_11.setOnCheckedChangeListener(this);
        imageTag_12.setOnCheckedChangeListener(this);
    }

    private void loadingTime() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(TagFuncActivity.this, TagResultActivity.class);
            intent.putExtra("selectedHairLength", selectedHairLength);
            intent.putExtra("selectedPerm", selectedPerm);
            intent.putStringArrayListExtra("selectedImages", selectedImages);
            startActivity(intent);
            finish();
        }, 2000);
    }

}