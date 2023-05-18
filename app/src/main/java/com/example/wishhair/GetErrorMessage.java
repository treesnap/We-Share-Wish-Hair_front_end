package com.example.wishhair;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class GetErrorMessage {
    public static String getErrorMessage(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String jsonError = new String(networkResponse.data);
            try {
                JSONObject jsonObject = new JSONObject(jsonError);
                return jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("getErrorMessage", "fail to get error message");
        return "null";
    }
}
