package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adListBusRequest_camera extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/adListBusRequest_camera.php";
    private Map<String, String> map;

    public adListBusRequest_camera(String bus_type, String bus_name, String height, String camera_latitude, String camera_longtitude, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_type",bus_type);
        map.put("bus_name", bus_name);
        map.put("camera_height",height);
        map.put("camera_latitude", camera_longtitude);
        map.put("camera_longtitude", camera_latitude);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
