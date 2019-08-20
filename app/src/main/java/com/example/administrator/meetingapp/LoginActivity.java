package com.example.administrator.meetingapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

// 로그인 화면
public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private Button btn_register, btn_login;
    private String user_id, user_password, userNickname, userKakaoID, userSex, userAge, userBlood, userHeight, userBodyType, userArea, userJob, userPersonality, userHobby, userReligion, userSmoking, userAlcohol;
    private static String URL_LOGIN_INFO = "http://leejunho9496.cafe24.com/MeetingApp/login.php"; // 로그인 정보 세션에 저장하기 위해 정보 불러옴.

    private String userIdHolder;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);

        btn_register = (Button) findViewById(R.id.register_btn);
        btn_login = (Button) findViewById(R.id.login_btn);
        final EditText idText = (EditText) findViewById(R.id.id_edit);
        final EditText passwordText = (EditText) findViewById(R.id.password_edit);

        // Register 버튼 누를 시
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

        // Login 버튼 누를 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId = idText.getText().toString();
                final String password = passwordText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) { // 로그인 성공 시
                                userIdHolder = userId;
                                getUserInfo();
                                /*
                                Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
                                startActivity(intent);
                                finish();
                                */

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("입력하신 정보가 틀렸습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userId, password, responseLister);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 로그인 정보 세션에 저장 하기 위함.
    private void getUserInfo() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("loginInfo");

                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    user_id = object.getString("user_id").trim();
                                    user_password = object.getString("password").trim();
                                    userNickname = object.getString("nickname").trim();
                                    userKakaoID = object.getString("kakaoID").trim();
                                    userSex = object.getString("sex").trim();
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

                                    session.createSession(user_id, user_password, userNickname, userKakaoID, userSex, userAge, userBlood, userHeight, userBodyType, userArea, userJob, userPersonality, userHobby, userReligion, userSmoking, userAlcohol);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error1 : " + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Error2 : " + error, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userIdHolder);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
