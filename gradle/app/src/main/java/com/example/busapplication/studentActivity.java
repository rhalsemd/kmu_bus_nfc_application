package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;

public class studentActivity extends AppCompatActivity {

    String IDvalue;
    String ad="3";
    String checkUser="학생";
    //예외처리 다이얼로그에 쓰임
    String whoSLD;
    boolean timeche=true;// 버스시간 확인
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        String value12 = intent.getStringExtra("value"); //메인에서 넘어온 아이디값
        final TextView Datacheck1 = (TextView)findViewById(R.id.Datacheck1);//pw로그인

        IDvalue=value12;
        Datacheck1.setText(IDvalue);//지워도 됨 - 값넘어온지 확인 하는 것

        //누구님 안녕하세요 칸
        final TextView StudenttextView = (TextView)findViewById(R.id.StudenttextView);
        StudenttextView.setText(IDvalue+"님 안녕하세요");


        //공지사항
        final Button noticeStudent = (Button)findViewById(R.id.noticeStudent);
        noticeStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_user = new Intent(studentActivity.this, AppNoticeActivity.class);
                intent_user.putExtra("checkuser", checkUser);
                intent_user.putExtra("value", IDvalue);
                startActivity(intent_user);
                // TextView 클릭될 시 할 코드작성
            }
        });
        //IDvalue이 관리자 아이디랑 같으면
        Button adStudentButton=(Button)findViewById(R.id.adStudentButton);//버스노선 확인
        Button busmap=(Button)findViewById(R.id.busmap);//버스노선 확인
        Button busbook=(Button)findViewById(R.id.busbook);//버스예약
        Button bussupervision=(Button)findViewById(R.id.bussupervision);//예약버스관리
        Button suggestions=(Button)findViewById(R.id.suggestions);//건의사항
        Button busride=(Button)findViewById(R.id.busride);//버스탑승
        Button studentlogout=(Button)findViewById(R.id.studentlogout);//건의사항 체크

        if(IDvalue.equals(ad))//3은 관리자 ID, 관리자인경우 버튼 보이게
        {
            adStudentButton.setVisibility(View.VISIBLE);
            adStudentButton.setEnabled(true);
            adStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(studentActivity.this)
                            .setMessage("원하는 기능을 확인했습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent i = new Intent(studentActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(i);
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
        else {//관리자가 아닌경우 버튼 안보이게
            adStudentButton.setVisibility(View.INVISIBLE);
            adStudentButton.setEnabled(false);
        }


        busmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), studentMapActivity.class);
                    //다른 화면으로 값 전달
                    intent.putExtra("value1", IDvalue);
                    startActivity(intent);

                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        busbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(timeche==true){
                        //화면전환
                        Intent intent = new Intent(getApplicationContext(), studentBookActivity.class);
                        //다른 화면으로 값 전달
                        intent.putExtra("value1", IDvalue);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "지금은 예약시간이 아닙니다,", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        bussupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), studentBookMangerActivity.class);
                    //다른 화면으로 값 전달
                    intent.putExtra("value1", IDvalue);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        suggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), studentSuggestionActivity.class);
                    //다른 화면으로 값 전달
                    intent.putExtra("value1", IDvalue);
                    startActivity(intent);//화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

        busride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                    Intent intent = new Intent(getApplicationContext(), adNFCNumberActivity.class);
                    //다른 화면으로 값 전달
                    intent.putExtra("value1", IDvalue);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        studentlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환

                try {
                    if (IDvalue.equals(ad))//3은 관리자 ID
                    {
                        new AlertDialog.Builder(studentActivity.this)
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
                        Move();
                    }
                }catch (Exception e){
                    Excep(e);
                }
            }
        });
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        try {
            if (IDvalue.equals(ad))//3은 관리자 ID
            {
                new AlertDialog.Builder(studentActivity.this)
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
                Move();
            }
        }catch (Exception e){
            Excep(e);
        }
        //super.onBackPressed();
    }
    void Move()
    {
        Intent i = new Intent(studentActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void ADmove()
    {
        Intent i = new Intent(studentActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
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
    void Excep(Exception e)
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
