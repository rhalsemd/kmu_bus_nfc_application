package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class driverSuggestionsActivity extends AppCompatActivity {

    //DB에 넣을 때 아래의 변수 지우기
    String value;
    String ad="3";
    int i;

    String title;
    String Content;
    String check_admin;

    private ArrayList<driverSuggestionsDTO> suggestions = new ArrayList<>();
    driverSuggestionsDTO suggestion_check = new driverSuggestionsDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dirver_suggestions);

        Intent intent = getIntent(); /*데이터 수신*/
        check_admin = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값


        final TextView Datacheck9 = (TextView)findViewById(R.id.Datacheck9);//pw로그인
        Datacheck9.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것
        Button driverbusMain2=(Button)findViewById(R.id.driverbusMain2);//메인화면으로
        final TextView textView55 = (TextView)findViewById(R.id.textView55);//
        final TextView textView59 = (TextView)findViewById(R.id.textView59);//
        final ConstraintLayout mylayout = (ConstraintLayout)findViewById(R.id.backSuggestions5);
        if(check_admin.equals("어드민"))
        {
            driverbusMain2.setBackgroundResource(R.drawable.mainscreen3);
            mylayout.setBackgroundResource(R.drawable.background3);
            textView59.setBackgroundColor(Color.parseColor("#7030A0"));
            textView55.setBackgroundColor(Color.parseColor("#7030A0"));

        }




        //checkButton

        //표
        //컬럼으로 (내용,날짜, 제목, 내용)으로하고 Content에 내용부분 넣기
        //db자료를 2차원 배열같은데 넣고 setText에 db가 들어간 배열 출력
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.testTable); // 테이블 id 명

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length();i++){
                        JSONObject student_suggestions = array.getJSONObject(i);

                        suggestions.add(new driverSuggestionsDTO(
                                student_suggestions.getString("title"),
                                student_suggestions.getString("content"),
                                student_suggestions.getString("time"),
                                student_suggestions.getString("userID")
                        ));
                    }
                    for (i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(driverSuggestionsActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 2 ; j++){//컬럼임
                            final int cun=i;
                            Button rowButton = new Button(driverSuggestionsActivity.this);
                        //    rowButton.setBackgroundResource(R.drawable.barrow2);//버튼배경
                            suggestion_check = suggestions.get(cun);
                            if(check_admin.equals("어드민"))
                            {
                                rowButton.setBackgroundResource(R.drawable.bartext_purple);//버튼배경
                            }
                            else if(check_admin.equals("버스기사"))
                            {
                                rowButton.setBackgroundResource(R.drawable.barrow2);//버튼배경
                            }
                            if(j==0)
                            {
                                rowButton.setText(suggestion_check.getTitle());
                                //rowButton.setText(suggestion_check.getTitle());
                                rowButton.setWidth(200);
                                rowButton.setHeight(50);
                            }
                            else if(j==1)
                            {
                                rowButton.setText(suggestion_check.getuserID());
                                rowButton.setWidth(300);
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
                                    title=suggestion_check.getTitle();//제목 제목 DB값
                                    Content= suggestion_check.getContent();
                                    // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                                    // 커스텀 다이얼로그를 호출한다.
                                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                                    // customDialog.callFunction();
                                    callFunction(driverSuggestionsActivity.this,cun);
                                }
                            });
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

        driverSuggestionsRequest driversuggestionsrequest= new driverSuggestionsRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(driverSuggestionsActivity.this);
        queue.add(driversuggestionsrequest);

        driverbusMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_admin.equals("버스기사")){excep();}//화면이동
                else if(check_admin.equals("어드민"))
                {
                    Intent i = new Intent(driverSuggestionsActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
            }
        });


    }

    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        if(check_admin.equals("버스기사")){excep();}//화면이동
        else if(check_admin.equals("어드민"))
        {
            Intent i = new Intent(driverSuggestionsActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        //super.onBackPressed();
    }
    void excep(){
        try {
            Move();
            //화면전환
        }catch (Exception e){

            Excep(e);
        }
    }

    void post_dialog(String Title, String Content)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title).setMessage(Content);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Move();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void dialog(String who,String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(who+" : "+talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Move();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void Move(){
        Intent i = new Intent(driverSuggestionsActivity.this/*현재 액티비티 위치*/ , DriverActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(value.equals(ad))
        {
            dialog("기능고장",exceptionAsStrting);
        }
        else{
            dialog("오류", exceptionAsStrting);
        }
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Context context,int i) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog_read);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        suggestion_check = suggestions.get(i);
        final String title_call=suggestion_check.getTitle();
        final String content_call=suggestion_check.getContent();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView message = (TextView) dlg.findViewById(R.id.mesgase);
        final TextView title = (TextView) dlg.findViewById(R.id.TTtitle);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);

        title.setMovementMethod(ScrollingMovementMethod.getInstance());

        message.setMovementMethod(new ScrollingMovementMethod());
        if(!check_admin.equals("어드민"))
        {
            title.setBackgroundColor(Color.parseColor("#70AD47"));
            okButton.setBackgroundColor(Color.parseColor("#70AD47"));

        }

        title.setText(title_call);
        message.setText(content_call);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}

