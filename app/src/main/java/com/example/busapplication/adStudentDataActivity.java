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
import android.widget.Button;
import android.widget.EditText;
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

public class adStudentDataActivity extends AppCompatActivity {

    String title="제목";//제목 DB에 넣기
    private ArrayList<adStudentDataDTO> suggestions = new ArrayList<>();
    adStudentDataDTO suggestion_check = new adStudentDataDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_student_data);

        Button ADbusMain9=(Button)findViewById(R.id.ADbusMain9);//메인화면으로
        //checkButton
        ADbusMain9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(adStudentDataActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
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
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.studentCheTable); // 테이블 id 명

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length();i++){
                        JSONObject student_suggestions = array.getJSONObject(i);

                        suggestions.add(new adStudentDataDTO(
                                student_suggestions.getString("userID"),
                                student_suggestions.getString("userName"),
                                student_suggestions.getString("sanctions")
                        ));
                    }
                    for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(adStudentDataActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 3 ; j++){//컬럼임
                            final int cun=i;
                            Button rowButton = new Button(adStudentDataActivity.this);
                            rowButton.setBackgroundResource(R.drawable.barrow);//버튼배경
                            suggestion_check = suggestions.get(cun);

                            if(j==0)
                            {
                                rowButton.setText(suggestion_check.getUserID());
                                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                rowButton.setWidth(400);
                                rowButton.setHeight(50);
                            }

                            else if(j==1)
                            {
                                rowButton.setText(suggestion_check.getUserName());
                                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                rowButton.setWidth(10);
                                rowButton.setHeight(50);

                            }
                            else if(j==2)
                            {
                                rowButton.setText(suggestion_check.getSanctions());
                                rowButton.setTextColor(Color.RED);     // 폰트컬러
                                rowButton.setWidth(10);
                                rowButton.setHeight(50);

                            }
                            rowButton.setTextSize(12);                     // 폰트사이즈
                            rowButton.setTypeface(null, Typeface.BOLD);
                            rowButton.setEnabled(true);
                            rowButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //버튼 클릭될 시 할 코드작성
                                    title=suggestion_check.getUserID();//제목 제목 DB값
                                    // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                                    // 커스텀 다이얼로그를 호출한다.
                                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                                    // customDialog.callFunction();
                                    callFunction(adStudentDataActivity.this,cun);
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

        adStudentDataRequest_loadUser loaduser = new adStudentDataRequest_loadUser(responseListener);
        RequestQueue queue = Volley.newRequestQueue(adStudentDataActivity.this);
        queue.add(loaduser);
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Context context,int i) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog Mapdlg1 = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        Mapdlg1.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        Mapdlg1.setContentView(R.layout.map_custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        Mapdlg1.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        suggestion_check = suggestions.get(i);

        final String userID_call= suggestion_check.getUserID();
        String user_title = "userID: "+userID_call;
        final TextView TTtitle1 = (TextView) Mapdlg1.findViewById(R.id.TTtitle1);//지도 변경 입력칸
        TTtitle1.setText(user_title);
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText MAPmesgase = (EditText) Mapdlg1.findViewById(R.id.MAPmesgase);//지도 변경 입력칸
        final Button MAPokButton = (Button) Mapdlg1.findViewById(R.id.MAPokButton);//변경 버튼
        final Button MAPcancelButton = (Button) Mapdlg1.findViewById(R.id.MAPcancelButton);//취소버튼

        MAPmesgase.setInputType(InputType.TYPE_CLASS_NUMBER);//숫자 키보드로 변경
        MAPokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                Toast.makeText(adStudentDataActivity.this, "\"" +  MAPmesgase.getText().toString() + "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) {
                                //DB에서 받은 값 각 변수에 저장
                                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "수정되지않았습니다.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                };
                adStudentDataRequest_ModifyUser modifyuser = new adStudentDataRequest_ModifyUser(userID_call, MAPmesgase.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(adStudentDataActivity.this) ;
                queue.add(modifyuser);
                Refresh();//세로고침
                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
        MAPcancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adStudentDataActivity.this, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                Mapdlg1.dismiss();
            }
        });
    }
    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
