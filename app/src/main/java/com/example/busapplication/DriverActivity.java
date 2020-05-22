package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DriverActivity extends AppCompatActivity {
    String IDvalue;
    String ad="3";//관리자

    //예외처리 다이얼로그에 쓰임
    String whoSLD;
    String checkUser="버스기사";
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        String value12 = intent.getStringExtra("value"); //메인에서 넘어온 아이디값
        final TextView Datacheck2 = (TextView)findViewById(R.id.Datacheck2);//pw로그인

        IDvalue=value12;
        Datacheck2.setText(IDvalue);//지워도 됨 - 값넘어온지 확인 하는 것

        //IDvalue이 관리자 아이디랑 같으면
        Button adDriverButton=(Button)findViewById(R.id.adDriverButton);//버스노선 확인
        Button PersonalData=(Button)findViewById(R.id.PersonalData);//개인정보확인
        Button suggestionsCheck=(Button)findViewById(R.id.suggestionsCheck);//건의사항 체크
        Button bookManagement=(Button)findViewById(R.id.bookManagement);//버스예약관리
        Button memberlogout=(Button)findViewById(R.id.memberlogout);//로그아웃

        //누구님 안녕하세요 칸
        final TextView drivertextView = (TextView)findViewById(R.id.drivertextView);
        drivertextView.setText(IDvalue+"님 안녕하세요");


        //공지사항
        final Button noticeDriver = (Button)findViewById(R.id.noticeDriver);
        noticeDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_user = new Intent(DriverActivity.this, AppNoticeActivity.class);
                intent_user.putExtra("checkuser", checkUser);
                intent_user.putExtra("value", IDvalue);
                startActivity(intent_user);
                // TextView 클릭될 시 할 코드작성
            }
        });

        if(IDvalue.equals(ad))//3은 관리자 ID, 관리자인 경우
        {
            adDriverButton.setVisibility(View.VISIBLE);
            adDriverButton.setEnabled(true);

            adDriverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(DriverActivity.this)
                            .setMessage("원하는 기능을 확인했습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ADmove();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }).show();
                    //화면전환
                }
            });
        }
        else {
            //관리자가 아니면 버튼을 숨김
            adDriverButton.setVisibility(View.INVISIBLE);
            adDriverButton.setEnabled(false);
        }

        PersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(getApplicationContext(), drivermangerDataActivity.class);
                    intent.putExtra("value1",IDvalue);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });

        suggestionsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), driverSuggestionsActivity.class);
                    intent.putExtra("value", "버스기사");
                    startActivity(intent);
                    //화면전환
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });

        bookManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(getApplicationContext(), driverBookActivity.class);
                    intent.putExtra("value1",IDvalue);
                    startActivity(intent);
                }catch (Exception e)
                {
                    Excep(e);
                }
                //화면전환
            }
        });


        memberlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //화면전환
                    if (IDvalue.equals(ad))//3은 관리자 ID, 관리자인 경우
                    {
                        new AlertDialog.Builder(DriverActivity.this)
                                .setMessage("원하는 기능을 확인했습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ADmove();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                }).show();
                    } else {
                        //회원인 경우
                        Move();
                    }
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    void dialog(String who,String talk)
    {
        whoSLD=who;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(who+" : "+talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                if(whoSLD.equals("기능고장"))
                {
                    ADmove();
                }
                else {
                    Move();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void ADmove()
    {
        Intent i = new Intent(DriverActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Move()
    {
        Intent i = new Intent(DriverActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(IDvalue.equals(ad))
        {
            dialog("기능고장",exceptionAsStrting);
        }
        else{
            dialog("오류", exceptionAsStrting);
        }
    }
}
