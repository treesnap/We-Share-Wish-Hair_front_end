package com.example.wishhair.func;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.wishhair.R;

public class FuncLoading extends Dialog {
    public FuncLoading(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.func_loading);
    }
}
