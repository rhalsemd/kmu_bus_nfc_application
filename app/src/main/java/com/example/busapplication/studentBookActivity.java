package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
public class studentBookActivity extends AppCompatActivity {
    //bus spinner
    Spinner BookSpinner;
    //Adapter
    spinnerRows_bule adapterSpinner1;

    //time spinner
    Spinner bookTimeSpinner;
    //Adapter
    spinnerRows_bule adapterSpinner2;

    String talk;
    String value;
    String ad = "admin";

    String SpinnerValue;
    TextView Datacheck20;

    String text;
    //시간 선택
    int Timeche = 0;
    //버스 선택
    int Busche = 0;
    String bookTime;
    String bookName;

    List<String> load_busName = new ArrayList<>();
    List<String> load_busType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값

        //UI생성
        BookSpinner = (Spinner) findViewById(R.id.bookBusSpinner);
        bookTimeSpinner = (Spinner) findViewById(R.id.bookTimeSpinner);

        try {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);

                        load_busName.add("노선 선택");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject bus_name = array.getJSONObject(i);
                            load_busName.add(bus_name.getString("bus_name"));
                        }

                        //Adapter
                        adapterSpinner1 = new spinnerRows_bule(studentBookActivity.this, load_busName);
                        //Adapter 적용
                        BookSpinner.setAdapter(adapterSpinner1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_loadName loadname = new studentBookRequest_loadName(responseListener);
            RequestQueue queue = Volley.newRequestQueue(studentBookActivity.this);
            queue.add(loadname);
        } catch (Exception e) {
            Excep(e);
        }


        //버스 노선 표현
        final TextView BusTextView = (TextView) findViewById(R.id.BusTextView);
        final TextView random = (TextView) findViewById(R.id.random);//렌덤문자열
        random.setText(getRandomPassword(2));

        final TextView randomInput = (TextView) findViewById(R.id.randomInput);//렌덤문자 입력
        //서버에 확인 조건추가

        //버스예약 가능 시간
        //임의의 값 true로 지정
        final boolean cheBook = true;//임의의 수 좌석이 있다고 가정하여 작성
        final boolean cheBookTime = true;//예약시간이었다고 가정하여 작성
        Button book = (Button) findViewById(R.id.book);//버스예약
        Button studentbusMain2 = (Button) findViewById(R.id.studentbusMain2);//메인화면으로


        //버스 노선 표현
        BusTextView.setSelected(true);
        BusTextView.setText("노선을 선택해주세요");

        BookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();// 무엇을 선탣했는지 보여준다
                try {
                    Busche = position;

                    try {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray array = new JSONArray(response);

                                    load_busType.clear();
                                    load_busType.add("시간 선택");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject bus_name = array.getJSONObject(i);
                                        load_busType.add(bus_name.getString("bus_type"));
                                    }

                                    //Adapter
                                    adapterSpinner2 = new spinnerRows_bule(studentBookActivity.this, load_busType);
                                    //Adapter 적용
                                    bookTimeSpinner.setAdapter(adapterSpinner2);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //서버로 volley를 이용해서 요청
                        studentBookRequest_loadType loadType = new studentBookRequest_loadType(BookSpinner.getSelectedItem().toString(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(studentBookActivity.this);
                        queue.add(loadType);
                    } catch (Exception e) {
                        Excep(e);
                    }
                } catch (Exception e) {
                    Excep(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //UI생성
        //시간 스피너


        bookTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Timeche = position;

                } catch (Exception e) {
                    Excep(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    bookTime = bookTimeSpinner.getSelectedItem().toString();
                    bookName = BookSpinner.getSelectedItem().toString();


                    if (Timeche != 0 && Busche != 0 && random.getText().toString().equals(randomInput.getText().toString())) {

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Boolean reserve_able = false;
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
                                                reserve_able = true;
                                            }else{reserve_able = false;}

                                            reservation(bookTime, bookName, reserve_able);
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
                        studentBookRequest_reserveTime reserveTime = new studentBookRequest_reserveTime(bookTime, bookName, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(studentBookActivity.this);
                        queue.add(reserveTime);



                    } else if (!(random.getText().toString().equals(randomInput.getText().toString()))) {
                        Toast.makeText(getApplicationContext(), "보안문자를 한번 더 확인해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (Timeche == 0 || Busche == 0) {
                        Toast.makeText(getApplicationContext(), "항목을 제대로 선택해주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } catch (Exception e) {
                    Excep(e);
                }

            }
        });
          studentbusMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //화면전환
                Move();
            }
        });
    }

    void reservation(String bookTime, String bookName, Boolean reserve_able)
    {
        //예약시간이 맞으면 예약값 디비에 들어간다
        if(reserve_able == true) {
            long now = System.currentTimeMillis();
            Date mDate = new Date(now);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String booked_date = simpleDate.format(mDate);


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        boolean maxed_out = jsonObject.getBoolean("maxed_out");
                        boolean bus_not_able = jsonObject.getBoolean("bus_not_able");
                        boolean user_already = jsonObject.getBoolean("user_already");
                        if (success) {
                            talk = "예약이 되었습니다.";
                            dialog_back(talk);
                            //Intent i = new Intent(studentBookActivity.this/*현재 액티비티 위치*/, studentActivity.class/*이동 액티비티 위치*/);
                            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            //startActivity(i);
                        } else if (user_already) {
                            talk = "이미 예약한 유저입니다.";
                            dialog_back(talk);
                        } else if (maxed_out) {
                            talk = "예약석이 만석입니다.";
                            dialog_back(talk);
                        } else if (bus_not_able) {
                            talk = "버스가 현재 운행불가입니다. 관리자와 연락바랍니다.";
                            dialog_back(talk);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            //서버로 volley를 이용해서 요청
            studentBookRequest_book book = new studentBookRequest_book(bookTime, bookName, value, booked_date, responseListener);
            RequestQueue queue = Volley.newRequestQueue(studentBookActivity.this);
            queue.add(book);
        }
        else{
            Toast.makeText(getApplicationContext(), "현재 예약시간이 아닙니다!", Toast.LENGTH_SHORT).show();
        }  //예약시간이 맞지 않으면 띄운다.
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
    void Move()
    {
        Intent i = new Intent(studentBookActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
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
    //랜덤문자
    public String getRandomPassword( int length ){
        char[] charaters = {
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '0','1','2','3','4','5','6','7','8','9'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for( int i = 0 ; i < length ; i++ ){
            sb.append( charaters[ rn.nextInt( charaters.length ) ] );
        }
        return sb.toString();
    }
}
