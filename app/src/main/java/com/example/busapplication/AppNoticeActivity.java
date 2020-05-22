package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppNoticeActivity extends AppCompatActivity {

    //DB에 넣을 때 아래의 변수 지우기
    String title="제목";//제목 DB에 넣기
    String Content="내용";//내용 db에 넣기
    String strValue;//다른 액티비티에서 넘어온 값
    String strValue2;//다른 액티비티에서 넘어온 값

    private ArrayList<driverSuggestionsDTO> suggestions = new ArrayList<>();
    driverSuggestionsDTO suggestion_check = new driverSuggestionsDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notice);

        Intent intent = getIntent(); /*데이터 수신*/
        String value = intent.getExtras().getString("checkuser"); //메인에서 넘어온 아이디값
        String value2 = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        strValue=value;
        strValue2=value2;

        Button noticeMain=(Button)findViewById(R.id.noticeMain);//메인화면으로
        //공지사항
        final TextView Datacheck101 = (TextView)findViewById(R.id.Datacheck101);
        Datacheck101.setText(strValue);
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.AppNoticeTable); // 테이블 id 명

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
                                student_suggestions.getString("user")
                        ));
                    }
                    for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(AppNoticeActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 2 ; j++){//컬럼임
                            final int cun= i;
                            Button rowButton = new Button(AppNoticeActivity.this);
                            rowButton.setBackgroundResource(R.drawable.barrow);//버튼배경
                            suggestion_check = suggestions.get(cun);

                            if(j==0)
                            {
                                rowButton.setText(String.valueOf(cun));
                                rowButton.setWidth(10);
                                rowButton.setHeight(50);
                            }

                            else if(j==1)
                            {
                                rowButton.setText(suggestion_check.getTitle());
                                //rowButton.setText(suggestion_check.getTitle());
                                rowButton.setWidth(400);
                                rowButton.setHeight(50);
                                rowButton.setTextColor(Color.BLACK);

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
                                    callFunction(AppNoticeActivity.this,cun);
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

        AppNoticeRequest noticerequest = new AppNoticeRequest(strValue, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AppNoticeActivity.this);
        queue.add(noticerequest);

        noticeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strValue.equals("버스기사"))
                {
                    Intent i = new Intent(AppNoticeActivity.this/*현재 액티비티 위치*/ , DriverActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
                else if(strValue.equals("학생"))
                {
                    Intent i = new Intent(AppNoticeActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(strValue.equals("버스기사"))
        {
            Intent i = new Intent(AppNoticeActivity.this/*현재 액티비티 위치*/ , DriverActivity.class/*이동 액티비티 위치*/);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        else if(strValue.equals("학생"))
        {
            Intent i = new Intent(AppNoticeActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        // 코드 작성
    }
    public void callFunction(Context context, int i) {

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
