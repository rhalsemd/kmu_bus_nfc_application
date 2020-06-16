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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
public class studentSuggestionActivity extends AppCompatActivity {

    String talk;
    String ad="admin";
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


        final EditText titleText = (EditText)findViewById(R.id.titleText);//제출 내용
        final EditText suggestionsWrite = (EditText)findViewById(R.id.suggestionsWrite);//제출 내용

        Button submitButton=(Button)findViewById(R.id.submitButton);//제출하는 버튼
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v)
          {
            try{
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String time = simpleDate.format(mDate);
                String userID = value;
                String title = titleText.getText().toString();
                String content = suggestionsWrite.getText().toString();

                if(title.length() != 0 && content.length() != 0) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "글등록완료!", Toast.LENGTH_SHORT).show();
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
                    studentSuggestionRequest studentsuggestionrequest = new studentSuggestionRequest(userID, title, content, time, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(studentSuggestionActivity.this);
                    queue.add(studentsuggestionrequest);
                }
                //else if(PhoneCheck == false) {Toast.makeText(getApplicationContext(), "휴대폰인증을 해주세요!", Toast.LENGTH_SHORT).show(); }
                else if(title.length() == 0 && content.length() != 0){Toast.makeText(getApplicationContext(), "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();}
                else if(content.length() == 0 && title.length() != 0){Toast.makeText(getApplicationContext(), "내용을 작성해주세요!", Toast.LENGTH_SHORT).show(); }
                else {Toast.makeText(getApplicationContext(), "제대로 작성해주세요!", Toast.LENGTH_SHORT).show();}
            }catch (Exception e)
            {
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
        Move();
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
