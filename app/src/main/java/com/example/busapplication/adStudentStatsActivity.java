package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class adStudentStatsActivity extends AppCompatActivity {

    Calendar myCalendar = Calendar.getInstance();//달력

    String title;
    String Content;
    //Adapter
    spinnerRows adapterSpinner1;
    //버스 spinner
    Spinner BusSpinner10;
    String busStr;
    //Adapter
    spinnerRows adapterSpinner2;
    //시간 spinner
    Spinner TimeSpinner10;
    String timeStr;
    //Adapter
    spinnerRows adapterSpinner3;
    //모드 spinner
    Spinner ModeSpinner10;
    int modenum;

    TextView cheDayText;//달력
    String daySave;//달력 저장


    TextView busText1;
    TextView timeText1;
    TextView peoText1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_student_stats);

       final Button stastsButton = (Button)findViewById(R.id.stastsButton);//확인
       final Button dayButton = (Button)findViewById(R.id.dayButton);//달력
       final Button ADbusMain19 = (Button)findViewById(R.id.ADbusMain19);//뒤로가기

        cheDayText = (TextView)findViewById(R.id.cheDayText10);
         busText1 = (TextView)findViewById(R.id.busText1);
         timeText1 = (TextView)findViewById(R.id.timeText1);
         peoText1 = (TextView)findViewById(R.id.peoText1);

        ADbusMain19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adStudentStatsActivity.this/*현재 액티비티 위치*/ , adStudentActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });


        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(adStudentStatsActivity.this,R.style.DialogTheme,myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        //모드 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("모드 선택");
        data1.add("일일 탑승객수");
        data1.add("예약자별 탑승 호차");


        //UI생성
        //UI생성
        //버스 스피너
        ModeSpinner10 = (Spinner)findViewById(R.id.ModeSP);
        //Adapter
        adapterSpinner3 = new spinnerRows(this, data1);
        //Adapter 적용
        ModeSpinner10.setAdapter(adapterSpinner3);

        ModeSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    modenum=position;
                    if(position==2){
                        BusSpinner10.setEnabled(false);
                        BusSpinner10.setClickable(false);

                        TimeSpinner10.setEnabled(false);
                        TimeSpinner10.setClickable(false);
                        busText1.setText("ID");
                        peoText1.setText("호차");
                        timeText1.setText("시간");

                    }
                    else{
                        BusSpinner10.setEnabled(true);
                        BusSpinner10.setClickable(true);

                        TimeSpinner10.setEnabled(true);
                        TimeSpinner10.setClickable(true);

                        busText1.setText("호차");
                        peoText1.setText("탑승자수");
                        timeText1.setText("시간");
                    }

                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //버스 spinner
        List<String> data2 = new ArrayList<>();
        data2.add("노선 선택");
        for(int i=0;i<10;i++){

            data2.add(Integer.toString(i)+"호차");
        }

        //UI생성
        //UI생성
        //버스 스피너
        BusSpinner10 = (Spinner)findViewById(R.id.BusSP);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data2);
        //Adapter 적용
        BusSpinner10.setAdapter(adapterSpinner1);

        BusSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    busStr=parent.getItemAtPosition(position).toString();
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
        List<String> data3 = new ArrayList<>();
        data3.add("시간 선택");
        data3.add("주간등교1");
        data3.add("주간등교2");
        data3.add("주간하교");
        data3.add("야간하교");

        //UI생성
        //UI생성
        //시간 스피너
        TimeSpinner10 = (Spinner)findViewById(R.id.TimeSP);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data3);
        //Adapter 적용
        TimeSpinner10.setAdapter(adapterSpinner2);

        TimeSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    timeStr=parent.getItemAtPosition(position).toString();

                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stastsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.adStustsTable);
        for (int i = 0; i < 100; i++) {//  row 임 대신에 컬럼갯
            TableRow tableRow = new TableRow(adStudentStatsActivity.this);//컬럼
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


            for(int j = 0 ; j < 3 ; j++){//컬럼임
                final int cun=i;
                Button rowButton = new Button(adStudentStatsActivity.this);
                rowButton.setBackgroundResource(R.drawable.bartext_purple);//버튼배경
                if(j==0)
                {
                    rowButton.setText(Integer.toString(i*10000000));
                    //rowButton.setText(suggestion_check.getTitle());
                    rowButton.setWidth(100);
                    rowButton.setHeight(50);
                }
                else if(j==1)
                {
                    rowButton.setText(Integer.toString(cun*100000));
                    rowButton.setWidth(100);
                    rowButton.setHeight(50);

                }
                else if(j==2)
                {
                    rowButton.setText(Integer.toString(cun*100000));
                    rowButton.setWidth(100);
                    rowButton.setHeight(50);

                }

                rowButton.setTextSize(12);                     // 폰트사이즈
                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                rowButton.setTypeface(null, Typeface.BOLD);
                rowButton.setEnabled(true);
                rowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //버튼 클릭될 시 할 코드작성
                        title=Integer.toString(cun);//제목 제목 DB값
                        Content=Integer.toString(cun);
                        // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                        // 커스텀 다이얼로그를 호출한다.
                        // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                        // customDialog.callFunction();
                    }
                });
                tableRow.addView(rowButton);
            }
            tableLayout.addView(tableRow);
        }
    }

    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        Edialog(exceptionAsStrting);
    }
    void Edialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage("오류 : "+talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {//달력
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            daySave=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
            cheDayText.setText("선택한 날짜 : "+daySave);
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(adStudentStatsActivity.this/*현재 액티비티 위치*/ , adStudentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        // 코드 작성
    }
}
