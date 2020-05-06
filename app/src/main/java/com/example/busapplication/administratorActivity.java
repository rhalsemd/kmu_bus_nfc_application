package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class administratorActivity extends AppCompatActivity {

    String datacheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        final TextView Datacheck3 = (TextView)findViewById(R.id.Datacheck3);//pw로그인
        Datacheck3.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것
        datacheck=value;//값전달

        Button totalData=(Button)findViewById(R.id.totalData);//전체정보
        Button studentData=(Button)findViewById(R.id.studentData);//학생 데이터
        Button busData=(Button)findViewById(R.id.busData);//버스정보확인
        Button adlogout=(Button)findViewById(R.id.adlogout);//건의사항 체크
        Button studentMode=(Button)findViewById(R.id.studentMode);//학생모드


        totalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                try {
                    Intent i = new Intent(administratorActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        studentData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), adStudentDataActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                //화면전환
            }
        });


        busData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                try {
                    Intent intent=new Intent(getApplicationContext(), adBusDataActivity.class);
                    intent.putExtra("value",datacheck);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

        adlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(administratorActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
                //화면전환
            }
        });

        Button driverMode=(Button)findViewById(R.id.driverMode);//기사모드
        driverMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                try {
                    Intent intent=new Intent(getApplicationContext(), DriverActivity.class);
                    intent.putExtra("value",datacheck);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

        studentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(getApplicationContext(), studentActivity.class);
                    intent.putExtra("value",datacheck);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }

                //화면전환
            }
        });
    }

    void Move()
    {
        Intent i = new Intent(administratorActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage("오류 : "+talk);
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
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        dialog(exceptionAsStrting);
    }
}
