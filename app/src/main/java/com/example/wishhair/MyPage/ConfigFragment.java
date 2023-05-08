package com.example.wishhair.MyPage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.MainActivity;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

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
    final static private String url = UrlConst.URL + "/api/user";
    static private String accessToken;
    Button config_apply;
    EditText configNickname;
    RadioButton btnMan;
    RadioButton btnWoman;
    RadioGroup btnGroup;
    String sexAfter;


    public ConfigFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainActivity.ChangeFragment(2);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformationModifyFragment.
     */



    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
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
        View view = inflater.inflate(R.layout.my_config_fragment, container, false);
        config_apply = view.findViewById(R.id.config_apply);
        configNickname = view.findViewById(R.id.config_input_nickname);
        btnMan = view.findViewById(R.id.config_radiobtn_sex_man);
        btnWoman = view.findViewById(R.id.config_radiobtn_sex_woman);
        btnGroup = view.findViewById(R.id.config_radiobtn_group);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Toolbar toolbar = getView().findViewById(R.id.config_toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.ChangeFragment(2);
            }
        });

        loginSP = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        accessToken = loginSP.getString("accessToken", "fail acc");

//        ImageButton btn = view.findViewById(R.id.modify_commit_button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModifyRequest(accessToken);
//            }
//        });

        config_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigRequest(accessToken);

            }
        });
        btnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btn = btnGroup.findViewById(i);
                switch (i) {
                    case R.id.config_radiobtn_sex_man -> {
                        sexAfter = "MAN";
                        break;
                    }
                    case R.id.config_radiobtn_sex_woman -> {
                        sexAfter = "WOMAN";
                        break;
                    }
                }
            }
        });
    }

    public void ConfigRequest(String accessToken) {
        String nickname_after = configNickname.getText().toString();
        JSONObject obj = new JSONObject();

        try {
            obj.put("nickname",nickname_after);
            obj.put("sex", sexAfter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, url , obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(getContext(), R.style.ConfigAlertDialogTheme);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.mypage_config_dialog, getView().findViewById(R.id.dialog_config_layout));
                myAlertBuilder.setView(v);
                AlertDialog alertDialog = myAlertBuilder.create();

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
                        break;
                    case 401:
                        // Unauthorized 에러 처리
                        // 헤더 미포함 시, 로그인이 필요합니다
                        break;
                    case 404:
                        // Not Found 에러 처리
                        break;
                    case 409:
                        // 닉네임 중복
                        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(getContext(), R.style.ConfigAlertDialogTheme);
                        View v = LayoutInflater.from(getContext()).inflate(R.layout.mypage_config_dialog, getView().findViewById(R.id.dialog_config_layout));
                        myAlertBuilder.setView(v);
                        AlertDialog alertDialog = myAlertBuilder.create();

                        TextView dialogTv = v.findViewById(R.id.dialog_config_tv);
                        dialogTv.setText("동일한 닉네임이 존재합니다 !");
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
}