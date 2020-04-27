package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class studentBookActivity extends AppCompatActivity {
    //bus spinner
    Spinner BookSpinner;
    //Adapter
    spinnerRows adapterSpinner1;

    //time spinner
    Spinner bookTimeSpinner;
    //Adapter
    spinnerRows adapterSpinner2;

    String talk;
    String value;
    String ad="3";

    String SpinnerValue;
    TextView Datacheck20;

    String text;
    //시간 선택
    int Timeche=0;
    //버스 선택
    int Busche;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck7 = (TextView)findViewById(R.id.Datacheck7);
        Datacheck7.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것

        //버스 선택 spinner
        List<String> data = new ArrayList<>();
        data.add("1 ~ 10 호차 노선 선택"); data.add("1호차"); data.add("2호차"); data.add("3호차");
        data.add("4호차");data.add("5호차"); data.add("6호차"); data.add("7호차");
        data.add("8호차"); data.add("9호차");data.add("10호차");
        //UI생성
        BookSpinner = (Spinner)findViewById(R.id.bookBusSpinner);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data);
        //Adapter 적용
        BookSpinner.setAdapter(adapterSpinner1);

        //버스 노선 표현
        final TextView BusTextView = (TextView) findViewById(R.id.BusTextView);

        final String BookSpinnertext = BookSpinner.getSelectedItem().toString();
        final TextView random = (TextView)findViewById(R.id.random);//렌덤문자열
        random.setText(getRandomPassword(2));

        final TextView randomInput = (TextView)findViewById(R.id.randomInput);//렌덤문자 입력
        //서버에 확인 조건추가

        //버스예약 가능 시간
        //임의의 값 true로 지정
        final boolean cheBook=true;//임의의 수 좌석이 있다고 가정하여 작성
        final boolean cheBookTime=true;//예약시간이었다고 가정하여 작성
        Button book=(Button)findViewById(R.id.book);//버스예약
        Button studentbusMain2=(Button)findViewById(R.id.studentbusMain2);//메인화면으로


        //버스 노선 표현
        BusTextView.setSelected(true);
        BusTextView.setText("노선을 선택해주세요");

        BookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
                try{
                    Busche=position;
                    Datacheck20 = (TextView)findViewById(R.id.Datacheck20);
                    Datacheck20.setText(String.valueOf(Busche));
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //시간 선택spinner
        List<String> data2 = new ArrayList<>();
        data2.add("시간 선택");
        data2.add("주간 등교 1");
        data2.add("주간 등교 2");
        data2.add("주간 하교 ");
        data2.add("야간 하교 ");
        //UI생성
        //UI생성
        //시간 스피너
        bookTimeSpinner = (Spinner)findViewById(R.id.bookTimeSpinner);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data2);
        //Adapter 적용
        bookTimeSpinner.setAdapter(adapterSpinner2);


        bookTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Timeche=position;
                    Datacheck20 = (TextView)findViewById(R.id.Datacheck20);
                    Datacheck20.setText(String.valueOf(Timeche));
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final TextView Datacheck8 = (TextView)findViewById(R.id.Datacheck8);//
        Datacheck8.setText("OK");//지워도 됨 - 값넘어온지 확인 하는 것

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (random.getText().toString().equals(randomInput.getText().toString()) && cheBook == true&&cheBookTime==true) {
                        //예약됨
                        Datacheck8.setText("OK");
                        //바로 매인 화면 넘어가는 것도 아니여서 넣음
                        talk = "예약이 되었습니다.";
                        dialog(talk);
                        //DB확인 하기 불펼할까봐 주석처리함
                        //   Intent i = new Intent(studentBookActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
                        //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        // startActivity(i);
                    } else if (!random.getText().toString().equals(randomInput.getText().toString())) {
                        //랜덤문자 틀렸음
                        Datacheck8.setText("아니");
                        talk = "랜덤문자를 입력해주세요";
                        dialog(talk);
                    } else if (cheBook == false) {
                        //좌석수 부족
                        Datacheck8.setText("아니");
                        talk = "잔여 좌석이 없습니다.";
                        dialog(talk);
                    } else if (cheBookTime == false) {
                        //예약시간 틀림
                        Datacheck8.setText("아니");
                        talk = "예약시간을 확인해주세요.";
                        dialog(talk);
                    }
                    //화면전환
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });
        studentbusMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Move();
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
    void Move()
    {
        Intent i = new Intent(studentBookActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
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
    //랜덤문자
    public String getRandomPassword( int length ){
        char[] charaters = {
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '0','1','2','3','4','5','6','7','8','9'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for( int i = 0 ; i < length ; i++ ){
            sb.append( charaters[ rn.nextInt( charaters.length ) ] );
        }
        return sb.toString();
    }
}
