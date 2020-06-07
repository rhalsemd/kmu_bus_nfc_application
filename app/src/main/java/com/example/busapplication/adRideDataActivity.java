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

    List<String> load_busName = new ArrayList<>(); //busspinner 값들
    List<String> load_busType = new ArrayList<>(); //Timespinner 값들
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_ride_data);

        Intent intent = getIntent(); /*데이터 수신*/
       // value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        // TextView Datacheck6 = (TextView)findViewById(R.id.Datacheck6);
        //Datacheck6.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것
        final TextView reasonWrite = (TextView)findViewById(R.id.reasonWrite);

        //버스 운행불가
        Button stapBus=(Button)findViewById(R.id.stapBus);
        //버스 운행불가취소
        Button cancellation=(Button)findViewById(R.id.cancellation);
        Button ADbusMain3=(Button)findViewById(R.id.ADbusMain3);//메인화면으로 이동

        //버스스피너
        BusSpinner = (Spinner)findViewById(R.id.busSpinner);
        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);

                        load_busName.add("노선 선택");
                        for(int i=0; i<array.length();i++){
                            JSONObject bus_name = array.getJSONObject(i);
                            load_busName.add(bus_name.getString("bus_name"));
                        }

                        //Adapter
                        adapterSpinner1 = new spinnerRows(adRideDataActivity.this, load_busName);
                        //Adapter 적용
                        BusSpinner.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(adRideDataActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }
        //시간 spinner
        TimeSpinner = (Spinner)findViewById(R.id.timeSpinner);

        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bustext = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
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
                                    adapterSpinner2 = new spinnerRows(adRideDataActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BusSpinner.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adRideDataActivity.this);
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
                Timetext = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                try{
                    Timenum=position;
                    Datacheck21.setText(Timetext);
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
                    String userID = value; //메인에서 넘어온 유저ID 값
                    String Reason = reasonWrite.getText().toString();
                    if(Timenum != 0 && Busnum != 0 && !Reason.equals("")){
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Intent intent = new Intent(adRideDataActivity.this, administratorActivity.class);
                                        dialog("등록되었습니다.");
                                    } else {
                                        Toast.makeText(getApplicationContext(), "DB관리자에게 연락바랍니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        adRideDataRequest registerRequest = new adRideDataRequest(Timetext, Bustext, Reason, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adRideDataActivity.this);
                        queue.add(registerRequest);
                    }
                    else if(Busnum == 0)
                    {
                        Toast.makeText(getApplicationContext(), "호차를 선택해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else if(Timenum == 0)
                    {
                        Toast.makeText(getApplicationContext(), "시간을 선택해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else if(Reason.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "사유를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }

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
        ADmove();
    }
    void ADmove()
    {
        Intent i = new Intent(adRideDataActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
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
                ADmove();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void Edialog(String talk)
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
        Edialog(exceptionAsStrting);
    }
}
