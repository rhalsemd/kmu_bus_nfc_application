package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class studentBookRequest_book extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/studentBookRequest/book.php";
    private Map<String, String> map;


    public studentBookRequest_book(String bus_type, String bus_name, String userID, String booked_time, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_name", bus_name);
        map.put("bus_type", bus_type);
        map.put("userID", userID);
        map.put("booked_time", booked_time);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
