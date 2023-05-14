package com.example.wishhair.review;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wishhair.CustomTokenHandler;
import com.example.wishhair.R;
import com.example.wishhair.review.write.Retrofit2MultipartUploader;
import com.example.wishhair.review.write.WriteRequestData;
import com.example.wishhair.review.write.WriteReviewAdapter;
import com.example.wishhair.sign.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewModifyActivity extends AppCompatActivity {

    private Button btn_addPicture, btn_back, btn_submit;
    private EditText editText_content;
    private RatingBar ratingBar;

    private TextView hairStyleName;

    private RecyclerView recyclerView;
    private WriteReviewAdapter writeReviewAdapter;
    private final WriteRequestData writeRequestData = new WriteRequestData();

    private final ArrayList<Uri> items = new ArrayList<>();
    private final ArrayList<String> itemPaths = new ArrayList<>();

    private ReviewItem reviewItem;
    private final HashMap<Integer, String> hairStyles = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity_modify);
//        accessToken
        CustomTokenHandler customTokenHandler = new CustomTokenHandler(this);
        String accessToken = customTokenHandler.getAccessToken();

        reviewItem = (ReviewItem) getIntent().getSerializableExtra("reviewItem");

        writeRequestData.setReviewId(reviewItem.getReviewId());
//        back
        btn_back = findViewById(R.id.modify_review_btn_back);
        btn_back.setOnClickListener(view -> finish());

//         hair Style
        hairStyleName = findViewById(R.id.modify_review_hairStyleName);
        hairStyleName.setText(reviewItem.getHairStyleName());

        getHairStyles(accessToken);

//        RatingBar
        ratingBar = findViewById(R.id.modify_review_ratingBar);
        ratingBar.setRating(Float.parseFloat(reviewItem.getScore()));
        writeRequestData.setRating(Float.parseFloat(reviewItem.getScore()));
        ratingBar.setOnRatingBarChangeListener((ratingBar, choice, fromUser) -> writeRequestData.setRating(choice));

//        picture
        recyclerView = findViewById(R.id.modify_review_picture_recyclerView);

        writeReviewAdapter = new WriteReviewAdapter(items, getApplicationContext());
        recyclerView.setAdapter(writeReviewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, true));

//        addPicture
        btn_addPicture = findViewById(R.id.modify_review_addPicture);
        btn_addPicture.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2222);
        });

//        content
        editText_content = findViewById(R.id.modify_review_content);
        editText_content.setText(reviewItem.getContent());

//        submit
        Retrofit2MultipartUploader uploader = new Retrofit2MultipartUploader(this);
        btn_submit = findViewById(R.id.modify_review_submit);
        btn_submit.setOnClickListener(view -> {
            for (int i = 0; i < items.size(); i++) {
                itemPaths.add(getRealPathFromUri(items.get(i)));
            }

            String contents = String.valueOf(editText_content.getText());
            writeRequestData.setContent(contents);
            uploader.modifyFiles(writeRequestData.getHairStyleId(), writeRequestData.getRating(), writeRequestData.getContent(), itemPaths, accessToken, writeRequestData.getReviewId());
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
                            Log.e("modify photo error", "file select error", e);
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

                for (Map.Entry<Integer, String> entry : hairStyles.entrySet()) {
                    if (entry.getValue().equals(reviewItem.getHairStyleName())) {
                        writeRequestData.setReviewId(entry.getKey());
                        break;
                    }
                }
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
