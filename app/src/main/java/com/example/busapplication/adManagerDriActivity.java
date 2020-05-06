package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

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
                if(Bustext!=null&&Timetext!=null&&NameeditText.getText().length()!=0){
                    //db자료넣기
                    Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Refresh();
                }else{
                    Toast.makeText(getApplicationContext(), "정보를 적절하게 넣어주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Bustext!=null&&Timetext!=null&&NameeditText.getText().length()!=0){
                    //db자료넣기
                    Toast.makeText(getApplicationContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                    Refresh();
                }else{
                    Toast.makeText(getApplicationContext(), "정보를 적절하게 넣어주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //버스 spinner
        List<String> data2 = new ArrayList<>();
        data2.add("1 ~ 10 호차 노선 선택"); data2.add("1호차"); data2.add("2호차"); data2.add("3호차");
        data2.add("4호차");data2.add("5호차"); data2.add("6호차"); data2.add("7호차");
        data2.add("8호차"); data2.add("9호차");data2.add("10호차");//data.add("전체 버스 선택");
        //배열[]에 db값 넣기
        /* for(int i=0;i<n;i++)
        {
            data2.add(배열[i]);
        }*/
        //버스스피너
        BusSpinner = (Spinner)findViewById(R.id.BUSSpinner3);
        //Adapter
        adapterSpinner1 = new spinnerRows(this, data2);
        //Adapter 적용
        BusSpinner.setAdapter(adapterSpinner1);

        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0)
                    {
                        Bustext = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                    }
                }catch (Exception e)
                {

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
        TimeSpinner = (Spinner)findViewById(R.id.TIMESpinner3);
        //Adapter
        adapterSpinner2 = new spinnerRows(this, data3);
        //Adapter 적용
        TimeSpinner.setAdapter(adapterSpinner2);

        TimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
                    if(position!=0)
                    {
                        Timetext = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                    }
                }catch (Exception e)
                {
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

        for (int i = 0; i < 50; i++) {//  row 임 대신에 컬럼갯
            final TableRow tableRow = new TableRow(this);//컬럼
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            for(int j = 0 ; j < 3 ; j++){//컬럼임
                final int cun=i;
                final int row=j;
                final Button rowButton = new Button(this);


                rowButton.setText(i+","+j);                   // 데이터삽입, 배열[i,j]
                rowButton.setTextSize(18);                     // 폰트사이즈
                rowButton.setGravity(Gravity.CENTER);    // 폰트정렬

                rowButton.setBackgroundResource(R.drawable.barrow);//버튼배경
                rowButton.setWidth(100);
                rowButton.setHeight(50);
                rowButton.setTextColor(Color.BLUE);
                if(j==0)
                {
                    rowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //버튼 클릭될 시 할 코드작성

                            title=String.valueOf(cun);//제목 제목 DB값
                            //  Content=String.valueOf(cun);//내용
                            callNameDialog(title,adManagerDriActivity.this);
                        }
                    });
                }
                else  if(j==1||j==2)
                {
                    rowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //버튼 클릭될 시 할 코드작성
                            title=String.valueOf(cun);//제목 제목 DB값
                            //  Content=String.valueOf(cun);//내용

                            callBusDialog(row,title,adManagerDriActivity.this);
                        }
                    });
                }
                tableRow.addView(rowButton);
            }
            tableLayout.addView(tableRow);
        }
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

    // 호출할 다이얼로그 함수를 정의한다.
    public void callBusDialog(int num,String title,Context context) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg2 = new Dialog(context);
        //모드 spinner
        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg2.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg2.setContentView(R.layout.movedialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg2.show();

        List<String> data1 = new ArrayList<>();
        if(num==1){
            data1.add("시간 선택");
            data1.add("주간등교1");
            data1.add("주간등교2");
            data1.add("주간하교");
            data1.add("야간하교");
        }
        else if(num==2){
            data1.add("1 ~ 10 호차 노선 선택"); data1.add("1호차"); data1.add("2호차"); data1.add("3호차");
            data1.add("4호차");data1.add("5호차"); data1.add("6호차"); data1.add("7호차");
            data1.add("8호차"); data1.add("9호차");data1.add("10호차");
        }
        Dialogspinner = (Spinner) Mapdlg2.findViewById(R.id.MODspinner2);
        //Adapter
        adapterSpinner3 = new spinnerRows(context, data1);
        //Adapter 적용
        Dialogspinner.setAdapter(adapterSpinner3);

        Dialogspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    modStr = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Button okButton3 = (Button) Mapdlg2.findViewById(R.id.okButton3);//변경 버튼
        final Button cancelButton3 = (Button) Mapdlg2.findViewById(R.id.cancelButton3);//취소버튼
        final TextView TTtitle3 = (TextView) Mapdlg2.findViewById(R.id.TTtitle3);//지도 변경 입력칸
        TTtitle3.setText(title);
        okButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB변경코드 넣기
                Refresh();
                Mapdlg2.dismiss();
            }
        });
        cancelButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adManagerDriActivity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg2.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

