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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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
    String busStr;
    //Adapter
    spinnerRows adapterSpinner2;
    //시간 spinner
    Spinner TimeSpinner;
    String timeStr;
    int Busnum=0;
    int Timenum=0;

    private ArrayList<driverReservationActivityDTO> suggestions = new ArrayList<>();
    driverReservationActivityDTO suggestion_check = new driverReservationActivityDTO();

    List<String> load_busName = new ArrayList<>(); //busspinner 값들
    List<String> load_busType = new ArrayList<>(); //Timespinner 값들

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_reservation);

        final Button ADbusMain15 = (Button)findViewById(R.id.ADbusMain15);//뒤로가기
        ADbusMain15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adReservationActivity.this/*현재 액티비티 위치*/ , adStudentActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });
//모드 spinner
        final Button reserCheButton = (Button)findViewById(R.id.reserCheButton);//확인
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.adReverTable);

        reserCheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Busnum==0&&Timenum==0){
                    Toast.makeText(getApplicationContext(), "시간이나 노선을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(Busnum!=0 && Timenum!=0){
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);

                                for(int i=0; i<array.length();i++){
                                    JSONObject student_suggestions = array.getJSONObject(i);

                                    suggestions.add(new driverReservationActivityDTO(
                                            student_suggestions.getString("userID")
                                    ));
                                }
                                for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                                    TableRow tableRow = new TableRow(adReservationActivity.this);//컬럼
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                                    for(int j = 0 ; j < 2 ; j++){//컬럼임
                                        final int cun=i;
                                        Button rowButton = new Button(adReservationActivity.this);
                                        rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                                        suggestion_check = suggestions.get(cun);

                                        if(j==0)
                                        {
                                            rowButton.setText(Integer.toString(i+1));
                                            rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                            rowButton.setWidth(100);
                                            rowButton.setHeight(50);
                                        }

                                        else if(j==1)
                                        {
                                            rowButton.setText(suggestion_check.getUserID());
                                            rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                            rowButton.setWidth(100);
                                            rowButton.setHeight(50);
                                            rowButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //버튼 클릭될 시 할 코드작성
                                                    title=suggestion_check.getUserID();//제목 제목 DB값
                                                    // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                                                    // 커스텀 다이얼로그를 호출한다.
                                                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                                                    // customDialog.callFunction();
                                                    //callFunction(adReservationActivity.this,cun, suggestion_check.getUserID());
                                                }
                                            });
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

                    driverReservationRequest_load_bookedUser load_bookeduser = new driverReservationRequest_load_bookedUser(busStr, timeStr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(adReservationActivity.this);
                    queue.add(load_bookeduser);
                }
                else{}
            }
        });


        //버스 spinner
        BusSpinner = (Spinner)findViewById(R.id.BUSSpinner7);
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
                        adapterSpinner1 = new spinnerRows(adReservationActivity.this, load_busName);
                        //Adapter 적용
                        BusSpinner.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(adReservationActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }

        //시간 spinner
        TimeSpinner = (Spinner)findViewById(R.id.TIMESpinner7);

        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                                    adapterSpinner2 = new spinnerRows(adReservationActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BusSpinner.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adReservationActivity.this);
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
        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        Intent i = new Intent(adReservationActivity.this/*현재 액티비티 위치*/ , adStudentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
