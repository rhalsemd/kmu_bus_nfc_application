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
import java.util.ArrayList;
import java.util.List;
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


    String checkMap="초기화";//호선 선택
    private GoogleMap mMap;

    int i=0;
    private  boolean ThreadCheck=false;//쓰레드 돌아가는 조건 체크
    private  boolean cheThread=false;//쓰레드가 켜저있는 지 안켜저 있는 지 확인 체크

    Thread thread;//쓰레드

    //마커 변수들
    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    //스피너 몇번 채크되어있는 지 확인
    int Position=0;

    //스피너 몇번 채크되어있는 지 확인
    int PositionMAP=0;

    //현재위치 쿨릭시 마크 찍히는 것 확인
    double a=0.1;


    String value;
    String ad="3";

    //지도 데이터 넣기
    List<Double>  longitude_list = new ArrayList<>();//경도
    List<Double>  latitude_list = new ArrayList<>();//위도
    List<String> map_list = new ArrayList<>();//위도

    //지도 카메라 데이터
    double Camera_latitude;
    double Camera_longitude;
    int Camera_center;

    int cuna;
    int TimeStr;
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

        //시간 선택spinner
        List<String> data2 = new ArrayList<>();
        data2.add("시간 선택");
        data2.add("주간 등교 1");
        data2.add("주간 등교 2");
        data2.add("주간 하교 ");
        data2.add("야간 하교 ");
        //UI생성
        //UI생성
        //시간 스피너
        TimeSpinner = (Spinner)findViewById(R.id.TimeSpinner);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data2);
        //Adapter 적용
        TimeSpinner.setAdapter(adapterSpinner2);


        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1)//주간 등교 1
                {
                    try {
                        TimeStr=1;
                    }catch (Exception e){
                        Excep(e);
                    }
                }
                else if(position==2)//주간 등교 2
                {
                    try {
                        TimeStr=2;
                        //화면전환
                    }catch (Exception e){
                        Excep(e);
                    }
                }
                else if(position==3)//주간 하교
                {
                    try {
                        TimeStr=3;
                        //화면전환
                    }catch (Exception e){
                        Excep(e);
                    }
                }
                else if(position==4)//야간 하교
                {
                    try {
                        TimeStr=4;
                        //화면전환
                    }catch (Exception e){
                        Excep(e);
                    }

                }
                else if(position==0)//야간 하교
                {
                    try {
                        TimeStr=0;
                        //화면전환
                    }catch (Exception e){
                        Excep(e);
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //지도 노선spinner (콤보박스)
        List<String> data = new ArrayList<>();
        data.add("1 ~ 10 호차 노선 또는 현재위치 선택");
        data.add("1호차");
        data.add("2호차");
        data.add("3호차");
        data.add("4호차");
        data.add("5호차");
        data.add("6호차");
        data.add("7호차");
        data.add("8호차");
        data.add("9호차");
        data.add("10호차");
        data.add("현재위치");
        //UI생성
        spinner1 = (Spinner) findViewById(R.id.MapSpinner);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data);
        //Adapter 적용
        spinner1.setAdapter(adapterSpinner1);

        //메인화면 전환
        Button studentbusMain1 = (Button) findViewById(R.id.studentbusMain1);
        studentbusMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Move();
                    if (cheThread == true) {
                        ThreadCheck = false;
                        cheThread = false;

                        //쓰레드 끝나고 실행
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        //spinner
        final TextView Datacheck3 = (TextView) findViewById(R.id.Datacheck3);
       // cheThread=false;
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String text = parent.getItemAtPosition(position).toString();
              //  Datacheck3.setText(""+parent.getItemAtPosition(position));//값전달확인

                //1호차~10호차는 코드가 동일 그래서 1호차만 주석 작성
                if (position == 1)//1호차
                {
                    try {
                        //몇번째인지 확인
                        Position = 1;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 2) {
                    try{
                    Position=2;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 3) {
                    try{
                    Position=3;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 4) {
                    try{
                    Position=4;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 5) {
                    try{
                    Position=5;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 6) {
                    try {
                    Position=6;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 7) {
                    try{
                    Position=7;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 8) {
                    try{
                    Position=8;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 9) {
                    try{
                    Position=9;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 10) {
                    try{
                    Position=10;
                        if (cheThread == true)
                        {
                            cheThread=false;
                            ThreadCheck=false;
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
                }
                else if (position == 11) {
                    try{
                        Position=11;
                    }catch (Exception e)
                    {
                        Excep(e);
                    }
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
                    //화면전환
                    //1호차~10호차는 코드가 동일 그래서 1호차만 주석 작성
                    if (Position == 1&&TimeStr==1)//1호차
                    {
                        try {
                            if (cheThread == true) {
                                //쓰레드 종료를 하는 변수
                                ThreadCheck = false;
                                cheThread = false;

                                //쓰레드 끝나고 실행
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //로딩화면 -
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                //쓰레드가 끝나기 전에 실항하는 것을 막음
                                //마커를 쓰레드 실행시 a,b,c를 짝고 여기는 d를 찍는 다고 가정하면
                                //쓰레드가 덜끝나서 a,b,d(여기것),c를 찍는다. 이것을 방지하기 위해 쓰레드가 끝날때까지 기다림
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                //마커를 다지우고 세로 찍음
                                                googleMap.clear();
                                                mMap = googleMap;
                                                //카메라 위치
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8310565, 128.4947782), 12));
                                                //마커 커스텀
                                                setCustomMarkerView();
                                                //마커좌표
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            } else if (cheThread == false) {
                                //쓰레드가 꺼져있다면
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8310565, 128.4947782), 12));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 2&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8371365, 128.5023483), 12));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8371365, 128.5023483), 12));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 3&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8341345, 128.506252), 12));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8341345, 128.506252), 12));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 4&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8471709, 128.5239554 ), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8471709, 128.5239554 ), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 5&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.897439, 128.522450), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.897439, 128.522450), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 6&&TimeStr==1) {
                        try {
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8928164,128.5162592), 12));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8928164,128.5162592), 12));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 7&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8945031,128.514698), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8945031,128.514698), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 8&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.880293,128.54318), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.880293,128.54318), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 9&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8772531,128.5582868), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.8772531,128.5582868), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 10&&TimeStr==1) {
                        try{
                            if (cheThread == true)
                            {
                                cheThread=false;
                                ThreadCheck=false;
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CheckTypesTask task = new CheckTypesTask();
                                task.execute();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap googleMap) {
                                                googleMap.clear();
                                                mMap = googleMap;
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.870315,128.5560197), 11));
                                                setCustomMarkerView();
                                                getSampleMarkerItems();
                                            }
                                        });
                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, 2000);
                            }
                            else if (cheThread == false) {
                                mapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        googleMap.clear();
                                        mMap = googleMap;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.870315,128.5560197), 11));
                                        setCustomMarkerView();
                                        getSampleMarkerItems();
                                    }
                                });
                            }
                        }catch (Exception e)
                        {
                            Excep(e);
                        }
                    }
                    else if (Position == 11)//현재위치
                    {
                        ThreadCheck=true;
                        cheThread = true;

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
                }catch (Exception e)
                {
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
        }
    }
    //마커 커스텀
    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }
    //마커 좌표랑 이름
    private void getSampleMarkerItems() {
        if(Position==1)
        {
            ArrayList<MarkerItem> sampleList1 = new ArrayList();
            sampleList1.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList1.add(new MarkerItem(35.8261066, 128.5275695, "월성네거리"));
            sampleList1.add(new MarkerItem(35.8227182, 128.5328747, "보성은하타운"));
            sampleList1.add(new MarkerItem(35.8110565, 128.5147782, "신라병원"));
            sampleList1.add(new MarkerItem(35.8038032, 128.4983229, "화원읍사무소"));
            sampleList1.add(new MarkerItem(35.8001091, 128.4958788, "화남초등"));
            for (MarkerItem markerItem : sampleList1) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==2)
        {
            ArrayList<MarkerItem> sampleList2 = new ArrayList();
            sampleList2.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList2.add(new MarkerItem(35.8071365, 128.5223483, "대곡청구제네스"));
            sampleList2.add(new MarkerItem(35.8064834, 128.5330442, "KM스포렉스"));
            for (MarkerItem markerItem : sampleList2) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==3)
        {
            ArrayList<MarkerItem> sampleList3 = new ArrayList();
            sampleList3.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList3.add(new MarkerItem(35.8241349 ,128.5428181, "월촌역코리아타운"));
            sampleList3.add(new MarkerItem(35.8241345, 128.536252, "상원고앞"));
            sampleList3.add(new MarkerItem(35.815247 ,128.5255423, "여대원한의원"));
            sampleList3.add(new MarkerItem(35.8069696, 128.5085719, "금강맨션육교"));
            for (MarkerItem markerItem : sampleList3) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==4)
        {
            ArrayList<MarkerItem> sampleList4 = new ArrayList();
            sampleList4.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList4.add(new MarkerItem(35.8367621 ,128.5395359, "보광병원"));
            sampleList4.add(new MarkerItem(35.8371709, 128.5539554, "KB국민은행송현동지점"));
            sampleList4.add(new MarkerItem(35.8342458 ,128.5989792, "효명초등학교"));
            sampleList4.add(new MarkerItem(35.8333294, 128.6053953, "상동리버아크로파트"));
            for (MarkerItem markerItem : sampleList4) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==5)
        {
            ArrayList<MarkerItem> sampleList5 = new ArrayList();
            sampleList5.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList5.add(new MarkerItem(35.932469 ,128.561479, "칠곡2차영남타운102동앞도로"));
            sampleList5.add(new MarkerItem(35.937439, 128.562450, "칠곡3차화성타운105동독서실뒤"));
            sampleList5.add(new MarkerItem(35.945632 ,128.563829, "동화골든빌아파트111동앞"));
            for (MarkerItem markerItem : sampleList5) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==6)
        {
            ArrayList<MarkerItem> sampleList6 = new ArrayList();
            sampleList6.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList6.add(new MarkerItem(35.909428,128.542418, "매천화성드림파크101동"));
            sampleList6.add(new MarkerItem(35.9332885,128.5395932, "롯데아파트"));
            sampleList6.add(new MarkerItem(35.9328164,128.5462592, "칠곡네거리"));
            sampleList6.add(new MarkerItem(35.9245031,128.544698, "다비치"));
            sampleList6.add(new MarkerItem(35.9190125,128.5451486, "태전교"));
            sampleList6.add(new MarkerItem(35.923916,128.5459033, "장보고식자재마트육교"));
            for (MarkerItem markerItem : sampleList6) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==7)
        {
            ArrayList<MarkerItem> sampleList7 = new ArrayList();
            sampleList7.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList7.add(new MarkerItem(35.884342,128.5536123, "북부정류장"));
            sampleList7.add(new MarkerItem(35.8982372,128.5466768, "팔달새마을금고육교"));
            sampleList7.add(new MarkerItem(35.9245031,128.544698, "다비치"));
            sampleList7.add(new MarkerItem(35.939649,128.5462868, "칠곡동화타운"));
            sampleList7.add(new MarkerItem(35.9512605,128.5529781, "칠곡3차보성GS주유소"));
            for (MarkerItem markerItem : sampleList7) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==8)
        {
            ArrayList<MarkerItem> sampleList8 = new ArrayList();
            sampleList8.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList8.add(new MarkerItem(35.890738,128.585844, "침산초등정문앞"));
            sampleList8.add(new MarkerItem(35.900293,128.59318, "산격주공아파트신우유통"));
            sampleList8.add(new MarkerItem(35.919006,128.598958, "월드메르디앙703동앞OK마트"));
            sampleList8.add(new MarkerItem(35.924536,128.598446, "동서변리벤빌(서변동한라아파트)107동옆"));
            for (MarkerItem markerItem : sampleList8) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==9)
        {
            ArrayList<MarkerItem> sampleList9 = new ArrayList();
            sampleList9.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList9.add(new MarkerItem(35.901469,128.611738, "산격대우아파트건너"));
            sampleList9.add(new MarkerItem(35.8972531,128.6182868, "대구북중학교"));
            sampleList9.add(new MarkerItem(35.925239,128.640626, "이시아폴리스 더샵 102동앞육교"));
            sampleList9.add(new MarkerItem(35.9142041,128.6386452, "대구축산농협불로지점"));
            for (MarkerItem markerItem : sampleList9) {
                addMarker(markerItem, false);
            }
        }
        else if(Position==10)
        {
            ArrayList<MarkerItem> sampleList10 = new ArrayList();
            sampleList10.add(new MarkerItem(35.8525064, 128.4849374, "계명대정문"));
            sampleList10.add(new MarkerItem(35.901869,128.61195, "산격대우아파트건너편"));
            sampleList10.add(new MarkerItem(35.89547,128.615881, "복현소방서건너편"));
            sampleList10.add(new MarkerItem(35.890315,128.6260197, "복현푸르지오102동건너편"));
            sampleList10.add(new MarkerItem(35.8796022,128.632697, "동대구역 청솔아파트 앞"));
            sampleList10.add(new MarkerItem(35.871031,128.6345523, "만촌동 이마트"));
            for (MarkerItem markerItem : sampleList10) {
                addMarker(markerItem, false);
            }
        }
    }
    //마커 색깔
    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String destination = markerItem.getdestination();
        String formatted =destination;
        tv_marker.setText(formatted);
        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.ic_marker_phone_blue);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.ic_marker_phone);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(destination);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));
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

