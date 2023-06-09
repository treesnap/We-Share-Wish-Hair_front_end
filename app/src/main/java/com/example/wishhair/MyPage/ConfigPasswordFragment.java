package com.example.wishhair.MyPage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MainActivity;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MainActivity mainActivity;
    private OnBackPressedCallback callback;
    private SharedPreferences loginSP;
    final static private String url = UrlConst.URL + "/api/user/password";
    static private String accessToken;
    private Button config_password_apply;
    private EditText config_password, config_new_password, config_verfication;
    private TextView verification_Error;
    private Drawable check_success, check_fail;
    private String newPW, verifyPW;

    public ConfigPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainActivity.onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public static ConfigPasswordFragment newInstance(String param1, String param2) {
        ConfigPasswordFragment fragment = new ConfigPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_config_password_config_fragment, container, false);
        config_password_apply = view.findViewById(R.id.config_password_apply);
        config_password = view.findViewById(R.id.config_password_input_previous);
        config_new_password = view.findViewById(R.id.config_password_input_new);
        config_verfication = view.findViewById(R.id.config_password_input_verification);
        verification_Error = view.findViewById(R.id.config_password_tv_error);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = getView().findViewById(R.id.config_password_toolbar);
        //        backButton
        Button backBtn = view.findViewById(R.id.config_password_back_btn);
        backBtn.setOnClickListener(view1 -> {
            mainActivity.ChangeFragment(8);
        });

        check_success = ContextCompat.getDrawable(getContext(), R.drawable.sign_check_success);
        check_fail = ContextCompat.getDrawable(getContext(), R.drawable.sign_check_fail);

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

        config_password_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigPasswordRequest(accessToken);
            }
        });

        config_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputValidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // 비밀번호 확인과 다를 때
        config_verfication.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verification_Error.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                configValidate();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (config_new_password.getText().toString().equals(config_verfication.getText().toString())) {
                    verification_Error.setVisibility(View.INVISIBLE);
                    config_password_apply.setEnabled(true);
                } else {
                    verification_Error.setVisibility(View.VISIBLE);
                    config_password_apply.setEnabled(false);
                }
            }
        });

    }

    public void ConfigPasswordRequest(String accessToken) {
        String password_new = config_new_password.getText().toString();
        String password_old = config_password.getText().toString();
        JSONObject obj = new JSONObject();

        try {
            obj.put("oldPassword",password_old);
            obj.put("newPassword",password_new);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url , obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(getContext(), R.style.ConfigAlertDialogTheme);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.mypage_config_dialog, getView().findViewById(R.id.dialog_config_layout));
                myAlertBuilder.setView(v);
                AlertDialog alertDialog = myAlertBuilder.create();
                TextView tv = v.findViewById(R.id.dialog_config_tv);
                tv.setText("비밀번호 변경 완료 !");

                v.findViewById(R.id.dialog_config_OKbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                // 다이얼로그 형태 지우기
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                alertDialog.show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("test","error test 409");

                int errorCode = volleyError.networkResponse != null ? volleyError.networkResponse.statusCode : -1;
                switch (errorCode) {
                    case 400:
                        // Bad Request 에러 처리
                        AlertDialog.Builder myAlertBuilder_err400 = new AlertDialog.Builder(getContext(), R.style.ConfigAlertDialogTheme);
                        View v_err400 = LayoutInflater.from(getContext()).inflate(R.layout.mypage_config_dialog, getView().findViewById(R.id.dialog_config_layout));
                        myAlertBuilder_err400.setView(v_err400);
                        AlertDialog alertDialog = myAlertBuilder_err400.create();

                        TextView dialogTv = v_err400.findViewById(R.id.dialog_config_tv);
                        dialogTv.setText("기존 비밀번호가 일치하지 않습니다");
                        v_err400.findViewById(R.id.dialog_config_OKbtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });

                        // 다이얼로그 형태 지우기
                        if (alertDialog.getWindow() != null) {
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        }
                        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
                        alertDialog.show();
                        break;
                    case 401:
                        // Unauthorized 에러 처리
                        // 헤더 미포함 시, 로그인이 필요합니다
                        break;
                    case 404:
                        // Not Found 에러 처리
                        break;
                    default:
                        // 기타 에러 처리
                        break;
                }
            }
        }) {

//            @Nullable
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap();
//                params.put("Authorization", "bearer" + accessToken);
//                return params;
//            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }
    private boolean inputValidate() {
        newPW = config_new_password.getText().toString();
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,20}$";


        boolean validateInput = Pattern.matches(passwordPattern, newPW);
        if (validateInput) {
            config_new_password.setCompoundDrawablesWithIntrinsicBounds(null, null, check_success, null);
            return true;
        } else {
            config_new_password.setCompoundDrawablesWithIntrinsicBounds(null, null, check_fail, null);
            return false;
        }
    }

    private boolean configValidate() {
        verifyPW = config_verfication.getText().toString();
        if (verifyPW.equals(newPW)) {
            config_verfication.setCompoundDrawablesWithIntrinsicBounds(null, null, check_success, null);
            return true;
        } else {
            config_verfication.setCompoundDrawablesWithIntrinsicBounds(null, null, check_fail, null);
            return false;
        }
    }
}