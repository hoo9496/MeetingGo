package com.example.administrator.meetingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// 하트리스트의 내가 호감을 표시한 이성의 정보 화면
public class SendHartListUserInfoActivity extends AppCompatActivity {

    private TextView nickname, age, blood, height, bodyType, area, job, personality, hobby, religion, smoking, alcohol;
    private String imageURL;
    CircleImageView profileImage;
    private String userNickname, userAge, userBlood, userHeight, userBodyType, userArea, userJob, userPersonality, userHobby, userReligion, userSmoking, userAlcohol;
    private static String URL_USER_INFO = "http://leejunho9496.cafe24.com/MeetingApp/hartList_userInfo.php";
    private String mySex;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_hart_list_user_info);

        nickname = (TextView) findViewById(R.id.nickname);
        age = (TextView) findViewById(R.id.age);
        blood = (TextView) findViewById(R.id.blood);
        area = (TextView) findViewById(R.id.area);
        height = (TextView) findViewById(R.id.height);
        bodyType = (TextView) findViewById(R.id.bodyType);
        job = (TextView) findViewById(R.id.job);
        personality = (TextView) findViewById(R.id.personality);
        hobby = (TextView) findViewById(R.id.hobby);
        religion = (TextView) findViewById(R.id.religion);
        smoking = (TextView) findViewById(R.id.smoking);
        alcohol = (TextView) findViewById(R.id.alcohol);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        userNickname = getIntent().getStringExtra("userNickname");

        session = new Session(this);
        HashMap<String, String> user = session.getMyInfo();
        mySex = user.get(session.SEX);

        sendUserInfo();
    }

    // 클릭한 이성 정보 제공
    private void sendUserInfo() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_USER_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("userInfo");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userNickname = object.getString("nickname").trim();
                                    userAge = object.getString("age").trim();
                                    userBlood = object.getString("blood").trim();
                                    userHeight = object.getString("height").trim();
                                    userBodyType = object.getString("bodyType").trim();
                                    userArea = object.getString("area").trim();
                                    userJob = object.getString("job").trim();
                                    userPersonality = object.getString("personality").trim();
                                    userHobby = object.getString("hobby").trim();
                                    userReligion = object.getString("religion").trim();
                                    userSmoking = object.getString("smoking").trim();
                                    userAlcohol = object.getString("alcohol").trim();

                                    nickname.setText(userNickname);
                                    age.setText(userAge);
                                    blood.setText(userBlood);
                                    height.setText(userHeight);
                                    bodyType.setText(userBodyType);
                                    area.setText(userArea);
                                    job.setText(userJob);
                                    personality.setText(userPersonality);
                                    hobby.setText(userHobby);
                                    religion.setText(userReligion);
                                    smoking.setText(userSmoking);
                                    alcohol.setText(userAlcohol);
                                }

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingApp/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(SendHartListUserInfoActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(SendHartListUserInfoActivity.this).addToRequestQue(imageRequest);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SendHartListUserInfoActivity.this, "Error1 : 이성 정보를 불러오지 못했습니다.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendHartListUserInfoActivity.this, "Error2 : 이성 정보를 불러오지 못했습니다." + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_nickname", userNickname);
                params.put("my_sex", mySex);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // back 버튼을 누른 경우
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SendHartListUserInfoActivity.this, HartListActivity.class));
        finish();
        super.onBackPressed();
    }

}
