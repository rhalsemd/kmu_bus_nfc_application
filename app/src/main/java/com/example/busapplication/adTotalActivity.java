package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class adTotalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_total);


        Button NoticeDelbutton=(Button)findViewById(R.id.NoticeDelbutton);//공지사항
        Button SuggestionDelbutton=(Button)findViewById(R.id.SuggestionDelbutton);//건의사항
        Button ADbusMain10=(Button)findViewById(R.id.ADbusMain10);//메인화면으로
        Button NoticeAddbutton=(Button)findViewById(R.id.NoticeAddbutton);//공지사항 작성

        NoticeAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adTotalActivity.this/*현재 액티비티 위치*/ , adNoticeAddActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        ADbusMain10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adTotalActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        NoticeDelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adTotalActivity.this/*현재 액티비티 위치*/ , adNoticeDelActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        SuggestionDelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adTotalActivity.this/*현재 액티비티 위치*/ , driverSuggestionsActivity.class/*이동 액티비티 위치*/);
                i.putExtra("value", "어드민");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adTotalActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        // 코드 작성
    }
}
