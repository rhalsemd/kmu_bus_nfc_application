package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import java.util.ArrayList;

public class adNoticeDelActivity extends AppCompatActivity {

    //DB에 넣을 때 아래의 변수 지우기
    String title="제목";//제목 DB에 넣기
    String Content="내용";//내용 db에 넣기

    private ArrayList<driverSuggestionsDTO> suggestions = new ArrayList<>();
    driverSuggestionsDTO suggestion_check = new driverSuggestionsDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_notice_del);

        Button ADbusMain7=(Button)findViewById(R.id.ADbusMain7);//메인화면으로
        //checkButton
        ADbusMain7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(adNoticeDelActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();

                }catch (Exception e)
                {

                }
            }
        });
        //표
        //컬럼으로 (내용,날짜, 제목, 내용)으로하고 Content에 내용부분 넣기
        //db자료를 2차원 배열같은데 넣고 setText에 db가 들어간 배열 출력
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.noticeTable); // 테이블 id 명

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
                        TableRow tableRow = new TableRow(adNoticeDelActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 3 ; j++){//컬럼임
                            final int cun=i;
                            Button rowButton = new Button(adNoticeDelActivity.this);
                            rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                            suggestion_check = suggestions.get(cun);

                            if(j==0)
                            {
                                rowButton.setText(String.valueOf(cun));
                                //rowButton.setText(suggestion_check.getTitle());
                                rowButton.setWidth(30);
                                rowButton.setHeight(50);
                            }

                            else if(j==1)
                            {
                                rowButton.setText(suggestion_check.getTitle());
                                rowButton.setWidth(50);
                                rowButton.setHeight(50);
                                rowButton.setTextColor(Color.RED);
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
                                        callFunction(adNoticeDelActivity.this,cun);
                                    }
                                });

                            }
                            else if(j==2)
                            {
                                rowButton.setText(suggestion_check.getuserID());
                                rowButton.setWidth(50);
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

        adNoticeDelRequest_loadNotice loadnotice = new adNoticeDelRequest_loadNotice(responseListener);
        RequestQueue queue = Volley.newRequestQueue(adNoticeDelActivity.this);
        queue.add(loadnotice);

    }
    public void callFunction(Context context,int i) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.del_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView delmesgase1 = (TextView) dlg.findViewById(R.id.delmesgase1);
        final TextView title3 = (TextView) dlg.findViewById(R.id.title3);
        final Button delButton1 = (Button) dlg.findViewById(R.id.delButton1);//삭제 버튼
        final Button cancelButton3 = (Button) dlg.findViewById(R.id.cancelButton3);//취소 버튼

        title3.setMovementMethod(ScrollingMovementMethod.getInstance());

        delmesgase1.setMovementMethod(new ScrollingMovementMethod());

        suggestion_check = suggestions.get(i);

        final String title_call = suggestion_check.getTitle();
        final String Content_call = suggestion_check.getContent();
        final String time_call = suggestion_check.getTime();

        title3.setText(title_call);
        delmesgase1.setText(Content_call);

        delButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                //DB에서 데이터 삭제
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) {
                                //DB에서 받은 값 각 변수에 저장
                                Toast.makeText(getApplicationContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "삭제되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                };
                adNoticeDelRequest_DeleteNotice deletenotice = new adNoticeDelRequest_DeleteNotice(title_call, Content_call, time_call, responseListener);
                RequestQueue queue = Volley.newRequestQueue(adNoticeDelActivity.this) ;
                queue.add(deletenotice);
                dlg.dismiss();
            }
        });

        cancelButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(adNoticeDelActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
