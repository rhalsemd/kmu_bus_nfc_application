package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
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

public class AppCheckActivity extends AppCompatActivity implements View.OnClickListener, Dialog.OnCancelListener  {

    String talk;

    //DB저장 변수
    String userName;
    String userID;
    String userID_check;
    String userBirth_FINDID;
    String userBirth_FINDPW;
    String temporarilyPW;

    boolean pwche=false;
    boolean IDche=false;

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

     //버튼 클릭시 사용하는 EditText
     EditText checkname;
     EditText RRNtextDri1;
     EditText RRNtextDri2;

     EditText RRNtextDri3 ;
     EditText RRNtextDri4 ;

     EditText Idcheck ;
     EditText phone1;

    String userPhone;
    String b;//랜덤문자 담기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_check);

          checkname = (EditText)findViewById(R.id.checkname);//이름
          RRNtextDri1 = (EditText)findViewById(R.id.RRNtextDri1);//주민번호 앞
          RRNtextDri2 = (EditText)findViewById(R.id.RRNtextDri2);//주민번호 뒤

          RRNtextDri3 = (EditText)findViewById(R.id.RRNtextDri3);//주민번호 앞
          RRNtextDri4 = (EditText)findViewById(R.id.RRNtextDri4);//주민번호 뒤

          Idcheck = (EditText)findViewById(R.id.IDcheck);//ID
          phone1 = (EditText)findViewById(R.id.phone);//휴대번호



        //ID찾기
        final  Button IdFind=(Button)findViewById(R.id.IdFind);//ID찾기
        //PW찾기
        final Button PwFind=(Button)findViewById(R.id.PwFind);//PW찾기
        final Button checkMain=(Button)findViewById(R.id.checkMain);//메인화면으로 이동

        //아래의 onClick(View v)로 정의
        IdFind.setOnClickListener(this);
        PwFind.setOnClickListener(this);


        checkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(AppCheckActivity.this/*현재 액티비티 위치*/, MainActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
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

    //다이얼로그
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
    //매인으로 이동
    void Move()
    {
        Intent i = new Intent(AppCheckActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    void Excep(Exception e)//예외처리를 부르는 코드
    {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsStrting = sw.toString();
        Edialog(exceptionAsStrting);
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AppCheckActivity.this/*현재 액티비티 위치*/, MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        //super.onBackPressed();
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
        //시간이끝나면 다이얼로그 종료
            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
                authDialog.cancel();
            }
        }.start();
        emailAuth_btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        userName = checkname.getText().toString();//이름값 string에 저장
        userID =Idcheck.getText().toString();//ID값 string에 저장
        userBirth_FINDID = RRNtextDri1.getText().toString()+RRNtextDri2.getText().toString();
        userBirth_FINDPW = RRNtextDri3.getText().toString()+RRNtextDri4.getText().toString();

        switch (v.getId()) {

            case R.id.IdFind:
                //EditText 사용
                try {

                    if(userName.length() != 0 && RRNtextDri1.getText().length()==6 && RRNtextDri2.getText().length()==1) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    userID_check = jsonObject.getString("userID");
                                    if (success)
                                    {
                                        String userPhone_check = jsonObject.getString("userPhone");
                                        calldialog(userPhone_check,"ID");

                                    }
                                    else
                                    {
                                        return;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "없는 회원입니다.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        AppCheckRequest_FindID appcheckFindID = new AppCheckRequest_FindID(userName, userBirth_FINDID, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AppCheckActivity.this);
                        queue.add(appcheckFindID);


                    }
                    else if(userName.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "이름 미입력!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(RRNtextDri1.getText().length()==6))
                    {
                        Toast.makeText(getApplicationContext(), "앞자리 6자리 숫자를 정확하게 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if(!(RRNtextDri2.getText().length()==1))
                    {
                        Toast.makeText(getApplicationContext(), "뒷자리 1자리 숫자를 정확하게 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else {}
                }catch (Exception e)
                {
                    Excep(e);
                }
                break;

            case R.id.PwFind:
                //EditText 사용
                try {
                    if (userID.length() != 0 && RRNtextDri3.getText().length() == 6 && RRNtextDri4.getText().length() == 1) {

                        temporarilyPW = getTemporarilyRandomPassword(10);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if (success) {
                                    String userPhone_check = jsonObject.getString("userPhone");
                                    calldialog(userPhone_check,"PW");
                                    } else {
                                        return;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "없는 회원입니다.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청

                        AppCheckRequest_FindPW appcheckFindPW = new AppCheckRequest_FindPW(userID, userBirth_FINDPW, temporarilyPW, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AppCheckActivity.this);
                        queue.add(appcheckFindPW);

                    }
                    else if (userID.length() == 0)
                    {
                        Toast.makeText(getApplicationContext(), "ID 미입력!", Toast.LENGTH_SHORT).show();
                    }
                    else if (!(RRNtextDri3.getText().length() == 6))
                    {
                        Toast.makeText(getApplicationContext(), "앞자리 6자리 숫자를 정확하게 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else if (!(RRNtextDri4.getText().length() == 1))
                    {
                        Toast.makeText(getApplicationContext(), "뒷자리 1자리 숫자를 정확하게 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                    else {}
                }
                catch(Exception e)
                {
                    Excep(e);
                }
                break;

            case R.id.emailAuth_btn : //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                try {
                    if( emailAuth_number.getText().length()>0) {
                        int user_answer = Integer.parseInt(emailAuth_number.getText().toString());
                        int App_answer = Integer.parseInt(b.toString());
                        if (user_answer == App_answer) {
                            Chedialog("문자 인증 성공");
                            if(pwche==true)
                            {
                                sendSMS(userPhone,"비밀번호:"+temporarilyPW);
                                pwche=false;
                            }
                            else if(IDche==true)
                            {
                                Edialog(userID_check); //ID 띄워주기
                                IDche = false;
                            }
                            //자료넣기
                        } else {
                            Chedialog("문자 인증 실패");
                        }
                    }
                    else {
                        Edialog("번호를 적어두세요");
                    }
                }catch (Exception e)
                {
                    Chedialog( "인증번호를 적어주세요");
                }
                break;
        }
    }
    @Override
    public void onCancel(DialogInterface dialog) {
        countDownTimer.cancel();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출

    //인증 다이얼로그 메시지
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

    //임시램덤함수
    public String getTemporarilyRandomPassword( int length ){
        char[] charaters = {
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                '0','1','2','3','4','5','6','7','8','9','!','@','#','$','%','^','&','*','(',')'};
        StringBuffer sb = new StringBuffer();
        Random rn = new Random();
        for( int i = 0 ; i < length ; i++ ){
            sb.append( charaters[ rn.nextInt( charaters.length ) ] );
        }
        return sb.toString();
    }

    private void calldialog(String userPhone_check, String IDorPW)
    {
        userPhone = userPhone_check;
        b = getRandomPassword(6);
        sendSMS(userPhone_check, b);
        dialog = LayoutInflater.from(this);
        dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
        authDialog = new Dialog(this); //Dialog 객체 생성
        authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
        authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
        authDialog.setOnCancelListener(this); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
        authDialog.show(); //Dialog를 나타내어 준다.
        countDownTimer();
        if(IDorPW.equals("ID"))
        {
            IDche = true;
            Toast.makeText(getApplicationContext(), "ID찾기", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), userPhone_check , Toast.LENGTH_SHORT).show();
        }
        else if(IDorPW.equals("PW"))
        {
            pwche = true;
            Toast.makeText(getApplicationContext(), "PW찾기" + userPhone_check , Toast.LENGTH_SHORT).show();
        }
        else{}
    }

}
