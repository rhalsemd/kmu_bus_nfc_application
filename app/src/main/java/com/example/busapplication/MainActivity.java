package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    //DB값 확인할때 불편할까봐 몇개의 화면은 메인화면으로 전화하는 기능 몇개는 막아둠

    //예외처리 고의로 하는 방법
/*
    try {
      Exception e = new Exception("고의로 발생시켰음.");
      throw e;	 // 예외를 발생시킴*/
        //화면전환}
    String IDsId;//ID값 string에 저장
    String PWsld;//ID값 string에 저장

    String talk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText IDText = (EditText)findViewById(R.id.ID);//ID로그인
        final EditText PWText = (EditText)findViewById(R.id.PW);//pw로그인
        final TextView EXITtextView = (TextView)findViewById(R.id.EXITtextView);//나가기
         IDsId = IDText.getText().toString();//ID값 string에 저장
         PWsld = PWText.getText().toString();//ID값 string에 저장

        //로그인
        Button login1=(Button)findViewById(R.id.loginbutton);//로그인
        //ID PW찾기
        final TextView IdPwFind = (TextView)findViewById(R.id.IdPwFing);
        //회원가입
        final TextView membership = (TextView)findViewById(R.id.membership);

        login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent intent=new Intent(getApplicationContext(),SubActivity.class);
                String s=IDText.getText().toString();
                if(IDText.getText().toString().equals("1"))
                {
                    Intent intent=new Intent(getApplicationContext(),studentActivity.class);
                    intent.putExtra("value",s);
                    startActivity(intent);
                    finish();
                }
                else if(IDText.getText().toString().equals("2"))
                {
                    Intent intent=new Intent(getApplicationContext(), DriverActivity.class);
                    intent.putExtra("value",s);
                    startActivity(intent);
                    finish();
                }
                else if(IDText.getText().toString().equals("3"))
                {
                    Intent intent=new Intent(getApplicationContext(),administratorActivity.class);
                    intent.putExtra("value",s);
                    startActivity(intent);
                    finish();
                }
                else if(IDText.getText().toString().equals("4"))//id와 pw틀렸을시
                {
                    talk="ID와 PW를 확인해주세요.";
                    dialog(talk);
                }
                else if(IDText.getText().toString().equals("5"))//제제 횟수 초과
                {
                    talk="제제 횟수가 초과했습니다.\n 관리자에게 문의해주세요.";
                    dialog(talk);
                }
                //startActivity(intent);
            }
        });

        IdPwFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog( );
                // TextView 클릭될 시 할 코드작성

            }
        });

        membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                Intent intent=new Intent(getApplicationContext(), AppMembershipActivity.class);
                startActivity(intent);
                finish();

            }
        });
        EXITtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                ActivityCompat.finishAffinity(MainActivity.this);
            }
        });

    }
    public void onBackPressed() {
        ActivityCompat.finishAffinity(MainActivity.this);
    }
    void dialog(String talk )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void dialog( )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage("학생 인지 버스기사인지를 알려주세요");

        builder.setPositiveButton("버스기사", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Intent intent=new Intent(getApplicationContext(), AppCheckActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("학생", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                //계명대사이트 연결
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://sso.kmu.ac.kr/kmusso/ext/edward/login_form.do?Return_Url=https://edward.kmu.ac.kr/com/SsoCtr/sso_login.do?redirectUrl=https://portal.kmu.ac.kr/proc/Login.do"));
                startActivity(intent);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
