package com.example.wishhair.sign.token;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CustomTokenHandler {
    Activity activity;
    SharedPreferences sharedPreferences;

    public CustomTokenHandler(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    public String getAccessToken() {
        return sharedPreferences.getString("accessToken", "fail to get accessToken");
    }
}
