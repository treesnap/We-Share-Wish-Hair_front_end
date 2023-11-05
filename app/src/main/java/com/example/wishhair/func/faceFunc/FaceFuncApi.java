package com.example.wishhair.func.faceFunc;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;

public interface FaceFuncApi {
    @Multipart
    @PATCH("/api/hair_style/recommend")
    Call<ResponseBody> uploadImages(
            @Header("Authorization") String token,
            @Part()MultipartBody.Part file
            );
}
