package com.example.administrator.meetingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

// 프로필 정보 수정 화면
public class ProfileEditActivity extends AppCompatActivity {

    private TextView nickname, sex, age, blood, bodyType, area, job, religion, smoking, alcohol;
    private String userId, password;
    private EditText kakaoID, height, personality, hobby;
    private Spinner mSpinner;
    private ArrayAdapter<String> adapter;
    private TextView changeAge, changeBlood, changeBodyType, changeArea, changeJob, changeReligion, changeSmoking, changeAlcohol; // 수정하기 텍스트
    private Button btn_save;
    Session session;
    private String getNickname, getKakaoID, getSex, getAge, getBlood, getHeight, getBodyType, getArea, getJob, getPersonality, getHobby, getReligion, getSmoking, getAlcohol;

    private static String URL_UPDATE_PROFILE = "http://leejunho9496.cafe24.com/MeetingApp/updateProfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        setResult(RESULT_CANCELED);

        session = new Session(this);

        nickname = (TextView) findViewById(R.id.nickname);
        kakaoID = (EditText) findViewById(R.id.kakaoID);
        sex = (TextView) findViewById(R.id.sex);
        age = (TextView) findViewById(R.id.age);
        blood = (TextView) findViewById(R.id.blood);
        height = (EditText) findViewById(R.id.height);
        bodyType = (TextView) findViewById(R.id.bodyType);
        area = (TextView) findViewById(R.id.area);
        job = (TextView) findViewById(R.id.job);
        personality = (EditText) findViewById(R.id.personality);
        hobby = (EditText) findViewById(R.id.hobby);
        religion = (TextView) findViewById(R.id.religion);
        smoking = (TextView) findViewById(R.id.smoking);
        alcohol = (TextView) findViewById(R.id.alcohol);
        btn_save = (Button) findViewById(R.id.btn_edit_save);

        changeAge = (TextView) findViewById(R.id.change_age);
        changeBlood = (TextView) findViewById(R.id.change_blood);
        changeBodyType = (TextView) findViewById(R.id.change_bodyType);
        changeArea = (TextView) findViewById(R.id.change_area);
        changeJob = (TextView) findViewById(R.id.change_job);
        changeReligion = (TextView) findViewById(R.id.change_religion);
        changeSmoking = (TextView) findViewById(R.id.change_smoking);
        changeAlcohol = (TextView) findViewById(R.id.change_alcohol);

        getMyProfile();

