package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class adBusDataActivity extends AppCompatActivity {

    //모드 spinner
    Spinner MODESpinner;
    //Adapter
    spinnerRows adapterSpinner1;
    //버스 spinner
    Spinner BusSpinner;
    //Adapter
    spinnerRows adapterSpinner2;
    //시간 spinner
    Spinner TimeSpinner;
    //Adapter
    spinnerRows adapterSpinner3;

    //spinner 어떤것이 선택되었는 지를 보여준다
    String Bustext;
    String Timetext;

    //spinner 몇번째것이 선택되었는 지를 보여준다
    int MODEnum=0;
    int Busnum=0;
    int Timenum=0;

    EditText changeditText;

    List<String> load_busName = new ArrayList<>(); //busspinner 값들
    List<String> load_busType = new ArrayList<>(); //Timespinner 값들

    String datacheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_bus_data);

        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        datacheck=value;//값전달

        Button MapCheckbutton=(Button)findViewById(R.id.MapCheckbutton);//지도로 버스 좌표 변경
        Button BUSAddbutton=(Button)findViewById(R.id.BUSAddbutton);//버스 추가
        Button TimeAddbutton=(Button)findViewById(R.id.TimeAddbutton);//시간 추가

        Button ADbusMain4=(Button)findViewById(R.id.ADbusMain4);//메인화면으로

        TextView ListBUtextView = (TextView)findViewById(R.id.ListBUtextView);//지도로 버스 좌표변경
        changeditText = (EditText)findViewById(R.id.changeditText);//edit 시간 추가

        Button TimeDel_button=(Button)findViewById(R.id.TimeDel_button);//시간 삭제
        TimeDel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        BUSAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(load_busName.size()!=0&&Timenum!=0) {
                    String add_bus = Integer.toString(load_busName.size()) + "호차";
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "호차가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                    Refresh();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "호차 추가 실패!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                return;
                            }

                        }
                    };
                    adBusDataRequest_addBus busdatarequest_addBus = new adBusDataRequest_addBus(Timetext, add_bus, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(adBusDataActivity.this);
                    queue.add(busdatarequest_addBus);
                }
                else if (Timenum == 0) {
                    Toast.makeText(getApplicationContext(), "새로운 호차를 선택할시에는 무조건 시간을 선택하여야합니다. 시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                }

            }
        });

        ListBUtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView 클릭될 시 할 코드작성
                //화면전환
                if(Busnum!=0&&Timenum!=0) {
                    Intent intent_value = new Intent(adBusDataActivity.this/*현재 액티비티 위치*/, adMapListActivity.class/*이동 액티비티 위치*/);
                    intent_value.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent_value.putExtra("Busvalue", Bustext);//값전달
                    intent_value.putExtra("Timevalue", Timetext);//값전달
                    startActivity(intent_value);
                }
                else {
                    Toast.makeText(getApplicationContext(), "버스노선과 버스시간, 모드를 선택헤주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MapCheckbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Busnum!=0&&Timenum!=0)
                {
                    Intent intent_value = new Intent(adBusDataActivity.this/*현재 액티비티 위치*/ , adListBUSActivity.class/*이동 액티비티 위치*/);
                    intent_value.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent_value.putExtra("Busvalue", Bustext);//값전달
                    intent_value.putExtra("Timevalue", Timetext);//값전달
                    startActivity(intent_value);
                }
                else {
                    Toast.makeText(getApplicationContext(), "버스노선과 버스시간을 선택헤주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TimeAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeditText.getText().length()!=0&&Busnum!=0) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "시간이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                    Refresh();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "시간 추가 실패!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                return;
                            }

                        }
                    };
                    adBusDataRequest_addTime busdatarequest_addtime = new adBusDataRequest_addTime(Bustext, changeditText.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(adBusDataActivity.this);
                    queue.add(busdatarequest_addtime);
                }
                else if (Busnum == 0) {
                    Toast.makeText(getApplicationContext(), "호차를 선택해주세요!", Toast.LENGTH_SHORT).show();
                } else if (changeditText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "시간을 제대로 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else if (Busnum == 0 && changeditText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "호차와 시간을 제대로 입력해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

        });

        ADbusMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adBusDataActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });


        //모드 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("모드 선택");
        data1.add("좌표확인및카메라변경");
        data1.add("지도확인");
        //배열[]에 db값 넣기
        /* for(int i=0;i<n;i++)
        {
            data1.add(배열[i]);
        }*/


        //버스 spinner
        BusSpinner = (Spinner)findViewById(R.id.BUSSpinner2);
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
                        adapterSpinner1 = new spinnerRows(adBusDataActivity.this, load_busName);
                        //Adapter 적용
                        BusSpinner.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(adBusDataActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }

        //시간 spinner
        TimeSpinner = (Spinner)findViewById(R.id.TIMESpinner2);

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
                                    adapterSpinner2 = new spinnerRows(adBusDataActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BusSpinner.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adBusDataActivity.this);
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
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        Edialog(exceptionAsStrting);
    }
    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adBusDataActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        // 코드 작성
    }
}
