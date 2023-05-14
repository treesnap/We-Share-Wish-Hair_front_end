package com.example.wishhair.review.write;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WriteReviewActivity extends AppCompatActivity {
    private static final String TAG = "WriteReviewActivity";

    private Button btn_addPicture, btn_back, btn_submit;
    private EditText editText_content;
    private RatingBar ratingBar;

    private final HashMap<Integer, String> hairStyles = new HashMap<>();
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner hairStyleSpinner;

    private RecyclerView recyclerView;
    private WriteReviewAdapter writeReviewAdapter;
    private final WriteRequestData writeRequestData = new WriteRequestData();

    private final ArrayList<Uri> items = new ArrayList<>();
    private final ArrayList<String> itemPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity_write);
//        accessToken
        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();
//        back
        btn_back = findViewById(R.id.write_review_btn_back);
        btn_back.setOnClickListener(view -> finish());
//         hair Style info
        hairStyleSpinner = findViewById(R.id.write_review_spinner_hairStyle);
        getHairStyles(accessToken);

//        setHairStyle
        hairStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedName = parent.getItemAtPosition(position).toString();

                int selectedId = -1;
                for (Map.Entry<Integer, String> entry : hairStyles.entrySet()) {
                    if (entry.getValue().equals(selectedName)) {
                        selectedId = entry.getKey();
                        break;
                    }
                }
                writeRequestData.setHairStyleId(selectedId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

//        RatingBar
        ratingBar = findViewById(R.id.write_review_ratingBar);
        ratingBar.setOnRatingBarChangeListener((ratingBar, choice, fromUser) -> writeRequestData.setRating(choice));

//        addPicture
        recyclerView = findViewById(R.id.write_review_picture_recyclerView);
        btn_addPicture = findViewById(R.id.write_review_addPicture);
        btn_addPicture.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2222);
        });

//        content
        editText_content = findViewById(R.id.write_review_content);

//        submit
        Retrofit2MultipartUploader uploader = new Retrofit2MultipartUploader(this);
        btn_submit = findViewById(R.id.write_review_submit);
        btn_submit.setOnClickListener(view -> {
            String contents = String.valueOf(editText_content.getText());
            writeRequestData.setContent(contents);

            for (int i = 0; i < items.size(); i++) {
                itemPaths.add(getRealPathFromUri(items.get(i)));
            }
            uploader.uploadFiles(writeRequestData.getHairStyleId(), writeRequestData.getRating(), writeRequestData.getContent(), itemPaths, accessToken);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (data.getClipData() == null) { //이미지를 하나만 선택한경우
                Uri imageUri = data.getData();
                items.add(imageUri);

                writeReviewAdapter = new WriteReviewAdapter(items, getApplicationContext());
                recyclerView.setAdapter(writeReviewAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
            } else { // 이미지 여러장
                ClipData clipData = data.getClipData();
//                이미지 선택 갯수 제한
//                !TODO : 이미지 여러장 나눠서 첨부하면 4장이상 들어감 >> items maxsize 설정해서 하면 될 것 같음
                if (clipData.getItemCount() > 4) {
                    Toast.makeText(this, "사진은 4장까지만 선택 가능합니다", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        try {
                            items.add(imageUri);
                        } catch (Exception e) {
                            Log.e(TAG, "file select error", e);
                        }
                    }
                    writeReviewAdapter = new WriteReviewAdapter(items, getApplicationContext());
                    recyclerView.setAdapter(writeReviewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));
                }
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return  url;
    }

    private void getHairStyles(String accessToken) {
        String hairStylesUrl = UrlConst.URL + "/api/hair_style";
        JsonObjectRequest hairStylesRequest = new JsonObjectRequest(Request.Method.GET, hairStylesUrl, null, response -> {
            try {
                JSONArray array = response.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Integer hairStyleId = object.getInt("hairStyleId");
                    String hairStyleName = object.getString("hairStyleName");

                    hairStyles.put(hairStyleId, hairStyleName);
                }
                spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new ArrayList<>(hairStyles.values()));
                hairStyleSpinner.setAdapter(spinnerAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> Log.e("hairStyleRequestError", error.toString())) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap();
                params.put("Authorization", "bearer" + accessToken);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(hairStylesRequest);
    }

}
