package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class driverBookRequest_bookedPersonnel extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/driverBookRequest/bookedPersonnel.php";
    private Map<String, String> map;


    public driverBookRequest_bookedPersonnel(String driverID, String bus_name, String bus_type, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("driverID",driverID);
        map.put("bus_name",bus_name);
        map.put("bus_type",bus_type);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
