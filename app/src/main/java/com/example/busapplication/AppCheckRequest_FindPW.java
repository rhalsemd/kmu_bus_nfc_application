package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AppCheckRequest_FindPW extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/AppCheckRequest/Findpw.php";
    private Map<String, String> map;


    public AppCheckRequest_FindPW(String userID, String userBirth, String userPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userBirth", userBirth);
        map.put("userPassword", userPassword);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
