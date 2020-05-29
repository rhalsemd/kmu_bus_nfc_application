package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;
import java.util.TimerTask;
import java.util.Timer;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class studentMapActivity extends AppCompatActivity implements OnMapReadyCallback  {

    //지도 노선
    Spinner spinner1;
    //Adapter
    spinnerRows adapterSpinner1;

    //지도 시간
    Spinner TimeSpinner;
    //Adapter
    spinnerRows adapterSpinner2;

    studentMapDTO Maps_check = new studentMapDTO();

    String checkMap="초기화";//호선 선택
    private GoogleMap mMap;

    int i=0;
    private  boolean ThreadCheck=false;//쓰레드 돌아가는 조건 체크

    Thread thread;//쓰레드

    //마커 변수들
    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    //스피너 몇번 채크되어있는 지 확인
    int Position=0;
    int TimeStr=0;
    //스피너 몇번 채크되어있는 지 확인
    int PositionMAP=0;

    //현재위치 쿨릭시 마크 찍히는 것 확인
    double a;


    String value;
    String ad="3";

    String nowMap;

    List<String> load_busName = new ArrayList<>();
    List<String> load_busType = new ArrayList<>();
    ArrayList<MarkerItem> bus_route = new ArrayList();

    Marker NowBus;//실시간 마커
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_map);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck4 = (TextView) findViewById(R.id.Datacheck4);//pw로그인
        Datacheck4.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것
        //지도 맵
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //메인화면 전환
        Button studentbusMain1 = (Button) findViewById(R.id.studentbusMain1);
        studentbusMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Move();

                    finish();
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        //시간 스피너
        TimeSpinner = (Spinner)findViewById(R.id.TimeSpinner);

        //지도 노선spinner (콤보박스)
        spinner1 = (Spinner) findViewById(R.id.MapSpinner);

        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);

                        load_busName.add("노선 선택");
                        for(int i=0; i<array.length();i++){
                            JSONObject bus_name = array.getJSONObject(i);
                            load_busName.add(bus_name.getString("bus_name"));
                        }

                        //Adapter
                        adapterSpinner1 = new spinnerRows(studentMapActivity.this, load_busName);
                        //Adapter 적용
                        spinner1.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(studentMapActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }

        //spinner
        final TextView Datacheck3 = (TextView) findViewById(R.id.Datacheck3);
        // cheThread=false;
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = parent.getItemAtPosition(position).toString();
                Datacheck3.setText(""+parent.getItemAtPosition(position));//값전달확인

                text = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
                try {
                    //몇번째인지 확인
                    Position = position;
                    nowMap=text;
                    String bus_name = spinner1.getSelectedItem().toString();

                    //시간 선택spinner
                    try {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);

                                    load_busType.clear();
                                    load_busType.add("시간 선택");
                                    load_busType.add("현재위치");
                                    for(int i=0; i<array.length();i++){
                                        JSONObject bus_name = array.getJSONObject(i);
                                        load_busType.add(bus_name.getString("bus_type"));
                                    }

                                    //Adapter
                                    adapterSpinner2 = new spinnerRows(studentMapActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(bus_name, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(studentMapActivity.this);
                        queue.add(loadType);
                    }catch (Exception e){
                        Excep(e);
                    }
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //몇번째인지 확인
                    TimeStr = position;

                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Datacheck3.setText(checkMap);

        Button mapbutton=(Button)findViewById(R.id.mapbutton);//PW찾기
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String MapTime = TimeSpinner.getSelectedItem().toString();
                    String MapName = spinner1.getSelectedItem().toString();
                    if(ThreadCheck==true) {
                        ThreadCheck=false;
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (TimeStr != 0 && TimeStr != 1 && Position != 0 ) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    final String camera_la = jsonObject.getString("camera_la");
                                    final String camera_lo = jsonObject.getString("camera_lo");
                                    final String camera_height = jsonObject.getString("camera_height");
                                    studentMapDTO route_DTO = new studentMapDTO(jsonObject.getString("route1"), jsonObject.getString("route1_la"), jsonObject.getString("route1_lo"),
                                            jsonObject.getString("route2"), jsonObject.getString("route2_la"), jsonObject.getString("route2_lo"),
                                            jsonObject.getString("route3"), jsonObject.getString("route3_la"), jsonObject.getString("route3_lo"),
                                            jsonObject.getString("route4"), jsonObject.getString("route4_la"), jsonObject.getString("route4_lo"),
                                            jsonObject.getString("route5"), jsonObject.getString("route5_la"), jsonObject.getString("route5_lo"),
                                            jsonObject.getString("route6"), jsonObject.getString("route6_la"), jsonObject.getString("route6_lo"),
                                            jsonObject.getString("route7"), jsonObject.getString("route7_la"), jsonObject.getString("route7_lo"),
                                            jsonObject.getString("route8"), jsonObject.getString("route8_la"), jsonObject.getString("route8_lo"),
                                            jsonObject.getString("route9"), jsonObject.getString("route9_la"), jsonObject.getString("route9_lo"),
                                            jsonObject.getString("route10"), jsonObject.getString("route10_la"), jsonObject.getString("route10_lo"));

                                    bus_route.clear(); //이전에 찍힌게 남아있을 수 있어서
                                    bus_route.add(new MarkerItem(35.8525064, 128.4849374,"계명대정문"));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute1_la()),Double.parseDouble(route_DTO.getRoute1_lo()), route_DTO.getRoute1()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute2_la()),Double.parseDouble(route_DTO.getRoute2_lo()), route_DTO.getRoute2()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute3_la()),Double.parseDouble(route_DTO.getRoute3_lo()), route_DTO.getRoute3()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute4_la()),Double.parseDouble(route_DTO.getRoute4_lo()), route_DTO.getRoute4()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute5_la()),Double.parseDouble(route_DTO.getRoute5_lo()), route_DTO.getRoute5()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute6_la()),Double.parseDouble(route_DTO.getRoute6_lo()), route_DTO.getRoute6()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute7_la()),Double.parseDouble(route_DTO.getRoute7_lo()), route_DTO.getRoute7()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute8_la()),Double.parseDouble(route_DTO.getRoute8_lo()), route_DTO.getRoute8()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute9_la()),Double.parseDouble(route_DTO.getRoute9_lo()), route_DTO.getRoute9()));
                                    bus_route.add(new MarkerItem(Double.parseDouble(route_DTO.getRoute10_la()),Double.parseDouble(route_DTO.getRoute10_lo()), route_DTO.getRoute10()));

                                    CheckTypesTask task = new CheckTypesTask();
                                    task.execute();

                                    ThreadCheck=true;
                                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            googleMap.clear();
                                            mMap = googleMap;
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8772531,128.5582868), 11));
                                            setCustomMarkerView();
                                            for (MarkerItem markerItem : bus_route) {
                                                if (markerItem.getLat() != 0 && markerItem.getLon() != 0 && markerItem.getdestination() != "0") {
                                                    addMarker(markerItem, false);
                                                }
                                            }
                                            thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    while (ThreadCheck) {
                                                        try {
                                                            Thread.sleep(2000); //2초 간격으로 실행
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    NowBus.remove();
                                                                    //  googleMap.clear();
                                                                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                                                    //37.56 + a, 126.97 + a 대신 Db죄표
                                                                    LatLng SEOUL = new LatLng(35.8772531 + a, 128.5582868 + a);
                                                                    LatLng SEOUL12 = new LatLng(35.8772531 , 128.5582868 );
                                                                    MarkerOptions markerOptions = new MarkerOptions();
                                                                    markerOptions.position(SEOUL);
                                                                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.schooolbusshow);
                                                                    Bitmap b = bitmapdraw.getBitmap();
                                                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                                                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                                                    //  googleMap.addMarker(markerOptions);
                                                                    // Marker markerName = googleMap.addMarker(markerOptions);
                                                                    NowBus = mMap.addMarker(markerOptions);
                                                                    //  googleMap.addMarker(markerOptions);
                                                                    //     mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                                                                    //  googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                                                                    //확인용으로 a += 0.1를 찍음
                                                                    a += 0.001;

                                                                }
                                                            });
                                                        } catch (InterruptedException e) {
                                                            StringWriter sw = new StringWriter();
                                                            e.printStackTrace(new PrintWriter(sw));
                                                            String exceptionAsStrting = sw.toString();
                                                            if(value.equals(ad))
                                                            {
                                                                dialog("기능고장",exceptionAsStrting);
                                                            }
                                                            else{
                                                                dialog("오류", exceptionAsStrting);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                            thread.start();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentMapRequest maprequest = new studentMapRequest(MapTime, MapName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(studentMapActivity.this);
                        queue.add(maprequest);
                    }
                    else if (TimeStr == 1){

                        ThreadCheck=true;

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (ThreadCheck) {
                                    try {
                                        Thread.sleep(2000); //2초 간격으로 실행
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                                    @Override
                                                    public void onMapReady(GoogleMap googleMap) {
                                                        googleMap.clear();
                                                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                                        //37.56 + a, 126.97 + a 대신 Db죄표
                                                        LatLng SEOUL = new LatLng(37.56 + a, 126.97 + a);

                                                        MarkerOptions markerOptions = new MarkerOptions();
                                                        markerOptions.position(SEOUL);
                                                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.schooolbusshow);
                                                        Bitmap b = bitmapdraw.getBitmap();
                                                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                                                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                                                        googleMap.addMarker(markerOptions);

                                                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
                                                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                                                        //확인용으로 a += 0.1를 찍음
                                                        a += 0.1;
                                                    }
                                                });
                                            }
                                        });
                                    } catch (InterruptedException e) {
                                        StringWriter sw = new StringWriter();
                                        e.printStackTrace(new PrintWriter(sw));
                                        String exceptionAsStrting = sw.toString();
                                        if(value.equals(ad))
                                        {
                                            dialog("기능고장",exceptionAsStrting);
                                        }
                                        else{
                                            dialog("오류", exceptionAsStrting);
                                        }
                                    }
                                }
                            }
                        });
                        thread.start();
                    }

                } catch (Exception e) {
                    Excep(e);
                }

            }
        });
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //지도 처음 화면
        if (checkMap.equals("초기화")) {
            // for loop를 통한 n개의 마커 생성
            mMap = googleMap;
            LatLng SEOUL = new LatLng(35.8525064, 128.4849374);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEOUL);
            markerOptions.title("계명대학교");
            markerOptions.snippet("계명대학교 정문");
            mMap.addMarker(markerOptions);
            mMap.addMarker(markerOptions).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            NowBus=mMap.addMarker(markerOptions);
        }
    }
    //마커 커스텀
    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }

    //마커 색깔
    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String destination = markerItem.getdestination();
        String formatted =destination;
        tv_marker.setText(formatted);
        if (isSelectedMarker) {
         //   tv_marker.setBackgroundResource(R.drawable.ic_marker_phone_blue);
          //  tv_marker.setTextColor(Color.WHITE);
        } else {
           // tv_marker.setBackgroundResource(R.drawable.ic_marker_phone);
           // tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(destination);
        markerOptions.position(position);
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));
        return mMap.addMarker(markerOptions);
    }
    // View를 Bitmap으로 변환, 마커 커스텀
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    //로딩화면
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        CustomProgressDialog progressDlg = new CustomProgressDialog(studentMapActivity.this);

        @Override
        protected void onPreExecute() {
            progressDlg.getWindow().setBackgroundDrawable(
                    new ColorDrawable(
                            android.graphics.Color.TRANSPARENT));
            progressDlg.show();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDlg.dismiss(); // 없애기
            super.onPostExecute(result);
        }
    }
    void dialog(String who,String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(who+" : "+talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Move();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void dialog2(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void Move(){
        Intent i = new Intent(studentMapActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    //뒤로가기 막음
    public void onBackPressed() {
        try {
            Move();
            finish();
            //화면전환
        }catch (Exception e){
            Excep(e);
        }
        //super.onBackPressed();
    }
    void Excep(Exception e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(value.equals(ad))
        {
            dialog("기능고장",exceptionAsStrting);
        }
        else{
            dialog("오류", exceptionAsStrting);
        }
    }
}

