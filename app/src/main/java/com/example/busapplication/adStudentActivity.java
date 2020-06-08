package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.PrintWriter;
import java.io.StringWriter;

public class adStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_student);
        Button studentNumButton1=(Button)findViewById(R.id.studentNumButton1);//학생 통계
        Button stuResButton=(Button)findViewById(R.id.stuResButton);//학생 통계
        Button Reverationbutton=(Button)findViewById(R.id.Reverationbutton);//예약자
        final Button ADbusMain18 = (Button)findViewById(R.id.ADbusMain18);//뒤로가기
        ADbusMain18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adStudentActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });


        studentNumButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(adStudentActivity.this/*현재 액티비티 위치*/ , adStudentStatsActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

            }
        });

        stuResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(adStudentActivity.this/*현재 액티비티 위치*/ , adStudentDataActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        //Reverationbutton
        Reverationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Intent i = new Intent(adStudentActivity.this/*현재 액티비티 위치*/ , adReservationActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(adStudentActivity.this/*현재 액티비티 위치*/ , administratorActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        // 코드 작성
    }
}
