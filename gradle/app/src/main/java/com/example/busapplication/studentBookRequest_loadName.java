package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class studentBookRequest_loadName extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/studentBookRequest/loadName.php";
    private Map<String, String> map;


    public studentBookRequest_loadName( Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
