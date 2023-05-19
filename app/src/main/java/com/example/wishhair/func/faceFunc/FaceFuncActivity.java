package com.example.wishhair.func.faceFunc;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wishhair.sign.token.CustomTokenHandler;
import com.example.wishhair.func.FuncLoading;
import com.example.wishhair.R;
import com.example.wishhair.func.UploadCallback;

import java.io.FileNotFoundException;
import java.io.IOException;

public class FaceFuncActivity extends AppCompatActivity implements UploadCallback {

    private ImageView userImage;
    private String imagePath;

    private FaceFuncUploader uploader;
    private FuncLoading loading;

    @Override
    public void onUploadCallback(boolean isSuccess, String result) {
        if (isSuccess) {
            loading.dismiss();
            Intent intent = new Intent(FaceFuncActivity.this, FaceResultActivity.class);
            intent.putExtra("result", result);
            startActivity(intent);
            finish();
        } else {
            loading.dismiss();
            Log.e("loading", "fail, done");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.func_face_activity);
//        accessToken
        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();

//        back
        Button btn_back = findViewById(R.id.func_btn_back);
        btn_back.setOnClickListener(view -> finish());

//        selectUserImage
        userImage = findViewById(R.id.func_faceImage);
        userImage.setOnClickListener(view -> setImageView(userImage));

//        loading
        loading = new FuncLoading(this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        uploader = new FaceFuncUploader(this);
        Button btn_submit = findViewById(R.id.func_btn_submit);
        btn_submit.setOnClickListener(view -> {
            uploader.uploadUserImages(imagePath, accessToken, this);
            loading.show();
        });
    }


    private void setImageView(ImageView imageView) {
//        기존에 이미지 지움
        imageView.setImageDrawable(null);
        imagePath = null;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityResult.launch(intent);
//        배경 남아서 못생겨지는거 임시방편
        imageView.setBackground(null);
    }

    // TODO 사진 사이즈 별로 다르게 들어감
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        imagePath = getRealPathFromUri(uri);
                        Glide.with(getApplicationContext()).load(uri).into(userImage);
                    }
                }
            }
    );

    private String getRealPathFromUri(Uri uri) {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return  url;
    }
}
