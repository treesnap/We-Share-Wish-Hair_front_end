package com.example.wishhair.func.faceFunc;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.wishhair.BuildConfig;
import com.example.wishhair.func.UploadCallback;
import com.example.wishhair.sign.UrlConst;
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public FaceFuncUploader(Context context) {
        //로그를 보기 위한 Interceptor
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new OkHttpProfilerInterceptor());
        }
        OkHttpClient client = builder
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlConst.URL)
                .client(client)
                .build();
        this.api = retrofit.create(FaceFuncApi.class);
        this.context = context;
    }

    public void uploadUserImages(String imagePath, String accessToken, List<String> tags, UploadCallback callback) {
        File file = new File(imagePath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        String fileName = "userImage.jpg";
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", fileName, imageBody);

        Call<ResponseBody> call = api.uploadImages("bearer" + accessToken, imagePart, tags);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    JSONObject resultObject = new JSONObject(response.body().string());
                    String result = resultObject.getString("result");
                    callback.onUploadCallback(response.isSuccessful(), result);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onUploadCallback(false, null);
            }
        });
    }
}
