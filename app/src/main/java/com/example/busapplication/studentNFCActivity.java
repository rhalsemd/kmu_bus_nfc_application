package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class studentNFCActivity extends AppCompatActivity {
    String talk;
    String ad="3";
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_nfc);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck10 = (TextView)findViewById(R.id.Datacheck10);//로그인
        Datacheck10.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것

        Button studentbusMain5=(Button)findViewById(R.id.studentbusMain5);//메인화면으로가기
        //nfc 실패시 뜨는 메시지 조건을 cheNFC=false로 함
        final boolean cheNFC=true;

        //nfc는 어떤걸로 처리할지 몰라 버튼으로 처리
        Button nfcButton=(Button)findViewById(R.id.nfcButton);//nfc 버튼

        studentbusMain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Move();
                //화면전환
            }
        });
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final TextView Datacheck11 = (TextView) findViewById(R.id.Datacheck11);//로그인
                    Datacheck11.setText("ok");//지워도 됨 - 값확인 하는 것
                    if (cheNFC == false) {
                        talk = "예약시간이나 버스를 확인해 주세요";
                        dialog(talk);
                    } else {//확인용 완성하면 지워야함
                        talk = "NFC가 체크되었습니다.";
                        dialog(talk);
                    }
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

    }
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        Intent i = new Intent(studentNFCActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    void  Excep(Exception e)
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
