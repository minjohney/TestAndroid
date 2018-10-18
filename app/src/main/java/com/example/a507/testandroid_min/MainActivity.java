package com.example.a507.testandroid_min;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    protected Button btHomePage, btDial, btCall, btSMS, btMAP, btREC, btTTS;
    protected TextView txRcog;
    protected EditText extts;
    protected TextToSpeech tts;
    private static int CODE_RECOG = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btHomePage = (Button) findViewById(R.id.btHomepage);
        btHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ice.mokwon.ac.kr"));
                startActivity(intent);
            }
        });
        extts = (EditText) findViewById(R.id.extts);
        btTTS = (Button) findViewById(R.id.btTTS);
        btTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = extts.getText().toString();
                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        tts = new TextToSpeech(this,this)


        btDial = (Button) findViewById(R.id.btDial);
        btDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0428297670"));
                startActivity(intent);
            }
        });
        btCall = (Button) findViewById(R.id.btCall);
        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0428297670"));
                startActivity(intent);
            }
        });
        btSMS = (Button) findViewById(R.id.btSMS);
        btSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:0428297670"));
                intent.putExtra("sms_body", "Mokwon University");
                startActivity(intent);
            }
        });
        btMAP = (Button) findViewById(R.id.btMAP);
        btMAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:36.321609,127.337957?z=20"));
                startActivity(intent);
            }
        });
        txRcog = (TextView) findViewById(R.id.txRecog);
        btREC = (Button) findViewById(R.id.btREC);
        btREC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog();

            }
        });

    }

    private void voiceRecog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //FREE_FORM : 내가 가지고 있는 정보 확인, WEB : 서버 정보에 있는 것까지
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak..");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
        startActivityForResult(intent, CODE_RECOG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //부모함수 호출
        if (requestCode == CODE_RECOG) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<String> arList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String sRecog = arList.get(0);
                txRcog.setText(sRecog);

            }

        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(0.6f);
            tts.setSpeechRate(1f);
        }
    }
}
