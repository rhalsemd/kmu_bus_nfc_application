package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class adNoticeAddActivity extends AppCompatActivity {

    //모드 spinner
    Spinner UserSpinner;
    //Adapter
    spinnerRows adapterSpinner;
    String user;//spinner 선텍을 알려준다
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_notice_add);

        final EditText NoticeTitleEd = (EditText)findViewById(R.id.NoticeTitleEd);//제출 내용
        final EditText NoticeWriteEd = (EditText)findViewById(R.id.NoticeWriteEd);//제출 내용

        Button submitNoticeButton=(Button)findViewById(R.id.submitNoticeButton);//제출하는 버튼
        Button ADbusMain11=(Button)findViewById(R.id.ADbusMain11);//제출하는 버튼
        ADbusMain11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
            }
        });
        submitNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = NoticeTitleEd.getText().toString();
                String content = NoticeWriteEd.getText().toString();

                if(user.equals("버스기사")||user.equals("학생")||user.equals("전체"))
                {
                    if (title == "" || content == "")
                    {
                        Toast.makeText(getApplicationContext(), "제목이나 내용을 적어주세요", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                            try{
                                long now = System.currentTimeMillis();
                                Date mDate = new Date(now);
                                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                String time = simpleDate.format(mDate);
                                if(title.length() != 0 && content.length() != 0) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "공지사항이 저장되었습니다", Toast.LENGTH_SHORT).show();
                                                    Move();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "내용을 한번 더 확인해주세요!", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };
                                    //서버로 volley를 이용해서 요청
                                    adNoticeAddRequest noticeaddrequest = new adNoticeAddRequest(title, content, time, user, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(adNoticeAddActivity.this);
                                    queue.add(noticeaddrequest);
                                }
                                else{Toast.makeText(getApplicationContext(), "제목 혹은 내용을 제대로 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();}

                            }catch (Exception e)
                            {
                            }

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "버스기사나 학생 또는 전체를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //모드 spinner
        List<String> data1 = new ArrayList<>();
        data1.add("유저 선택");
        data1.add("버스기사");
        data1.add("학생");
        data1.add("전체");
        //UI생성
        //시간 스피너
        UserSpinner = (Spinner)findViewById(R.id.UserSpinner);
        //Adapter
        adapterSpinner = new spinnerRows(this, data1);
        //Adapter 적용
        UserSpinner.setAdapter(adapterSpinner);

        UserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = parent.getItemAtPosition(position).toString();// 무엇을 선택했는지 보여준다
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void Move()
    {
        Intent i = new Intent(adNoticeAddActivity.this/*현재 액티비티 위치*/ , adTotalActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Move();
    }
}
