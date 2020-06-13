package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.*;
import android.location.*;

import org.json.JSONException;
import org.json.JSONObject;

public class driverBookActivity extends AppCompatActivity
{
    //위치정보가 gps수신이라고 뜨면 gps 수신이 되므로 경도와 위도 등 정보 변경

    Button tutorial_button;
    TextView GPStextView;//GPS확인
    Button go;
    //지도가 켜저있는 지 확인

    //종점 확인
    int endlongitude=0;//종점 위도
    int endlatitude=0;//종점 경도

    //DB 값넣기
    String longitudeDB;
    String latitudeDB;
    //버스가 종점에 욌는 지 확인
    boolean busStop=false;
    boolean cheMap=false;

    String value;
    String ad="3";//관리자
    String count_personnel="0"; //DB에서 불러오는 탑승인원 수 (예약수 X 실제 탑승인원수 표시)
    String booked_personnel="0";

    String SpinnerValue;
    TextView Datacheck19;
    String text;
    int cou;
    String cun;
    int countFinsh=0;

    String adBusStr1;//관리자인경우 스피너
    String adTimeStr1;//관리자인경우 스피너
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_book);


        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox) ;

        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    // TODO : CheckBox is checked.
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    // TODO : CheckBox is unchecked.
                }
            }
        }) ;

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        adBusStr1 = intent.getExtras().getString("adBusStr1");
        adTimeStr1 = intent.getExtras().getString("adTimeStr1");

        Button driverbusMain3=(Button)findViewById(R.id.driverbusMain3);//메인화면 전환
        //버스예약 - 수동승인 버튼
        final TextView count = (TextView)findViewById(R.id.count);
        final TextView count2 = (TextView)findViewById(R.id.count2);
        Button permission=(Button)findViewById(R.id.permission);
        Button cancelpermission =(Button)findViewById(R.id.cancelpermission);

       //예약자 명단
        final TextView resertList = (TextView)findViewById(R.id.resertList);

        //학번입력 editText
        final TextView editText = (TextView)findViewById(R.id.editText);


        GPStextView= (TextView)findViewById(R.id.GPStextView);

        go=(Button)findViewById(R.id.go);

        //버스 실제 탑승인원 체크

        try {
            String driverID = value;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String personnel = jsonObject.getString("personnel");
                        if (success) {
                            count_personnel = personnel;
                            count2.setText(personnel);
                        } else {
                            //do nothing
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            driverBookRequest_setPersonnel setPersonnel = new driverBookRequest_setPersonnel(driverID, adBusStr1, adTimeStr1, responseListener);
            RequestQueue queue = Volley.newRequestQueue(driverBookActivity.this);
            queue.add(setPersonnel);
        }catch (Exception e){
            Excep(e);
        }

        //해당 버스 예약 인원 불러오는 코드
        try {
            String driverID = value;
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String personnel = jsonObject.getString("booked_personnel");
                        if (success) {
                            booked_personnel = personnel;
                        } else {
                            //do nothing
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            driverBookRequest_bookedPersonnel bookedPersonnel = new driverBookRequest_bookedPersonnel(driverID, adBusStr1, adTimeStr1, responseListener);
            RequestQueue queue = Volley.newRequestQueue(driverBookActivity.this);
            queue.add(bookedPersonnel);

            //화면전환
        }catch (Exception e){
            Excep(e);
        }

        /*checkVacant(); //버스 실탑승인원 DB에서 불러와서 초기화 시켜주는 코드
        count2.setText(count_personnel);
        checkBooked(); //해당 버스 예약인원 체크하는 코드*/






        // LocationManager 객체를 얻어온다
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        driverbusMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cheMap==true) {
                    lm.removeUpdates(mLocationListener);
                   Move();
                }
                else if(cheMap==false)
                {
                    Move();
                }
            }
        });

        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userID = editText.getText().toString();
                    String driverID = value;

                    if (userID.length() != 0) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        boolean maxed_out = jsonObject.getBoolean("maxed_out");
                                        boolean user_already = jsonObject.getBoolean("user_already");

                                        if (success) {
                                            Toast.makeText(getApplicationContext(), "승인되었습니다.", Toast.LENGTH_SHORT).show();
                                            String personnel_after = jsonObject.getString("personnel_after");

                                            count2.setText(personnel_after);

                                        } else if(user_already){
                                            Toast.makeText(getApplicationContext(), "이미 예약된 유저입니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else if(maxed_out){
                                            Toast.makeText(getApplicationContext(), "자리가 이미 다 찼습니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "catch!", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }

                                }
                            };
                        long now = System.currentTimeMillis();
                        Date mDate = new Date(now);
                        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        String seated_time  = simpleDate.format(mDate);

                        //서버로 volley를 이용해서 요청
                        driverBookRequest_checkVacant checkVacant = new driverBookRequest_checkVacant(userID, driverID, adBusStr1, adTimeStr1, seated_time, responseListener); //승인하면서 동시에 현재탑승인원 갱신
                        RequestQueue queue = Volley.newRequestQueue(driverBookActivity.this);
                        queue.add(checkVacant);
                    }
                    else if (userID.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "ID를 제대로 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Excep(e);
                }
            }
        });

        cancelpermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        String userID = editText.getText().toString();
                        String driverID = value;

                        Response.Listener<String> responseListener = new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                        String personnel_after = jsonObject.getString("personnel_after");
                                        count2.setText(personnel_after);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "취소되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "catch!", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        driverBookRequest_cancelPersonnel cancelpersonnel = new driverBookRequest_cancelPersonnel(userID, driverID, adBusStr1, adTimeStr1, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(driverBookActivity.this);
                        queue.add(cancelpersonnel);

                }catch(Exception e) {
                    Excep(e);
                }
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if ( Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                        ActivityCompat.requestPermissions( driverBookActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                                0 );
                    }
                    else {
                        cheMap=true;
                        //아래의 minTime으로 시간 변경

                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                5000, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                5000, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                    }

                }catch(SecurityException ex){
                    //ex.printStackTrace();
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String exceptionAsStrting = sw.toString();
                    if(value.equals(ad))
                    {
                        dialog2("기능고장",exceptionAsStrting);
                    }
                    else{
                        dialog2("오류", exceptionAsStrting);
                    }
                }
            }
        });

        resertList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_user = new Intent(driverBookActivity.this, driverReservationActivity.class);
                intent_user.putExtra("value", value);
                intent_user.putExtra("adBusStr2", adBusStr1);
                intent_user.putExtra("adTimeStr2", adTimeStr1);
                startActivity(intent_user);

            }
        });

        //목적지 도착시 gps종료
       if(busStop==true)
       {
           countFinsh=0;
           GPStextView.setText("GPS 수신전...");
           lm.removeUpdates(mLocationListener);
       }
    }
    //gps통신에 메시지를 넣기 너무 애매함
    //ui에 gps가 없는 데 갑자기 gps경고 메시지가 뜨는 게 이상해서 ui에 추가
    //1초마다 이벤트를 부르는 데 통신이 안되면 1초마다 메시지를 부르는 것도 이상하고 그렇다고 계속 통신이 안돠는 데 한번만 메시지 부르고
    //끄는 것도 이상하고 메시지가 갑자기 나타났다가 저절로 꺼지는 것도 먼가 아님
    //운전자 운전 중에 휴대폰을 보다가 메시지가 갑자기 켜졌다가 꺼졌다가 하는 것도 아니여서 텍스트로 대처함(1초마다 확인)
    private final LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            countFinsh++;
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.


            longitudeDB=Double.toString(longitude);
            latitudeDB=Double.toString(latitude);

            GPStextView.setText("GPS 수신중...");
            //목적지 도착시 gps종료
            if(countFinsh>=7200)
            {
                busStop=true;
            }


            //위도 경도값 DB에 업데이트하는 코드
            String driverID = value;
            try {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                            } else {
                                //do nothing
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청
                driverBookRequest_driverDepart driverdepart = new driverBookRequest_driverDepart(driverID,latitudeDB,longitudeDB, responseListener);
                RequestQueue queue = Volley.newRequestQueue(driverBookActivity.this);
                queue.add(driverdepart);
            }catch (Exception e){
                Excep(e);
            }
        }
        public void onProviderDisabled(String provider) {
            //통신이 안될때
            // Disabled시
            GPStextView.setText("현재위치 파악중...");
            Log.d("test", "onProviderDisabled, provider:" + provider);//확인용
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);//확인용
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);//확인용
        }
    };

    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void book_warning()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage("수동승인에 실패 했습니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        busStop=true;
        if(cheMap==true) {
            Move();
        }
        else if(cheMap==false)
        {
            Move();
        }
        //super.onBackPressed();
    }
    void dialog2(String who,String talk)
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
    void Move(){
        Intent i = new Intent(driverBookActivity.this/*현재 액티비티 위치*/ , DriverActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(value.equals(ad))
        {
            dialog2("기능고장",exceptionAsStrting);
        }
        else{
            dialog2("오류", exceptionAsStrting);
        }
    }


}
