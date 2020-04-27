package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import  java.util.Timer;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.location.*;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
public class driverBookActivity extends AppCompatActivity
{
    //위치정보가 gps수신이라고 뜨면 gps 수신이 되므로 경도와 위도 등 정보 변경
    TextView textview_coordinate;//지도 전체 정보
    TextView textview_longitude;//경도
    TextView textview_latitude;//위도

    TextView GPStextView;//GPS확인
    Button go;
    //지도가 켜저있는 지 확인
    boolean cheMap=false;

    //종점 확인
    int endlongitude=0;//종점 위도
    int endlatitude=0;//종점 경도

    //DB 값넣기
    String longitudeDB;
    String latitudeDB;
    //버스가 종점에 욌는 지 확인
    boolean busStop=false;

    String value;
    String ad="3";//관리자

    String SpinnerValue;
    TextView Datacheck19;
    String text;
    int cou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_book);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck5 = (TextView)findViewById(R.id.Datacheck5);//pw로그인
        Datacheck5.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것

        Button driverbusMain3=(Button)findViewById(R.id.driverbusMain3);//메인화면 전환
        //버스예약 - 수동승인 버튼
        final TextView count = (TextView)findViewById(R.id.count);
        Button permission=(Button)findViewById(R.id.permission);

       //예약자 명단
        final TextView resertList = (TextView)findViewById(R.id.resertList);

        //학번입력 editText
        final TextView editText = (TextView)findViewById(R.id.editText);

        //수동승인이 실패 조건으로 chebook==false로 만듬
        final boolean chebook=true;//
        textview_coordinate  = (TextView) findViewById(R.id.Datacheck15);
        //1초마다 현제위치 갱신
        textview_longitude = (TextView)findViewById(R.id.Datacheck16);
        textview_latitude = (TextView)findViewById(R.id.Datacheck17);
        GPStextView= (TextView)findViewById(R.id.GPStextView);
        textview_coordinate.setText("위치정보 미수신중");
        go=(Button)findViewById(R.id.go);

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
                    //좌석수가 모질하면
                    if (chebook == false) {
                        book_warning();
                    }
                    else if(cou==45)
                    {
                        dialog("인원이 찼습니다.");
                    }
                    count.setText(String.valueOf(cou++) + "/45");

                    //화면전환
                }catch (Exception e){
                    Excep(e);

                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    cheMap=true;
                    //아래의 minTime으로 시간 변경
                    textview_coordinate.setText("수신중..");
                        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
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
                // TextView 클릭될 시 할 코드작성

            }
        });

        //목적지 도착시 gps종료
       if(busStop==true)
       {
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
            textview_coordinate.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);

            textview_longitude.setText("\n위도 : " + longitude );
            textview_latitude.setText( "\n경도 : " + latitude );

            longitudeDB=Double.toString(longitude);
            latitudeDB=Double.toString(latitude);

            GPStextView.setText("GPS 수신중...");
            //목적지 도착시 gps종료
            if(longitude==endlongitude&&latitude==endlatitude)
            {
                busStop=true;
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
