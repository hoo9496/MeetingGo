package com.example.administrator.meetingapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 하트리스트 화면
public class HartListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Session session;
    private String myNickname, mySex;
    private static String URL_SEND_HARTLIST = "http://leejunho9496.cafe24.com/MeetingApp/sendHartList.php";
    private static String URL_GET_HARTLIST = "http://leejunho9496.cafe24.com/MeetingApp/getHartList.php";
    private static String URL_MATCHING_LIST = "http://leejunho9496.cafe24.com/MeetingApp/matchingListInfo.php";
    private ArrayList<String> sendHart = new ArrayList<String>(), getHart = new ArrayList<String>(), matchingHart = new ArrayList<String>(); // 닉네임 저장

    private String userNickname;

    private ListView sendListView, getListView, matchingListView;
    private ImageListAdapter mImageListAdapter;
    private List<String> sendList = new ArrayList<>(), getList = new ArrayList<>(), matchingList = new ArrayList<>(); // 닉네임을 통해 이미지 경로 저장

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hart_list);

        session = new Session(this);
        HashMap<String, String> user = session.getMyInfo();
        myNickname = user.get(session.NICKNAME);
        mySex = user.get(session.SEX);

        sendHartLists();
        getHartLists();
        matchingLists();

        sendListView = (ListView)findViewById(R.id.send_feeling_image);
        getListView = (ListView)findViewById(R.id.get_feeling_image);
        matchingListView = (ListView)findViewById(R.id.matching_image);

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

        sendListView.setOnItemClickListener(sendListener); // 보낸 하트 리스트 뷰 클릭 이벤트
        getListView.setOnItemClickListener(getListener); // 받은 하트 리스트 뷰 클릭 이벤트
        matchingListView.setOnItemClickListener(matchingListener); // 받은 하트 리스트 뷰 클릭 이벤트
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
            startActivity(new Intent(HartListActivity.this, IntroductionActivity.class));
            finish();
        }
        if (id == R.id.navi_filltering) {
            startActivity(new Intent(HartListActivity.this, FillterChoiceActivity.class));
            finish();
        }
        if (id == R.id.navi_hartList) {
            startActivity(new Intent(HartListActivity.this, HartListActivity.class));
            finish();
        }
        if (id == R.id.navi_mypage) {
            startActivity(new Intent(HartListActivity.this, MyPageActivity.class));
            finish();
        }
        if (id==R.id.navi_home) {
            startActivity(new Intent(HartListActivity.this, MainActivity.class));
            finish();
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 보낸 하트 리스트 뷰 클릭 이벤트
    AdapterView.OnItemClickListener sendListener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast.makeText(HartListActivity.this, sendHart.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(HartListActivity.this, SendHartListUserInfoActivity.class);
            intent.putExtra("userNickname", sendHart.get(position));
            startActivity(intent);
            finish();
        }
    };

    // 받은 하트 리스트 뷰 클릭 이벤트
    AdapterView.OnItemClickListener getListener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast.makeText(HartListActivity.this, sendHart.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(HartListActivity.this, GetHartListUserInfoActivity.class);
            intent.putExtra("userNickname", getHart.get(position));
            startActivity(intent);
            finish();
        }
    };

    // 매칭된 이성 리스트 뷰 클릭 이벤트
    AdapterView.OnItemClickListener matchingListener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast.makeText(HartListActivity.this, sendHart.get(position), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(HartListActivity.this, MatchingListUserInfoActivity.class);
            intent.putExtra("userNickname", matchingHart.get(position));
            startActivity(intent);
            finish();
        }
    };

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 내가 호감을 표시한 이성 리스트
    private void sendHartLists() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_HARTLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("sendHart");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userNickname = object.getString("user_nickname").trim();
                                    // nick = strnick.toString();
                                    sendHart.add(userNickname);
                                    sendList.add("http://leejunho9496.cafe24.com/MeetingApp/profile_image/"+sendHart.get(i)+".jpeg");
                                }
                            }
                            mImageListAdapter = new ImageListAdapter(HartListActivity.this, sendList);
                            sendListView.setAdapter(mImageListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HartListActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 나에게 호감을 표시한 이성 리스트
    private void getHartLists() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_HARTLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("getHart");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userNickname = object.getString("user_nickname").trim();
                                    getHart.add(userNickname);
                                    getList.add("http://leejunho9496.cafe24.com/MeetingApp/profile_image/"+getHart.get(i)+".jpeg");
                                }
                            }
                            mImageListAdapter = new ImageListAdapter(HartListActivity.this, getList);
                            getListView.setAdapter(mImageListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HartListActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 나와 매칭에 성공한 이성 리스트
    private void matchingLists() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MATCHING_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("matching");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    userNickname = object.getString("user_nickname").trim();
                                    matchingHart.add(userNickname);
                                    matchingList.add("http://leejunho9496.cafe24.com/MeetingApp/profile_image/"+matchingHart.get(i)+".jpeg");
                                }
                            }
                            mImageListAdapter = new ImageListAdapter(HartListActivity.this, matchingList);
                            matchingListView.setAdapter(mImageListAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HartListActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HartListActivity.this, MainActivity.class));
        finish();
    }

}