        // 수정 완료 버튼
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateComplete();
            }
        });

        // 나이 수정 버튼
        changeAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ageChangeDialog();
            }
        });

        // 혈액형 수정 버튼
        changeBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloodChangeDialog();
            }
        });

        // 체형 수정 버튼
        changeBodyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bodyTypeChangeDialog();
            }
        });

        // 지역 수정 버튼
        changeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaChangeDialog();
            }
        });

        // 직업 수정 버튼
        changeJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jobChangeDialog();
            }
        });

        // 종교 수정 버튼
        changeReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                religionChangeDialog();
            }
        });

        // 흡연여부 수정 버튼
        changeSmoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smokingChangeDialog();
            }
        });

        // 주량 수정 버튼
        changeAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alcoholChangeDialog();
            }
        });

    }

    // 로그인 시 내 정보 불러오기
    private void getMyProfile() {
        HashMap<String, String> user = session.getMyInfo();
        userId = user.get(session.USER_ID);
        password = user.get(session.PASSWORD);
        getNickname = user.get(session.NICKNAME);
        getKakaoID = user.get(session.KAKAOID);
        getSex = user.get(session.SEX);
        getAge = user.get(session.AGE);
        getBlood = user.get(session.BLOOD);
        getHeight = user.get(session.HEIGHT);
        getBodyType = user.get(session.BODYTYPE);
        getArea = user.get(session.AREA);
        getJob = user.get(session.JOB);
        getPersonality = user.get(session.PERSONALITY);
        getHobby = user.get(session.HOBBY);
        getReligion = user.get(session.RELIGION);
        getSmoking = user.get(session.SMOKING);
        getAlcohol = user.get(session.ALCOHOL);

        nickname.setText(getNickname);
        kakaoID.setText(getKakaoID);
        sex.setText(getSex);
        age.setText(getAge);
        blood.setText(getBlood);
        height.setText(getHeight);
        bodyType.setText(getBodyType);
        area.setText(getArea);
        job.setText(getJob);
        personality.setText(getPersonality);
        hobby.setText(getHobby);
        religion.setText(getReligion);
        smoking.setText(getSmoking);
        alcohol.setText(getAlcohol);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////// 스피너 다이어로그 메서드 ////////////////////////////////////////////////////////////////////////////////////////////

    // 나이 변경 스피너
    private void ageChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_age, null);
        mBuilder.setTitle("나이를 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.age_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.age));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                age.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 혈액형 변경 스피너
    private void bloodChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_blood, null);
        mBuilder.setTitle("혈액형을 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.blood_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.blood));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blood.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 체형 변경 스피너
    private void bodyTypeChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_bodytype, null);
        mBuilder.setTitle("체형을 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.bodyType_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.bodyType));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bodyType.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 지역 변경 스피너
    private void areaChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_area, null);
        mBuilder.setTitle("지역을 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.area_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.area));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                area.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 직업 변경 스피너
    private void jobChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_job, null);
        mBuilder.setTitle("직업을 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.job_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.job));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                job.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 종교 변경 스피너
    private void religionChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_religion, null);
        mBuilder.setTitle("종교를 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.religion_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.religion));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                religion.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 흡연여부 변경 스피너
    private void smokingChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_smoking, null);
        mBuilder.setTitle("흡연여부를 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.smoking_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.smoking));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                smoking.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 주량 변경 스피너
    private void alcoholChangeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileEditActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_alcohol, null);
        mBuilder.setTitle("주량을 선택해주세요.");
        mSpinner = (Spinner) mView.findViewById(R.id.alcohol_spinner);
        adapter = new ArrayAdapter<String>(ProfileEditActivity.this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.alcohol));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alcohol.setText(mSpinner.getSelectedItem().toString().trim());
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 프로필 수정 완료 버튼
    private void updateComplete() {

        final String setKakaoID = this.kakaoID.getText().toString().trim();
        final String setAge = this.age.getText().toString().trim();
        final String setBlood = this.blood.getText().toString().trim();
        final String setHeight = this.height.getText().toString().trim();
        final String setBodyType = this.bodyType.getText().toString().trim();
        final String setArea = this.area.getText().toString().trim();
        final String setJob = this.job.getText().toString().trim();
        final String setPersonality = this.personality.getText().toString().trim();
        final String setHobby = this.hobby.getText().toString().trim();
        final String setReligion = this.religion.getText().toString().trim();
        final String setSmoking = this.smoking.getText().toString().trim();
        final String setAlcohol = this.alcohol.getText().toString().trim();

        if (setKakaoID.equals("")) {
            Toast.makeText(ProfileEditActivity.this, "카카오톡 ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (setHeight.equals("")) {
            Toast.makeText(ProfileEditActivity.this, "키를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (setPersonality.equals("")) {
            Toast.makeText(ProfileEditActivity.this, "성격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (setHobby.equals("")) {
            Toast.makeText(ProfileEditActivity.this, "취미를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }


        // StringRequest => URL을 지정하고 응답으로 원시 문자열을 받는다.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response); // JSONObject => JSON형태의 데이터를 관리
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(ProfileEditActivity.this, "프로필 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                session.createSession(userId, password, getNickname, setKakaoID, getSex, setAge, setBlood, setHeight, setBodyType, setArea, setJob, setPersonality, setHobby, setReligion, setSmoking, setAlcohol);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileEditActivity.this, "프로필 수정 실패 " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileEditActivity.this, "프로필 수정 실패 " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("my_nickname", getNickname);
                params.put("kakaoID", setKakaoID);
                params.put("my_sex", getSex);
                params.put("age", setAge);
                params.put("blood", setBlood);
                params.put("height", setHeight);
                params.put("bodyType", setBodyType);
                params.put("area", setArea);
                params.put("job", setJob);
                params.put("personality", setPersonality);
                params.put("hobby", setHobby);
                params.put("religion", setReligion);
                params.put("smoking", setSmoking);
                params.put("alcohol", setAlcohol);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this); // Request를 보낼 queue를 생성
        requestQueue.add(stringRequest); // // RequestQueue에 현재 Task를 추가
    }

    // back 버튼 누를 시
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileEditActivity.this, MyPageActivity.class));
        finish();
        super.onBackPressed();
    }

}
