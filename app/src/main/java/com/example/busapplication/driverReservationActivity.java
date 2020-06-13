package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class driverReservationActivity extends AppCompatActivity {

    String title;
    String Content;
    String value;
    String adBusStr2;//관리자만 쓰는 변수, 버스를 받음
    String adTimeStr2;//관리자만 쓰는 변수, 시간을 받음
    private ArrayList<driverReservationActivityDTO> suggestions = new ArrayList<>();
    driverReservationActivityDTO suggestion_check = new driverReservationActivityDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reservation);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        adBusStr2 = intent.getExtras().getString("adBusStr2"); //메인에서 넘어온 아이디값
        adTimeStr2 = intent.getExtras().getString("adTimeStr2"); //메인에서 넘어온 아이디값


        final Button ADbusMain15 = (Button)findViewById(R.id.ADbusMain15);//뒤로가기
        ADbusMain15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(driverReservationActivity.this/*현재 액티비티 위치*/ , driverBookActivity.class/*이동 액티비티 위치*/);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.DirReserTable);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);

                    for(int i=0; i<array.length();i++){
                        JSONObject student_suggestions = array.getJSONObject(i);

                        suggestions.add(new driverReservationActivityDTO(
                                student_suggestions.getString("userID")
                        ));
                    }
                    for (int i = 0; i < suggestions.size(); i++) {//  row 임 대신에 컬럼갯
                        TableRow tableRow = new TableRow(driverReservationActivity.this);//컬럼
                        tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


                        for(int j = 0 ; j < 2 ; j++){//컬럼임
                            final int cun=i;
                            Button rowButton = new Button(driverReservationActivity.this);
                            rowButton.setBackgroundResource(R.drawable.barrow3);//버튼배경
                            suggestion_check = suggestions.get(cun);

                            if(j==0)
                            {
                                rowButton.setText(Integer.toString(i+1));
                                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                rowButton.setWidth(100);
                                rowButton.setHeight(50);
                            }

                            else if(j==1)
                            {
                                rowButton.setText(suggestion_check.getUserID());
                                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                                rowButton.setWidth(100);
                                rowButton.setHeight(50);
                                rowButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //버튼 클릭될 시 할 코드작성
                                        title=suggestion_check.getUserID();//제목 제목 DB값
                                        // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                                        // 커스텀 다이얼로그를 호출한다.
                                        // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                                        // customDialog.callFunction();
                                        callFunction(driverReservationActivity.this,cun, suggestion_check.getUserID());
                                    }
                                });
                            }

                            rowButton.setTextColor(Color.BLACK);
                            rowButton.setTextSize(12);                     // 폰트사이즈
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

        driverReservationRequest_load_bookedUser load_bookeduser = new driverReservationRequest_load_bookedUser(adBusStr2, adTimeStr2, responseListener);
        RequestQueue queue = Volley.newRequestQueue(driverReservationActivity.this);
        queue.add(load_bookeduser);

       /* for (int i = 0; i < 100; i++) {//  row 임 대신에 컬럼갯
            TableRow tableRow = new TableRow(driverReservationActivity.this);//컬럼
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));


            for(int j = 0 ; j < 2 ; j++){//컬럼임
                final int cun=i;
                Button rowButton = new Button(driverReservationActivity.this);
                rowButton.setBackgroundResource(R.drawable.barrow2);//버튼배경
                if(j==0)
                {
                    rowButton.setText(Integer.toString(i));
                    //rowButton.setText(suggestion_check.getTitle());
                    rowButton.setWidth(150);
                    rowButton.setHeight(50);
                }
                else if(j==1)
                {
                    rowButton.setText(Integer.toString(cun));
                    rowButton.setWidth(350);
                    rowButton.setHeight(50);

                }
                rowButton.setTextSize(12);                     // 폰트사이즈
                rowButton.setTextColor(Color.BLACK);     // 폰트컬러
                rowButton.setTypeface(null, Typeface.BOLD);
                rowButton.setEnabled(true);
                rowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //버튼 클릭될 시 할 코드작성
                        title=Integer.toString(cun);//제목 제목 DB값
                        Content=Integer.toString(cun);
                        // CustomDialogRead customDialog = new CustomDialogRead(title,Content,driverSuggestionsActivity.this);
                        // 커스텀 다이얼로그를 호출한다.
                        // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                        // customDialog.callFunction();
                        callFunction(driverReservationActivity.this, cun );
                    }
                });
                tableRow.addView(rowButton);
            }
            tableLayout.addView(tableRow);
        }*/
    }

    public void callFunction(Context context, int num, String userID) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.reservationdialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView IDreserText = (TextView) dlg.findViewById(R.id.IDreserText);
        final Button cancelReverButton = (Button) dlg.findViewById(R.id.cancelReverButton);
        final Button rideButton = (Button) dlg.findViewById(R.id.rideButton);//탑슴 버튼

        IDreserText.setMovementMethod(ScrollingMovementMethod.getInstance());
        IDreserText.setText(userID);


        cancelReverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });

        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            boolean user_already = jsonObject.getBoolean("user_already");
                            if(success) {
                                //DB에서 받은 값 각 변수에 저장
                                Toast.makeText(getApplicationContext(), "탑승되었습니다.", Toast.LENGTH_SHORT).show();

                            }
                            else if(user_already)
                            {
                                Toast.makeText(getApplicationContext(), "이미 탑승된 인원입니다!", Toast.LENGTH_SHORT).show();
                            }
                            else{}

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                };
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String seated_time  = simpleDate.format(mDate);

                driverReservationRequest_seat_bookedUser seatuser = new driverReservationRequest_seat_bookedUser(IDreserText.getText().toString(), adBusStr2, adTimeStr2, seated_time, responseListener);
                RequestQueue queue = Volley.newRequestQueue(driverReservationActivity.this) ;
                queue.add(seatuser);
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                // 커스텀 다이얼로그를 종료한다.
                Refresh();
                dlg.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(driverReservationActivity.this/*현재 액티비티 위치*/ , driverBookActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Refresh()//세로고침
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
