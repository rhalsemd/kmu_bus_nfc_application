package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class adManagerDriActivity extends AppCompatActivity {

    //DB에 넣을 때 아래의 변수 지우기
    String title="제목";//제목 DB에 넣기

    //버스 spinner
    Spinner BusSpinner;
    //Adapter
    spinnerRows adapterSpinner1;
    //시간 spinner
    Spinner TimeSpinner;
    //Adapter
    spinnerRows adapterSpinner2;

    //다이얼로그
    Spinner Dialogspinner;
    spinnerRows adapterSpinner3;
    String modStr;

    String Bustext;
    String Timetext;
    int Busnum=0;
    int Timenum=0;

    private ArrayList<driverBusDTO> suggestions = new ArrayList<>();
    driverBusDTO suggestion_check = new driverBusDTO();

    List<String> load_busName = new ArrayList<>(); //busspinner 값들
    List<String> load_busType = new ArrayList<>(); //Timespinner 값들

    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_manager_dri);

        Button addButton=(Button)findViewById(R.id.addButton);//버스기사 추가
        Button DelButton=(Button)findViewById(R.id.DelButton);//버스 기사 삭제
        Button ADbusMain17=(Button)findViewById(R.id.ADbusMain17);//뒤로가기
        ADbusMain17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(adManagerDriActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        final EditText NameeditText = (EditText)findViewById(R.id.NameeditText);//edit 시간 추가

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Busnum!=0&&Timenum!=0&&NameeditText.getText().length()!=0){
                    String driver_ID = NameeditText.getText().toString();
                    //db자료넣기
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success)
                                {
                                    Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    Refresh();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    };
                    adManagerDriRequest admanagerdrirequest = new adManagerDriRequest(Bustext,Timetext, driver_ID,"add", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(adManagerDriActivity.this) ;
                    queue.add(admanagerdrirequest);

                }else{
                    Toast.makeText(getApplicationContext(), "정보를 적절하게 넣어주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Busnum!=0&&Timenum!=0){
                    String driver_ID = NameeditText.getText().toString();
                    //db자료넣기
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if(success)
                                {
                                    Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    Refresh();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    };
                    adManagerDriRequest admanagerdrirequest = new adManagerDriRequest(Bustext,Timetext,driver_ID,"del", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(adManagerDriActivity.this) ;
                    queue.add(admanagerdrirequest);

                }else{
                    Toast.makeText(getApplicationContext(), "정보를 적절하게 넣어주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //버스 spinner
        BusSpinner = (Spinner)findViewById(R.id.BUSSpinner3);
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
                        adapterSpinner1 = new spinnerRows(adManagerDriActivity.this, load_busName);
                        //Adapter 적용
                        BusSpinner.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(adManagerDriActivity.this);
            queue.add(loadname);
        }catch (Exception e){
            Excep(e);
        }

        //시간 spinner
        TimeSpinner = (Spinner)findViewById(R.id.TIMESpinner3);

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
                                    adapterSpinner2 = new spinnerRows(adManagerDriActivity.this, load_busType);
                                    //Adapter 적용
                                    TimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BusSpinner.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(adManagerDriActivity.this);
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


        //표
        //컬럼으로 (내용,날짜, 제목, 내용)으로하고 Content에 내용부분 넣기
        //db자료를 2차원 배열같은데 넣고 setText에 db가 들어간 배열 출력
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.DriverMagTable); // 테이블 id 명

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length();i++){
                        JSONObject student_suggestions = array.getJSONObject(i);

                        suggestions.add(new driverBusDTO(
                                student_suggestions.getString("bus_type"),
                                student_suggestions.getString("bus_name"),
                                student_suggestions.getString("userID")
                        ));
                    }
                    for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(adManagerDriActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 3 ; j++){//컬럼임
                            final int cun= i;
                            Button rowButton = new Button(adManagerDriActivity.this);
                            rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                            suggestion_check = suggestions.get(cun);

                            if(j==0)
                            {
                                rowButton.setText(suggestion_check.getUserID());
                                //rowButton.setText(suggestion_check.getTitle());
                                rowButton.setWidth(100);
                                rowButton.setHeight(50);

                                rowButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //버튼 클릭될 시 할 코드작성

                                      //  callNameDialog(suggestion_check.getUserID(),suggestion_check.getBus_name(),suggestion_check.getBus_type(), adManagerDriActivity.this);
                                    }
                                });
                            }

                            else if(j==1)
                            {

                                rowButton.setText(suggestion_check.getBus_name());
                                rowButton.setWidth(100);
                                rowButton.setHeight(50);

                            }

                            else if(j==2)
                            {

                                rowButton.setText(suggestion_check.getBus_type());
                                rowButton.setWidth(100);
                                rowButton.setHeight(50);

                            }

                            rowButton.setTextSize(12);                     // 폰트사이즈
                            rowButton.setTextColor(Color.BLACK);     // 폰트컬러
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

        adManagerDriRequest_driverlist driverlistrequest = new adManagerDriRequest_driverlist(responseListener);
        RequestQueue queue = Volley.newRequestQueue(adManagerDriActivity.this);
        queue.add(driverlistrequest);

    }
    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callNameDialog(String title,Context context) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg1 = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg1.setContentView(R.layout.map_custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg1.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView TTtitle1 = (TextView) Mapdlg1.findViewById(R.id.TTtitle1);//지도 변경 입력칸
        TTtitle1.setText(title);
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText MAPmesgase = (EditText) Mapdlg1.findViewById(R.id.MAPmesgase);//지도 변경 입력칸
        final Button MAPokButton = (Button) Mapdlg1.findViewById(R.id.MAPokButton);//변경 버튼
        final Button MAPcancelButton = (Button) Mapdlg1.findViewById(R.id.MAPcancelButton);//취소버튼

        MAPokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                //DB변경코드 넣기
                Refresh();
                Toast.makeText(adManagerDriActivity.this, "\"" +  MAPmesgase.getText().toString() + "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();
                Refresh();//세로고침
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
        MAPcancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adManagerDriActivity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
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
        //super.onBackPressed();
        Intent i = new Intent(adManagerDriActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}

