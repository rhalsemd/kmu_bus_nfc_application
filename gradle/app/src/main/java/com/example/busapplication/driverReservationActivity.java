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

public class driverReservationActivity extends AppCompatActivity {

    String title;
    String Content;
    String value;
    String advalue1;//관리자만 쓰는 변수, 버스를 받음
    String advalue2;//관리자만 쓰는 변수, 시간을 받음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reservation);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value"); //메인에서 넘어온 아이디값
        advalue1 = intent.getExtras().getString("adBusStr"); //메인에서 넘어온 아이디값
        advalue2 = intent.getExtras().getString("adTimeStr"); //메인에서 넘어온 아이디값

        final TextView textView102 = (TextView) findViewById(R.id.textView102);
        textView102.setText(value+" "+advalue1+" "+advalue2);
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
        for (int i = 0; i < 100; i++) {//  row 임 대신에 컬럼갯
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
        }
    }
    public void callFunction(Context context, int num ) {

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
        IDreserText.setText(Integer.toString(num));


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
