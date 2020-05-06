package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class studentSuggestionRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/studentSuggestionRequest.php";
    private Map<String, String> map;


    public studentSuggestionRequest(String userID, String title, String content, String time, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("title", title);
        map.put("content", content);
        map.put("time", time);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
