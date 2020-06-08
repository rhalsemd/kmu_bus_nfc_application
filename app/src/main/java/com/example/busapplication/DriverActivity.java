package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {
    String IDvalue;
    String ad="3";//관리자
    ImageView myStar;
    //예외처리 다이얼로그에 쓰임
    String whoSLD;
    String checkUser="버스기사";
    int i=0;

    //Adapter
    spinnerRows adAdapterSpinner1;
    //버스 spinner
    Spinner adBusSpinner;
    //Adapter
    spinnerRows adAdapterSpinner2;
    //시간 spinner
    Spinner adTimeSpinner;
    String adBusStr1;
    String adTimeStr1;
    //Adapter
    spinnerRows_green DriadapterSpinner1;
    //버스 spinner
    Spinner DriBusSpinner;
    String DriStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);


        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        String value12 = intent.getStringExtra("value"); //메인에서 넘어온 아이디값


        IDvalue=value12;


        //IDvalue이 관리자 아이디랑 같으면
        Button adDriverButton=(Button)findViewById(R.id.adDriverButton);//버스노선 확인
        Button PersonalData=(Button)findViewById(R.id.PersonalData);//개인정보확인
        Button suggestionsCheck=(Button)findViewById(R.id.suggestionsCheck);//건의사항 체크
        Button bookManagement=(Button)findViewById(R.id.bookManagement);//버스예약관리
        Button memberlogout=(Button)findViewById(R.id.memberlogout);//로그아웃

        //누구님 안녕하세요 칸
        final TextView drivertextView = (TextView)findViewById(R.id.drivertextView);
        drivertextView.setText(IDvalue+"님 안녕하세요");


        //공지사항
        final Button noticeDriver = (Button)findViewById(R.id.noticeDriver);
        noticeDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_user = new Intent(DriverActivity.this, AppNoticeActivity.class);
                intent_user.putExtra("checkuser", checkUser);
                intent_user.putExtra("value", IDvalue);
                startActivity(intent_user);
                // TextView 클릭될 시 할 코드작성
            }
        });

        if(IDvalue.equals(ad))//3은 관리자 ID, 관리자인 경우
        {
            adDriverButton.setVisibility(View.VISIBLE);
            adDriverButton.setEnabled(true);

            adDriverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(DriverActivity.this)
                            .setMessage("원하는 기능을 확인했습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ADmove();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }).show();
                    //화면전환
                }
            });
        }
        else {
            //관리자가 아니면 버튼을 숨김
            adDriverButton.setVisibility(View.INVISIBLE);
            adDriverButton.setEnabled(false);
        }

        PersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent=new Intent(getApplicationContext(), drivermangerDataActivity.class);
                    intent.putExtra("value1",IDvalue);
                    startActivity(intent);
                    //화면전환
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });

        suggestionsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), driverSuggestionsActivity.class);
                    intent.putExtra("value", "버스기사");
                    startActivity(intent);
                    //화면전환
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });

        bookManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(IDvalue.equals(ad)){
                        adCallFunction(DriverActivity.this);
                    }
                    else{
                        DriCallFunction(DriverActivity.this);
                    }

                }catch (Exception e)
                {
                    Excep(e);
                }
                //화면전환
            }
        });


        memberlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //화면전환
                    if (IDvalue.equals(ad))//3은 관리자 ID, 관리자인 경우
                    {
                        new AlertDialog.Builder(DriverActivity.this)
                                .setMessage("원하는 기능을 확인했습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ADmove();
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                }).show();
                    } else {
                        //회원인 경우
                        Move();
                    }
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        try {
            //화면전환
            if (IDvalue.equals(ad))//3은 관리자 ID, 관리자인 경우
            {
                new AlertDialog.Builder(DriverActivity.this)
                        .setMessage("원하는 기능을 확인했습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ADmove();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            } else {
                //회원인 경우
                Move();
            }
        }catch (Exception e)
        {
            Excep(e);
        }
        //super.onBackPressed();
    }
    void dialog(String who,String talk)
    {
        whoSLD=who;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(who+" : "+talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                if(whoSLD.equals("기능고장"))
                {
                    ADmove();
                }
                else {
                    Move();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void ADmove()
    {
        Intent i = new Intent(DriverActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Move()
    {
        Intent i = new Intent(DriverActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(IDvalue.equals(ad))
        {
            dialog("기능고장",exceptionAsStrting);
        }
        else{
            dialog("오류", exceptionAsStrting);
        }
    }

    public void DriCallFunction(Context context) {
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
        data1.add("노선 선택");
        data1.add("주간등교1 1호차");
        data1.add("주간등교2 2호차");
        DriBusSpinner = (Spinner) Mapdlg2.findViewById(R.id.Drispinner);
        //Adapter
        DriadapterSpinner1 = new spinnerRows_green(context, data1);
        //Adapter 적용
        DriBusSpinner.setAdapter(DriadapterSpinner1);
        DriBusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    DriStr = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Button okButton3 = (Button) Mapdlg2.findViewById(R.id.okButton3);//변경 버튼
        final Button cancelButton3 = (Button) Mapdlg2.findViewById(R.id.cancelButton3);//취소버튼
        okButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DriStr.equals("노선 선택")) {
                    Toast.makeText(DriverActivity.this, "버스를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // TextView 클릭될 시 할 코드작
                    Intent intent_user = new Intent(DriverActivity.this, driverBookActivity.class);
                    intent_user.putExtra("value1",IDvalue);
                    intent_user.putExtra("adBusStr", "확인입니다");
                    intent_user.putExtra("adTimeStr", "확인이예요");
                    startActivity(intent_user);
                }
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg2.dismiss();
            }
        });
        cancelButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg2.dismiss();
            }
        });
    }

    public void adCallFunction(Context context) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg2 = new Dialog(context);
        //모드 spinner
        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg2.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg2.setContentView(R.layout.move_reser_dialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg2.show();

        List<String> data1 = new ArrayList<>();
        data1.add("노선 선택");
        for(int i=0;i<10;i++){
            data1.add(Integer.toString(i)+"호차");
        }
        adBusSpinner=(Spinner)Mapdlg2.findViewById(R.id.adBusspinner1);
        //Adapter
        adAdapterSpinner1 = new spinnerRows(context, data1);
        //Adapter 적용
        adBusSpinner.setAdapter(adAdapterSpinner1);
        adBusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    adBusStr1 = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        List<String> data2 = new ArrayList<>();
        data2.add("노선 선택");
        data2.add("주간등교1");
        data2.add("주간등교2");
        data2.add("주간하교");
        data2.add("야간하교");
        adTimeSpinner = (Spinner) Mapdlg2.findViewById(R.id.adTimespinner1);
        //Adapter
        adAdapterSpinner2 = new spinnerRows(context, data2);
        //Adapter 적용
        adTimeSpinner.setAdapter(adAdapterSpinner2);
        adTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    adTimeStr1 = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final Button cancelAdButton = (Button) Mapdlg2.findViewById(R.id.cancelAdButton);//변경 버튼
        final Button cheAdButton = (Button) Mapdlg2.findViewById(R.id.cheAdButton);//취소버튼
        cheAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adBusStr1.equals("노선 선택")||adTimeStr1.equals("노선 선택")) {
                    Toast.makeText(DriverActivity.this, "버스를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // TextView 클릭될 시 할 코드작
                    Intent intent_user = new Intent(DriverActivity.this, driverBookActivity.class);
                    intent_user.putExtra("value1",IDvalue);
                    intent_user.putExtra("adBusStr1", adBusStr1);
                    intent_user.putExtra("adTimeStr1", adTimeStr1);
                    startActivity(intent_user);
                }
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg2.dismiss();
            }
        });
        cancelAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg2.dismiss();
            }
        });
    }
}
