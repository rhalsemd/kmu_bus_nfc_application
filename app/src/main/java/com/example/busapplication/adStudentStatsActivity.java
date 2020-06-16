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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    TextView cheDayText;//달력
    String daySave = "";//달력 저장



    int Modenum=0;
    int Busnum=0;
    int Timenum=0;

    private ArrayList<adStudentStatsActivityDTO> suggestions = new ArrayList<>();
    adStudentStatsActivityDTO suggestion_check = new adStudentStatsActivityDTO();

    private ArrayList<adStudentStatsActivityDTO_reserver> suggestions_reserver = new ArrayList<>();
    adStudentStatsActivityDTO_reserver suggestion_check_reserver = new adStudentStatsActivityDTO_reserver();

    List<String> load_busName = new ArrayList<>(); //busspinner 값들
    List<String> load_busType = new ArrayList<>(); //Timespinner 값들
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_student_stats);

       final Button stastsButton = (Button)findViewById(R.id.stastsButton);//확인
       final Button dayButton = (Button)findViewById(R.id.dayButton);//달력
       final Button ADbusMain19 = (Button)findViewById(R.id.ADbusMain19);//뒤로가기

        cheDayText = (TextView)findViewById(R.id.cheDayText10);

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
        ModeSpinner10 = (Spinner)findViewById(R.id.ModeSP);
        //Adapter
        adapterSpinner3 = new spinnerRows(this, data1);
        //Adapter 적용
        ModeSpinner10.setAdapter(adapterSpinner3);

        ModeSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Modenum=position;
                    if(position==2){
                        BusSpinner10.setEnabled(false);
                        BusSpinner10.setClickable(false);
                        BusSpinner10.setVisibility(View.INVISIBLE);

                        TimeSpinner10.setEnabled(false);
                        TimeSpinner10.setClickable(false);
                        TimeSpinner10.setVisibility(View.INVISIBLE);



                    }
                    else {
                        BusSpinner10.setEnabled(true);
                        BusSpinner10.setClickable(true);
                        BusSpinner10.setVisibility(View.VISIBLE);

                        TimeSpinner10.setEnabled(true);
                        TimeSpinner10.setClickable(true);
                        TimeSpinner10.setVisibility(View.VISIBLE);;


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
        BusSpinner10 = (Spinner)findViewById(R.id.BusSP);
        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);

                        load_busName.clear();
                        load_busName.add("노선 선택");
                        for(int i=0; i<array.length();i++){
                            JSONObject bus_name = array.getJSONObject(i);
                            load_busName.add(bus_name.getString("bus_name"));
                        }

                        //Adapter
                        adapterSpinner1 = new spinnerRows(adStudentStatsActivity.this, load_busName);
                        //Adapter 적용
                        BusSpinner10.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(adStudentStatsActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }

        //시간 spinner
        TimeSpinner10 = (Spinner)findViewById(R.id.TimeSP);

        BusSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                busStr = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
                try{
                    Busnum=position;
                    try {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);

                                    load_busType.clear();
                                    load_busType.add("시간 선택");
                                    for(int i=0; i<array.length();i++){
                                        JSONObject bus_name = array.getJSONObject(i);
                                        load_busType.add(bus_name.getString("bus_type"));
                                    }

                                    //Adapter
                                    adapterSpinner2 = new spinnerRows(adStudentStatsActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner10.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BusSpinner10.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adStudentStatsActivity.this);
                        queue.add(loadType);
                    }catch (Exception e){
                        Excep(e);
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
        TimeSpinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeStr = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                try{
                    Timenum=position;
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.adStustsTable);

        stastsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(Modenum == 1) {
                        tableLayout.removeAllViews();
                        suggestions.clear();

                        if(daySave !="" && Timenum!=0 && Busnum!=0 && Modenum!=0) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray array = new JSONArray(response);

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject student_suggestions = array.getJSONObject(i);

                                            suggestions.add(new adStudentStatsActivityDTO(
                                                    student_suggestions.getString("total")
                                            ));
                                        }
                                        for (int i = 0; i < 1; i++) {//  row 임 대신에 컬럼갯
                                            TableRow tableRow = new TableRow(adStudentStatsActivity.this);//컬럼
                                            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                            for(int j = 0 ; j < 3 ; j++){//컬럼임
                                                final int cun=i;
                                                Button rowButton = new Button(adStudentStatsActivity.this);
                                                if(i==0){
                                                    rowButton.setBackgroundColor(Color.parseColor("#7030A0"));
                                                    rowButton.setTextColor(Color.WHITE);
                                                    if (j == 0) {
                                                        if(Modenum==2){
                                                            rowButton.setText("ID");
                                                        }
                                                        else{
                                                            rowButton.setText("호차");
                                                        }

                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);

                                                    } else if (j == 1) {
                                                        if(Modenum==2){
                                                            rowButton.setText("호차");
                                                        }
                                                        else{
                                                            rowButton.setText("탐승자수");
                                                        }


                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);
                                                    }
                                                    else if (j == 2) {
                                                        if(Modenum==2){
                                                            rowButton.setText("시간");
                                                        }
                                                        else{
                                                            rowButton.setText("시간");
                                                        }


                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);
                                                    }
                                                    rowButton.setEnabled(false);

                                                }
                                                rowButton.setTextSize(12);                     // 폰트사이즈
                                                rowButton.setTypeface(null, Typeface.BOLD);
                                                tableRow.addView(rowButton);
                                            }
                                            tableLayout.addView(tableRow);
                                        }
                                        for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯

                                            TableRow tableRow = new TableRow(adStudentStatsActivity.this);//컬럼
                                            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                                            for (int j = 0; j < 3; j++) {//컬럼임
                                                final int cun = i;
                                                Button rowButton = new Button(adStudentStatsActivity.this);
                                                rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                                                suggestion_check = suggestions.get(cun);


                                                    if (j == 0) {
                                                        rowButton.setText(busStr);
                                                        rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);
                                                    } else if (j == 1) {
                                                        rowButton.setText(timeStr);
                                                        rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);
                                                    } else if (j == 2) {
                                                        rowButton.setText(suggestion_check.getTotal_seated());
                                                        rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                        rowButton.setWidth(100);
                                                        rowButton.setHeight(50);
                                                    }
                                                    rowButton.setTextColor(Color.BLACK);

                                                rowButton.setTextSize(12);                     // 폰트사이즈
                                                rowButton.setTypeface(null, Typeface.BOLD);
                                                rowButton.setEnabled(true);


                                                tableRow.addView(rowButton);
                                            }
                                            tableLayout.addView(tableRow);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            //서버로 volley를 이용해서 요청
                            adStudentStatsRequest_load_specific_bus load_specific_bus = new adStudentStatsRequest_load_specific_bus(busStr, timeStr, daySave, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(adStudentStatsActivity.this);
                            queue.add(load_specific_bus);
                        }
                        else if(Timenum==0 || Busnum==0){
                            Toast.makeText(getApplicationContext(), "시간이나 노선을 선택해주세요!", Toast.LENGTH_SHORT).show();
                        }
                        else if(Modenum==0){
                            Toast.makeText(getApplicationContext(), "모드를 선택해주세요!", Toast.LENGTH_SHORT).show();
                        }
                        else if(daySave == ""){
                            Toast.makeText(getApplicationContext(), "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(Modenum == 2){
                        tableLayout.removeAllViews();
                        suggestions_reserver.clear();

                        if(daySave != ""){
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray array = new JSONArray(response);

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject student_suggestions_reserver = array.getJSONObject(i);

                                            suggestions_reserver.add(new adStudentStatsActivityDTO_reserver(
                                                    student_suggestions_reserver.getString("userID"),
                                                    student_suggestions_reserver.getString("bus_name"),
                                                    student_suggestions_reserver.getString("bus_type")
                                            ));
                                        }
                                        for (int i = 0; i < suggestions_reserver.size(); i++) {//  row 임 대신에 컬럼갯

                                            TableRow tableRow = new TableRow(adStudentStatsActivity.this);//컬럼
                                            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                                            for (int j = 0; j < 3; j++) {//컬럼임
                                                final int cun = i;
                                                Button rowButton = new Button(adStudentStatsActivity.this);
                                                rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                                                suggestion_check_reserver = suggestions_reserver.get(cun);

                                                if (j == 0) {
                                                    rowButton.setText(suggestion_check_reserver.getUserID());
                                                    rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                    rowButton.setWidth(100);
                                                    rowButton.setHeight(50);
                                                } else if (j == 1) {
                                                    rowButton.setText(suggestion_check_reserver.getBus_name());
                                                    rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                    rowButton.setWidth(100);
                                                    rowButton.setHeight(50);
                                                } else if (j == 2) {
                                                    rowButton.setText(suggestion_check_reserver.getBus_type());
                                                    rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                                    rowButton.setWidth(100);
                                                    rowButton.setHeight(50);
                                                }

                                                rowButton.setTextColor(Color.BLACK);
                                                rowButton.setTextSize(12);                     // 폰트사이즈
                                                rowButton.setTypeface(null, Typeface.BOLD);
                                                rowButton.setEnabled(true);


                                                tableRow.addView(rowButton);
                                            }
                                            tableLayout.addView(tableRow);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            //서버로 volley를 이용해서 요청

                            adStudentStatsRequest_load_reserver load_reserver = new adStudentStatsRequest_load_reserver(daySave, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(adStudentStatsActivity.this);
                            queue.add(load_reserver);
                        }
                        else if(daySave == ""){
                            Toast.makeText(getApplicationContext(), "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show();
                        }

                    }
            }
        });
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
            if(month+1<=9&&dayOfMonth>=10){
                daySave=String.valueOf(year)+"-0"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
            }
            else if(dayOfMonth<=9&&month+1>=10){
                daySave=String.valueOf(year)+"-"+String.valueOf(month+1)+"-0"+String.valueOf(dayOfMonth);
            }
            else if(month+1<=9&&dayOfMonth<=9){
                daySave=String.valueOf(year)+"-0"+String.valueOf(month+1)+"-0"+String.valueOf(dayOfMonth);
            }
            else{
                daySave=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
            }

            cheDayText.setText(daySave);
        }
    };

    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(adStudentStatsActivity.this/*현재 액티비티 위치*/ , adStudentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        // 코드 작성
    }
}
