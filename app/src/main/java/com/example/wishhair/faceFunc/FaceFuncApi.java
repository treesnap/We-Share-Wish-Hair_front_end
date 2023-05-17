package com.example.wishhair.faceFunc;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FaceFuncApi {
    @Multipart
    @PATCH("/api/user/face_shape")
    Call<ResponseBody> uploadImages(
            @Header("Authorization") String token,
            @Part()MultipartBody.Part file
            );
}
