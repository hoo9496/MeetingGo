package com.example.administrator.meetingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText userId_edit, password_edit;
    private Button signup, idValidate;
    private String userId, password;
    private boolean validate = false; // 사용할 수 있는 회원 아이디 인지 체크해주기 위함.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userId_edit = (EditText) findViewById(R.id.userId_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        signup = (Button) findViewById(R.id.signup_btn);
        idValidate = (Button) findViewById(R.id.IdValidate_btn);

        // 아이디 중복체크 버튼
        idValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idValidate = userId_edit.getText().toString().trim();

                // 현재 중복 체크가 이뤄진 상태면 바로 함수 종료
                if (validate) {
                    return;
                }

                // 현재 체크가 안되어 있지만 닉네임이 공백일 시
                if (idValidate.equals("")) {
                    Toast.makeText(SignupActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 정상적으로 아이디를 입력 했을 시 중복 체크 진행
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // 해당 웹사이트에 접속한 이후에 특정한 jsonResponse, 즉 응답을 다시 받을 수 있도록 해줌.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); // 해당 과정이 정상적으로 수행이 됬는지, response의 값을 의미

                            // success가 되었다면 즉, 사용할 수 있는 ID 일 시
                            if (success) {
                                Toast.makeText(SignupActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                userId_edit.setEnabled(false); // 닉네임을 더 이상 수정할 수 없도록 고정.
                                validate = true;
                            }
                            // 중복 체크 실패 시 즉, 사용 할 수 없는 ID일 시
                            else {
                                Toast.makeText(SignupActivity.this, "사용할 수 없는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // 오류 발생 시
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                IdValidateRequest validateRequest = new IdValidateRequest(idValidate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(validateRequest);
            }
        });

        // 등록 버튼 클릭 이벤트
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = userId_edit.getText().toString().trim();
                password = password_edit.getText().toString().trim();

                // 현재 중복 체크가 되어있지 않다면 중복 체크를 해달라는 메시지 출력
                if (!validate) {
                    Toast.makeText(SignupActivity.this, "아이디 중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() == 0) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() <= 5) {
                    Toast.makeText(SignupActivity.this, "비밀번호는 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(SignupActivity.this, ProfileRegisterActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }

}
