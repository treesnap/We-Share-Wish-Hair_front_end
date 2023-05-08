package com.example.wishhair.faceFunc;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.wishhair.R;

public class FaceFuncLoading extends Dialog {
    public FaceFuncLoading(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.func_face_loading);
    }
}
