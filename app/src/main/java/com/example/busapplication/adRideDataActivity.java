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

public class adRideDataActivity extends AppCompatActivity {
    //시간 spinner
    Spinner TimeSpinner;
    //Adapter
    spinnerRows adapterSpinner1;
    //버스 spinner
    Spinner BusSpinner;
    //Adapter
    spinnerRows adapterSpinner2;

    //spinner값확인
    String SpinnerValue1;
    String SpinnerValue2;
    TextView Datacheck18;
    TextView Datacheck21;

     String Timetext;
     String Bustext;

     int Timenum;
    int Busnum;

    String value;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_ride_data);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck6 = (TextView)findViewById(R.id.Datacheck6);
        Datacheck6.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것
        final TextView reasonWrite = (TextView)findViewById(R.id.reasonWrite);
        final TextView Datacheck18 = (TextView)findViewById(R.id.Datacheck18);
        final TextView Datacheck21 = (TextView)findViewById(R.id.datacheck21);
        //버스 운행불가
        Button stapBus=(Button)findViewById(R.id.stapBus);
        //버스 운행불가취소
        Button cancellation=(Button)findViewById(R.id.cancellation);
        Button ADbusMain3=(Button)findViewById(R.id.ADbusMain3);//메인화면으로 이동

        //버스 spinner
        List<String> data = new ArrayList<>();
        data.add("1 ~ 10 호차 노선 선택"); data.add("1호차"); data.add("2호차"); data.add("3호차");
        data.add("4호차");data.add("5호차"); data.add("6호차"); data.add("7호차");
        data.add("8호차"); data.add("9호차");data.add("10호차");data.add("전체 버스 선택");

        //버스스피너
        BusSpinner = (Spinner)findViewById(R.id.busSpinner);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data);
        //Adapter 적용
        BusSpinner.setAdapter(adapterSpinner2);

        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
                try{
                    Timenum=position;
                    Datacheck18.setText(text);
                }catch (Exception e)
                {
                    Excep(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //시간 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("시간 선택");
        data1.add("주간 등교 1");
        data1.add("주간 등교 2");
        data1.add("주간 하교 ");
        data1.add("야간 하교 ");
        //UI생성
        //UI생성
        //시간 스피너
        TimeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data1);
        //Adapter 적용
        TimeSpinner.setAdapter(adapterSpinner1);

        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                try{
                    Busnum=position;
                    Datacheck21.setText(text);
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stapBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //DB확인할때 불펼할까봐 화면 전환 막아둠
                    Datacheck18.setText(Timetext + "-" + Bustext + "-" + reasonWrite.getText());
                    //ADmove();
                    //화면전환
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        cancellation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
            }
        });

        ADbusMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADmove();
            }
        });
    }


    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    void ADmove()
    {
        Intent i = new Intent(adRideDataActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
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
                ADmove();
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
