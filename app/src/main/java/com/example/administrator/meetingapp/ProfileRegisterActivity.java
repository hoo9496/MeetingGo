package com.example.administrator.meetingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// 프로필 등록 화면
public class ProfileRegisterActivity extends AppCompatActivity {

    private int PICK_FROM_GALLERY = 1;

    private EditText nickname, kakaoID, height, personality, hobby;
    private RadioGroup sexGroup;
    private String userId, password;
    private String sex="남";
    private Spinner age, blood, bodyType, area, job, religion, smoking, alcohol;
    private ArrayAdapter ageAdapter, bloodAdapter, bodyTypeAdapter, areaAdapter, jobAdapter, religionAdapter, smokingAdapter, alcoholAdapter;
    private Button btnNicknameValidate, btnRegister;
    private Bitmap bitmap;
    CircleImageView profileImage;
    private boolean imageCheck = false; // 사진 등록 확인을 위함.
    private boolean validate = false; // 사용할 수 있는 회원 닉네임 인지 체크해주기 위함.
    private boolean flagRegister = false; // 모든 항목 입력 확인
    private static String URL_REGIST = "http://leejunho9496.cafe24.com/MeetingApp/register.php";

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_register);

        btnNicknameValidate = (Button) findViewById(R.id.btnNicknameValidate);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        nickname = (EditText) findViewById(R.id.nickname);
        kakaoID = (EditText) findViewById(R.id.kakaoID);
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
        age = (Spinner) findViewById(R.id.age);
        blood = (Spinner) findViewById(R.id.blood);
        height = (EditText) findViewById(R.id.height);
        bodyType = (Spinner) findViewById(R.id.bodyType);
        area = (Spinner) findViewById(R.id.area);
        job = (Spinner) findViewById(R.id.job);
        personality = (EditText) findViewById(R.id.personality);
        hobby = (EditText) findViewById(R.id.hobby);
        religion = (Spinner) findViewById(R.id.religion);
        smoking = (Spinner) findViewById(R.id.smoking);
        alcohol = (Spinner) findViewById(R.id.alcohol);
        btnRegister = (Button) findViewById(R.id.btnRegistration);

        ageAdapter = ageAdapter.createFromResource(this, R.array.age, android.R.layout.simple_spinner_dropdown_item);
        age.setAdapter(ageAdapter);
        bloodAdapter = bloodAdapter.createFromResource(this, R.array.blood, android.R.layout.simple_spinner_dropdown_item);
        blood.setAdapter(bloodAdapter);
        bodyTypeAdapter = bodyTypeAdapter.createFromResource(this, R.array.bodyType, android.R.layout.simple_spinner_dropdown_item);
        bodyType.setAdapter(bodyTypeAdapter);
        areaAdapter = areaAdapter.createFromResource(this, R.array.area, android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(areaAdapter);
        jobAdapter = jobAdapter.createFromResource(this, R.array.job, android.R.layout.simple_spinner_dropdown_item);
        job.setAdapter(jobAdapter);
        religionAdapter = religionAdapter.createFromResource(this, R.array.religion, android.R.layout.simple_spinner_dropdown_item);
        religion.setAdapter(religionAdapter);
        smokingAdapter = smokingAdapter.createFromResource(this, R.array.smoking, android.R.layout.simple_spinner_dropdown_item);
        smoking.setAdapter(smokingAdapter);
        alcoholAdapter = alcoholAdapter.createFromResource(this, R.array.alcohol, android.R.layout.simple_spinner_dropdown_item);
        alcohol.setAdapter(alcoholAdapter);

        session = new Session(this);

        // SignupActivity에서 받아온 유저 아이디와 패스워드
        userId = getIntent().getStringExtra("userId");
        password = getIntent().getStringExtra("password");

        // 라디오 버튼 선택
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioMan:
                        sex = "남";
                        break;
                    case R.id.radioWoman:
                        sex = "여";
                        break;
                }
            }
        });

        // 닉네임 중복체크 버튼
        btnNicknameValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nicknameValidate = nickname.getText().toString().trim();

                // 현재 중복 체크가 이뤄진 상태면 바로 함수 종료
                if (validate) {
                    return;
                }

                // 현재 체크가 안되어 있지만 닉네임이 공백일 시
                if (nicknameValidate.equals("")) {
                    Toast.makeText(ProfileRegisterActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 정상적으로 닉네임을 입력 했을 시 중복 체크 진행
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // 해당 웹사이트에 접속한 이후에 특정한 jsonResponse, 즉 응답을 다시 받을 수 있도록 해줌.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); // 해당 과정이 정상적으로 수행이 됬는지, response의 값을 의미

                            // success가 되었다면 즉, 사용할 수 있는 ID 일 시
                            if (success) {
                                Toast.makeText(ProfileRegisterActivity.this, "사용 가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                                nickname.setEnabled(false); // 닉네임을 더 이상 수정할 수 없도록 고정.
                                validate = true;
                            }
                            // 중복 체크 실패 시 즉, 사용 할 수 없는 ID일 시
                            else {
                                Toast.makeText(ProfileRegisterActivity.this, "사용할 수 없는 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // 오류 발생 시
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                NicknameValidateRequest validateRequest = new NicknameValidateRequest(nicknameValidate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ProfileRegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        // Register 버튼
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 중복 체크가 되어있지 않다면 중복 체크를 해달라는 메시지 출력
                if (!validate) {
                    Toast.makeText(ProfileRegisterActivity.this, "닉네임 중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (imageCheck == false) {
                    Toast.makeText(ProfileRegisterActivity.this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Regist();
                    if (flagRegister == false) {
                        return;
                    } else {
                        startActivity(new Intent(ProfileRegisterActivity.this, LoginActivity.class));
                    }
                }
            }
        });

        // 프로필 사진등록 클릭 시 갤러리 오픈
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
    }

    private void Regist() {

        // trim() => 문자열 좌측끝과 우측끝 공백을 제거
        final String photo = getStringImage(bitmap);
        final String nickname = this.nickname.getText().toString().trim();
        final String kakaoID = this.kakaoID.getText().toString().trim();
        final String age = this.age.getSelectedItem().toString().trim();
        final String blood = this.blood.getSelectedItem().toString().trim();
        final String height = this.height.getText().toString().trim();
        final String bodyType = this.bodyType.getSelectedItem().toString().trim();
        final String area = this.area.getSelectedItem().toString().trim();
        final String job = this.job.getSelectedItem().toString().trim();
        final String personality = this.personality.getText().toString().trim();
        final String hobby = this.hobby.getText().toString().trim();
        final String religion = this.religion.getSelectedItem().toString().trim();
        final String smoking = this.smoking.getSelectedItem().toString().trim();
        final String alcohol = this.alcohol.getSelectedItem().toString().trim();

        if (kakaoID.equals("")) {
            Toast.makeText(ProfileRegisterActivity.this, "카카오톡 ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
            flagRegister = false;
            return;
        }
        if (height.equals("")) {
            Toast.makeText(ProfileRegisterActivity.this, "키를 입력해주세요.", Toast.LENGTH_SHORT).show();
            flagRegister = false;
            return;
        }
        if (personality.equals("")) {
            Toast.makeText(ProfileRegisterActivity.this, "성격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            flagRegister = false;
            return;
        }
        if (hobby.equals("")) {
            Toast.makeText(ProfileRegisterActivity.this, "취미를 입력해주세요.", Toast.LENGTH_SHORT).show();
            flagRegister = false;
            return;
        }

        // StringRequest => URL을 지정하고 응답으로 원시 문자열을 받는다.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); // JSONObject => JSON형태의 데이터를 관리
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ProfileRegisterActivity.this, "회원가입을 축하합니다!", Toast.LENGTH_SHORT).show();
                                flagRegister = true;
                                session.createSession(userId, password, nickname, kakaoID, sex, age, blood, height, bodyType, area, job, personality, hobby, religion, smoking, alcohol);

                                Intent intent = new Intent(ProfileRegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileRegisterActivity.this, "Register Error1! " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileRegisterActivity.this, "Register Error2! " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo", photo);
                params.put("user_id", userId);
                params.put("password", password);
                params.put("nickname", nickname);
                params.put("kakaoID", kakaoID);
                params.put("sex", sex);
                params.put("age", age);
                params.put("blood", blood);
                params.put("height", height);
                params.put("bodyType", bodyType);
                params.put("area", area);
                params.put("job", job);
                params.put("personality", personality);
                params.put("hobby", hobby);
                params.put("religion", religion);
                params.put("smoking", smoking);
                params.put("alcohol", alcohol);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileRegisterActivity.this, SignupActivity.class));
        finish();
    }

}
