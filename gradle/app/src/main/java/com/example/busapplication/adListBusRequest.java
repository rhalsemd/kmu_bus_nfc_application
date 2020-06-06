package com.example.busapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adListBusRequest extends StringRequest {

    //서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://busapplication.dothome.co.kr/php/adListBusRequest.php";
    private Map<String, String> map;

    public adListBusRequest(String Busvalue, String Timevalue, String fixedvalue, String column_id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("bus_type",Timevalue);
        map.put("bus_name", Busvalue);
        map.put("fixed_value", fixedvalue);
        map.put("column_id", column_id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
