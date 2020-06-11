package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adBusDataRequest_delTime extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/adBusDataRequest/adBusDataRequest_delTime.php";
    private Map<String, String> map;

    public adBusDataRequest_delTime(String Busvalue, String TimeValue, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_name", Busvalue);
        map.put("bus_type", TimeValue);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
