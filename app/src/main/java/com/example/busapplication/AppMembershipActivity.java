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
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AppMembershipActivity extends AppCompatActivity  implements View.OnClickListener, Dialog.OnCancelListener {

    String talk;
    //DB저장 변수
    String NameTextsId;
    String IdTextsId;
    String PWTextsld;
    Boolean PhoneCheck = false; //휴대폰 체크 불린값



    int ch=10;

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

    boolean Useche=false;
    String b;//랜덤문자열 넣기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_membership);

        final EditText NameText = (EditText)findViewById(R.id.name);//이름
        final EditText IdText = (EditText)findViewById(R.id.number);//학번, 버스기사
        final EditText PWText = (EditText)findViewById(R.id.PwText);//pw
        final EditText PwTextche = (EditText)findViewById(R.id.PwTextche);//pw
        phone = (EditText)findViewById(R.id.phone);//휴대폰

        final EditText RRNtext1 = (EditText)findViewById(R.id.RRNtext1);//주민번호 앞자리
        final EditText RRNtext2 = (EditText)findViewById(R.id.RRNtext2);//주민번호 뒤자리

        final TextView UseUsertext = (TextView)findViewById(R.id.UseUsertext);

         NameTextsId = NameText.getText().toString();//이름값 string에 저장
         IdTextsId = IdText.getText().toString();//ID값 string에 저장
         PWTextsld = PWText.getText().toString();//PW값 string에 저장


        Button approvalbutton=(Button)findViewById(R.id.approvalbutton);//회원가입 승인
        Button membershipmain=(Button)findViewById(R.id.membershipmain);//메인화면으로 이동


        Button confirm=(Button)findViewById(R.id.confirm);//인증
        ////아래의 onClick(View v)로 정의
        confirm.setOnClickListener(this);

        UseUsertext.setOnClickListener(new View.OnClickListener() {//이용약관
            @Override
            public void onClick(View v) {
                try {
                    callFunction(AppMembershipActivity.this);
                }catch (Exception e)
                {
                    Excep(e);
                }
            }
        });
        approvalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String userName = NameText.getText().toString();
                    String userID = IdText.getText().toString();
                    String userPassword = PWText.getText().toString();
                    String userPhone = phone.getText().toString();
                    String userBirth = RRNtext1.getText().toString() + RRNtext2.getText().toString();

                    if(userName.length() != 0 && userID.length() != 0 && userPassword.length() != 0  && userPhone.length() != 0
                        && userBirth.length() != 0 && /*PhoneCheck != false && */PWText.getText().toString().equals(PwTextche.getText().toString()) &&Useche==true) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "회원등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AppMembershipActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "중복된 회원이거나,없는 회원입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        //서버로 volley를 이용해서 요청
                        AppMembershipRequest appMembershipRequest = new AppMembershipRequest(userID, userPassword, userName, userBirth, userPhone, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(AppMembershipActivity.this);
                        queue.add(appMembershipRequest);
                    }
                    //else if(PhoneCheck == false) {Toast.makeText(getApplicationContext(), "휴대폰인증을 해주세요!", Toast.LENGTH_SHORT).show(); }
                    else if(!(PWText.getText().toString().equals(PwTextche.getText().toString()))){Toast.makeText(getApplicationContext(), "비밀번호가 같지 않습니다.", Toast.LENGTH_SHORT).show();}
                   else if(Useche==false){
                        Toast.makeText(getApplicationContext(), "회원약관을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else{Toast.makeText(getApplicationContext(), "제대로 입력해주세요!", Toast.LENGTH_SHORT).show(); }

                }catch (Exception e)
                {
                    Excep(e);
                }
                //startActivity(intent);
            }
        });

        membershipmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(AppMembershipActivity.this/*현재 액티비티 위치*/, MainActivity.class/*이동 액티비티 위치*/);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                }catch (Exception e)
                {
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
    void dialog(String talk)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<알림>").setMessage(talk);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AppMembershipActivity.this/*현재 액티비티 위치*/, MainActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        //super.onBackPressed();
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
    void Move()
    {
        Intent i = new Intent(AppMembershipActivity.this/*현재 액티비티 위치*/ , MainActivity.class/*이동 액티비티 위치*/);
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
            //카운트가 끝나면 종료
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
            case R.id.confirm:
                if(phone.getText().length()==11)
                {
                    //다이얼로그 출력
                    String a=phone.getText().toString();

                    b=getRandomPassword(6);
                    a="01000000000";
                    sendSMS(a,b);
                    dialog = LayoutInflater.from(this);
                    dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
                    authDialog = new Dialog(this); //Dialog 객체 생성
                    authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
                    authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
                    authDialog.setOnCancelListener(AppMembershipActivity.this); //다이얼로그를 닫을 때 일어날 일을 정의하기 위해 onCancelListener 설정
                    authDialog.show(); //Dialog를 나타내어 준다.
                    countDownTimer();
                }
                else {
                        Edialog("번호를 적어두세요");
                    }
                break;

            case R.id.emailAuth_btn : //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                try {
                    if(emailAuth_number.getText().length()>0) {
                        int user_answer = Integer.parseInt(emailAuth_number.getText().toString());
                        int App_answer = Integer.parseInt(b.toString());
                        if (user_answer == App_answer) {
                            Chedialog("문자 인증 성공");
                            PhoneCheck = true;
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

    //이용약관
    public void callFunction(Context context) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.del_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView delmesgase1 = (TextView) dlg.findViewById(R.id.delmesgase1);
        final TextView title3 = (TextView) dlg.findViewById(R.id.title3);
        final Button delButton1 = (Button) dlg.findViewById(R.id.delButton1);//삭제 버튼
        final Button cancelButton3 = (Button) dlg.findViewById(R.id.cancelButton3);//취소 버튼

        title3.setMovementMethod(ScrollingMovementMethod.getInstance());
        title3.setBackgroundColor(Color.parseColor("#003399"));


        delmesgase1.setMovementMethod(new ScrollingMovementMethod());


        final String title_call = "이용약관";
        final String Content_call = "이용약관\n" +
                "제1장 총칙\n" +
                "\n" +
                "제1조 (목적) 이 약관은 ________(이하 “회사”라 합니다)가 모바일 기기를 통해 제공하는 게임 서비스 및 이에 부수하는 네트워크, 웹사이트, 기타 서비스(이하 “서비스”라 합니다)의 이용에 대한 회사와 서비스 이용자의 권리ㆍ의무 및 책임사항입니다.\n" +
                "\n" +
                "제2조 (용어의 정의) ① 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                " 1. “회사”라 함은 모바일 기기를 통하여 서비스를 제공하는 사업자를 의미합니다.\n" +
                " 2. “회원”이란 이 약관에 따라 이용계약을 체결하고, 회사가 제공하는 서비스를 이용하는 자를 의미합니다.\n" +
                " 3. “임시회원”이란 일부 정보만 제공하고 회사가 제공하는 서비스의 일부만 이용하는 자를 의미합니다.\n" +
                " 4. “모바일 기기”란 콘텐츠를 다운로드 받거나 설치하여 사용할 수 있는 기기로서, 휴대폰, 스마트폰, 휴대정보단말기(PDA), 태블릿 등을 의미합니다. \n" +
                " 5. “계정정보”란 회원의 회원번호와 외부계정정보, 기기정보, 별명, 프로필 사진, 친구목록 등 회원이 회사에 제공한 정보와 게임이용정보 (캐릭터 정보, 아이템, 레벨 등), 이용요금 결제 정보 등을 통칭합니다.\n" +
                " 6. “콘텐츠”란 모바일 기기로 이용할 수 있도록 회사가 서비스 제공과 관련하여 디지털 방식으로 제작한 유료 또는 무료의 내용물 일체(게임 및 네트워크 서비스, 애플리케이션, 게임 머니, 게임 아이템 등)를 의미합니다. \n" +
                " 7. “오픈마켓”이란 모바일 기기에서 게임 콘텐츠를 설치하고 결제할 수 있도록 구축된 전자상거래 환경을 의미합니다.\n" +
                "\n" +
                "제3조 (회사정보 등의 제공) 회사는 다음 각 호의 사항을 회원이 알아보기 쉽도록 게임 서비스 내에 표시합니다. 다만, 개인정보처리방침과 약관은 회원이 연결화면을 통하여 볼 수 있도록 할 수 있습니다.\n" +
                " 1. 상호 및 대표자의 성명 \n" +
                " 2. 영업소 소재지 주소(회원의 불만을 처리할 수 있는 곳의 주소를 포함한다) \n" +
                " 3. 전화번호, 전자우편주소\n" +
                " 4. 사업자 등록번호\n" +
                " 5. 통신판매업 신고번호 \n";

        title3.setText(title_call);
        delmesgase1.setText(Content_call);
        delButton1.setText("취소");
        cancelButton3.setText("확인");
        cancelButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Useche=true;
                Toast.makeText(getApplicationContext(), String.valueOf(Useche), Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }

        });

        delButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
