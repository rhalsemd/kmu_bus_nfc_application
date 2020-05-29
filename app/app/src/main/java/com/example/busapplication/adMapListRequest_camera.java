package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adMapListRequest_camera extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/adMapListRequest_camera.php";
    private Map<String, String> map;

    public adMapListRequest_camera(String Busvalue, String Timevalue, String zoom, String latitude, String longtitude, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_type",Timevalue);
        map.put("bus_name", Busvalue);
        map.put("camera_height", zoom);
        map.put("camera_la", latitude);
        map.put("camera_lo", longtitude);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
