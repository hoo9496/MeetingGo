package com.example.administrator.meetingapp;

import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// 내 정보 화면
public class MyPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nickname;
    private Button btnPhotoEdit, btnProfileEdit, btnMemberWithdraw;
    Session session;
    private String getNickname, mySex;
    CircleImageView profileImage;
    private String imageURL;

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    private static String URL_DELETE_MEMBER = "http://leejunho9496.cafe24.com/MeetingApp/deleteMember.php"; // 회원 탈퇴 처리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        session = new Session(this);

        nickname = (TextView) findViewById(R.id.nickname);
        btnPhotoEdit = (Button) findViewById(R.id.btnPhotoEdit);
        btnProfileEdit = (Button) findViewById(R.id.btnProfileEdit);
        btnMemberWithdraw = (Button) findViewById(R.id.btnMemberWithdraw);

        // 액션바 설정하기
        getSupportActionBar().setTitle("MeetingGo"); // 액션바 타이틀 지정
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF999999)); // 액션바 배경색 변경

        // sideBar 만들기
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        HashMap<String, String> user = session.getMyInfo();
        getNickname = user.get(session.NICKNAME);
        mySex = user.get(session.SEX);

        nickname.setText(getNickname);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        // 서버에서 사진 불러오기
        imageURL = "http://leejunho9496.cafe24.com/MeetingApp/profile_image/" + getNickname + ".jpeg";
        ImageRequest imageRequest = new ImageRequest(imageURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profileImage.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyPageActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(MyPageActivity.this).addToRequestQue(imageRequest);

        // 프로필 사진 수정 버튼
        btnPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPageActivity.this, PhotoEditActivity.class));
                finish();
            }
        });

        // 프로필 내용 수정 버튼
        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyPageActivity.this, ProfileEditActivity.class));
                finish();
            }
        });

        // 회원탈퇴 버튼
        btnMemberWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDeleteMember();
            }
        });

    }

    // sideBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navi_introduction) {
            startActivity(new Intent(MyPageActivity.this, IntroductionActivity.class));
            finish();
        }
        if (id == R.id.navi_filltering) {
            startActivity(new Intent(MyPageActivity.this, FillterChoiceActivity.class));
            finish();
        }
        if (id == R.id.navi_hartList) {
            startActivity(new Intent(MyPageActivity.this, HartListActivity.class));
            finish();
        }
        if (id == R.id.navi_mypage) {
            startActivity(new Intent(MyPageActivity.this, MyPageActivity.class));
            finish();
        }
        if (id==R.id.navi_home) {
            startActivity(new Intent(MyPageActivity.this, MainActivity.class));
            finish();
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 회원 탈퇴 처리
    private void UserDeleteMember() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_MEMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); // JSONObject => JSON형태의 데이터를 관리
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(MyPageActivity.this, "회원 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                session.deleteMember();
                                startActivity(new Intent(MyPageActivity.this, LoginActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyPageActivity.this, "탈퇴 에러1 : " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyPageActivity.this, "탈퇴 에러2 " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", getNickname);
                params.put("my_sex", mySex);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyPageActivity.this, MainActivity.class));
        finish();
    }

}
