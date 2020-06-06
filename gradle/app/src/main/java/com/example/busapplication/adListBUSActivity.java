package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class adListBUSActivity extends AppCompatActivity {

    //DB에 넣을 때 아래의 변수 지우기
    String title="제목";//제목 DB에 넣기
    String Content="내용";//내용 db에 넣기

    public boolean RunRefresh=false;
    int i=0;
    String a;
    String Busvalue;
    String Timevalue;

    ArrayList<MarkerItem> bus_route = new ArrayList();
    MarkerItem markeritem = new MarkerItem();

    //모드 spinner
    Spinner MODspinner;
    spinnerRows adapterSpinner5;
    String modStr="모드 선택";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_list_bus);

        Intent intent = getIntent(); /*데이터 수신*/
        Busvalue = intent.getStringExtra("Busvalue"); //메인에서 넘어온 아이디값
        Timevalue = intent.getStringExtra("Timevalue"); //메인에서 넘어온 아이디값

        TextView map_cha_textView = (TextView)findViewById(R.id.map_cha_textView);//지도로 버스 좌표변경
        TextView CHECKtextView = (TextView)findViewById(R.id.CHECKtextView);//값넘어온것 확인
        CHECKtextView.setText(Timevalue+" "+Busvalue);

        final Button camerabutton = (Button)findViewById(R.id.camerabutton);//카메라 변경
        final Button ADbusMain5 = (Button)findViewById(R.id.ADbusMain5);//뒤로가기
        ADbusMain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adListBUSActivity.this/*현재 액티비티 위치*/ , adBusDataActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });
        map_cha_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이동
                Intent intent_value = new Intent(adListBUSActivity.this/*현재 액티비티 위치*/ , adMapListActivity.class/*이동 액티비티 위치*/);
                intent_value.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent_value.putExtra("Busvalue", Busvalue);//값전달
                intent_value.putExtra("Timevalue", Timevalue);//값전달
                startActivity(intent_value);
            }
        });

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //카메라 변경
                callFunction(adListBUSActivity.this);
            }
        });
        //표
        //컬럼으로 (내용,날짜, 제목, 내용)으로하고 Content에 내용부분 넣기
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.BUStestTable); // 테이블 id 명
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
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

                    for (int i = 0; i < bus_route.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(adListBUSActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for(int j = 0 ; j < 3 ; j++){//컬럼임
                            final int cun=i;
                            markeritem = bus_route.get(cun);
                            final String destination = markeritem.getdestination();
                            final String latitude = Double.toString(markeritem.getLat());
                            final String longtitude = Double.toString(markeritem.getLon());
                            final String destinationTen;
                            final String lat_column = "route"+ (i+1) + "_la";
                            final String long_column = "route"+ (i+1) + "_lo";
                            final String route_column = "route"+ (i+1);

                                Button rowButton = new Button(adListBUSActivity.this);
                                rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경

                                if (j == 0) {
                                    if(destination.length()>=7){
                                        destinationTen=destination.substring(0, 7)+"...";
                                    }
                                    else{
                                        destinationTen=destination;
                                    }
                                    rowButton.setText(destinationTen);

                                    rowButton.setWidth(300);
                                    rowButton.setHeight(100);
                                    rowButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //버튼 클릭될 시 할 코드작성
                                            callFunction(cun, destination, route_column, adListBUSActivity.this);
                                        }
                                    });

                                }
                                else if (j == 1) {
                                    rowButton.setText(latitude);
                                    rowButton.setWidth(150);
                                    rowButton.setHeight(100);
                                    rowButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //버튼 클릭될 시 할 코드작성
                                            callFunction(cun, latitude, lat_column, adListBUSActivity.this);
                                        }
                                    });
                                } else if (j == 2) {
                                    rowButton.setText(longtitude);
                                    rowButton.setWidth(150);
                                    rowButton.setHeight(100);
                                    rowButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //버튼 클릭될 시 할 코드작성
                                            callFunction(cun, longtitude, long_column, adListBUSActivity.this);
                                        }
                                    });

                                }
                                rowButton.setTextSize(12);                     // 폰트사이즈
                                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                rowButton.setTypeface(null, Typeface.BOLD);
                                rowButton.setEnabled(true);

                                tableRow.addView(rowButton);
                        }
                        tableLayout.addView(tableRow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //서버로 volley를 이용해서 요청
        studentMapRequest maprequest = new studentMapRequest(Timevalue, Busvalue, responseListener);
        RequestQueue queue = Volley.newRequestQueue(adListBUSActivity.this);
        queue.add(maprequest);

        //db자료를 2차원 배열같은데 넣고 setText에 db가 들어간 배열 출력

    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        Edialog(exceptionAsStrting);
    }
    //다이얼로그
    void Edialog(String talk)
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
    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(int num,String Mainvalue, String col, Context context) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg1 = new Dialog(context);
        final String mainvalue = Mainvalue;
        final String column_id = col;
        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg1.setContentView(R.layout.map_custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg1.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView TTtitle1 = (TextView) Mapdlg1.findViewById(R.id.TTtitle1);//지도 변경 입력칸
        //Toast.makeText(getApplicationContext(), destination, Toast.LENGTH_SHORT).show();
        TTtitle1.setText(mainvalue);
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText MAPmesgase = (EditText) Mapdlg1.findViewById(R.id.MAPmesgase);//지도 변경 입력칸
        final Button MAPokButton = (Button) Mapdlg1.findViewById(R.id.MAPokButton);//변경 버튼
        final Button MAPcancelButton = (Button) Mapdlg1.findViewById(R.id.MAPcancelButton);//취소버튼
        if(num==1||num==2)
        {
            MAPmesgase.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);//숫자 키보드로 변경
        }

        MAPokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.xf

                if (MAPmesgase.getText().toString().length() != 0)
                {
                    try {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        Refresh();//세로고침
                                        // 커스텀 다이얼로그를 종료한다.
                                        Mapdlg1.dismiss();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "DB관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        adListBusRequest listbusrequest = new adListBusRequest(Busvalue, Timevalue, MAPmesgase.getText().toString(), column_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adListBUSActivity.this);
                        queue.add(listbusrequest);

                    } catch (Exception e) {
                        Excep(e);
                    }
                }
                else
                {
                    Toast.makeText(adListBUSActivity.this, "변경내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        MAPcancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adListBUSActivity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
    }
    public void callFunction(Context context) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg1 = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg1.setContentView(R.layout.cameradialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg1.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText ZOOMmesgase = (EditText) Mapdlg1.findViewById(R.id.ZOOMmesgase);//지도 줌
        final EditText lomesgase = (EditText) Mapdlg1.findViewById(R.id.lomesgase);//지도 경도
        final EditText lamesgase = (EditText) Mapdlg1.findViewById(R.id.lamesgase);//지도 위도

        final Button chaButton1 = (Button) Mapdlg1.findViewById(R.id.chaButton1);//변경 버튼
        final Button cancelButton2 = (Button) Mapdlg1.findViewById(R.id.cancelButton2);//취소버튼

        chaButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ZOOMmesgase.getText().toString().length()!=0 && lamesgase.getText().toString().length()!=0 &&lomesgase.getText().toString().length()!=0)
                {
                    try {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "DB관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        adListBusRequest_camera listbusrequest_camera = new adListBusRequest_camera(Timevalue, Busvalue, ZOOMmesgase.getText().toString(), lamesgase.getText().toString(), lomesgase.getText().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adListBUSActivity.this);
                        queue.add(listbusrequest_camera);

                    } catch (Exception e) {
                        Excep(e);
                    }
                }
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
        cancelButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adListBUSActivity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adListBUSActivity.this/*현재 액티비티 위치*/ , adBusDataActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
        // 코드 작성
    }
}
