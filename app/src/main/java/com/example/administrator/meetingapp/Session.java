package com.example.administrator.meetingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

// 기기에 로그인 정보 저장
public class Session {

    SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    public static final String USER_ID = "user_id"; // 아이디
    public static final String PASSWORD = "password"; // 비밀번호
    public static final String NICKNAME = "nickname"; // 닉네임
    public static final String KAKAOID = "kakaoID"; // 카카오톡 ID
    public static final String SEX = "sex"; // 성별
    public static final String AGE = "age"; // 나이
    public static final String BLOOD = "blood"; // 혈액형
    public static final String HEIGHT = "height"; // 키
    public static final String BODYTYPE = "bodyType"; // 체형
    public static final String AREA = "area"; // 지역
    public static final String JOB = "job"; // 직업
    public static final String PERSONALITY = "personality"; // 성격
    public static final String HOBBY = "hobby"; // 취미
    public static final String RELIGION = "religion"; // 종교
    public static final String SMOKING = "smoking"; // 흡연여부
    public static final String ALCOHOL = "alcohol"; // 주량

    public Session(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("Login", PRIVATE_MODE);
        editor = pref.edit(); // 데이터를 저장하기 위함.
    }

    public void createSession(String userId, String password, String nickname, String kakaoID, String sex, String age, String blood, String height, String bodyType, String area, String job, String personality, String hobby, String religion, String smoking, String alcohol) {
        editor.putBoolean("is_login", true);
        editor.putString(USER_ID, userId);
        editor.putString(PASSWORD, password);
        editor.putString(NICKNAME, nickname);
        editor.putString(KAKAOID, kakaoID);
        editor.putString(SEX, sex);
        editor.putString(AGE, age);
        editor.putString(BLOOD, blood);
        editor.putString(HEIGHT, height);
        editor.putString(BODYTYPE, bodyType);
        editor.putString(AREA, area);
        editor.putString(JOB, job);
        editor.putString(PERSONALITY, personality);
        editor.putString(HOBBY, hobby);
        editor.putString(RELIGION, religion);
        editor.putString(SMOKING, smoking);
        editor.putString(ALCOHOL, alcohol);
        editor.apply();
    }

    public boolean isLoggin() {
        return pref.getBoolean("is_login", false);
    }

    public void checkLogin() {
        if (!this.isLoggin()) {
            Intent i = new Intent(context, SplashActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String ,String> getMyInfo() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, pref.getString(USER_ID, null));
        user.put(PASSWORD, pref.getString(PASSWORD, null));
        user.put(NICKNAME, pref.getString(NICKNAME, null));
        user.put(KAKAOID, pref.getString(KAKAOID, null));
        user.put(SEX, pref.getString(SEX, null));
        user.put(AGE, pref.getString(AGE, null));
        user.put(BLOOD, pref.getString(BLOOD, null));
        user.put(HEIGHT, pref.getString(HEIGHT, null));
        user.put(BODYTYPE, pref.getString(BODYTYPE, null));
        user.put(AREA, pref.getString(AREA, null));
        user.put(JOB, pref.getString(JOB, null));
        user.put(PERSONALITY, pref.getString(PERSONALITY, null));
        user.put(HOBBY, pref.getString(HOBBY, null));
        user.put(RELIGION, pref.getString(RELIGION, null));
        user.put(SMOKING, pref.getString(SMOKING, null));
        user.put(ALCOHOL, pref.getString(ALCOHOL, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, SplashActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }

    public void deleteMember() {
        editor.clear();
        editor.commit();
    }

}
