package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class studentBookMangerActivity extends AppCompatActivity {

    private Button buschoicebutton;

    String value;
    String ad="admin";
    String talk;
    Boolean reserve_able = false;

    //DB받을 변수
    String day;
    String bookDay;
    String bookBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_manger);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값




        //DB받을 textview
        final TextView daychetext = (TextView)findViewById(R.id.daychetext);//로그인
        final TextView bookDaychetext = (TextView)findViewById(R.id.bookDaychetext);//로그인
        final TextView bookBuschetext = (TextView)findViewById(R.id.bookBuschetext);//로그인

        try {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if(success) {
                                String booked_time = jsonObject.getString("booked_time");
                                String bus_type = jsonObject.getString("bus_type");
                                String bus_name = jsonObject.getString("bus_name");

                                daychetext.setText(booked_time);
                                bookDaychetext.setText(bus_type);
                                bookBuschetext.setText(bus_name);
                            }
                            else{
                                talk = "예약내역이 없습니다.";
                                dialog_back(talk);}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //서버로 volley를 이용해서 요청
                studentBookMangerRequest_getInfo getinfo = new studentBookMangerRequest_getInfo(value, responseListener);
                RequestQueue queue = Volley.newRequestQueue(studentBookMangerActivity.this);
                queue.add(getinfo);

        } catch (Exception e) {
            Excep(e);
        }
        //DB를 읽으면 화면이 켜질때 자동으로 출력

        buschoicebutton=(Button)findViewById(R.id.buschoicebutton);//버스보이는 것 버튼
        buschoicebutton.setEnabled(false);
        final boolean cheBookTime=true;//취소예약시간이었다고 가정하여 작성
        Button BusCancellationButton2=(Button)findViewById(R.id.BusCancellationButton2);//버스취소 버튼
        Button studentbusMain3=(Button)findViewById(R.id.studentbusMain3);//메인화면으로가기
        studentbusMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
                //화면전환
            }
        });

        BusCancellationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                boolean is_before = jsonObject.getBoolean("is_before");
                                String start_time = jsonObject.getString("start_time");
                                String end_time = jsonObject.getString("end_time");
                                if (success) {
                                    try {
                                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        SimpleDateFormat for_date = new SimpleDateFormat("yyyy-MM-dd");

                                        Calendar time = Calendar.getInstance();
                                        Calendar before_time = Calendar.getInstance();

                                        if(is_before){  //등교일때 하루를 빼주어 이전날짜의 시작시간부터 데이터 타입을 설정한다
                                            before_time.add(Calendar.DATE, -1);
                                        }
                                        String date_type_before = for_date.format(before_time.getTime());
                                        String date_type = for_date.format(time.getTime());

                                        String time_start = date_type_before +" "+ start_time;
                                        String time_end = date_type +" "+ end_time;

                                        Date date_start = transFormat.parse(time_start);
                                        Date date_end = transFormat.parse(time_end);

                                        Calendar cal_start = Calendar.getInstance();
                                        Calendar cal_end = Calendar.getInstance();
                                        Calendar cal_now = Calendar.getInstance();

                                        cal_start.setTime(date_start);
                                        cal_end.setTime(date_end);
                                        cal_end.add(Calendar.HOUR, -2);

                                        String cal_now_text = transFormat.format(cal_now.getTime());
                                        Date cal_now_date = transFormat.parse(cal_now_text);
                                        cal_now.setTime(cal_now_date);

                                        if (cal_now.after(cal_start) && cal_now.before(cal_end)) { //예약시간내에 있을시
                                            cancel_reserve(); //취소 가능시간일때 실행
                                        }else{dialog("예약취소 가능시간이 아닙니다!\n취소하고 싶으시면 버스기사에게\n직접 말해주세요!");}
                                    }
                                    catch(Exception e){}

                                } else if (start_time.length() == 0) {
                                    {Toast.makeText(getApplicationContext(), "오류! 관리자에게 연락부탁드립니다.", Toast.LENGTH_SHORT).show();}
                                }
                                else{}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //서버로 volley를 이용해서 요청
                    studentBookRequest_reserveTime reserveTime = new studentBookRequest_reserveTime(bookDaychetext.getText().toString(), bookBuschetext.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(studentBookMangerActivity.this);
                    queue.add(reserveTime);


                } catch (Exception e) {
                    Excep(e);
                }
                    //if (cheBookTime == false) {
                    //talk = "예약취소시간을 확인해주세요.";}

            }
        });

    }

    void cancel_reserve(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        talk = "예약취소 되었습니다.";
                        dialog(talk);

                    } else {
                        talk = "취소되지 않았습니다!";
                        dialog(talk);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //서버로 volley를 이용해서 요청
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String canceled_time = simpleDate.format(mDate);

        studentBookMangerRequest_cancelBook cancelbook = new studentBookMangerRequest_cancelBook(value, canceled_time, responseListener);
        RequestQueue queue = Volley.newRequestQueue(studentBookMangerActivity.this);
        queue.add(cancelbook);
    }
    void Move(){
        Intent i = new Intent(studentBookMangerActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
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

    void dialog_back(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
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
    void Excep(Exception e)
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
