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
    protected Button  btREC, btTTS, btEcho;
    protected TextView txRcog;
    protected EditText extts,etRelay;
    protected TextToSpeech tts;
    private static int CODE_RECOG = 1234, CODE_ECHO = 1227;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        extts = (EditText) findViewById(R.id.extts);
        btTTS = (Button) findViewById(R.id.btTTS);
        btTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               speakStr(extts.getText().toString());
            }
        });
        tts = new TextToSpeech(this,this);
        btEcho = (Button) findViewById(R.id.btEcho);
        btEcho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog(CODE_ECHO);
            }
        });
        etRelay = (EditText) findViewById(R.id.etRelay);

        txRcog = (TextView) findViewById(R.id.txRecog);
        btREC = (Button) findViewById(R.id.btREC);
        btREC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog(CODE_RECOG);

            }
        });



    }

    private  void speakStr(String str){
        tts.speak(str,TextToSpeech.QUEUE_FLUSH, null,null);
        while(tts.isSpeaking()){
            try{
                Thread.sleep(100);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

        private void voiceRecog(int nCode){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //FREE_FORM : 내가 가지고 있는 정보 확인, WEB : 서버 정보에 있는 것까지
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak..");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
        startActivityForResult(intent, nCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //부모함수 호출
        if (resultCode == Activity.RESULT_OK && data != null)
        {
            if (requestCode == CODE_RECOG) {
            ArrayList<String> arList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String sRecog = arList.get(0);
                txRcog.setText(sRecog);

            }
            else if(requestCode==CODE_ECHO){
                ArrayList<String> arList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String sRecog = arList.get(0);
                String sDelay = etRelay.getText().toString();
                int nDelay = Integer.parseInt(sDelay); //단위는 초단위
                try {
                    Thread.sleep(nDelay*1000); //단위는 밀리초단위

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speakStr(sRecog);
            }

        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(1.0f);
            tts.setSpeechRate(1.0f);
        }
    }
}
