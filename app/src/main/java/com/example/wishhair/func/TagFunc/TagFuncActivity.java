package com.example.wishhair.func.TagFunc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.example.wishhair.R;
import com.example.wishhair.func.faceFunc.FaceFuncActivity;

import java.util.ArrayList;

public class TagFuncActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private String selectedHairLength, selectedPerm;
//      #TODO  태그 하드코딩 >> enum 활용해서 수정 가능
    private final ArrayList<String> selectedImages = new ArrayList<>();
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_activity_tag);
//        back
        Button btn_back = findViewById(R.id.tagFunc_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        hair Length RadioGroup
        findViewById(R.id.tagFunc_hairLength_1);
        findViewById(R.id.tagFunc_hairLength_2);
        findViewById(R.id.tagFunc_hairLength_3);

        selectedHairLength = "H_LONG";
        RadioGroup hairLengthGroup = findViewById(R.id.tagFunc_hairLength);
        hairLengthGroup.setOnCheckedChangeListener((radioGroup, checkId) -> {
            switch (checkId) {
                case R.id.tagFunc_hairLength_1 -> selectedHairLength = "H_LONG";
                case R.id.tagFunc_hairLength_2 -> selectedHairLength = "MEDIUM";
                case R.id.tagFunc_hairLength_3 -> selectedHairLength = "SHORT";
            }
        });

//        perm RadioGroup
        findViewById(R.id.tagFunc_perm_1);
        findViewById(R.id.tagFunc_perm_2);

        selectedPerm = "PERM";
        RadioGroup permGroup = findViewById(R.id.tagFunc_perm);
        permGroup.setOnCheckedChangeListener(((radioGroup, checkId) -> {
            switch (checkId) {
                case R.id.tagFunc_perm_1 -> selectedPerm = "PERM";
                case R.id.tagFunc_perm_2 -> selectedPerm = "NO_PERM";
            }
        }));

//        image Tags
        initImageTags();
//        submit
        Button btn_submit = findViewById(R.id.tagFunc_btn_submit);
        btn_submit.setOnClickListener(view -> {
            Intent intent = new Intent(TagFuncActivity.this, FaceFuncActivity.class);
            intent.putExtra("selectedHairLength", selectedHairLength);
            intent.putExtra("selectedPerm", selectedPerm);
            intent.putStringArrayListExtra("selectedImages", selectedImages);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        int id = compoundButton.getId();
        switch (id) {
            case R.id.tagFunc_imageTag_1:
                if (isChecked) {
                    selectedImages.add("CUTE");
                } else {
                    selectedImages.remove("CUTE");
                }
                break;
            case R.id.tagFunc_imageTag_2:
                if (isChecked) {
                    selectedImages.add("UPSTAGE");
                } else {
                    selectedImages.remove("UPSTAGE");
                }
                break;
            case R.id.tagFunc_imageTag_3:
                if (isChecked) {
                    selectedImages.add("NEAT");
                } else {
                    selectedImages.remove("NEAT");
                }
                break;
            case R.id.tagFunc_imageTag_4:
                if (isChecked) {
                    selectedImages.add("COMMON");
                } else {
                    selectedImages.remove("COMMON");
                }
                break;
            case R.id.tagFunc_imageTag_5:
                if (isChecked) {
                    selectedImages.add("LIGHT");
                } else {
                    selectedImages.remove("LIGHT");
                }
                break;
            case R.id.tagFunc_imageTag_6:
                if (isChecked) {
                    selectedImages.add("HEAVY");
                } else {
                    selectedImages.remove("HEAVY");
                }
                break;
            case R.id.tagFunc_imageTag_7:
                if (isChecked) {
                    selectedImages.add("COOL");
                } else {
                    selectedImages.remove("COOL");
                }
                break;
            case R.id.tagFunc_imageTag_8:
                if (isChecked) {
                    selectedImages.add("VOLUMINOUS");
                } else {
                    selectedImages.remove("VOLUMINOUS");
                }
                break;
            case R.id.tagFunc_imageTag_9:
                if (isChecked) {
                    selectedImages.add("TRENDY");
                } else {
                    selectedImages.remove("TRENDY");
                }
                break;
            case R.id.tagFunc_imageTag_10:
                if (isChecked) {
                    selectedImages.add("FORMAL");
                } else {
                    selectedImages.remove("FORMAL");
                }
                break;
            case R.id.tagFunc_imageTag_11:
                if (isChecked) {
                    selectedImages.add("MANLY");
                } else {
                    selectedImages.remove("MANLY");
                }
                break;
            case R.id.tagFunc_imageTag_12:
                if (isChecked) {
                    selectedImages.add("SOFTY");
                } else {
                    selectedImages.remove("SOFTY");
                }
                break;
            default:
                break;

        }
    }

    private void initImageTags() {
        CheckBox imageTag_1, imageTag_2, imageTag_3, imageTag_4, imageTag_5, imageTag_6, imageTag_7, imageTag_8, imageTag_9, imageTag_10, imageTag_11, imageTag_12;
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

}
