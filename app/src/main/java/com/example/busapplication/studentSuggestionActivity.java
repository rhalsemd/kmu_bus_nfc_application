package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class studentSuggestionActivity extends AppCompatActivity {

    String titleTextDB;
    String suggestionsWriteDB;

    String talk;
    String ad="3";
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_suggestion);

        Button studentbusMain4=(Button)findViewById(R.id.studentbusMain4);//메인화면으로
        studentbusMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
            }
        });

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck14 = (TextView)findViewById(R.id.Datacheck14);//pw로그인
        Datacheck14.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것

        EditText titleText = (EditText)findViewById(R.id.titleText);//제출 내용
        EditText suggestionsWrite = (EditText)findViewById(R.id.suggestionsWrite);//제출 내용

        titleTextDB=titleText.getText().toString();
        suggestionsWriteDB=suggestionsWrite.getText().toString();

        Button submitButton=(Button)findViewById(R.id.submitButton);//제출하는 버튼
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //제목이나 내용이 비어있는 것을 확인한다.
                    if (titleTextDB == "" || suggestionsWriteDB == "") {
                        talk = "제목이나 내용을 적어주세요";
                        dialog(talk);
                    } else {
                        Datacheck14.setText("as");//지워도 됨 - 값넘어온지 확인 하는 것
                        //메인화면으로 감
                        //db확인시 불편할까봐 막아둠
                        //  Move();
                    }
                }catch (Exception e){
                    Excep(e);
                }
            }
        });
    }
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    void dialog2(String who,String talk)
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
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    void Move()
    {
        Intent i = new Intent(studentSuggestionActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(value.equals(ad))
        {
            dialog2("기능고장",exceptionAsStrting);
        }
        else{
            dialog2("오류", exceptionAsStrting);
        }
    }
}
