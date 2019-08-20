package com.example.administrator.meetingapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// 닉네임 중복검사
public class NicknameValidateRequest extends StringRequest {

    final static private String URL_Validate = "http://leejunho9496.cafe24.com/MeetingApp/nicknameValidate.php"; // 접속할 서버 주소
    private Map<String, String> parameters;

    // 생성자
    public NicknameValidateRequest(String nicknameValidate, Response.Listener<String> listener) {     // Response.Listener => 응답을 받을 수 있도록 하기위함.

        super(Request.Method.POST, URL_Validate, listener, null); // 해당 URL에 파라미터들을 POST방식으로 즉, 해당 요청을 숨겨서 보내준다.
        parameters = new HashMap<>(); // 파라미터 각각의 값을 넣을 수 있도록 HaspMap을 만듬

        // userID를 parameters로 매칭 시켜줌.
        parameters.put("nickname", nicknameValidate);
    }

    @Override
    public Map<String ,String> getParams() {
        return parameters;
    }
}
