package com.example.busapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

public class adNFCNumberActivity extends AppCompatActivity {

    String IDvalue;

    NdefMessage message;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    EditText NFCChaeditText;
    TextView tvNFCContent;
    // TextView message;


    String Bustext;//버스호차 저장
    //여기 아래부터 버튼 클릭 이벤트 선배님코드 추가해야되는부분... 날라가서 찾지를못함
    public void Change_click(View v){
        try {
            if(myTag ==null) {
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {
                //todo nfc에 저장하는 함수로 하단에 write 함수를 만들어두었다. 관리자가 가져가야될 부분이다.
                write(NFCChaeditText.getText().toString(), myTag);
                Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
            }
        } catch (IOException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } catch (FormatException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }
    }


    String talk;
    String ad="3";
    String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_nfcnumber);

        Intent intent = getIntent();
        IDvalue = intent.getExtras().getString("value1"); //메인에서 넘어온 아이디값
        context = this;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(nfcAdapter != null){
            Toast.makeText(this, nfcAdapter.toString(), Toast.LENGTH_LONG).show();
        }
        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
        //todo 여기서부터 시작


        setContentView(R.layout.activity_ad_nfcnumber);

        final TextView Datacheck10 = (TextView)findViewById(R.id.Datacheck10);//로그인
        Datacheck10.setText(value);//지워도 됨 - 값넘어온지 확인 하는 것

        Button studentbusMain5=(Button)findViewById(R.id.studentbusMain5);//메인화면으로가기
        //nfc 실패시 뜨는 메시지 조건을 cheNFC=false로 함
        final boolean cheNFC=true;

        //nfc는 어떤걸로 처리할지 몰라 버튼으로 처리
        Button nfcButton=(Button)findViewById(R.id.nfcButton);//nfc 버튼

        studentbusMain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move();
                //화면전환
            }
        });
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cheNFC == false) {
                        talk = "예약시간이나 버스를 확인해 주세요";
                        dialog(talk);
                    } else {//확인용 완성하면 지워야함
                        talk = "NFC가 체크되었습니다.";
                        dialog(talk);
                    }
                }catch (Exception e){
                    Excep(e);
                }
            }
        });

    }
    //////////////////////////////////////////
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
    void Move(){
        Intent i = new Intent(adNFCNumberActivity.this/*현재 액티비티 위치*/ , studentActivity.class/*이동 액티비티 위치*/);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Move();
    }
    void  Excep(Exception e)
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
    /////////////////////////////////////////////



    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            //todo nfc 내부에 저장된 텍스트를 불러온다 그후 text 에 저장함으로 여기를 건드리면 된다.
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        TextView readone = (TextView)findViewById(R.id.readone);
        TextView foruse = (TextView)findViewById(R.id.foruse);

        if(text!=null) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(getApplicationContext(), "탑승완료!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "탑승실패!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                }
            };
            adNFCNumberRequest nfcrequest = new adNFCNumberRequest(IDvalue,text, responseListener);
            RequestQueue queue = Volley.newRequestQueue(adNFCNumberActivity.this);
            queue.add(nfcrequest);
        }
        else if (text==null) {
            Toast.makeText(getApplicationContext(), "탑승실패! 기사에게 문의하세요!", Toast.LENGTH_SHORT).show();
        } else {
        }

    }


    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }



    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}
