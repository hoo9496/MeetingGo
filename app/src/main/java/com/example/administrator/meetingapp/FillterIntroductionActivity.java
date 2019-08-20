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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// 소개 조건 선택을 통한 이성소개 화면
public class FillterIntroductionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nickname, age, blood, height, bodyType, area, job, personality, hobby, religion, smoking, alcohol;
    CircleImageView profileImage;
    private Button btn_hart, btn_pre, btn_next;
    private static String URL_FILLTER_AREA = "http://leejunho9496.cafe24.com/MeetingApp/fillterArea.php"; // 같은 지역 이성 소개
    private static String URL_FILLTER_JOB = "http://leejunho9496.cafe24.com/MeetingApp/fillterJob.php"; // 같은 직종 이성 소개
    private static String URL_FILLTER_RELIGION = "http://leejunho9496.cafe24.com/MeetingApp/fillterReligion.php"; // 같은 종교 이성 소개
    private static String URL_FILLTER_NO_SMOKING = "http://leejunho9496.cafe24.com/MeetingApp/fillterNoSmoking.php"; // 비흡연 이성 소개
    private static String URL_FILLTER_BLOOD = "http://leejunho9496.cafe24.com/MeetingApp/fillterBlood.php"; // 같은 혈액형 이성 소개
    private static String URL_USER_SAVE_SEND_LIST = "http://leejunho9496.cafe24.com/MeetingApp/saveHartList.php"; // 하트리스트에 저장
    private static String URL_HART_VALIDATE = "http://leejunho9496.cafe24.com/MeetingApp/hartValidate.php"; // 중복 검사
    Session session;
    private String myNickname, mySex, myBlood, myArea, myJob, myReligion; // 내 닉네임, 내 성별 (남, 여 구분)
    private String userNickname, userAge, userBlood, userHeight, userBodyType, userArea, userJob, userPersonality, userHobby, userReligion, userSmoking, userAlcohol;
    private String imageURL;
    private ArrayList<String> arrayNickname = new ArrayList<String>(), arrayAge = new ArrayList<String>(), arrayBlood = new ArrayList<String>(), arrayHeight = new ArrayList<String>(),
            arrayBodyType = new ArrayList<String>(), arrayArea = new ArrayList<String>(), arrayJob = new ArrayList<String>(), arrayPersonality = new ArrayList<String>(),
            arrayHobby = new ArrayList<String>(), arrayReligion = new ArrayList<String>(), arraySmoking = new ArrayList<String>(), arrayAlcohol = new ArrayList<String>();

    private int count = 0; // 배열 증가 및 감소
    private String choiceResult = ""; // FillterChoiceActivity에서 준 intent 값

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillter_introduction);

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
        btn_hart = (Button) findViewById(R.id.btn_hart);
        btn_pre = (Button) findViewById(R.id.pre_introduction);
        btn_next = (Button) findViewById(R.id.next_introduction);

        session = new Session(this);
        HashMap<String, String> user = session.getMyInfo();
        myNickname = user.get(session.NICKNAME);
        mySex = user.get(session.SEX);
        myBlood = user.get(session.BLOOD);
        myArea = user.get(session.AREA);
        myJob = user.get(session.JOB);
        myReligion = user.get(session.RELIGION);

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

        choiceResult = getIntent().getStringExtra("btnChoice");

        if (choiceResult.equals("area")) {
            fillterArea();
        } else if (choiceResult.equals("job")) {
            fillterJob();
        } else if (choiceResult.equals("religion")) {
            fillterReligion();
        } else if (choiceResult.equals("no_smoking")) {
            fillterNoSmoking();
        } else { // choiceResult.equals("blood")
            fillterBlood();
        }

        // 하트 버튼 누를 시
        btn_hart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HartValidate(); // 하트리스트에 있는지 검사 후 리스트에 없는 이성일 시 하트리스트에 추가
            }
        });

        // <(pre) 버튼 누를 시
        btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;

                if (count < 0) {
                    count = 0;
                    Toast.makeText(FillterIntroductionActivity.this, "처음 소개된 이성입니다.", Toast.LENGTH_SHORT).show();
                }

                nickname.setText(arrayNickname.get(count));
                age.setText(arrayAge.get(count));
                blood.setText(arrayBlood.get(count));
                height.setText(arrayHeight.get(count));
                bodyType.setText(arrayBodyType.get(count));
                area.setText(arrayArea.get(count));
                job.setText(arrayJob.get(count));
                personality.setText(arrayPersonality.get(count));
                hobby.setText(arrayHobby.get(count));
                religion.setText(arrayReligion.get(count));
                smoking.setText(arraySmoking.get(count));
                alcohol.setText(arrayAlcohol.get(count));

                // 서버에서 이미지 가져오기
                final String imageNickname = nickname.getText().toString().trim();
                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                ImageRequest imageRequest = new ImageRequest(imageURL,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                profileImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
            }
        });

        // >(next) 버튼 누를 시
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                if (count >= arrayNickname.size()) {
                    count = 0;
                    Toast.makeText(FillterIntroductionActivity.this, "더 이상 새로운 소개 이성이 없습니다." + "\n" + "처음 이성을 다시 소개합니다.", Toast.LENGTH_SHORT).show();
                }

                nickname.setText(arrayNickname.get(count));
                age.setText(arrayAge.get(count));
                blood.setText(arrayBlood.get(count));
                height.setText(arrayHeight.get(count));
                bodyType.setText(arrayBodyType.get(count));
                area.setText(arrayArea.get(count));
                job.setText(arrayJob.get(count));
                personality.setText(arrayPersonality.get(count));
                hobby.setText(arrayHobby.get(count));
                religion.setText(arrayReligion.get(count));
                smoking.setText(arraySmoking.get(count));
                alcohol.setText(arrayAlcohol.get(count));

                // 서버에서 이미지 가져오기
                final String imageNickname = nickname.getText().toString().trim();
                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                ImageRequest imageRequest = new ImageRequest(imageURL,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                profileImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
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
            startActivity(new Intent(FillterIntroductionActivity.this, IntroductionActivity.class));
            finish();
        }
        if (id == R.id.navi_filltering) {
            startActivity(new Intent(FillterIntroductionActivity.this, FillterChoiceActivity.class));
            finish();
        }
        if (id == R.id.navi_hartList) {
            startActivity(new Intent(FillterIntroductionActivity.this, HartListActivity.class));
            finish();
        }
        if (id == R.id.navi_mypage) {
            startActivity(new Intent(FillterIntroductionActivity.this, MyPageActivity.class));
            finish();
        }
        if (id==R.id.navi_home) {
            startActivity(new Intent(FillterIntroductionActivity.this, MainActivity.class));
            finish();
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// 필터링 메서드 ///////////////////////////////////////////////////////////////////////////////

    // 같은 지역 이성 필터링 결과
    private void fillterArea() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILLTER_AREA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("area");

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

                                    arrayNickname.add(userNickname);
                                    arrayAge.add(userAge);
                                    arrayBlood.add(userBlood);
                                    arrayHeight.add(userHeight);
                                    arrayBodyType.add(userBodyType);
                                    arrayArea.add(userArea);
                                    arrayJob.add(userJob);
                                    arrayPersonality.add(userPersonality);
                                    arrayHobby.add(userHobby);
                                    arrayReligion.add(userReligion);
                                    arraySmoking.add(userSmoking);
                                    arrayAlcohol.add(userAlcohol);
                                }

                                // 필터링 하자마자 첫 이성 제공을 위함.
                                nickname.setText(arrayNickname.get(0));
                                age.setText(arrayAge.get(0));
                                blood.setText(arrayBlood.get(0));
                                height.setText(arrayHeight.get(0));
                                bodyType.setText(arrayBodyType.get(0));
                                area.setText(arrayArea.get(0));
                                job.setText(arrayJob.get(0));
                                personality.setText(arrayPersonality.get(0));
                                hobby.setText(arrayHobby.get(0));
                                religion.setText(arrayReligion.get(0));
                                smoking.setText(arraySmoking.get(0));
                                alcohol.setText(arrayAlcohol.get(0));

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "같은 지역에 사는 이성이 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);
                params.put("my_area", myArea);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 같은 직종 이성 필터링 결과
    private void fillterJob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILLTER_JOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("job");

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

                                    arrayNickname.add(userNickname);
                                    arrayAge.add(userAge);
                                    arrayBlood.add(userBlood);
                                    arrayHeight.add(userHeight);
                                    arrayBodyType.add(userBodyType);
                                    arrayArea.add(userArea);
                                    arrayJob.add(userJob);
                                    arrayPersonality.add(userPersonality);
                                    arrayHobby.add(userHobby);
                                    arrayReligion.add(userReligion);
                                    arraySmoking.add(userSmoking);
                                    arrayAlcohol.add(userAlcohol);
                                }

                                nickname.setText(arrayNickname.get(0));
                                age.setText(arrayAge.get(0));
                                blood.setText(arrayBlood.get(0));
                                height.setText(arrayHeight.get(0));
                                bodyType.setText(arrayBodyType.get(0));
                                area.setText(arrayArea.get(0));
                                job.setText(arrayJob.get(0));
                                personality.setText(arrayPersonality.get(0));
                                hobby.setText(arrayHobby.get(0));
                                religion.setText(arrayReligion.get(0));
                                smoking.setText(arraySmoking.get(0));
                                alcohol.setText(arrayAlcohol.get(0));

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "같은 직종을 가진 이성이 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);
                params.put("my_job", myJob);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 같은 종교 이성 필터링 결과
    private void fillterReligion() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILLTER_RELIGION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("religion");

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

                                    arrayNickname.add(userNickname);
                                    arrayAge.add(userAge);
                                    arrayBlood.add(userBlood);
                                    arrayHeight.add(userHeight);
                                    arrayBodyType.add(userBodyType);
                                    arrayArea.add(userArea);
                                    arrayJob.add(userJob);
                                    arrayPersonality.add(userPersonality);
                                    arrayHobby.add(userHobby);
                                    arrayReligion.add(userReligion);
                                    arraySmoking.add(userSmoking);
                                    arrayAlcohol.add(userAlcohol);
                                }

                                nickname.setText(arrayNickname.get(0));
                                age.setText(arrayAge.get(0));
                                blood.setText(arrayBlood.get(0));
                                height.setText(arrayHeight.get(0));
                                bodyType.setText(arrayBodyType.get(0));
                                area.setText(arrayArea.get(0));
                                job.setText(arrayJob.get(0));
                                personality.setText(arrayPersonality.get(0));
                                hobby.setText(arrayHobby.get(0));
                                religion.setText(arrayReligion.get(0));
                                smoking.setText(arraySmoking.get(0));
                                alcohol.setText(arrayAlcohol.get(0));

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "같은 종교를 가진 이성이 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);
                params.put("my_religion", myReligion);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 비흡연 이성 필터링 결과
    private void fillterNoSmoking() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILLTER_NO_SMOKING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("noSmoking");

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

                                    arrayNickname.add(userNickname);
                                    arrayAge.add(userAge);
                                    arrayBlood.add(userBlood);
                                    arrayHeight.add(userHeight);
                                    arrayBodyType.add(userBodyType);
                                    arrayArea.add(userArea);
                                    arrayJob.add(userJob);
                                    arrayPersonality.add(userPersonality);
                                    arrayHobby.add(userHobby);
                                    arrayReligion.add(userReligion);
                                    arraySmoking.add(userSmoking);
                                    arrayAlcohol.add(userAlcohol);
                                }

                                nickname.setText(arrayNickname.get(0));
                                age.setText(arrayAge.get(0));
                                blood.setText(arrayBlood.get(0));
                                height.setText(arrayHeight.get(0));
                                bodyType.setText(arrayBodyType.get(0));
                                area.setText(arrayArea.get(0));
                                job.setText(arrayJob.get(0));
                                personality.setText(arrayPersonality.get(0));
                                hobby.setText(arrayHobby.get(0));
                                religion.setText(arrayReligion.get(0));
                                smoking.setText(arraySmoking.get(0));
                                alcohol.setText(arrayAlcohol.get(0));

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "흡연을 하지 않는 이성이 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 같은 혈액형 이성 필터링 결과
    private void fillterBlood() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FILLTER_BLOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("blood");

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

                                    arrayNickname.add(userNickname);
                                    arrayAge.add(userAge);
                                    arrayBlood.add(userBlood);
                                    arrayHeight.add(userHeight);
                                    arrayBodyType.add(userBodyType);
                                    arrayArea.add(userArea);
                                    arrayJob.add(userJob);
                                    arrayPersonality.add(userPersonality);
                                    arrayHobby.add(userHobby);
                                    arrayReligion.add(userReligion);
                                    arraySmoking.add(userSmoking);
                                    arrayAlcohol.add(userAlcohol);
                                }

                                nickname.setText(arrayNickname.get(0));
                                age.setText(arrayAge.get(0));
                                blood.setText(arrayBlood.get(0));
                                height.setText(arrayHeight.get(0));
                                bodyType.setText(arrayBodyType.get(0));
                                area.setText(arrayArea.get(0));
                                job.setText(arrayJob.get(0));
                                personality.setText(arrayPersonality.get(0));
                                hobby.setText(arrayHobby.get(0));
                                religion.setText(arrayReligion.get(0));
                                smoking.setText(arraySmoking.get(0));
                                alcohol.setText(arrayAlcohol.get(0));

                                // 서버에서 이미지 가져오기
                                final String imageNickname = nickname.getText().toString().trim();
                                imageURL = "http://leejunho9496.cafe24.com/MeetingGo/profile_image/" + imageNickname + ".jpeg";
                                ImageRequest imageRequest = new ImageRequest(imageURL,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap response) {
                                                profileImage.setImageBitmap(response);
                                            }
                                        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(FillterIntroductionActivity.this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(FillterIntroductionActivity.this).addToRequestQue(imageRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "같은 혈액형을 가진 이성이 없습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "Error Reading : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("my_sex", mySex);
                params.put("my_blood", myBlood);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 하트 리스트의 내가 관심이 있는 이성에 저장
    private void UserSaveSendList() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_USER_SAVE_SEND_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); // JSONObject => JSON형태의 데이터를 관리
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                // Toast.makeText(IntroductionActivity.this, strNickname + "님에게 호감을 표시했습니다.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FillterIntroductionActivity.this, "호감표시 실패1 " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "호감표시 실패2 " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("user_nickname", arrayNickname.get(count));
                params.put("my_sex", mySex);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 하트 누를 시 테이블에 같은 이성 중복 등록 방지
    private void HartValidate() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HART_VALIDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // 해당 웹사이트에 접속한 이후에 특정한 jsonResponse, 즉 응답을 다시 받을 수 있도록 해줌.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); // 해당 과정이 정상적으로 수행이 됬는지, response의 값을 의미

                            // success가 되었을 시
                            if (success) {
                                UserSaveSendList();
                                Toast.makeText(FillterIntroductionActivity.this, arrayNickname.get(count) + "님에게 하트를 보냈습니다.", Toast.LENGTH_SHORT).show();
                            }
                            // 이미 리스트에 있을 시
                            else {
                                Toast.makeText(FillterIntroductionActivity.this, "하트리스트에 있는 이성입니다.\n하트리스트를 확인해주세요.", Toast.LENGTH_LONG).show();
                            }
                        }
                        // 오류 발생 시
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FillterIntroductionActivity.this, "다시 시도2 " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", myNickname);
                params.put("user_nickname", arrayNickname.get(count));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가
    }

}
