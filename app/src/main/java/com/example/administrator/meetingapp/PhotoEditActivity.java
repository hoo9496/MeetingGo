package com.example.administrator.meetingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// 프로필 사진 수정 화면
public class PhotoEditActivity extends AppCompatActivity {

    private int PICK_FROM_GALLERY = 1;

    private Button btnUpdatePhoto;
    private Bitmap bitmap;
    CircleImageView profileImage;
    private String imageURL;
    Session session;
    private String myNickname, mySex;
    private static String URL_UPDATE_PHOTO = "http://leejunho9496.cafe24.com/MeetingApp/updatePhoto.php";
    private boolean imageCheck = false; // 사진 등록 확인을 위함.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        session = new Session(this);

        btnUpdatePhoto = (Button) findViewById(R.id.btn_update_photo);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        HashMap<String, String> user = session.getMyInfo();
        myNickname = user.get(session.NICKNAME);
        mySex = user.get(session.SEX);

        // 서버에서 사진 불러오기
        imageURL = "http://leejunho9496.cafe24.com/MeetingApp/profile_image/" + myNickname  + ".jpeg";
        ImageRequest imageRequest = new ImageRequest(imageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profileImage.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PhotoEditActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(PhotoEditActivity.this).addToRequestQue(imageRequest);

        // 프로필 사진 클릭 시 갤러리 오픈
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        // 수정 완료 버튼 이벤트
        btnUpdatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageCheck==false) {
                    Toast.makeText(PhotoEditActivity.this, "사진을 변경해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    updatePhotoComplete();
                }
            }
        });
    }

    // 갤러리 불러오기
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //uri를 이용해 ImageView에 바로 이미지 넣기
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
                imageCheck = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    // back 버튼 누를 시
    @Override
    public void onBackPressed() {
        startActivity(new Intent(PhotoEditActivity.this, MyPageActivity.class));
        finish();
        super.onBackPressed();
    }

    // 사진 수정 처리
    private void updatePhotoComplete() {

        // trim() => 문자열 좌측끝과 우측끝 공백을 제거
        final String photo = getStringImage(bitmap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); // JSONObject => JSON형태의 데이터를 관리
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(PhotoEditActivity.this, "프로필 사진 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PhotoEditActivity.this, MyPageActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PhotoEditActivity.this, "Register Error1! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PhotoEditActivity.this, "Register Error2! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo", photo);
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가
    }

}
