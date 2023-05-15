package com.example.wishhair.faceFunc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wishhair.BuildConfig;
import com.example.wishhair.sign.UrlConst;
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FaceFuncUploader {
    private final FaceFuncApi api;
    private final Context context;

    public FaceFuncUploader(FaceFuncApi api, Context context) {
        //로그를 보기 위한 Interceptor
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConst.URL)
                .client(client)
                .build();
        this.api = retrofit.create(FaceFuncApi.class);
        this.context = context;
    }

    public void uploadUserImages(String imagePath, String accessToken) {

        File file = new File(imagePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        String fileName = "userImage.jpg";
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("files", fileName, imageBody);

        Call<ResponseBody> call = api.uploadImages("bearer" + accessToken, imagePart);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                TODO 다음 페이지 연결
                Intent intent = new Intent();
                context.startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Func Image Upload fail", t.toString());
            }
        });

    }
}
