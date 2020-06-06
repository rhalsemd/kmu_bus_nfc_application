package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adMapListRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/adMapListRequest.php";
    private Map<String, String> map;

    public adMapListRequest(String Busvalue, String Timevalue, String route_id, String route_value, String latitude_id, String latitude_value, String longtitude_id, String longtitude_value, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_type",Timevalue);
        map.put("bus_name", Busvalue);
        map.put("route_id",route_id);
        map.put("latitude_id",latitude_id);
        map.put("longtitude_id", longtitude_id);
        map.put("route_value",route_value);
        map.put("latitude_value",latitude_value);
        map.put("longtitude_value", longtitude_value);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
