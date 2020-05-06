package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Random;

public class drivermangerDataActivity extends AppCompatActivity  implements View.OnClickListener, Dialog.OnCancelListener {

    String value;
    String ad="3";

    //DB에 저장할 휴대폰 변경 변수
    String changPho;
    //다이얼오그 커스텀
    /*Dialog에 관련된 필드*/
    LayoutInflater dialog; //LayoutInflater
    View dialogLayout; //layout을 담을 View
    Dialog authDialog; //dialog 객체
    EditText phone;
    /*카운트 다운 타이머에 관련된 필드*/

    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //인증 번호를 입력 하는 칸
    Button emailAuth_btn; // 인증버튼
    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 300 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    TextView phoneChangText;

    String b;//랜덤 문자 넣기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_manger_data);

        Intent intent = getIntent(); /*데이터 수신*/
        value = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        final TextView Datacheck12 = (TextView)findViewById(R.id.Datacheck12);//pw로그인
        Datacheck12.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것


        Button driverbusMain2=(Button)findViewById(R.id.driverbusMain1);//메인화면으로가기


        final TextView PWChangeche = (TextView)findViewById(R.id.PWChangeche);
        final TextView PWChange = (TextView)findViewById(R.id.PWChange);

        phoneChangText = (TextView)findViewById(R.id.phoneChangText);

        Button PWChangeButton=(Button)findViewById(R.id.PWChangeButton);//ID 변경버튼
        Button phoneChangbutton=(Button)findViewById(R.id.phoneChangbutton);//휴대폰 변경버튼

        driverbusMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
                //화면전환
            }
        });

        //onClick(View v)로 정의
        phoneChangbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userID = value;
                    String userPhone = phoneChangText.getText().toString();
                    if(userPhone.length() == 11) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "휴대폰번호를 한번 더 확인해주세요!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        drivermangerDataRequest_ChangePhone changePhone = new drivermangerDataRequest_ChangePhone(userID, userPhone, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(drivermangerDataActivity.this);
                        queue.add(changePhone);
                    }
                    else{Toast.makeText(getApplicationContext(), "휴대폰번호를 한번 더 확인해주세요!", Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Excep(e);
                }
            }
        });


        PWChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String PWChangechesId = PWChangeche.getText().toString();//변경할 PW값 string에 저장
                    String PWChangesId = PWChange.getText().toString();//변경할 PW값 string에 저장
                    String userID = value;
                    String userPassword = PWChangesId;
                    if(PWChangechesId.equals(PWChangesId) && (PWChangechesId.length()!=0)) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "제대로 로그인 되어있는지 확인해주세요!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        drivermangerDataRequest_ChangePW changePW = new drivermangerDataRequest_ChangePW(userID, userPassword, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(drivermangerDataActivity.this);
                        queue.add(changePW);
                    }
                    else{Toast.makeText(getApplicationContext(), "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();}
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

        //문자 서비스
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
        }
        else {
// Permission has already been granted
        }
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
    void dialog(String who,String talk)
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
    void Move(){
        Intent i = new Intent(drivermangerDataActivity.this/*현재 액티비티 위치*/ , DriverActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        if(value.equals(ad))
        {
            dialog("기능고장",exceptionAsStrting);
        }
        else{
            dialog("오류", exceptionAsStrting);
        }
    }

    //여기서 문자 인증 다이얼로그 ~290
    public void countDownTimer() { //카운트 다운 메소드
        time_counter = (TextView) dialogLayout.findViewById(R.id.emailAuth_time_counter);
        //줄어드는 시간을 나타내는 TextView
        emailAuth_number = (EditText) dialogLayout.findViewById(R.id.emailAuth_number);
        //사용자 인증 번호 입력창
        emailAuth_btn = (Button) dialogLayout.findViewById(R.id.emailAuth_btn);
        //인증하기 버튼
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
            }
            //시간 종료시 다이얼로그 끄기
            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
                authDialog.cancel();
            }
        }.start();
        emailAuth_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.phoneChangbutton:
                if(phoneChangText.getText().length()==11)
                {
                    //다이얼로그 출력
                    String a=phoneChangText.getText().toString();
                    b=getRandomPassword(6);
                    sendSMS(a,b);
                    dialog = LayoutInflater.from(this);
                    dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
                    authDialog = new Dialog(this); //Dialog 객체 생성
                    authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
                    authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
                    authDialog.setOnCancelListener(this); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
                    authDialog.show(); //Dialog를 나타내어 준다.
                    countDownTimer();
                }
                else {
                    Edialog("번호를 적어두세요");
                }
                break;

            case R.id.emailAuth_btn : //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                try {
                    if( emailAuth_number.getText().length()>0) {
                        int user_answer = Integer.parseInt(emailAuth_number.getText().toString());
                        int App_answer = Integer.parseInt(b.toString());
                        if (user_answer == App_answer) {
                            Chedialog("문자 인증 성공");
                            //자료넣기
                            changPho=phoneChangText.getText().toString();
                        } else {
                            Chedialog("문자 인증 실패");
                        }
                    }
                    else {
                        Chedialog("숫자를 적어주세요");
                    }
                }catch (Exception e)
                {
                    Chedialog( "인증번호를 적어주세요");
                }
                break;
        }
    }
    void Edialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        countDownTimer.cancel();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출

    void Chedialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                authDialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //여기서 문자 서비스
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    //램덤함수
    public String getRandomPassword( int length ){

        char[] charaters = {
                '0','1','2','3','4','5','6','7','8','9'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for( int i = 0 ; i < length ; i++ ){
            sb.append( charaters[ rn.nextInt( charaters.length ) ] );
        }
        return sb.toString();
    }
}
