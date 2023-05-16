package com.example.wishhair.faceFunc;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.FuncLoading;
import com.example.wishhair.R;

import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaceFuncActivity extends AppCompatActivity {

    private ImageView userImage;
    private String imagePath;

    private FuncLoading loading;

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

        FaceFuncUploader uploader = new FaceFuncUploader(this);
        Button btn_submit = findViewById(R.id.func_btn_submit);
        btn_submit.setOnClickListener(view -> {
            uploader.uploadUserImages(imagePath, accessToken);
            loading.show();
            loadingTime();

        });

    }

    private void loadingTime() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(FaceFuncActivity.this, FaceResultActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
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
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            userImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
