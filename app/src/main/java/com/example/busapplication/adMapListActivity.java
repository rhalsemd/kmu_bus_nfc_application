package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class adMapListActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    //모드 spinner
    Spinner ZoomSpinner;
    //Adapter
    spinnerRows adapterSpinner;
    String ZoomStr = "";
    //카메라 위도 경도
    double camera_latitude;
    double camera_longitude;

    int marker_size = 0; //계명대정문은 제외

    String MODEvalue;
    String Busvalue; //메인에서 넘어온 아이디값
    String Timevalue;

    //지도추가삭제와 지도변경에서 위도값을 넣어줌
    Double latitude = 0.0; // 위도
    Double longitude = 0.0; // 경도
    //지도추가삭제에서 한번만 변경할수있도록함()
    boolean oneche=false;

    //지도변경에서 한번만 변경할수있도록함()
    boolean towche=false;

    //지도변경에서 한번만 변경할수있도록함()
    boolean che1=false;
    //지도변경에서 한번만 변경할수있도록함()
    boolean che2=false;
    //지도추가삭제에서 제목을 찾아줌, 지도변경에서 변경할 지도를 저장
    String savetitle;

    //삭제시 값넘어오는지 확인용
    TextView list_map_textView1;
    TextView list_map_textView2;

    EditText title_map_editText;
    ArrayList<MarkerItem> bus_route = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_map_list);


        //카메라 위도 경도
        camera_latitude=37.52487;
        camera_longitude=126.92723;
        Intent intent = getIntent(); /*데이터 수신*/
        MODEvalue = intent.getStringExtra("MODEvalue"); //메인에서 넘어온 아이디값
        Busvalue = intent.getStringExtra("Busvalue"); //메인에서 넘어온 아이디값
        Timevalue = intent.getStringExtra("Timevalue"); //메인에서 넘어온 아이디값
        TextView che_textView = (TextView)findViewById(R.id.che_textView);//지도로 버스 좌표변경
        che_textView.setText(Timevalue+" "+Busvalue+"\n"+MODEvalue);

        //지도 변경이나 지도 추가시에 필요함
        title_map_editText=(EditText)findViewById(R.id.title_map_editText);
        //표로 이동
        TextView list_map_textView= (TextView)findViewById(R.id.list_map_textView);
        final Button Cancel_map_button = (Button)findViewById(R.id.Cancel_map_button);//취소버튼
        final Button chaMapbutton = (Button)findViewById(R.id.chaMapbutton);//확인버튼
        final Button ADbusMain6 = (Button)findViewById(R.id.ADbusMain6);//뒤로가기

        ADbusMain6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(adMapListActivity.this/*현재 액티비티 위치*/ , adBusDataActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();

                }catch (Exception e)
                {

                }
            }
        });
        Cancel_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }catch (Exception e)
                {

                }
            }
        });
        //여기에 DB값넣기
        //변경버튼
        chaMapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //title_map_editText를 불러서쓰기/
                // /좌표 추가나 변경시 필요


                    if(MODEvalue.equals("지도추가삭제"))
                    {
                        String route_id = "route"+marker_size;
                        String latitude_id = "route"+marker_size+"_la";
                        String longtitude_id = "route"+marker_size+"_lo";
                        String route_value = title_map_editText.getText().toString();

                        if(latitude != 0.0 && longitude != 0.0 && route_value.length() != 0 && marker_size <= 10)
                        {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            //DB에서 받은 값 각 변수에 저장
                                            Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();

                                            finish();
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            };
                            adMapListRequest admaplistrequest = new adMapListRequest(Busvalue, Timevalue, route_id, route_value, latitude_id, latitude.toString(), longtitude_id, longitude.toString(), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(adMapListActivity.this);
                            queue.add(admaplistrequest);
                        }
                        else if(marker_size>10){Toast.makeText(getApplicationContext(), "10개 이상 마커를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();}
                        else if(latitude == 0.0 && longitude == 0.0) {Toast.makeText(getApplicationContext(), "마커를 추가해주세요!", Toast.LENGTH_SHORT).show();}
                        else if(route_value.length() == 0) {Toast.makeText(getApplicationContext(), "마커이름을 적어주세요!", Toast.LENGTH_SHORT).show();}
                    }
                    else if(MODEvalue.equals("지도변경"))
                    {

                    }
                    else if(MODEvalue.equals("좌표확인및카메라변경"))
                    {
                        if(camera_latitude != 0.0&&camera_longitude != 0.0&& ZoomStr!="ZOOM")
                        {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            //DB에서 받은 값 각 변수에 저장
                                            Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();

                                            finish();
                                            startActivity(intent);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        return;
                                    }
                                }
                            };
                            adMapListRequest_camera admaplistrequest_camera = new adMapListRequest_camera(Busvalue, Timevalue, ZoomStr, Double.toString(camera_latitude), Double.toString(camera_longitude), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(adMapListActivity.this);
                            queue.add(admaplistrequest_camera);
                        }
                        else if(camera_latitude == 0.0 && camera_longitude == 0.0)
                        {Toast.makeText(getApplicationContext(), "카메라 위치를 확인해주세요!", Toast.LENGTH_SHORT).show();}
                        else if(ZoomStr=="ZOOM")
                        {Toast.makeText(getApplicationContext(), "카메라 zoom을 선택하지 않으셨습니다.", Toast.LENGTH_SHORT).show();}
                    }

            }
        });

        list_map_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이동
                // TextView 클릭될 시 할 코드작성
                Intent intent_value = new Intent(adMapListActivity.this/*현재 액티비티 위치*/ , adListBUSActivity.class/*이동 액티비티 위치*/);
                intent_value.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent_value.putExtra("Busvalue", Busvalue);//값전달
                intent_value.putExtra("Timevalue", Timevalue);//값전달
                startActivity(intent_value);
            }
        });

        //모드 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("ZOOM");
        for(int i=1;i<15;i++)
        {
            data1.add(String.valueOf(i));
        }
        //UI생성
        //UI생성
        //시간 스피너
        ZoomSpinner = (Spinner)findViewById(R.id.Zoomspinner);
        //Adapter
        adapterSpinner = new spinnerRows(this, data1);
        //Adapter 적용
        ZoomSpinner.setAdapter(adapterSpinner);

        ZoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ZoomStr = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                try{
                    if(position==0)
                    {

                    }
                    else if(position>0)
                    {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(camera_latitude, camera_longitude), position));
                    }
                }catch (Exception e)
                {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


        list_map_textView1=(TextView)findViewById(R.id.textView50);
        list_map_textView2=(TextView)findViewById(R.id.textView51);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        //지도 카메라 좌표
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                camera_latitude = arg0.target.latitude;
                camera_longitude = arg0.target.longitude;
            }
        });

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

                    for (MarkerItem markerItem : bus_route) {
                        if (markerItem.getLat() != 0 && markerItem.getLon() != 0 && markerItem.getdestination() != "0") {
                            addMarker(markerItem, false);
                            marker_size++; //마커의 총 갯수
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(camera_la),Double.parseDouble(camera_lo)), Float.parseFloat(camera_height)));
                    //getMarkerItems();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //서버로 volley를 이용해서 요청
        studentMapRequest maprequest = new studentMapRequest(Timevalue, Busvalue, responseListener);
        RequestQueue queue = Volley.newRequestQueue(adMapListActivity.this);
        queue.add(maprequest);
    }
    Marker marker1;

    //마커 색깔
    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String destination = markerItem.getdestination();
        String formatted =destination;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(destination);
        markerOptions.position(position);
        return mMap.addMarker(markerOptions);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adMapListActivity.this/*현재 액티비티 위치*/ , adBusDataActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
        // 코드 작성
    }
}
