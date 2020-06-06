package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class adReservationActivity extends AppCompatActivity {
    String title;
    String Content;
    //Adapter
    spinnerRows adapterSpinner1;
    //버스 spinner
    Spinner BusSpinner;
    //Adapter
    spinnerRows adapterSpinner2;
    //시간 spinner
    Spinner TimeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_reservation);

        final Button ADbusMain15 = (Button)findViewById(R.id.ADbusMain15);//뒤로가기
        ADbusMain15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adReservationActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });
//모드 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("노선 선택");
        for(int i=0;i<10;i++){

            data1.add(Integer.toString(i)+"호차");
        }

        //UI생성
        //UI생성
        //버스 스피너
        BusSpinner = (Spinner)findViewById(R.id.BUSSpinner7);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data1);
        //Adapter 적용
        BusSpinner.setAdapter(adapterSpinner1);

        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{

                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //모드 spinner
        List<String> data2 = new ArrayList<>();
        data2.add("시간 선택");
        data2.add("주간등교1");
        data2.add("주간등교2");
        data2.add("주간하교");
        data2.add("야간하교");

        //UI생성
        //UI생성
        //버스 스피너
        TimeSpinner = (Spinner)findViewById(R.id.TIMESpinner7);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data2);
        //Adapter 적용
        TimeSpinner.setAdapter(adapterSpinner1);

        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{

                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.adReverTable);
        for (int i = 0; i < 100; i++) {//  row 임 대신에 컬럼갯
            TableRow tableRow = new TableRow(adReservationActivity.this);//컬럼
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


            for(int j = 0 ; j < 2 ; j++){//컬럼임
                final int cun=i;
                Button rowButton = new Button(adReservationActivity.this);
                rowButton.setBackgroundResource(R.drawable.bartext_purple);//버튼배경
                if(j==0)
                {
                    rowButton.setText(Integer.toString(i));
                    //rowButton.setText(suggestion_check.getTitle());
                    rowButton.setWidth(150);
                    rowButton.setHeight(50);
                }
                else if(j==1)
                {
                    rowButton.setText(Integer.toString(cun));
                    rowButton.setWidth(350);
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
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adReservationActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
