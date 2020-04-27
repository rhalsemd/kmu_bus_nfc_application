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

public class studentBookMangerActivity extends AppCompatActivity {

    private Button buschoicebutton;

    String value;
    String ad="3";
    String talk;

    //DB받을 변수
    String day;
    String bookDay;
    String bookBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_manger);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck13 = (TextView)findViewById(R.id.Datacheck13);//로그인
        Datacheck13.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것


        //DB받을 textview
        final TextView daychetext = (TextView)findViewById(R.id.daychetext);//로그인
        final TextView bookDaychetext = (TextView)findViewById(R.id.bookDaychetext);//로그인
        final TextView bookBuschetext = (TextView)findViewById(R.id.bookBuschetext);//로그인

        //DB를 읽으면 화면이 켜질때 자동으로 출력
        daychetext.setText("일시 : : "+day);
        bookDaychetext.setText("예약 시간 : "+bookDay);
        bookBuschetext.setText("예약 노선 : "+bookBus);

        buschoicebutton=(Button)findViewById(R.id.buschoicebutton);//버스보이는 것 버튼
        buschoicebutton.setEnabled(false);
        final boolean cheBookTime=true;//취소예약시간이었다고 가정하여 작성
        Button BusCancellationButton2=(Button)findViewById(R.id.BusCancellationButton2);//버스취소 버튼
        Button studentbusMain3=(Button)findViewById(R.id.studentbusMain3);//메인화면으로가기
        studentbusMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
                //화면전환
            }
        });

        BusCancellationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cheBookTime == false) {
                        talk = "예약취소시간을 확인해주세요.";
                        dialog(talk);
                    } else {
                        talk = "예약취소 되었습니다.";
                        dialog(talk);
                        //메인화면으로 전환
                    }
                    buschoicebutton.setText("BusCancellationButton2");//확인용
                }catch (Exception e){

                    Excep(e);
                }
            }
        });

    }
    void Move(){
        Intent i = new Intent(studentBookMangerActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
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
    //뒤로가기 버튼 막기
    @Override
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
            dialog2("기능고장",exceptionAsStrting);
        }
        else{
            dialog2("오류", exceptionAsStrting);
        }
    }
}
